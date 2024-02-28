package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.capabilities.ForgeCapabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class DumpSimilarInventoryAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_DUMPSIMILARINV;
    }

    @Override
    public boolean performClient(Player player, Level world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.DUMPSIMILARINV.createElement();
    }

    @Override
    public boolean isDefaultEnabled() {
        return true;
    }

    private boolean isSimilar(IItemHandler handler, ItemStack stack) {
        for (int i = 0 ; i < handler.getSlots() ; i++) {
            ItemStack s = handler.getStackInSlot(i);
            if (matchStack(stack, s)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchStack(ItemStack stack, ItemStack s) {
        if (!s.isEmpty()) {
            if (s.getItem() == stack.getItem()) {
                return true;
            }
            // Can we do more here?
        }
        return false;
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te.getCapability(ForgeCapabilities.ITEM_HANDLER, null).isPresent()) {
            te.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(inventory -> {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty() && isSimilar(inventory, stack)) {
                        stack = ItemHandlerHelper.insertItem(inventory, stack, false);
                        player.getInventory().setItem(i, stack);
                    }
                }
            });
        }
    }
}
