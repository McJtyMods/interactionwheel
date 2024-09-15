package mcjty.intwheel.network;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketRequestConfig() implements CustomPacketPayload {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(InteractionWheel.MODID, "requestconfig");
    public static final Type<PacketRequestConfig> TYPE = new Type<>(ID);

    public static final PacketRequestConfig INSTANCE = new PacketRequestConfig();

    public static final StreamCodec<FriendlyByteBuf, PacketRequestConfig> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public PacketRequestConfig() {
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            PlayerWheelConfiguration data = player.getData(InteractionWheel.HOTKEYS);
            PacketHandler.sendToPlayer(new PacketSyncConfigToClient(data), (ServerPlayer) player);
        });
    }
}
