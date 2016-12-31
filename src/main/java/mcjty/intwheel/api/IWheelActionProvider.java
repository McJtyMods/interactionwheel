package mcjty.intwheel.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IWheelActionProvider {

    @Nonnull
    // If pos is null then we're not pointing at any block
    List<WheelActionElement> getActions(@Nonnull EntityPlayer player, World world, @Nullable BlockPos pos);
}
