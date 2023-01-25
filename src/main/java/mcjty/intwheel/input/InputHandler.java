package mcjty.intwheel.input;


import mcjty.intwheel.gui.GuiWheel;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InputHandler {

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseButton event) {
        checkWheelKey();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        checkWheelKey();
    }

    private void checkWheelKey() {
        if (KeyBindings.keyOpenWheel.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            HitResult mouseOver = Minecraft.getInstance().hitResult;
            if (mouseOver instanceof BlockHitResult blockHitResult) {
                BlockPos pos = blockHitResult.getBlockPos();
                // @todo 1.19.2
                Minecraft.getInstance().setScreen(new GuiWheel());
//                player.openGui(InteractionWheel.instance, GuiProxy.GUI_WHEEL, player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
            } else {
                // @todo 1.19.2
                Minecraft.getInstance().setScreen(new GuiWheel());
//                player.openGui(InteractionWheel.instance, GuiProxy.GUI_WHEEL, player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
            }
        }
    }

}
