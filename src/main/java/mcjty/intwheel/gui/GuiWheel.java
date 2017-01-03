package mcjty.intwheel.gui;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.input.KeyBindings;
import mcjty.intwheel.network.PacketHandler;
import mcjty.intwheel.network.PacketPerformAction;
import mcjty.intwheel.network.PacketRequestConfig;
import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import mcjty.intwheel.proxy.GuiProxy;
import mcjty.intwheel.varia.RenderHelper;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiWheel extends GuiScreen {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 160;

    private int guiLeft;
    private int guiTop;

    private BlockPos pos;

    private static final ResourceLocation background = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel.png");
    private static final ResourceLocation hilight = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel_hilight.png");

    public GuiWheel() {
        RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mouseOver != null) {
            pos = mouseOver.getBlockPos();
        } else {
            pos = null;
        }
    }

    private List<String> getActions() {
        return InteractionWheel.interactionWheelImp.getActions(MinecraftTools.getPlayer(Minecraft.getMinecraft()), MinecraftTools.getWorld(Minecraft.getMinecraft()), pos);
    }

    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (this.width - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;

        PacketHandler.INSTANCE.sendToServer(new PacketRequestConfig());
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (Keyboard.isKeyDown(KeyBindings.keyOpenWheel.getKeyCode())) {
            closeThis();
        } else if (typedChar >= 'a' && typedChar <= 'z') {
            PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(MinecraftTools.getPlayer(mc));
            Map<String, Integer> hotkeys = config.getHotkeys();
            List<String> actions = getActions();
            for (String action : actions) {
                if (hotkeys.containsKey(action)) {
                    if (hotkeys.get(action) == keyCode) {
                        performAction(action);
                        closeThis();
                        return;
                    }
                }
            }
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        List<String> actions = getActions();

        int cx = mouseX - guiLeft - WIDTH / 2;
        int cy = mouseY - guiTop - HEIGHT / 2;

        int q = getSelectedSection(actions, cx, cy);
        if (q == -2) {
            EntityPlayerSP player = MinecraftTools.getPlayer(mc);
            player.openGui(InteractionWheel.instance, GuiProxy.GUI_CONFIG, player.getEntityWorld(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
            return;
        } else if (q == -1) {
            closeThis();
        } else {
            if (q < getActionSize(actions)) {
                performAction(actions, q);
            }
        }
        closeThis();
    }

    private void performAction(List<String> actions, int index) {
        String id = actions.get(index);
        performAction(id);
    }

    private void performAction(String id) {
        IWheelAction action = InteractionWheel.registry.get(id);
        if (action != null) {
            boolean extended = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
            if (action.performClient(MinecraftTools.getPlayer(mc), MinecraftTools.getWorld(mc), pos, extended)) {
                PacketHandler.INSTANCE.sendToServer(new PacketPerformAction(pos, id, extended));
            }
        }
    }

    private void closeThis() {
        this.mc.displayGuiScreen(null);
        if (this.mc.currentScreen == null) {
            this.mc.setIngameFocus();
        }
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        List<String> actions = getActions();
        int cx = mouseX - guiLeft - WIDTH / 2;
        int cy = mouseY - guiTop - HEIGHT / 2;
        int offset = getActionSize(actions) / 2;
        int q = getSelectedSection(actions, cx, cy);
        if (q == -2) {
            drawSelectedButton(q);
            renderTooltipText("Click for configuration");
        } else if (q != -1) {
            drawSelectedSection(offset, q);
            if (q < getActionSize(actions)) {
                drawTooltip(actions, q);
            }
        }
        drawIcons(actions, offset, q);
    }

    private void drawIcons(List<String> actions, int offset, int q) {
        PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(MinecraftTools.getPlayer(mc));
        Map<String, Integer> hotkeys = config.getHotkeys();

        for (int i = 0; i < getActionSize(actions); i++) {
            String id = actions.get(i);
            IWheelAction action = InteractionWheel.registry.get(id);
            if (action != null) {
                WheelActionElement element = action.createElement();
                mc.getTextureManager().bindTexture(new ResourceLocation(element.getTexture()));
                int txtw = element.getTxtw();
                int txth = element.getTxth();
                boolean selected = q == i;
                int u = selected ? element.getUhigh() : element.getUlow();
                int v = selected ? element.getVhigh() : element.getVlow();
                int offs = (i - offset + 8) % 8;
                int ox = guiLeft + iconOffsets.get(offs).getLeft();
                int oy = guiTop + iconOffsets.get(offs).getRight();
                RenderHelper.drawTexturedModalRect(ox, oy, u, v, 31, 31, txtw, txth);

                if (selected && hotkeys.containsKey(id)) {
                    double angle = Math.PI * 2.0 * offs / 8 - Math.PI / 2.0 + Math.PI / 8.0;
                    int tx = (int) (guiLeft + 80 + 86 * Math.cos(angle));
                    int ty = (int) (guiTop + 80 + 86 * Math.sin(angle));
                    String keyName = Keyboard.getKeyName(hotkeys.get(id));
                    RenderHelper.renderText(mc, tx - mc.fontRendererObj.getCharWidth(keyName.charAt(0)) / 2, ty - mc.fontRendererObj.FONT_HEIGHT / 2, keyName);
                }
            }
        }
    }

    private void renderTooltipText(String desc) {
        int width = mc.fontRendererObj.getStringWidth(desc);
        int x = guiLeft + (160 - width) / 2;
        int y = guiTop + HEIGHT + 5;
        RenderHelper.renderText(mc, x, y, desc);
    }

    private void drawTooltip(List<String> actions, int q) {
        boolean extended = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        String id = actions.get(q);
        IWheelAction action = InteractionWheel.registry.get(id);
        if (action != null) {
            WheelActionElement element = action.createElement();
            String desc = element.getDescription();
            String sneakDesc = element.getSneakDescription();
            if (extended && sneakDesc != null) {
                desc = sneakDesc;
            }
            renderTooltipText(desc);
        }
    }

    private void drawSelectedButton(int q) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft+74, guiTop+74, 74, 74, 12, 12);
    }

    private void drawSelectedSection(int offset, int q) {
        mc.getTextureManager().bindTexture(hilight);
        switch ((q - offset + 8) % 8) {
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
    }

    private int getActionSize(List<String> actions) {
        // @todo, overflow in case there are too many actions
        return Math.min(8, actions.size());
    }

    private int getSelectedSection(List<String> actions, int cx, int cy) {
        if (Math.abs(cx) < 6 && Math.abs(cy) < 6) {
            return -2;
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
