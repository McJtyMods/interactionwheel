package mcjty.intwheel;

import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.apiimp.InventoryWheelActions;
import mcjty.intwheel.apiimp.ItemHandlerWheelActions;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class WheelSupport {
    public static IWheelActions getWheelActions(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        IWheelActions actions = null;
        if (block instanceof IWheelActions) {
            actions = (IWheelActions) block;
        } else {
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                actions = new ItemHandlerWheelActions(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
            } else if (te instanceof IInventory) {
                actions = new InventoryWheelActions((IInventory) te);
            }
        }
        return actions;
    }
}
