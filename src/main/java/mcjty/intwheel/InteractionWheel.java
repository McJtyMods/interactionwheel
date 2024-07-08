package mcjty.intwheel;


import mcjty.intwheel.api.IInteractionWheel;
import mcjty.intwheel.apiimp.InteractionWheelImp;
import mcjty.intwheel.apiimp.WheelActionRegistry;
import mcjty.intwheel.input.InputHandler;
import mcjty.intwheel.input.KeyBindings;
import mcjty.intwheel.network.PacketHandler;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import mcjty.intwheel.setup.ModSetup;
import mcjty.intwheel.varia.RenderHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod(InteractionWheel.MODID)
public class InteractionWheel {

    public static final String MODID = "interactionwheel";

    public static ModSetup setup = new ModSetup();

    public static InteractionWheel instance;
    public static InteractionWheelImp interactionWheelImp = new InteractionWheelImp();

    public static WheelActionRegistry registry = new WheelActionRegistry();

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final Supplier<AttachmentType<PlayerWheelConfiguration>> HOTKEYS = ATTACHMENT_TYPES.register(
            "hotkeys", () -> AttachmentType.builder(PlayerWheelConfiguration::new)
                    .serialize(PlayerWheelConfiguration.CODEC)
                    .copyOnDeath()
                    .build());


    public InteractionWheel(IEventBus bus, Dist dist) {

        bus.addListener(this::processIMC);
        bus.addListener(setup::init);
        bus.addListener(PacketHandler::registerMessages);
        ATTACHMENT_TYPES.register(bus);

        if (dist.isClient()) {
            bus.addListener(KeyBindings::onRegisterKeyMappings);
            NeoForge.EVENT_BUS.addListener(RenderHandler::showFoundInventories);
            NeoForge.EVENT_BUS.register(new InputHandler());
        }
    }

    private void processIMC(final InterModProcessEvent event) {
        event.getIMCStream(IInteractionWheel.GET_INTERACTION_WHEEL::equals).forEach(message -> {
            Supplier<Function<IInteractionWheel, Void>> supplier = message.getMessageSupplier();
            supplier.get().apply(interactionWheelImp);
        });
    }
}
