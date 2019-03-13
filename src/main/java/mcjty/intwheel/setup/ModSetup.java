package mcjty.intwheel.setup;

import mcjty.intwheel.ForgeEventHandlers;
import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.apiimp.*;
import mcjty.intwheel.config.ConfigSetup;
import mcjty.intwheel.network.PacketHandler;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

public class ModSetup {

    private Logger logger;

    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        NetworkRegistry.INSTANCE.registerGuiHandler(InteractionWheel.instance, new GuiProxy());

        ConfigSetup.init(e);

        registerCapabilities();
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

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
        ConfigSetup.postInit();
    }

    private static void registerCapabilities(){
        CapabilityManager.INSTANCE.register(PlayerWheelConfiguration.class, new Capability.IStorage<PlayerWheelConfiguration>() {

            @Override
            public NBTBase writeNBT(Capability<PlayerWheelConfiguration> capability, PlayerWheelConfiguration instance, EnumFacing side) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void readNBT(Capability<PlayerWheelConfiguration> capability, PlayerWheelConfiguration instance, EnumFacing side, NBTBase nbt) {
                throw new UnsupportedOperationException();
            }

        }, () -> {
            throw new UnsupportedOperationException();
        });
    }
}
