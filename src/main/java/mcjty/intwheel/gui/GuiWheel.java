package mcjty.intwheel.gui;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.WheelSupport;
import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.api.WheelAction;
import mcjty.intwheel.input.KeyBindings;
import mcjty.intwheel.network.PacketHandler;
import mcjty.intwheel.network.PacketPerformAction;
import mcjty.intwheel.varia.RenderHelper;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiWheel extends GuiScreen {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 160;

    private int guiLeft;
    private int guiTop;

    private final IWheelActions actions;
    private final BlockPos pos;

    private static final ResourceLocation background = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel.png");
    private static final ResourceLocation hilight = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel_hilight.png");

    public GuiWheel(World world, int x, int y, int z) {
        pos = new BlockPos(x, y, z);
        actions = WheelSupport.getWheelActions(world, pos);
    }

    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (this.width - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (Keyboard.isKeyDown(KeyBindings.keyOpenWheel.getKeyCode())) {
            closeThis();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int cx = mouseX - guiLeft - WIDTH / 2;
        int cy = mouseY - guiTop - HEIGHT / 2;
        int q = getSelectedSection(cx, cy);
        if (q == -1) {
            closeThis();
        } else {
            if (q < actions.getActions().size()) {
                performAction(q);
            }
        }
        closeThis();
    }

    private void performAction(int index) {
        WheelAction action = actions.getActions().get(index);
        if (actions.performClient(action, MinecraftTools.getPlayer(mc))) {
            PacketHandler.INSTANCE.sendToServer(new PacketPerformAction(pos, action.getId()));
        }
    }

    private void closeThis() {
        this.mc.displayGuiScreen(null);
        if (this.mc.currentScreen == null) {
            this.mc.setIngameFocus();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        int cx = mouseX - guiLeft - WIDTH / 2;
        int cy = mouseY - guiTop - HEIGHT / 2;
        int q = getSelectedSection(cx, cy);
        if (q != -1) {
            mc.getTextureManager().bindTexture(hilight);
            switch (q) {
                case 0:
                    drawTexturedModalRect(guiLeft + 78, guiTop, 0, 0, 63, 63);
                    break;
                case 1:
                    drawTexturedModalRect(guiLeft + 107, guiTop + 22, 64, 0, 63, 63);
                    break;
                case 2:
                    drawTexturedModalRect(guiLeft + 107, guiTop + 78, 128, 0, 63, 63);
                    break;
                case 3:
                    drawTexturedModalRect(guiLeft + 78, guiTop + 108, 192, 0, 63, 63);
                    break;
                case 4:
                    drawTexturedModalRect(guiLeft + 23, guiTop + 107, 0, 64, 63, 63);
                    break;
                case 5:
                    drawTexturedModalRect(guiLeft, guiTop + 78, 64, 64, 63, 63);
                    break;
                case 6:
                    drawTexturedModalRect(guiLeft, guiTop + 22, 128, 64, 63, 63);
                    break;
                case 7:
                    drawTexturedModalRect(guiLeft + 22, guiTop, 192, 64, 63, 63);
                    break;
            }

            if (q < actions.getActions().size()) {
                String desc = actions.getActions().get(q).getDescription();
                int width = mc.fontRendererObj.getStringWidth(desc);
                int x = guiLeft + (160-width)/2;
                int y = guiTop + HEIGHT;
                RenderHelper.renderText(mc, x, y, desc);
            }
        }
        for (int i = 0 ; i < actions.getActions().size() ; i++) {
            WheelAction action = actions.getActions().get(i);
            mc.getTextureManager().bindTexture(new ResourceLocation(action.getTexture()));
            int v = action.getV() + ((q == i) ? 0 : 64);
            switch (i) {
                case 0: drawTexturedModalRect(guiLeft + 78+8, guiTop+8, action.getU(), v, 31, 31); break;
                case 1: drawTexturedModalRect(guiLeft + 107+12, guiTop + 22+20, action.getU(), v, 31, 31); break;
            }
        }
    }

    private int getSelectedSection(int cx, int cy) {
        double dist = Math.sqrt(cx*cx + cy*cy);
        if (dist < 37 || dist > 80) {
            return -1;
        }

        int q = -1;
        if (cx >= 0 && cy < 0 && Math.abs(cx) < Math.abs(cy)) {
            q = 0;
        } else if (cx >= 0 && cy < 0 && Math.abs(cx) >= Math.abs(cy)) {
            q = 1;
        } else if (cx >= 0 && cy >= 0 && Math.abs(cx) >= Math.abs(cy)) {
            q = 2;
        } else if (cx >= 0 && cy >= 0 && Math.abs(cx) < Math.abs(cy)) {
            q = 3;
        } else if (cx < 0 && cy >= 0 && Math.abs(cx) < Math.abs(cy)) {
            q = 4;
        } else if (cx < 0 && cy >= 0 && Math.abs(cx) >= Math.abs(cy)) {
            q = 5;
        } else if (cx < 0 && cy < 0 && Math.abs(cx) >= Math.abs(cy)) {
            q = 6;
        } else if (cx < 0 && cy < 0 && Math.abs(cx) < Math.abs(cy)) {
            q = 7;
        }
        return q;
    }
}
