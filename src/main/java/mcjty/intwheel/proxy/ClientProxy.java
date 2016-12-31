package mcjty.intwheel.proxy;

import mcjty.intwheel.input.InputHandler;
import mcjty.intwheel.input.KeyBindings;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

//        MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
//        OBJLoader.INSTANCE.addDomain(ImmersiveCraft.MODID);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        FMLCommonHandler.instance().bus().register(new InputHandler());
        KeyBindings.init();
    }
}
