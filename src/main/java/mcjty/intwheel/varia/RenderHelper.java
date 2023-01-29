package mcjty.intwheel.varia;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;

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
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(x2, y1, zLevel).color(f1, f2, f3, f).endVertex();
        buffer.vertex(x1, y1, zLevel).color(f1, f2, f3, f).endVertex();
        buffer.vertex(x1, y2, zLevel).color(f5, f6, f7, f4).endVertex();
        buffer.vertex(x2, y2, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.end();

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static void drawTexturedModalRect(PoseStack poseStack, VertexConsumer builder, int x, int y, int textureX, int textureY, int width, int height, int totw, int toth, float parentU, float parentV) {
        Matrix4f matrix = poseStack.last().pose();
        float f = 1.0f / totw;
        float f1 = 1.0f / toth;
        float zLevel = 50;
        builder.vertex(matrix, (x + 0), (y + height), zLevel).uv(parentU + ((textureX + 0) * f), parentV + ((textureY + height) * f1)).endVertex();
        builder.vertex(matrix, (x + width), (y + height), zLevel).uv(parentU + ((textureX + width) * f), parentV + ((textureY + height) * f1)).endVertex();
        builder.vertex(matrix, (x + width), (y + 0), zLevel).uv(parentU + ((textureX + width) * f), parentV + ((textureY + 0) * f1)).endVertex();
        builder.vertex(matrix, (x + 0), (y + 0), zLevel).uv(parentU + ((textureX + 0) * f), parentV + ((textureY + 0) * f1)).endVertex();
    }


    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(PoseStack poseStack, int x, int y, int u, int v, int width, int height) {
        Matrix4f matrix = poseStack.last().pose();
        float zLevel = 0.01f;
        float f = (1 / 256.0f);
        float f1 = (1 / 256.0f);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, (x + 0), (y + height), zLevel).uv(((u + 0) * f), ((v + height) * f1)).endVertex();
        buffer.vertex(matrix, (x + width), (y + height), zLevel).uv(((u + width) * f), ((v + height) * f1)).endVertex();
        buffer.vertex(matrix, (x + width), (y + 0), zLevel).uv(((u + width) * f), ((v + 0) * f1)).endVertex();
        buffer.vertex(matrix, (x + 0), (y + 0), zLevel).uv(((u + 0) * f), ((v + 0) * f1)).endVertex();
        tessellator.end();
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, r, g, b, a, width, height
     */
    public static void drawTexturedModalRect(PoseStack poseStack, int x, int y, int u, int v, float r, float g, float b, float a, int width, int height) {
        Matrix4f matrix = poseStack.last().pose();
        float zLevel = 0.01f;
        float f = (1 / 256.0f);
        float f1 = (1 / 256.0f);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(matrix, (x + 0), (y + height), zLevel).uv(((u + 0) * f), ((v + height) * f1)).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, (x + width), (y + height), zLevel).uv(((u + width) * f), ((v + height) * f1)).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, (x + width), (y + 0), zLevel).uv(((u + width) * f), ((v + 0) * f1)).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, (x + 0), (y + 0), zLevel).uv(((u + 0) * f), ((v + 0) * f1)).color(r, g, b, a).endVertex();
        tessellator.end();
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(PoseStack poseStack, int x, int y, int u, int v, int width, int height, int txtw, int txth) {
        Matrix4f matrix = poseStack.last().pose();
        float zLevel = 0.01f;
        float f = (1.0f / txtw);
        float f1 = (1.0f / txth);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(matrix, (x + 0), (y + height), zLevel).uv(((u + 0) * f), ((v + height) * f1)).endVertex();
        buffer.vertex(matrix, (x + width), (y + height), zLevel).uv(((u + width) * f), ((v + height) * f1)).endVertex();
        buffer.vertex(matrix, (x + width), (y + 0), zLevel).uv(((u + width) * f), ((v + 0) * f1)).endVertex();
        buffer.vertex(matrix, (x + 0), (y + 0), zLevel).uv(((u + 0) * f), ((v + 0) * f1)).endVertex();
        tessellator.end();
    }

    public static int renderText(PoseStack matrixStack, int x, int y, String txt) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1f);

        matrixStack.pushPose();
        matrixStack.translate(0.0F, 0.0F, 32.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager._disableDepthTest();
        GlStateManager._disableBlend();
        Minecraft mc = Minecraft.getInstance();
        int width = mc.font.width(txt);
        mc.font.drawShadow(matrixStack, txt, x, y, 16777215);
        GlStateManager._enableDepthTest();
        // Fixes opaque cooldown overlay a bit lower
        // TODO: check if enabled blending still screws things up down the line.
        GlStateManager._enableBlend();


        matrixStack.popPose();

        return width;
    }

    public static int renderText(PoseStack matrixStack, int x, int y, String txt, int color) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);

        matrixStack.pushPose();
        matrixStack.translate(0.0F, 0.0F, 32.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager._disableDepthTest();
        GlStateManager._disableBlend();
        Minecraft mc = Minecraft.getInstance();
        int width = mc.font.width(txt);
        mc.font.draw(matrixStack, txt, x, y, color);
        GlStateManager._enableDepthTest();
        // Fixes opaque cooldown overlay a bit lower
        // TODO: check if enabled blending still screws things up down the line.
        GlStateManager._enableBlend();


        matrixStack.popPose();

        return width;
    }

    public static class Vector {
        public final float x;
        public final float y;
        public final float z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public float norm() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        public Vector normalize() {
            float n = norm();
            return new Vector(x / n, y / n, z / n);
        }
    }

    private static Vector Cross(Vector a, Vector b) {
        float x = a.y * b.z - a.z * b.y;
        float y = a.z * b.x - a.x * b.z;
        float z = a.x * b.y - a.y * b.x;
        return new Vector(x, y, z);
    }

    private static Vector Sub(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    private static Vector Add(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    private static Vector Mul(Vector a, float f) {
        return new Vector(a.x * f, a.y * f, a.z * f);
    }
}
