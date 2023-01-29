package mcjty.intwheel.varia;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.util.HashSet;
import java.util.Set;

public class RenderHandler {

    public static long time = -1;
    public static Set<BlockPos> foundPositions = new HashSet<>();

    public static void showFoundInventories(RenderLevelStageEvent evt) {
        if (evt.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        if (!foundPositions.isEmpty()) {
            if (System.currentTimeMillis() > time) {
                foundPositions.clear();
                time = -1;
                return;
            }
            renderBlocks(evt, foundPositions);
        }
    }

    private static void renderBlocks(RenderLevelStageEvent evt, Set<BlockPos> blocks) {
        if (System.currentTimeMillis() % 1000 < 200) {
            return;
        }
        PoseStack matrixStack = evt.getPoseStack();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(CustomRenderTypes.OVERLAY_LINES);

        matrixStack.pushPose();

        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

        for (BlockPos pos : blocks) {
            renderHighLightedBlocksOutline(matrixStack, builder, pos.getX(), pos.getY(), pos.getZ(), 1.0f, 1.0f, 1.0f, 1.0f);
        }

        matrixStack.popPose();

        RenderSystem.disableDepthTest();
        buffer.endBatch(CustomRenderTypes.OVERLAY_LINES);
    }

    private static void renderHighLightedBlocksOutline(PoseStack poseStack, VertexConsumer buffer, float mx, float my, float mz, float r, float g, float b, float a) {
        Matrix4f matrix = poseStack.last().pose();
        buffer.vertex(matrix, mx, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, mx, my + 1, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, mx + 1, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my + 1, mz).color(r, g, b, a).endVertex();

        buffer.vertex(matrix, mx, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx + 1, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my, mz + 1).color(r, g, b, a).endVertex();
        buffer.vertex(matrix, mx, my + 1, mz + 1).color(r, g, b, a).endVertex();
    }
}
