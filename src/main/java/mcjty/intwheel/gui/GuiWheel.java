package mcjty.intwheel.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.input.KeyBindings;
import mcjty.intwheel.network.PacketHandler;
import mcjty.intwheel.network.PacketPerformAction;
import mcjty.intwheel.network.PacketRequestConfig;
import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.varia.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mojang.blaze3d.platform.InputConstants.KEY_SPACE;

public class GuiWheel extends Screen {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 160;

    public static final int BUTTON_CONFIG = -2;
    public static final int BUTTON_LEFT = -3;
    public static final int BUTTON_RIGHT = -4;
    public static final float BASE_RED = 0.0f;
    public static final float BASE_GREEN = 0.9f;
    public static final float BASE_BLUE = 0.6f;
    public static final float BASE_ALPHA = 0.4f;

    private int guiLeft;
    private int guiTop;

    private final BlockPos pos;

    private int page = 0;
    private int pages = 1;

    private static final ResourceLocation background = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel.png");
    private static final ResourceLocation hilight = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel_hilight.png");

    // Set to >0 to close this (with a delay)
    private int closeMe = 0;

    public GuiWheel() {
        super(Component.literal("Wheel"));
        HitResult mouseOver = Minecraft.getInstance().hitResult;
        if (mouseOver instanceof BlockHitResult blockHitResult) {
            pos = blockHitResult.getBlockPos();
        } else {
            pos = null;
        }
    }

    private List<String> getActions() {
        return InteractionWheel.interactionWheelImp.getActions(Minecraft.getInstance().player, Minecraft.getInstance().level, pos);
    }

