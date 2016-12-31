package mcjty.intwheel.proxy;

import mcjty.intwheel.gui.GuiWheel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int guiid, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int guiid, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        return new GuiWheel(world);
    }
}
