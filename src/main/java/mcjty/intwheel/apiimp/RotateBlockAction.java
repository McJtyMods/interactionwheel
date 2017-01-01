package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RotateBlockAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_ROTATE;
    }

    @Override
    public boolean performClient(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public void performServer(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        IBlockState blockState = world.getBlockState(pos);
        blockState.getBlock().rotateBlock(world, pos, EnumFacing.UP);
    }
}
