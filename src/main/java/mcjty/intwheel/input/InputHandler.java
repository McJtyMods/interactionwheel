package mcjty.intwheel.input;


import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.setup.GuiProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class InputHandler {

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        checkWheelKey();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        checkWheelKey();
    }

    private void checkWheelKey() {
        if (KeyBindings.keyOpenWheel.isPressed()) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
            if (mouseOver != null && mouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = mouseOver.getBlockPos();
                player.openGui(InteractionWheel.instance, GuiProxy.GUI_WHEEL, player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
            } else {
                player.openGui(InteractionWheel.instance, GuiProxy.GUI_WHEEL, player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
            }
        }
    }

}
