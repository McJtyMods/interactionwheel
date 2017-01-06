package mcjty.intwheel.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import mcjty.intwheel.RenderHandler;
import mcjty.intwheel.input.InputHandler;
import mcjty.intwheel.input.KeyBindings;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.Callable;

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

    @Override
    public World getClientWorld() {
        return MinecraftTools.getWorld(Minecraft.getMinecraft());
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return MinecraftTools.getPlayer(Minecraft.getMinecraft());
    }

    @Override
    public <V> ListenableFuture<V> addScheduledTaskClient(Callable<V> callableToSchedule) {
        return Minecraft.getMinecraft().addScheduledTask(callableToSchedule);
    }

    @Override
    public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule) {
        return Minecraft.getMinecraft().addScheduledTask(runnableToSchedule);
    }
}
