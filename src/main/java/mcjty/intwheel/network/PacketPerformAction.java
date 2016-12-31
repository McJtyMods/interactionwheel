package mcjty.intwheel.network;

import io.netty.buffer.ByteBuf;
import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPerformAction implements IMessage {
    private BlockPos pos;
    private String actionId;

    @Override
    public void fromBytes(ByteBuf buf) {
        if (buf.readBoolean()) {
            pos = NetworkTools.readPos(buf);
        }
        actionId = NetworkTools.readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (pos != null) {
            buf.writeBoolean(true);
            NetworkTools.writePos(buf, pos);
        } else {
            buf.writeBoolean(false);
        }
        NetworkTools.writeString(buf, actionId);
    }

    public PacketPerformAction() {
    }

    public PacketPerformAction(BlockPos pos, String id) {
        this.pos = pos;
        this.actionId = id;
    }

    public static class Handler implements IMessageHandler<PacketPerformAction, IMessage> {
        @Override
        public IMessage onMessage(PacketPerformAction message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketPerformAction message, MessageContext ctx) {
            IWheelAction action = InteractionWheel.registry.get(message.actionId);
            if (action != null) {
                EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                action.performServer(player, player.getEntityWorld(), message.pos);
            }
        }
    }

}
