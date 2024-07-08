package mcjty.intwheel.network;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSyncConfigToServer(PlayerWheelConfiguration config) implements CustomPacketPayload {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(InteractionWheel.MODID, "syncconfigtoserver");
    public static final Type<PacketSyncConfigToServer> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, PacketSyncConfigToServer> CODEC = PlayerWheelConfiguration.STREAM_CODEC.map(PacketSyncConfigToServer::new, PacketSyncConfigToServer::config);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            player.setData(InteractionWheel.HOTKEYS, config);
        });
    }
}
