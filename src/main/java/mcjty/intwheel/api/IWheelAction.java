package mcjty.intwheel.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IWheelAction {

    String getId();

    // Perform an action. This is called client-side. If this returns false the server
    // side action is not performed
    boolean performClient(EntityPlayer player, World world, @Nullable BlockPos pos);

    // Perform an action. This is called server-side
    void performServer(EntityPlayer player, World world, @Nullable BlockPos pos);
}
