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
            if (heldItem.isCorrectToolForDrops(world.getBlockState(pos))) {
                // Nothing to do
                return;
            }
        }

        // Find a tool that works
        for (int i = 0 ; i < player.getInventory().getContainerSize() ; i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (!s.isEmpty()) {
                if (s.isCorrectToolForDrops(world.getBlockState(pos))) {
                    player.getInventory().setItem(i, heldItem);
                    player.setItemInHand(InteractionHand.MAIN_HAND, s);
                    player.containerMenu.broadcastChanges();
                    return;
                }
            }
        }
    }
}
