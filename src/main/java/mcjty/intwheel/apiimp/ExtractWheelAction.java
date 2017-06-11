package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ExtractWheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_EXTRACT;
    }

    @Override
    public boolean performClient(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.EXTRACT.createElement();
    }

    @Override
    public void performServer(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for (int i = 0 ; i < inventory.getSlots() ; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    int s = stack.getCount();
                    ItemStack extracted = inventory.extractItem(i, s, true);        // Simulate
                    if (player.inventory.addItemStackToInventory(extracted)) {
                        inventory.extractItem(i, s, false); // Do for real
                    }
                }
            }
        } else if (te instanceof IInventory) {
            IInventory inventory = (IInventory) te;
            for (int i = 0 ; i < inventory.getSizeInventory() ; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    int s = stack.getCount();
                    ItemStack extracted = inventory.decrStackSize(i, s);
                    if (!player.inventory.addItemStackToInventory(extracted)) {
                        inventory.setInventorySlotContents(i, extracted);
                    }
                }
            }
        }
        player.openContainer.detectAndSendChanges();
    }
}
