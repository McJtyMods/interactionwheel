package mcjty.intwheel.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public interface IWheelActionProvider {

    @Nonnull
    List<WheelActionElement> getActions(EntityPlayer player, World world, BlockPos pos);
}
