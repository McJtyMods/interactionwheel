package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PickToolWheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_PICKTOOL;
    }

    @Override
    public boolean performClient(Player player, Level world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.PICKTOOL.createElement();
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!heldItem.isEmpty()) {
            // @todo 1.19.2 use tags?
//            if (ForgeHooks.canToolHarvestBlock(world, pos, heldItem)) {
//                // Nothing to do
//                return;
//            }
        }

        // Find a tool that works
        for (int i = 0 ; i < player.getInventory().getContainerSize() ; i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (!s.isEmpty()) {
                // @todo 1.19.2 use tags?
//                if (ForgeHooks.canToolHarvestBlock(world, pos, s)) {
//                    player.getInventory().setItem(i, heldItem);
//                    player.setHeldItem(EnumHand.MAIN_HAND, s);
//                    player.openContainer.detectAndSendChanges();
//                    return;
//                }
            }
        }

        // We couldn't find a tool. Here we try again with a more complicated algorithm.
        for (int i = 0 ; i < player.getInventory().getContainerSize() ; i++) {
            if (i != player.getInventory().selected) {
                ItemStack s = player.getInventory().getItem(i);
                if (!s.isEmpty()) {
                    // Swap held item and this item
                    player.getInventory().setItem(i, heldItem);
                    player.setItemInHand(InteractionHand.MAIN_HAND, s);
                    // @todo 1.19.2 tags
//                    if (ForgeHooks.canHarvestBlock(world.getBlockState(pos).getBlock(), player, world, pos)) {
//                        player.openContainer.detectAndSendChanges();
//                        return;
//                    }
                    // Restore
                    player.getInventory().setItem(i, s);
                    player.setItemInHand(InteractionHand.MAIN_HAND, heldItem);
                }
            }
        }
    }
}
