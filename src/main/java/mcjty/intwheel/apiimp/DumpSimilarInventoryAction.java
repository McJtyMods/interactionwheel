package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashSet;
import java.util.Set;

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
        Set<Integer> ores = getOres(stack);
        for (int i = 0 ; i < handler.getSlots() ; i++) {
            ItemStack s = handler.getStackInSlot(i);
            if (matchStack(stack, ores, s)) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> getOres(ItemStack stack) {
        Set<Integer> ores = new HashSet<>();
        // @todo 1.19.2 tags
//        for (int id : OreDictionary.getOreIDs(stack)) {
//            ores.add(id);
//        }
        return ores;
    }

    private boolean matchStack(ItemStack stack, Set<Integer> ores, ItemStack s) {
        if (!s.isEmpty()) {
            if (s.getItem() == stack.getItem()) {
                return true;
            }
            if (!ores.isEmpty()) {
                // @todo 1.19.2 tags
//                int[] iDs = OreDictionary.getOreIDs(s);
//                for (int d : iDs) {
//                    if (ores.contains(d)) {
//                        return true;
//                    }
//                }
            }
        }
        return false;
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent()) {
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(inventory -> {
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
