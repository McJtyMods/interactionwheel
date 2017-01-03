package mcjty.intwheel.network;

import io.netty.buffer.ByteBuf;
import mcjty.intwheel.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashSet;
import java.util.Set;

public class PackedInventoriesToClient implements IMessage {
    private Set<BlockPos> positions;

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        positions = new HashSet<>(size);
        for (int i = 0 ; i < size ; i++) {
            positions.add(NetworkTools.readPos(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(positions.size());
        for (BlockPos pos : positions) {
            NetworkTools.writePos(buf, pos);
        }
    }

    public PackedInventoriesToClient() {
    }

    public PackedInventoriesToClient(Set<BlockPos> positions) {
        this.positions = new HashSet<>(positions);
    }

    public static class Handler implements IMessageHandler<PackedInventoriesToClient, IMessage> {
        @Override
        public IMessage onMessage(PackedInventoriesToClient message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PackedInventoriesToClient message, MessageContext ctx) {
            RenderHandler.foundPositions = message.positions;
            RenderHandler.time = System.currentTimeMillis() + 5000;
        }
    }

}
