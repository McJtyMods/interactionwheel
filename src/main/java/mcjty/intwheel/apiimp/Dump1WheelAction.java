package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class Dump1WheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_DUMP1;
    }

    @Override
    public boolean performClient(Player player, Level world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.DUMP1.createElement();
    }

    @Override
    public boolean isDefaultEnabled() {
        return false;
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (heldItem.isEmpty()) {
            return;
        }
        IItemHandler inventory = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
        if (inventory != null) {
            ItemStack remaining = ItemHandlerHelper.insertItem(inventory, heldItem, false);
            player.setItemInHand(InteractionHand.MAIN_HAND, remaining);
        }
    }
}
