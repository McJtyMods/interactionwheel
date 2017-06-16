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

public class PacketRequestConfig implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public PacketRequestConfig() {
    }

    public static class Handler implements IMessageHandler<PacketRequestConfig, IMessage> {
        @Override
        public IMessage onMessage(PacketRequestConfig message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketRequestConfig message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(player);
            NBTTagCompound tc = new NBTTagCompound();
            config.saveNBTData(tc);
            PacketHandler.INSTANCE.sendTo(new PacketSyncConfigToClient(tc), player);
        }
    }

}
