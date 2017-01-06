package mcjty.intwheel.network;

import io.netty.buffer.ByteBuf;
import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncConfigToClient implements IMessage {
    NBTTagCompound tc;

    @Override
    public void fromBytes(ByteBuf buf) {
        tc = NetworkTools.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeTag(buf, tc);
    }

    public PacketSyncConfigToClient() {
    }

    public PacketSyncConfigToClient(NBTTagCompound tc) {
        this.tc = tc;
    }

    public static class Handler implements IMessageHandler<PacketSyncConfigToClient, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncConfigToClient message, MessageContext ctx) {
            InteractionWheel.proxy.addScheduledTaskClient(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSyncConfigToClient message, MessageContext ctx) {
            EntityPlayer player = InteractionWheel.proxy.getClientPlayer();
            PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(player);
            config.loadNBTData(message.tc);
        }
    }

}
