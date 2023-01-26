package mcjty.intwheel.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBindings {

    public static KeyMapping keyOpenWheel;

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        keyOpenWheel = new KeyMapping("key.openwheel", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.x"), "key.categories.intwheel");
        event.register(keyOpenWheel);
    }
}
