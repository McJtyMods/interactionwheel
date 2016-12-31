package mcjty.intwheel.input;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindings {

    public static KeyBinding keyOpenWheel;

    public static void init() {
        keyOpenWheel = new KeyBinding("key.openwheel", Keyboard.KEY_X, "key.categories.intwheel");
        ClientRegistry.registerKeyBinding(keyOpenWheel);
    }
}
