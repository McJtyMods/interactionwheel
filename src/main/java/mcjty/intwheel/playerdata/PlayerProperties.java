package mcjty.intwheel.playerdata;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerProperties {

    // @todo 1.19.2
//    @CapabilityInject(PlayerWheelConfiguration.class)
    public static Capability<PlayerWheelConfiguration> PLAYER_WHEEL_CONFIGURATION;

    public static LazyOptional<PlayerWheelConfiguration> getWheelConfig(Player player) {
        return player.getCapability(PLAYER_WHEEL_CONFIGURATION, null);
    }


}
