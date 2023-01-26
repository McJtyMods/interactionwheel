package mcjty.intwheel;


import mcjty.intwheel.api.IInteractionWheel;
import mcjty.intwheel.apiimp.InteractionWheelImp;
import mcjty.intwheel.apiimp.WheelActionRegistry;
import mcjty.intwheel.input.InputHandler;
import mcjty.intwheel.input.KeyBindings;
import mcjty.intwheel.setup.ModSetup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        bus.addListener(this::processIMC);
        bus.addListener(setup::init);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(KeyBindings::onRegisterKeyMappings);
            MinecraftForge.EVENT_BUS.register(new InputHandler());
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        event.getIMCStream(IInteractionWheel.GET_INTERACTION_WHEEL::equals).forEach(message -> {
            Supplier<Function<IInteractionWheel, Void>> supplier = message.getMessageSupplier();
            supplier.get().apply(interactionWheelImp);
        });
    }
}
