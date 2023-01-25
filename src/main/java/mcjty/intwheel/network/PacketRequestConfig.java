package mcjty.intwheel.network;

import mcjty.intwheel.playerdata.PlayerProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRequestConfig {

    public PacketRequestConfig(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public PacketRequestConfig() {
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            PlayerProperties.getWheelConfig(player).ifPresent(config -> {
                CompoundTag tc = new CompoundTag();
                config.saveNBTData(tc);
                PacketHandler.INSTANCE.sendTo(new PacketSyncConfigToClient(tc), ((ServerPlayer)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            });
        });
        ctx.setPacketHandled(true);
    }
}
