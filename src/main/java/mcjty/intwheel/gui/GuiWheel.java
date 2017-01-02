package mcjty.intwheel.gui;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.input.KeyBindings;
import mcjty.intwheel.network.PacketHandler;
import mcjty.intwheel.network.PacketPerformAction;
import mcjty.intwheel.varia.RenderHelper;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiWheel extends GuiScreen {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 160;

    private int guiLeft;
    private int guiTop;

    private final List<WheelActionElement> actions;
    private final BlockPos pos;

    private static final ResourceLocation background = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel.png");
    private static final ResourceLocation hilight = new ResourceLocation(InteractionWheel.MODID, "textures/gui/wheel_hilight.png");

    public GuiWheel(World world) {
        RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mouseOver != null) {
            pos = mouseOver.getBlockPos();
        } else {
            pos = null;
        }
        actions = InteractionWheel.interactionWheelImp.getActions(MinecraftTools.getPlayer(Minecraft.getMinecraft()), world, pos);
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
        return false;
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
            if (q < getActionSize()) {
                performAction(q);
            }
        }
        closeThis();
    }

    private void performAction(int index) {
        WheelActionElement element = actions.get(index);
        IWheelAction action = InteractionWheel.registry.get(element.getId());
        if (action != null) {
            boolean extended = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
            if (action.performClient(MinecraftTools.getPlayer(mc), MinecraftTools.getWorld(mc), pos, extended)) {
                PacketHandler.INSTANCE.sendToServer(new PacketPerformAction(pos, element.getId(), extended));
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

        int cx = mouseX - guiLeft - WIDTH / 2;
        int cy = mouseY - guiTop - HEIGHT / 2;
        int offset = getActionSize() / 2;
        int q = getSelectedSection(cx, cy);
        if (q != -1) {
            drawSelectedSection(offset, q);
            if (q < getActionSize()) {
                drawTooltip(q);
            }
        }
        drawIcons(offset, q);
    }

    private void drawIcons(int offset, int q) {
        for (int i = 0; i < getActionSize(); i++) {
            WheelActionElement action = actions.get(i);
            mc.getTextureManager().bindTexture(new ResourceLocation(action.getTexture()));
            int txtw = action.getTxtw();
            int txth = action.getTxth();
            int u = q == i ? action.getUhigh() : action.getUlow();
            int v = q == i ? action.getVhigh() : action.getVlow();
            int offs = (i - offset + 8) % 8;
            int ox = guiLeft + iconOffsets.get(offs).getLeft();
            int oy = guiTop + iconOffsets.get(offs).getRight();
            RenderHelper.drawTexturedModalRect(ox, oy, u, v, 31, 31, txtw, txth);

            int tx = (int) (guiLeft + 80 + 70 * Math.cos(Math.PI * 2.0 * offs / 8 - Math.PI / 2));
            int ty = (int) (guiTop + 80 + 70 * Math.sin(Math.PI * 2.0 * offs / 8 - Math.PI / 2));
            RenderHelper.renderText(mc, tx, ty, "" + i);
        }
    }

    private void drawTooltip(int q) {
        boolean extended = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        String desc = actions.get(q).getDescription();
        String sneakDesc = actions.get(q).getSneakDescription();
        if (extended && sneakDesc != null) {
            desc = sneakDesc;
        }
        int width = mc.fontRendererObj.getStringWidth(desc);
        int x = guiLeft + (160 - width) / 2;
        int y = guiTop + HEIGHT;
        RenderHelper.renderText(mc, x, y, desc);
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

    private int getActionSize() {
        // @todo, overflow in case there are too many actions
        return Math.min(8, actions.size());
    }

    private int getSelectedSection(int cx, int cy) {
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
        int offset = getActionSize() / 2;
        return (q + offset) % 8;
    }
}
