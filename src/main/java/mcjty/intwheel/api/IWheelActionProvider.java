package mcjty.intwheel.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public interface IWheelActionProvider {

    @Nonnull
    List<WheelActionElement> getActions(World world, BlockPos pos);
}
