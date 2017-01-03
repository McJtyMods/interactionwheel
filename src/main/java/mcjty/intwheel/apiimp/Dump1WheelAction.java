package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.varia.InventoryHelper;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class Dump1WheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_DUMP1;
    }

    @Override
    public boolean performClient(EntityPlayer player, World world, BlockPos pos, boolean extended) {
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
    public void performServer(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (ItemStackTools.isEmpty(heldItem)) {
            return;
        }
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            heldItem = ItemHandlerHelper.insertItem(inventory, heldItem, false);
            player.setHeldItem(EnumHand.MAIN_HAND, heldItem);
        } else if (te instanceof IInventory) {
            IInventory inventory = (IInventory) te;
            int failed = InventoryHelper.mergeItemStackSafe(inventory, null, heldItem, 0, inventory.getSizeInventory(), null);
            if (failed > 0) {
                ItemStack putBack = heldItem.copy();
                ItemStackTools.setStackSize(putBack, failed);
                player.setHeldItem(EnumHand.MAIN_HAND, putBack);
            } else {
                player.setHeldItem(EnumHand.MAIN_HAND, ItemStackTools.getEmptyStack());
            }
        }
    }
}
