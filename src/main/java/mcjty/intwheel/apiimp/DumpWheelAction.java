package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemHandlerHelper;

public class DumpWheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_DUMP;
    }

    @Override
    public boolean performClient(Player player, Level world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.DUMP.createElement();
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
        int start = extended ? 0 : 9;
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te.getCapability(ForgeCapabilities.ITEM_HANDLER, null).isPresent()) {
            te.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(inventory -> {
                for (int i = start; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    stack = ItemHandlerHelper.insertItem(inventory, stack, false);
                    player.getInventory().setItem(i, stack);
                }
            });
        }
    }
}
