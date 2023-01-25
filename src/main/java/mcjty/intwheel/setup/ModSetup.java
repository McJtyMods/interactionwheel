package mcjty.intwheel.setup;

import mcjty.intwheel.ForgeEventHandlers;
import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.apiimp.*;
import mcjty.intwheel.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModSetup {

    private Logger logger;

    public void init(FMLCommonSetupEvent e) {
        logger = LogManager.getLogger();

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        PacketHandler.registerMessages("intwheel");

        InteractionWheel.interactionWheelImp.registerProvider(new DefaultWheelActionProvider());
        InteractionWheel.registry.register(new RotateBlockAction());
        InteractionWheel.registry.register(new SearchWheelAction());
        InteractionWheel.registry.register(new DumpWheelAction());
        InteractionWheel.registry.register(new Dump1WheelAction());
        InteractionWheel.registry.register(new DumpSimilarWheelAction());
        InteractionWheel.registry.register(new DumpSimilarInventoryAction());
        InteractionWheel.registry.register(new DumpOresAction());
        InteractionWheel.registry.register(new DumpBlocksAction());
        InteractionWheel.registry.register(new ExtractWheelAction());
        InteractionWheel.registry.register(new PickToolWheelAction());
    }

    public Logger getLogger() {
        return logger;
    }
}
