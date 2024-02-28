package mcjty.intwheel.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

public class KeyBindings {

    public static KeyMapping keyOpenWheel;

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        keyOpenWheel = new KeyMapping("key.openwheel", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.h"), "key.categories.interactionwheel");
        event.register(keyOpenWheel);
    }
}
