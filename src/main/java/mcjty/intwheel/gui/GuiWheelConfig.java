package mcjty.intwheel.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import mcjty.intwheel.varia.RenderHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class GuiWheelConfig extends Screen {

    private static final int WIDTH = 256;
    private static final int HEIGHT = 204;

    public static final int MARGIN = 4;
    public static final int SIZE = 30;

    private int guiLeft;
    private int guiTop;

    private static final ResourceLocation background = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel_config.png");

    public GuiWheelConfig() {
        super(Component.literal("Config"));
    }

    @Override
    public void init() {
        guiLeft = (this.width - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;
    }

    private int getRelativeX() {
        int windowWidth = getMinecraft().getWindow().getScreenWidth();
        if (windowWidth == 0) {
            return 0;
        } else {
            return (int) getMinecraft().mouseHandler.xpos() * width / windowWidth;
        }
    }

    private int getRelativeY() {
        int windowHeight = getMinecraft().getWindow().getScreenHeight();
        if (windowHeight == 0) {
            return 0;
        } else {
            return (int) getMinecraft().mouseHandler.ypos() * height / windowHeight;
        }
    }

    @Override
    public boolean charTyped(char typedChar, int modifiers) {
        if (!super.charTyped(typedChar, modifiers)) {
            int mouseX = getRelativeX();
            int mouseY = getRelativeY();
            int cx = mouseX - guiLeft;
            int cy = mouseY - guiTop;

            String id = getSelectedActionID(cx, cy);
            if (id != null) {
                PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(minecraft.player).map(s -> s).get();  // @todo not proper
                if ((typedChar >= 'a' && typedChar <= 'z')) {
                    config.getHotkeys().put(id, typedChar);
                    config.sendToServer();
                }
            }
        }
        return false;
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);

        int mouseX = getRelativeX();
        int mouseY = getRelativeY();
        int cx = mouseX - guiLeft;
        int cy = mouseY - guiTop;

        String id = getSelectedActionID(cx, cy);
        if (id != null) {
            PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(minecraft.player).map(s -> s).get();  // @todo not proper
            if (keyCode == InputConstants.KEY_LEFT) {
                List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(minecraft.player);
                int idx = actions.indexOf(id);
                if (idx > 0) {
                    String idprev = actions.get(idx - 1);
                    actions.set(idx - 1, id);
                    actions.set(idx, idprev);

                    config.setOrderActions(actions);
                    config.sendToServer();
                }
            } else if (keyCode == InputConstants.KEY_RIGHT) {
                List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(minecraft.player);
                int idx = actions.indexOf(id);
                if (idx < actions.size()-1) {
                    String idnext = actions.get(idx+1);
                    actions.set(idx+1, id);
                    actions.set(idx, idnext);

                    config.setOrderActions(actions);
                    config.sendToServer();
                }
            } else if (keyCode == InputConstants.KEY_HOME) {
                List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(minecraft.player);
                int idx = actions.indexOf(id);
                if (idx > 0) {
                    String idnext = actions.get(0);
                    actions.set(0, id);
                    actions.set(idx, idnext);

                    config.setOrderActions(actions);
                    config.sendToServer();
                }
            } else if (keyCode == InputConstants.KEY_END) {
                List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(minecraft.player);
                int idx = actions.indexOf(id);
                if (idx < actions.size()-1) {
                    String idnext = actions.get(actions.size()-1);
                    actions.set(actions.size()-1, id);
                    actions.set(idx, idnext);

                    config.setOrderActions(actions);
                    config.sendToServer();
                }
            }
            else if (keyCode == InputConstants.KEY_DELETE || keyCode == InputConstants.KEY_BACKSPACE) {
                config.getHotkeys().remove(id);
                config.sendToServer();
            }
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int cx = (int) (mouseX - guiLeft);
        int cy = (int) (mouseY - guiTop);

        List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(minecraft.player);

        int selected = getSelectedAction(cx, cy);
        if (selected >= 0 && selected < actions.size()) {
            PlayerProperties.getWheelConfig(minecraft.player).ifPresent(config -> {
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
            });
        }
        return true;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        RenderSystem.setShaderTexture(0, background);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderHelper.drawTexturedModalRect(poseStack, guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
        drawIcons(poseStack);

        int cx = mouseX - guiLeft;
        int cy = mouseY - guiTop;
        String id = getSelectedActionID(cx, cy);
        if (id != null) {
            drawTooltip(poseStack, id);
        }

    }

    private void renderTooltipText(PoseStack poseStack, String desc, int dy) {
//        int width = mc.fontRendererObj.getStringWidth(desc);
        int x = guiLeft + 5;//(WIDTH - width) / 2;
        int y = guiTop + 157 + 1 + dy;
        RenderHelper.renderText(poseStack, x, y, desc);
    }

    private void drawTooltip(PoseStack poseStack, String id) {
        IWheelAction action = InteractionWheel.registry.get(id);
        if (action != null) {
            WheelActionElement element = action.createElement();
            String desc = element.getDescription();
//            String sneakDesc = element.getSneakDescription();
//            if (extended && sneakDesc != null) {
//                desc = sneakDesc;
//            }
            renderTooltipText(poseStack, ChatFormatting.AQUA + id + ": " + ChatFormatting.WHITE + desc, 0);
            renderTooltipText(poseStack, ChatFormatting.YELLOW + "Click to enable/disable this action", 10);
            renderTooltipText(poseStack, ChatFormatting.YELLOW + "Press 'a' to 'z' to assign hotkey ('del' to remove hotkey)", 20);
            renderTooltipText(poseStack, ChatFormatting.YELLOW + "Arrows and home/end to order actions", 30);
        }
    }


    private void drawIcons(PoseStack poseStack) {
        PlayerProperties.getWheelConfig(minecraft.player).ifPresent(config -> {
            Map<String, Character> hotkeys = config.getHotkeys();

            List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(minecraft.player);
            int ox = 0;
            int oy = 0;
            for (String id : actions) {
                IWheelAction action = InteractionWheel.registry.get(id);
                WheelActionElement element = action.createElement();
                RenderSystem.setShaderTexture(0, new ResourceLocation(element.getTexture()));
                int txtw = element.getTxtw();
                int txth = element.getTxth();
                Boolean enabled = config.isEnabled(action.getId());
                if (enabled == null) {
                    enabled = action.isDefaultEnabled();
                }
                int u = enabled ? element.getUhigh() : element.getUlow();
                int v = enabled ? element.getVhigh() : element.getVlow();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                RenderHelper.drawTexturedModalRect(poseStack, guiLeft + ox * SIZE + MARGIN, guiTop + oy * SIZE + MARGIN, u, v, 31, 31, txtw, txth);

                if (hotkeys.containsKey(id)) {
                    String keyName = "" + hotkeys.get(id);
                    RenderHelper.renderText(poseStack, guiLeft + ox * SIZE + MARGIN + 1, guiTop + oy * SIZE + MARGIN + 1, keyName);
                }

                ox++;
                if (ox >= 8) {
                    ox = 0;
                    oy++;
                }
            }
        });
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
        List<String> actions = InteractionWheel.interactionWheelImp.getSortedActions(minecraft.player);
        int selected = getSelectedAction(cx, cy);
        if (selected >= 0 && selected < actions.size()) {
            return actions.get(selected);
        }
        return null;
    }
}
