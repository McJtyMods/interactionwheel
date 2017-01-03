package mcjty.intwheel.proxy;

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

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        registerCapabilities();
        PacketHandler.registerMessages("intwheel");

        InteractionWheel.interactionWheelImp.registerProvider(new DefaultWheelActionProvider());

        ConfigSetup.preInit(e);
    }

    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        NetworkRegistry.INSTANCE.registerGuiHandler(InteractionWheel.instance, new GuiProxy());

        InteractionWheel.registry.register(new RotateBlockAction());
        InteractionWheel.registry.register(new SearchWheelAction());
        InteractionWheel.registry.register(new DumpWheelAction());
        InteractionWheel.registry.register(new ExtractWheelAction());
        for (int i = 0 ; i < 30 ; i++) {
            InteractionWheel.registry.register(new DummyWheelAction("std.dummy" + i));
        }
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
