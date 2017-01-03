package mcjty.intwheel.gui;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.input.KeyBindings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Map;

public class GuiWheelConfig extends GuiScreen {

    private static final int WIDTH = 256;
    private static final int HEIGHT = 220;

    private int guiLeft;
    private int guiTop;

    private static final ResourceLocation background = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel_config.png");

    public GuiWheelConfig() {
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
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int cx = mouseX - guiLeft - WIDTH / 2;
        int cy = mouseY - guiTop - HEIGHT / 2;
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
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
        drawIcons();
    }

    private void drawIcons() {
        Map<String, IWheelAction> actions = InteractionWheel.registry.getActions();
        for (IWheelAction action : actions.values()) {
//
//            mc.getTextureManager().bindTexture(new ResourceLocation(action.getTexture()));
//            int txtw = action.getTxtw();
//            int txth = action.getTxth();
//            int u = q == i ? action.getUhigh() : action.getUlow();
//            int v = q == i ? action.getVhigh() : action.getVlow();
//            int offs = (i - offset + 8) % 8;
//            int ox = guiLeft + iconOffsets.get(offs).getLeft();
//            int oy = guiTop + iconOffsets.get(offs).getRight();
//            RenderHelper.drawTexturedModalRect(ox, oy, u, v, 31, 31, txtw, txth);
//
//            double angle = Math.PI * 2.0 * offs / 8 - Math.PI / 2.0 + Math.PI / 8.0;
//            int tx = (int) (guiLeft + 80 + 86 * Math.cos(angle));
//            int ty = (int) (guiTop + 80 + 86 * Math.sin(angle));
//            RenderHelper.renderText(mc, tx-mc.fontRendererObj.getCharWidth('4')/2, ty-mc.fontRendererObj.FONT_HEIGHT/2, "" + i);
        }
    }

}
