package mcjty.intwheel;


import mcjty.intwheel.api.IInteractionWheel;
import mcjty.intwheel.apiimp.InteractionWheelImp;
import mcjty.intwheel.apiimp.WheelActionRegistry;
import mcjty.intwheel.input.InputHandler;
import mcjty.intwheel.input.KeyBindings;
import mcjty.intwheel.setup.ModSetup;
import mcjty.intwheel.varia.RenderHandler;
import net.neoforged.neoforge.api.distmarker.Dist;
import net.neoforged.neoforge.common.MinecraftForge;
import net.neoforged.neoforge.eventbus.api.IEventBus;
import net.neoforged.neoforge.fml.common.Mod;
import net.neoforged.neoforge.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.fml.loading.FMLEnvironment;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod(InteractionWheel.MODID)
public class InteractionWheel {

    public static final String MODID = "interactionwheel";

    public static ModSetup setup = new ModSetup();

    public static InteractionWheel instance;
    public static InteractionWheelImp interactionWheelImp = new InteractionWheelImp();

    public static WheelActionRegistry registry = new WheelActionRegistry();

    public InteractionWheel() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Dist dist = FMLEnvironment.dist;

        bus.addListener(this::processIMC);
        bus.addListener(setup::init);

        if (dist.isClient()) {
            bus.addListener(KeyBindings::onRegisterKeyMappings);
            MinecraftForge.EVENT_BUS.addListener(RenderHandler::showFoundInventories);
            MinecraftForge.EVENT_BUS.register(new InputHandler());
        }
    }

    private void processIMC(final InterModProcessEvent event) {
        event.getIMCStream(IInteractionWheel.GET_INTERACTION_WHEEL::equals).forEach(message -> {
            Supplier<Function<IInteractionWheel, Void>> supplier = message.getMessageSupplier();
            supplier.get().apply(interactionWheelImp);
        });
    }
}
