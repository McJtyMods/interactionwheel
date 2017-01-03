package mcjty.intwheel.input;


import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.proxy.GuiProxy;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.List;

public class InputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.keyOpenWheel.isPressed()) {
            EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());
            WorldClient world = MinecraftTools.getWorld(Minecraft.getMinecraft());
            RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
            if (mouseOver != null && mouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = mouseOver.getBlockPos();
                List<String> actions = InteractionWheel.interactionWheelImp.getActions(player, world, pos);
                if (!actions.isEmpty()) {
                    player.openGui(InteractionWheel.instance, GuiProxy.GUI_WHEEL, player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
                }
            } else {
                List<String> actions = InteractionWheel.interactionWheelImp.getActions(player, world, null);
                if (!actions.isEmpty()) {
                    player.openGui(InteractionWheel.instance, GuiProxy.GUI_WHEEL, player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
                }
            }
        }
    }

}
