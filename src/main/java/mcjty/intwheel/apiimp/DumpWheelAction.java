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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class DumpWheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_DUMP;
    }

    @Override
    public boolean performClient(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.DUMP.createElement();
    }

    @Override
    public void performServer(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        int start = extended ? 0 : 9;
        System.out.println("start = " + start);
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for (int i = start ; i < player.inventory.getSizeInventory() ; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                stack = ItemHandlerHelper.insertItem(inventory, stack, false);
                player.inventory.setInventorySlotContents(i, stack);
            }
        } else if (te instanceof IInventory) {
            IInventory inventory = (IInventory) te;
            for (int i = start ; i < player.inventory.getSizeInventory() ; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                int failed = InventoryHelper.mergeItemStackSafe(inventory, null, stack, 0, inventory.getSizeInventory(), null);
                if (failed > 0) {
                    ItemStack putBack = stack.copy();
                    ItemStackTools.setStackSize(putBack, failed);
                    player.inventory.setInventorySlotContents(i, stack);
                } else {
                    player.inventory.setInventorySlotContents(i, ItemStackTools.getEmptyStack());
                }
            }
        }
    }
}
