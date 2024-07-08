package mcjty.intwheel.network;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public record PacketPerformAction(Optional<BlockPos> pos, String actionId, boolean extended) implements CustomPacketPayload {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(InteractionWheel.MODID, "performaction");
    public static final Type<PacketPerformAction> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, PacketPerformAction> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs::optional), PacketPerformAction::pos,
            ByteBufCodecs.STRING_UTF8, PacketPerformAction::actionId,
            ByteBufCodecs.BOOL, PacketPerformAction::extended,
            PacketPerformAction::new);

    public PacketPerformAction(BlockPos pos, String actionId, boolean extended) {
        this(Optional.ofNullable(pos), actionId, extended);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            IWheelAction action = InteractionWheel.registry.get(actionId);
            if (action != null) {
                Player player = ctx.player();
                action.performServer(player, player.level(), pos.orElse(null), extended);
            }
        });
    }
}