    @Override
    protected void init() {
        closeMe = 0;
        guiLeft = (this.width - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;

        page = 0;
        pages = 1;
        PacketHandler.INSTANCE.sendToServer(new PacketRequestConfig());
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        super.charTyped(codePoint, modifiers);
        if ((codePoint >= 'a' && codePoint <= 'z') || (codePoint >= 'A' && codePoint <= 'Z')) {
            PlayerProperties.getWheelConfig(minecraft.player).ifPresent(config -> {
                Map<String, Character> hotkeys = config.getHotkeys();
                List<String> actions = getActions();
                for (String action : actions) {
                    if (hotkeys.containsKey(action)) {
                        if (hotkeys.get(action) == codePoint) {
                            performAction(action);
                            closeThis(5);
                            return;
                        }
                    }
                }
            });
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (KeyBindings.keyOpenWheel.matches(keyCode, scanCode)) {
            KeyBindings.keyOpenWheel.consumeClick();
            closeThis(2);
        } else if (scanCode == KEY_SPACE) {
            page++;
            if (page >= pages) {
                page = 0;
            }
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        List<String> actions = getActions();

        int cx = (int) (mouseX - guiLeft - WIDTH / 2);
        int cy = (int) (mouseY - guiTop - HEIGHT / 2);

        int q = getSelectedSection(actions, cx, cy);
        if (q == BUTTON_CONFIG) {
            minecraft.setScreen(new GuiWheelConfig());
            return true;
        } else if (q == BUTTON_LEFT) {
            page--;
            if (page < 0) {
                page = pages - 1;
                if (page < 0) {
                    page = 0;
                }
            }
            return true;
        } else if (q == BUTTON_RIGHT) {
            page++;
            if (page > pages - 1) {
                page = 0;
            }
            return true;
        } else if (q == -1) {
            closeThis(2);
        } else {
            if (q < getActionSize(actions)) {
                performAction(actions, q);
            }
        }
        closeThis(2);
        return true;
    }

    private void performAction(List<String> actions, int index) {
        String id = actions.get(index + page * 8);
        performAction(id);
    }

    private void performAction(String id) {
        IWheelAction action = InteractionWheel.registry.get(id);
        if (action != null) {
            boolean extended = Screen.hasShiftDown();
            if (action.performClient(minecraft.player, minecraft.level, pos, extended)) {
                PacketHandler.INSTANCE.sendToServer(new PacketPerformAction(pos, id, extended));
            }
        }
    }

    private void closeThis(int amount) {
        closeMe = amount;
//        this.minecraft.setScreen(null);
    }

    private static final List<Pair<Integer, Integer>> iconOffsets = new ArrayList<>();

    static {
        iconOffsets.add(Pair.of(78 + 8, 8));
        iconOffsets.add(Pair.of(107 + 12, 22 + 19));
        iconOffsets.add(Pair.of(107 + 12, 78 + 9));
        iconOffsets.add(Pair.of(78 + 9, 108 + 11));
        iconOffsets.add(Pair.of(23 + 18, 107 + 11));
        iconOffsets.add(Pair.of(0 + 10, 78 + 9));
        iconOffsets.add(Pair.of(0 + 9, 22 + 19));
        iconOffsets.add(Pair.of(22 + 19, 8));
    }

    @Override
    public void tick() {
        if (closeMe > 0) {
            closeMe--;
            if (closeMe <= 0) {
                minecraft.setScreen(null);
                closeMe = 0;
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        RenderSystem.setShaderTexture(0, background);
        RenderSystem.setShaderColor(BASE_RED, BASE_GREEN, BASE_BLUE, BASE_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderHelper.drawTexturedModalRect(poseStack, guiLeft, guiTop, 0, 0, BASE_RED, BASE_GREEN, BASE_BLUE, 1.0f, WIDTH, HEIGHT);

        List<String> actions = getActions();
        pages = actions.isEmpty() ? 0 : ((actions.size() - 1) / 8 + 1);
        if (page >= pages) {
            page = 0;
        }
        if (pages > 1) {
            renderPageText(poseStack, (page + 1) + " / " + pages);
        }

        int cx = mouseX - guiLeft - WIDTH / 2;
        int cy = mouseY - guiTop - HEIGHT / 2;
        int offset = getActionSize(actions) / 2;
        int q = getSelectedSection(actions, cx, cy);
        if (q == BUTTON_CONFIG) {
            highlightConfigButton(poseStack);
            renderTooltipText(poseStack, "Click for configuration");
        } else if (q == BUTTON_LEFT) {
            highlightLeftButton(poseStack);
            renderTooltipText(poseStack, "Go to previous page");
        } else if (q == BUTTON_RIGHT) {
            highlightRightButton(poseStack);
            renderTooltipText(poseStack, "Go to next page");
        } else if (q != -1) {
            drawSelectedSection(poseStack, offset, q);
            if (q < getActionSize(actions)) {
                drawTooltip(poseStack, actions, q);
            }
        }
        drawIcons(poseStack, actions, offset, q);
    }

    private void drawIcons(PoseStack poseStack, List<String> actions, int offset, int q) {
        PlayerProperties.getWheelConfig(minecraft.player).ifPresent(config -> {
            Map<String, Character> hotkeys = config.getHotkeys();

            for (int i = 0; i < getActionSize(actions); i++) {
                String id = actions.get(i + page * 8);
                IWheelAction action = InteractionWheel.registry.get(id);
                if (action != null) {
                    WheelActionElement element = action.createElement();
                    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                    RenderSystem.setShaderColor(BASE_RED, BASE_GREEN, BASE_BLUE, i == q ? 0.7f : BASE_ALPHA);
                    RenderSystem.setShaderTexture(0, new ResourceLocation(element.getTexture()));
                    int txtw = element.getTxtw();
                    int txth = element.getTxth();
                    boolean selected = q == i;
                    int u = selected ? element.getUhigh() : element.getUlow();
                    int v = selected ? element.getVhigh() : element.getVlow();
                    int offs = (i - offset + 8) % 8;
                    int ox = guiLeft + iconOffsets.get(offs).getLeft();
                    int oy = guiTop + iconOffsets.get(offs).getRight();
                    RenderHelper.drawTexturedModalRect(poseStack, ox, oy, u, v, 31, 31, txtw, txth);

                    if (selected && hotkeys.containsKey(id)) {
                        double angle = Math.PI * 2.0 * offs / 8 - Math.PI / 2.0 + Math.PI / 8.0;
                        int tx = (int) (guiLeft + 80 + 86 * Math.cos(angle));
                        int ty = (int) (guiTop + 80 + 86 * Math.sin(angle));
                        String keyName = "" + hotkeys.get(id);
//                        String keyName = Keyboard.getKeyName(hotkeys.get(id));
                        RenderHelper.renderText(poseStack, tx - minecraft.font.width("" + keyName.charAt(0)) / 2, ty - minecraft.font.lineHeight / 2, keyName);
                    }
                }
            }
        });
    }

    private void renderTooltipText(PoseStack poseStack, String desc) {
        int width = minecraft.font.width(desc);
        int x = guiLeft + (160 - width) / 2;
        int y = guiTop + HEIGHT + 5;
        RenderHelper.renderText(poseStack, x, y, desc);
    }

    private void renderPageText(PoseStack poseStack, String desc) {
        int width = minecraft.font.width(desc);
        int x = guiLeft + (160 - width) / 2;
        int y = guiTop + 90;
        RenderHelper.renderText(poseStack, x, y, desc);
    }

    private void drawTooltip(PoseStack poseStack, List<String> actions, int q) {
        boolean extended = Screen.hasShiftDown();
        String id = actions.get(q + page * 8);
        IWheelAction action = InteractionWheel.registry.get(id);
        if (action != null) {
            WheelActionElement element = action.createElement();
            String desc = element.getDescription();
            String sneakDesc = element.getSneakDescription();
            if (extended && sneakDesc != null) {
                desc = sneakDesc;
            }
            renderTooltipText(poseStack, desc);
        }
    }

    private void highlightConfigButton(PoseStack poseStack) {
        RenderSystem.setShaderTexture(0, background);
        RenderSystem.setShaderColor(BASE_RED, BASE_GREEN, BASE_BLUE, BASE_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderHelper.drawTexturedModalRect(poseStack, guiLeft + 74, guiTop + 74, 74, 74, 12, 12);
    }

    private void highlightLeftButton(PoseStack poseStack) {
        RenderSystem.setShaderTexture(0, background);
        RenderSystem.setShaderColor(BASE_RED, BASE_GREEN, BASE_BLUE, BASE_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderHelper.drawTexturedModalRect(poseStack, guiLeft + 60, guiTop + 75, 60, 75, 10, 10);
    }

    private void highlightRightButton(PoseStack poseStack) {
        RenderSystem.setShaderTexture(0, background);
        RenderSystem.setShaderColor(BASE_RED, BASE_GREEN, BASE_BLUE, BASE_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderHelper.drawTexturedModalRect(poseStack, guiLeft + 90, guiTop + 75, 90, 75, 10, 10);
    }

    private void drawSelectedSection(PoseStack poseStack, int offset, int q) {
        RenderSystem.setShaderTexture(0, hilight);
        RenderSystem.setShaderColor(BASE_RED, BASE_GREEN, BASE_BLUE, 0.7f);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        switch ((q - offset + 8) % 8) {
            case 0 -> RenderHelper.drawTexturedModalRect(poseStack, guiLeft + 78, guiTop, 0, 0, 63, 63);
            case 1 -> RenderHelper.drawTexturedModalRect(poseStack, guiLeft + 107, guiTop + 22, 64, 0, 63, 63);
            case 2 -> RenderHelper.drawTexturedModalRect(poseStack, guiLeft + 107, guiTop + 78, 128, 0, 63, 63);
            case 3 -> RenderHelper.drawTexturedModalRect(poseStack, guiLeft + 78, guiTop + 108, 192, 0, 63, 63);
            case 4 -> RenderHelper.drawTexturedModalRect(poseStack, guiLeft + 23, guiTop + 107, 0, 64, 63, 63);
            case 5 -> RenderHelper.drawTexturedModalRect(poseStack, guiLeft, guiTop + 78, 64, 64, 63, 63);
            case 6 -> RenderHelper.drawTexturedModalRect(poseStack, guiLeft, guiTop + 22, 128, 64, 63, 63);
            case 7 -> RenderHelper.drawTexturedModalRect(poseStack, guiLeft + 22, guiTop, 192, 64, 63, 63);
        }
    }

    private int getActionSize(List<String> actions) {
        // @todo, overflow in case there are too many actions
        int s = actions.size();
        if (s == 0) {
            return s;
        }
        s -= page * 8;
        return Math.min(8, s);
    }

    private int getSelectedSection(List<String> actions, int cx, int cy) {
        if (Math.abs(cx) < 6 && Math.abs(cy) < 6) {
            return BUTTON_CONFIG;
        }

        if (Math.abs(cy) < 5 && (cx > -20 && cx < -10)) {
            return BUTTON_LEFT;
        }
        if (Math.abs(cy) < 5 && (cx > 10 && cx < 20)) {
            return BUTTON_RIGHT;
        }

        double dist = Math.sqrt(cx * cx + cy * cy);
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
        int offset = getActionSize(actions) / 2;
        return (q + offset) % 8;
    }
}
