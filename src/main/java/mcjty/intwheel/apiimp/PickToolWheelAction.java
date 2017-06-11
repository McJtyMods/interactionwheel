package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class PickToolWheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_PICKTOOL;
    }

    @Override
    public boolean performClient(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.PICKTOOL.createElement();
    }

    @Override
    public void performServer(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!heldItem.isEmpty()) {
            if (ForgeHooks.canToolHarvestBlock(world, pos, heldItem)) {
                // Nothing to do
                return;
            }
        }

        // Find a tool that works
        for (int i = 0 ; i < player.inventory.getSizeInventory() ; i++) {
            ItemStack s = player.inventory.getStackInSlot(i);
            if (!s.isEmpty()) {
                if (ForgeHooks.canToolHarvestBlock(world, pos, s)) {
                    player.inventory.setInventorySlotContents(i, heldItem);
                    player.setHeldItem(EnumHand.MAIN_HAND, s);
                    player.openContainer.detectAndSendChanges();
                    return;
                }
            }
        }

        // We couldn't find a tool. Here we try again with a more complicated algorithm.
        for (int i = 0 ; i < player.inventory.getSizeInventory() ; i++) {
            if (i != player.inventory.currentItem) {
                ItemStack s = player.inventory.getStackInSlot(i);
                if (!s.isEmpty()) {
                    // Swap held item and this item
                    player.inventory.setInventorySlotContents(i, heldItem);
                    player.setHeldItem(EnumHand.MAIN_HAND, s);
                    if (ForgeHooks.canHarvestBlock(world.getBlockState(pos).getBlock(), player, world, pos)) {
                        player.openContainer.detectAndSendChanges();
                        return;
                    }
                    // Restore
                    player.inventory.setInventorySlotContents(i, s);
                    player.setHeldItem(EnumHand.MAIN_HAND, heldItem);
                }
            }
        }
    }
}
