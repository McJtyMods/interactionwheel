package mcjty.intwheel.network;

import io.netty.buffer.ByteBuf;
import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncConfigToServer implements IMessage {
    NBTTagCompound tc;

    @Override
    public void fromBytes(ByteBuf buf) {
        tc = NetworkTools.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeTag(buf, tc);
    }

    public PacketSyncConfigToServer() {
    }

    public PacketSyncConfigToServer(NBTTagCompound tc) {
        this.tc = tc;
    }

    public static class Handler implements IMessageHandler<PacketSyncConfigToServer, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncConfigToServer message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSyncConfigToServer message, MessageContext ctx) {
            System.out.println("message = " + message);
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(player);
            config.loadNBTData(message.tc);
        }
    }

}
