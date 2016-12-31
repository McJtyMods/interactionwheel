package mcjty.intwheel;

import mcjty.intwheel.varia.RenderHelper;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Set;

public class RenderHandler {

    public static long time = -1;
    public static Set<BlockPos> foundPositions = new HashSet<>();

    public static void showFoundInventories(RenderWorldLastEvent evt) {
        if (!foundPositions.isEmpty()) {
            if (System.currentTimeMillis() > time) {
                foundPositions.clear();
                time = -1;
                return;
            }
            renderBlocks(evt, foundPositions);
        }
    }

    private static void renderBlocks(RenderWorldLastEvent evt, Set<BlockPos> blocks) {
        EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());

        double doubleX = player.lastTickPosX + (player.posX - player.lastTickPosX) * evt.getPartialTicks();
        double doubleY = player.lastTickPosY + (player.posY - player.lastTickPosY) * evt.getPartialTicks();
        double doubleZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * evt.getPartialTicks();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        for (BlockPos pos : blocks) {
            renderBoxOutline(pos);
        }

        GlStateManager.enableDepth();

        GlStateManager.popMatrix();
    }

    private static void renderBoxOutline(BlockPos pos) {
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GlStateManager.disableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.glLineWidth(2);
        GlStateManager.color(1, 1, 1);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        float mx = pos.getX();
        float my = pos.getY();
        float mz = pos.getZ();
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        RenderHelper.renderHighLightedBlocksOutline(buffer, mx, my, mz, .9f, .7f, 0, 1);

        tessellator.draw();

        Minecraft.getMinecraft().entityRenderer.enableLightmap();
        GlStateManager.enableTexture2D();
    }
}
