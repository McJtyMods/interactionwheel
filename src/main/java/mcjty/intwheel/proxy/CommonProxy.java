package mcjty.intwheel.proxy;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.apiimp.DumpWheelAction;
import mcjty.intwheel.apiimp.ExtractWheelAction;
import mcjty.intwheel.apiimp.RotateBlockAction;
import mcjty.intwheel.apiimp.SearchWheelAction;
import mcjty.intwheel.config.ConfigSetup;
import mcjty.intwheel.network.PacketHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        PacketHandler.registerMessages("intwheel");

        ConfigSetup.preInit(e);
    }

    public void init(FMLInitializationEvent e) {
//        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        NetworkRegistry.INSTANCE.registerGuiHandler(InteractionWheel.instance, new GuiProxy());

        InteractionWheel.registry.register(new RotateBlockAction());
        InteractionWheel.registry.register(new DumpWheelAction());
        InteractionWheel.registry.register(new ExtractWheelAction());
        InteractionWheel.registry.register(new SearchWheelAction());
    }

    public void postInit(FMLPostInitializationEvent e) {
        ConfigSetup.postInit();
    }
}
