package mcjty.intwheel.playerdata;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PlayerProperties {

    @CapabilityInject(PlayerWheelConfiguration.class)
    public static Capability<PlayerWheelConfiguration> PLAYER_WHEEL_CONFIGURATION;

    public static PlayerWheelConfiguration getPlayerGotNote(EntityPlayer player) {
        return player.getCapability(PLAYER_WHEEL_CONFIGURATION, null);
    }


}
