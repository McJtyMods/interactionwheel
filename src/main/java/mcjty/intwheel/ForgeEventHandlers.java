package mcjty.intwheel;

import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.playerdata.PropertiesDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onEntityConstructing(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerProperties.PLAYER_WHEEL_CONFIGURATION, null).isPresent()) {
                event.addCapability(new ResourceLocation(InteractionWheel.MODID, "hotkeys"), new PropertiesDispatcher());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // We need to copyFrom the capabilities
            event.getOriginal().getCapability(PlayerProperties.PLAYER_WHEEL_CONFIGURATION).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerProperties.PLAYER_WHEEL_CONFIGURATION).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
}
