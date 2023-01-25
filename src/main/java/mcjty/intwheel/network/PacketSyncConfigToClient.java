package mcjty.intwheel.network;

import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.varia.SafeClientTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncConfigToClient {
    private CompoundTag tc;

    public PacketSyncConfigToClient(FriendlyByteBuf buf) {
        tc = buf.readNbt();
    }
    public PacketSyncConfigToClient(CompoundTag tc) {
        this.tc = tc;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(tc);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Player player = SafeClientTools.getClientPlayer();
            PlayerProperties.getWheelConfig(player).ifPresent(config -> {
                config.loadNBTData(tc);
            });
        });
        ctx.setPacketHandled(true);
    }
}
