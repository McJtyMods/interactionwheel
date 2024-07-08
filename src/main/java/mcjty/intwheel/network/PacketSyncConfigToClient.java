package mcjty.intwheel.network;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import mcjty.intwheel.varia.SafeClientTools;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketSyncConfigToClient(PlayerWheelConfiguration data) implements CustomPacketPayload {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(InteractionWheel.MODID, "syncconfigtoclient");
    public static final Type<PacketSyncConfigToClient> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, PacketSyncConfigToClient> CODEC = PlayerWheelConfiguration.STREAM_CODEC.map(PacketSyncConfigToClient::new, PacketSyncConfigToClient::data);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = SafeClientTools.getClientPlayer();
            player.setData(InteractionWheel.HOTKEYS, data);
        });
    }
}
