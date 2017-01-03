package mcjty.intwheel.gui;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.input.KeyBindings;
import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import mcjty.intwheel.varia.RenderHelper;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GuiWheelConfig extends GuiScreen {

    private static final int WIDTH = 256;
    private static final int HEIGHT = 204;

    public static final int MARGIN = 4;
    public static final int SIZE = 26;

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

        int cx = mouseX - guiLeft;
        int cy = mouseY - guiTop;

        Map<String, IWheelAction> actions = InteractionWheel.registry.getActions();

        int action = getSelectedAction(cx, cy);
        if (action >= 0 || action < actions.size()) {
            PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(MinecraftTools.getPlayer(mc));
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
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
        drawIcons();
    }

    private void drawIcons() {
        PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(MinecraftTools.getPlayer(mc));

        List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(MinecraftTools.getPlayer(mc));
        int ox = MARGIN;
        int oy = MARGIN;
        for (String id : actions) {
            IWheelAction action = InteractionWheel.registry.get(id);
            WheelActionElement element = action.createElement();
            mc.getTextureManager().bindTexture(new ResourceLocation(element.getTexture()));
            int txtw = element.getTxtw();
            int txth = element.getTxth();
            Boolean enabled = config.isEnabled(action.getId());
            if (enabled == null) {
                enabled = action.isDefaultEnabled();
            }
            int u = enabled ? element.getUhigh() : element.getUlow();
            int v = enabled ? element.getVhigh() : element.getVlow();
            RenderHelper.drawTexturedModalRect(guiLeft + ox, guiTop + oy, u, v, 31, 31, txtw, txth);
            ox += SIZE;
            if (ox > WIDTH) {
                ox = MARGIN;
                oy += SIZE;
            }
//            double angle = Math.PI * 2.0 * offs / 8 - Math.PI / 2.0 + Math.PI / 8.0;
//            int tx = (int) (guiLeft + 80 + 86 * Math.cos(angle));
//            int ty = (int) (guiTop + 80 + 86 * Math.sin(angle));
//            RenderHelper.renderText(mc, tx-mc.fontRendererObj.getCharWidth('4')/2, ty-mc.fontRendererObj.FONT_HEIGHT/2, "" + i);
        }
    }

    private int getSelectedAction(int cx, int cy) {
        if (cx < MARGIN || cy < MARGIN) {
            return -1;
        }
        if (cx > WIDTH || cy > WIDTH) {
            return -1;
        }
        int totw = WIDTH / SIZE;
        int i = (cx - MARGIN) / SIZE + totw * (cy - MARGIN) / SIZE;
        return i;
    }
}
