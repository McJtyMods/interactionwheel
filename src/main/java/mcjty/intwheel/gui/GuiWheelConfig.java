package mcjty.intwheel.gui;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import mcjty.intwheel.varia.RenderHelper;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GuiWheelConfig extends GuiScreen {

    private static final int WIDTH = 256;
    private static final int HEIGHT = 204;

    public static final int MARGIN = 4;
    public static final int SIZE = 30;

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

        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int cx = mouseX - guiLeft;
        int cy = mouseY - guiTop;

        String id = getSelectedActionID(cx, cy);
        if (id != null) {
            PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(MinecraftTools.getPlayer(mc));
            if (keyCode == Keyboard.KEY_LEFT) {
                List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(MinecraftTools.getPlayer(mc));
                int idx = actions.indexOf(id);
                if (idx > 0) {
                    String idprev = actions.get(idx - 1);
                    actions.set(idx - 1, id);
                    actions.set(idx, idprev);

                    config.setOrderActions(actions);
                    config.sendToServer();
                }
            } else if (keyCode == Keyboard.KEY_RIGHT) {
                List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(MinecraftTools.getPlayer(mc));
                int idx = actions.indexOf(id);
                if (idx < actions.size()-1) {
                    String idnext = actions.get(idx+1);
                    actions.set(idx+1, id);
                    actions.set(idx, idnext);

                    config.setOrderActions(actions);
                    config.sendToServer();
                }
            } else if (keyCode == Keyboard.KEY_HOME) {
                List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(MinecraftTools.getPlayer(mc));
                int idx = actions.indexOf(id);
                if (idx > 0) {
                    String idnext = actions.get(0);
                    actions.set(0, id);
                    actions.set(idx, idnext);

                    config.setOrderActions(actions);
                    config.sendToServer();
                }
            } else if (keyCode == Keyboard.KEY_END) {
                List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(MinecraftTools.getPlayer(mc));
                int idx = actions.indexOf(id);
                if (idx < actions.size()-1) {
                    String idnext = actions.get(actions.size()-1);
                    actions.set(actions.size()-1, id);
                    actions.set(idx, idnext);

                    config.setOrderActions(actions);
                    config.sendToServer();
                }
            } else if ((typedChar >= 'a' && typedChar <= 'z') || keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK) {
                if (keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK) {
                    config.getHotkeys().remove(id);
                } else {
                    config.getHotkeys().put(id, keyCode);
                }
                config.sendToServer();
            }
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

        List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(MinecraftTools.getPlayer(mc));

        int selected = getSelectedAction(cx, cy);
        if (selected >= 0 && selected < actions.size()) {
            PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(MinecraftTools.getPlayer(mc));
            String id = actions.get(selected);
            IWheelAction action = InteractionWheel.registry.get(id);
            if (action != null) {
                Boolean enabled = config.isEnabled(id);
                if (enabled == null) {
                    enabled = action.isDefaultEnabled();
                }
                if (enabled) {
                    config.disable(id);
                } else {
                    config.enable(id);
                }
                config.sendToServer();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
        drawIcons();

        int cx = mouseX - guiLeft;
        int cy = mouseY - guiTop;
        String id = getSelectedActionID(cx, cy);
        if (id != null) {
            drawTooltip(id);
        }

    }

    private void renderTooltipText(String desc, int dy) {
//        int width = mc.fontRendererObj.getStringWidth(desc);
        int x = guiLeft + 5;//(WIDTH - width) / 2;
        int y = guiTop + 157 + 1 + dy;
        RenderHelper.renderText(mc, x, y, desc);
    }

    private void drawTooltip(String id) {
        IWheelAction action = InteractionWheel.registry.get(id);
        if (action != null) {
            WheelActionElement element = action.createElement();
            String desc = element.getDescription();
//            String sneakDesc = element.getSneakDescription();
//            if (extended && sneakDesc != null) {
//                desc = sneakDesc;
//            }
            renderTooltipText(desc, 0);
            renderTooltipText(TextFormatting.YELLOW + "Click to enable/disable this action", 10);
            renderTooltipText(TextFormatting.YELLOW + "Press 'a' to 'z' to assign hotkey ('del' to remove hotkey)", 20);
            renderTooltipText(TextFormatting.YELLOW + "Arrows and home/end to order actions", 30);
        }
    }


    private void drawIcons() {
        PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(MinecraftTools.getPlayer(mc));
        Map<String, Integer> hotkeys = config.getHotkeys();

        List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(MinecraftTools.getPlayer(mc));
        int ox = 0;
        int oy = 0;
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
            RenderHelper.drawTexturedModalRect(guiLeft + ox * SIZE + MARGIN, guiTop + oy * SIZE + MARGIN, u, v, 31, 31, txtw, txth);

            if (hotkeys.containsKey(id)) {
                String keyName = Keyboard.getKeyName(hotkeys.get(id));
                RenderHelper.renderText(mc, guiLeft + ox * SIZE + MARGIN +1, guiTop + oy * SIZE + MARGIN +1, keyName);
            }

            ox++;
            if (ox >= 8) {
                ox = 0;
                oy++;
            }
        }
    }

    private int getSelectedAction(int cx, int cy) {
        if (cx < MARGIN || cy < MARGIN) {
            return -1;
        }
        if (cx > WIDTH || cy > WIDTH) {
            return -1;
        }
        int totw = 8;
        int i = (cx - MARGIN) / SIZE + totw * ((cy - MARGIN) / SIZE);
        return i;
    }

    private String getSelectedActionID(int cx, int cy) {
        List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(MinecraftTools.getPlayer(mc));
        int selected = getSelectedAction(cx, cy);
        if (selected >= 0 && selected < actions.size()) {
            return actions.get(selected);
        }
        return null;
    }
}
