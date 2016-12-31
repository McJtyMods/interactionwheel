package mcjty.intwheel.proxy;

import mcjty.intwheel.InteractionWheel;
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
    }

    public void postInit(FMLPostInitializationEvent e) {
        ConfigSetup.postInit();
    }
}
