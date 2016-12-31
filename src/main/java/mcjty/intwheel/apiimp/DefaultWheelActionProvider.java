package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelActionProvider;
import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class DefaultWheelActionProvider implements IWheelActionProvider {

    @Override
    @Nonnull
    public List<WheelActionElement> getActions(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof IWheelActions) {
            return ((IWheelActions) block).getActions();
        } else {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof IInventory || (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))) {
                return StandardWheelActions.createStandardActions();
            }
        }
        return Collections.emptyList();
    }
}
