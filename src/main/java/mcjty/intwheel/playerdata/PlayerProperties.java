package mcjty.intwheel.playerdata;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerProperties {

    public static final Capability<PlayerWheelConfiguration> PLAYER_WHEEL_CONFIGURATION = CapabilityManager.get(new CapabilityToken<>(){});

    public static LazyOptional<PlayerWheelConfiguration> getWheelConfig(Player player) {
        return player.getCapability(PLAYER_WHEEL_CONFIGURATION, null);
    }


}
