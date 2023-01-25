package mcjty.intwheel.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public interface IWheelActionProvider {

    /**
     * Return a unique ID (usually combined with the modid) to identify this provider.
     * @return
     */
    String getID();

    /**
     * Update the actions. You get a set of actions that is already filled
     * in by previous registered providers. You can update the set here. Even remove
     * actions if you want.
     * @param actions a set with action IDs that you can modify
     * @param pos is the position of the block the player is looking at or null in case the player isn't pointing at a block
     */
    void updateWheelActions(@Nonnull Set<String> actions, @Nonnull Player player, Level world, @Nullable BlockPos pos);
}
