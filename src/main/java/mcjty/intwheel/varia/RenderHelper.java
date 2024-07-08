package mcjty.intwheel.varia;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class RenderHelper {
    public static float rot = 0.0f;

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     * x2 and y2 are not included.
     */
    public static void drawVerticalGradientRect(int x1, int y1, int x2, int y2, int color1, int color2) {
//        this.zLevel = 300.0F;
        float zLevel = 0.0f;

        float f = (color1 >> 24 & 255) / 255.0F;
        float f1 = (color1 >> 16 & 255) / 255.0F;
        float f2 = (color1 >> 8 & 255) / 255.0F;
        float f3 = (color1 & 255) / 255.0F;
        float f4 = (color2 >> 24 & 255) / 255.0F;
        float f5 = (color2 >> 16 & 255) / 255.0F;
        float f6 = (color2 >> 8 & 255) / 255.0F;
        float f7 = (color2 & 255) / 255.0F;
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.addVertex(x2, y1, zLevel).setColor(f1, f2, f3, f);
        buffer.addVertex(x1, y1, zLevel).setColor(f1, f2, f3, f);
        buffer.addVertex(x1, y2, zLevel).setColor(f5, f6, f7, f4);
        buffer.addVertex(x2, y2, zLevel).setColor(f5, f6, f7, f4);
        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.disableBlend();
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(GuiGraphics graphics, int x, int y, int u, int v, int width, int height) {
        PoseStack poseStack = graphics.pose();
        Matrix4f matrix = poseStack.last().pose();
        float zLevel = 0.01f;
        float f = (1 / 256.0f);
        float f1 = (1 / 256.0f);
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(matrix, (x + 0), (y + height), zLevel).setUv(((u + 0) * f), ((v + height) * f1));
        buffer.addVertex(matrix, (x + width), (y + height), zLevel).setUv(((u + width) * f), ((v + height) * f1));
        buffer.addVertex(matrix, (x + width), (y + 0), zLevel).setUv(((u + width) * f), ((v + 0) * f1));
        buffer.addVertex(matrix, (x + 0), (y + 0), zLevel).setUv(((u + 0) * f), ((v + 0) * f1));
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, r, g, b, a, width, height
     */
    public static void drawTexturedModalRect(GuiGraphics graphics, int x, int y, int u, int v, float r, float g, float b, float a, int width, int height) {
        PoseStack poseStack = graphics.pose();
        Matrix4f matrix = poseStack.last().pose();
        float zLevel = 0.01f;
        float f = (1 / 256.0f);
        float f1 = (1 / 256.0f);
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.addVertex(matrix, (x + 0), (y + height), zLevel).setUv(((u + 0) * f), ((v + height) * f1)).setColor(r, g, b, a);
        buffer.addVertex(matrix, (x + width), (y + height), zLevel).setUv(((u + width) * f), ((v + height) * f1)).setColor(r, g, b, a);
        buffer.addVertex(matrix, (x + width), (y + 0), zLevel).setUv(((u + width) * f), ((v + 0) * f1)).setColor(r, g, b, a);
        buffer.addVertex(matrix, (x + 0), (y + 0), zLevel).setUv(((u + 0) * f), ((v + 0) * f1)).setColor(r, g, b, a);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(GuiGraphics graphics, int x, int y, int u, int v, int width, int height, int txtw, int txth) {
        PoseStack poseStack = graphics.pose();
        Matrix4f matrix = poseStack.last().pose();
        float zLevel = 0.01f;
        float f = (1.0f / txtw);
        float f1 = (1.0f / txth);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(matrix, (x + 0), (y + height), zLevel).setUv(((u + 0) * f), ((v + height) * f1));
        buffer.addVertex(matrix, (x + width), (y + height), zLevel).setUv(((u + width) * f), ((v + height) * f1));
        buffer.addVertex(matrix, (x + width), (y + 0), zLevel).setUv(((u + width) * f), ((v + 0) * f1));
        buffer.addVertex(matrix, (x + 0), (y + 0), zLevel).setUv(((u + 0) * f), ((v + 0) * f1));
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    public static int renderText(GuiGraphics graphics, int x, int y, String txt) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1f);

        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(0.0F, 0.0F, 32.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager._disableDepthTest();
        GlStateManager._disableBlend();
        Minecraft mc = Minecraft.getInstance();
        int width = mc.font.width(txt);
        graphics.drawString(mc.font, txt, x, y, 16777215, false);
        GlStateManager._enableDepthTest();
        // Fixes opaque cooldown overlay a bit lower
        // TODO: check if enabled blending still screws things up down the line.
        GlStateManager._enableBlend();


        matrixStack.popPose();

        return width;
    }
}
