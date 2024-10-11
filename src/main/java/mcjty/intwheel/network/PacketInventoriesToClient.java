package mcjty.intwheel.network;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.varia.RenderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

public record PacketInventoriesToClient(Set<BlockPos> positions) implements CustomPacketPayload {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(InteractionWheel.MODID, "inventoriestoclient");
    public static final Type<PacketInventoriesToClient> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, PacketInventoriesToClient> CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.positions.size());
                for (BlockPos pos : packet.positions) {
                    buf.writeBlockPos(pos);
                }
            },
            buf -> {
                int size = buf.readInt();
                Set<BlockPos> positions = new HashSet<>(size);
                for (int i = 0; i < size; i++) {
                    positions.add(buf.readBlockPos());
                }
                return new PacketInventoriesToClient(positions);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            RenderHandler.foundPositions = positions;
            RenderHandler.time = System.currentTimeMillis() + 5000;
        });
    }
}
