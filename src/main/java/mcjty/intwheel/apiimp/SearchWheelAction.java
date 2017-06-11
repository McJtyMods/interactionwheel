package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.network.PackedInventoriesToClient;
import mcjty.intwheel.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashSet;
import java.util.Set;

public class SearchWheelAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_SEARCH;
    }

    @Override
    public boolean performClient(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.SEARCH.createElement();
    }

    @Override
    public void performServer(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        pos = player.getPosition();
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!heldItem.isEmpty()) {
            Set<BlockPos> found = new HashSet<>();
            for (int dx = -20 ; dx <= 20 ; dx++) {
                for (int dy = -20 ; dy <= 20 ; dy++) {
                    for (int dz = -20 ; dz <= 20 ; dz++) {
                        BlockPos p = pos.add(dx, dy, dz);
                        TileEntity te = world.getTileEntity(p);
                        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                            IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                            for (int i = 0 ; i < handler.getSlots() ; i++) {
                                ItemStack stack = handler.getStackInSlot(i);
                                if (!stack.isEmpty() && heldItem.isItemEqualIgnoreDurability(stack)) {
                                    found.add(p);
                                    break;
                                }
                            }
                        } else if (te instanceof IInventory) {
                            IInventory inventory = (IInventory) te;
                            for (int i = 0 ; i < inventory.getSizeInventory() ; i++) {
                                ItemStack stack = inventory.getStackInSlot(i);
                                if (!stack.isEmpty() && heldItem.isItemEqualIgnoreDurability(stack)) {
                                    found.add(p);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            PacketHandler.INSTANCE.sendTo(new PackedInventoriesToClient(found), (EntityPlayerMP) player);
        }
    }
}
