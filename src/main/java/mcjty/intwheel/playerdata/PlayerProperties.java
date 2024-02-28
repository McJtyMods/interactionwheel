package mcjty.intwheel.playerdata;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.util.LazyOptional;

public class PlayerProperties {

    public static final Capability<PlayerWheelConfiguration> PLAYER_WHEEL_CONFIGURATION = CapabilityManager.get(new CapabilityToken<>(){});

    public static LazyOptional<PlayerWheelConfiguration> getWheelConfig(Player player) {
        return player.getCapability(PLAYER_WHEEL_CONFIGURATION, null);
    }


}
