package mcjty.intwheel.network;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPerformAction {
    private BlockPos pos;
    private String actionId;
    private boolean extended;

    public PacketPerformAction(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            pos = buf.readBlockPos();
        }
        actionId = buf.readUtf(32767);
        extended = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        if (pos != null) {
            buf.writeBoolean(true);
            buf.writeBlockPos(pos);
        } else {
            buf.writeBoolean(false);
        }
        buf.writeUtf(actionId);
        buf.writeBoolean(extended);
    }

    public PacketPerformAction(BlockPos pos, String id, boolean extended) {
        this.pos = pos;
        this.actionId = id;
        this.extended = extended;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            IWheelAction action = InteractionWheel.registry.get(actionId);
            if (action != null) {
                ServerPlayer player = ctx.getSender();
                action.performServer(player, player.level(), pos, extended);
            }
        });
        ctx.setPacketHandled(true);
    }
}
