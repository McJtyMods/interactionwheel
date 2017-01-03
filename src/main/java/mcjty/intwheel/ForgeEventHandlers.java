package mcjty.intwheel;

import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.playerdata.PropertiesDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandlers {
    @SubscribeEvent
    public void onEntityConstructing(AttachCapabilitiesEvent.Entity event){
        if (event.getEntity() instanceof EntityPlayer) {
            if (!event.getEntity().hasCapability(PlayerProperties.PLAYER_WHEEL_CONFIGURATION, null)) {
                event.addCapability(new ResourceLocation(InteractionWheel.MODID, "hotkeys"), new PropertiesDispatcher());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // We need to copyFrom the capabilities
            if (event.getOriginal().hasCapability(PlayerProperties.PLAYER_WHEEL_CONFIGURATION, null)) {
                PlayerWheelConfiguration oldStore = event.getOriginal().getCapability(PlayerProperties.PLAYER_WHEEL_CONFIGURATION, null);
                PlayerWheelConfiguration newStore = PlayerProperties.getPlayerGotNote(event.getEntityPlayer());
                newStore.copyFrom(oldStore);
            }
        }
    }


}
