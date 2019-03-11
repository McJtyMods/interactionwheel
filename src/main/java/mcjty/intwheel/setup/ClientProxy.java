package mcjty.intwheel.setup;

import com.google.common.util.concurrent.ListenableFuture;
import mcjty.intwheel.RenderHandler;
import mcjty.intwheel.input.InputHandler;
import mcjty.intwheel.input.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.Callable;

public class ClientProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        FMLCommonHandler.instance().bus().register(new InputHandler());
        KeyBindings.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {

    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt) {
        RenderHandler.showFoundInventories(evt);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
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
