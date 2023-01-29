package mcjty.intwheel.input;


import mcjty.intwheel.gui.GuiWheel;
import net.minecraft.client.Minecraft;
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
            Minecraft.getInstance().setScreen(new GuiWheel());
        }
    }

}
