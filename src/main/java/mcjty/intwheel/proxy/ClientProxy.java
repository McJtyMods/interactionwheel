package mcjty.intwheel.proxy;

import mcjty.intwheel.RenderHandler;
import mcjty.intwheel.input.InputHandler;
import mcjty.intwheel.input.KeyBindings;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        MinecraftForge.EVENT_BUS.register(this);

//        MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
//        OBJLoader.INSTANCE.addDomain(ImmersiveCraft.MODID);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        FMLCommonHandler.instance().bus().register(new InputHandler());
        KeyBindings.init();
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt) {
        RenderHandler.showFoundInventories(evt);
    }

}
