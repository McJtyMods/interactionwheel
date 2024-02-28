package mcjty.intwheel.network;

import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.varia.SafeClientTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncConfigToServer {
    private CompoundTag tc;

    public PacketSyncConfigToServer(FriendlyByteBuf buf) {
        tc = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(tc);
    }

    public PacketSyncConfigToServer(CompoundTag tc) {
        this.tc = tc;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            PlayerProperties.getWheelConfig(player).ifPresent(config -> {
                config.loadNBTData(tc);
            });
        });
        ctx.setPacketHandled(true);
    }
}
