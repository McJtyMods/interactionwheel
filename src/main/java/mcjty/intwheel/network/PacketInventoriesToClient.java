package mcjty.intwheel.network;

import mcjty.intwheel.RenderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PacketInventoriesToClient {
    private Set<BlockPos> positions;

    public PacketInventoriesToClient(FriendlyByteBuf buf) {
        int size = buf.readInt();
        positions = new HashSet<>(size);
        for (int i = 0 ; i < size ; i++) {
            positions.add(buf.readBlockPos());
        }
    }

    public PacketInventoriesToClient(Set<BlockPos> positions) {
        this.positions = new HashSet<>(positions);
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(positions.size());
        for (BlockPos pos : positions) {
            buf.writeBlockPos(pos);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            RenderHandler.foundPositions = positions;
            RenderHandler.time = System.currentTimeMillis() + 5000;
        });
        ctx.setPacketHandled(true);
    }
}
