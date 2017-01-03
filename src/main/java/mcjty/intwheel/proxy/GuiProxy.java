package mcjty.intwheel.proxy;

import mcjty.intwheel.gui.GuiWheel;
import mcjty.intwheel.gui.GuiWheelConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    public static final int GUI_WHEEL = 0;
    public static final int GUI_CONFIG = 1;

    @Override
    public Object getServerGuiElement(int guiid, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int guiid, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        if (guiid == GUI_WHEEL) {
            return new GuiWheel(world);
        } else if (guiid == GUI_CONFIG) {
            return new GuiWheelConfig();
        } else {
            return null;
        }
    }
}
