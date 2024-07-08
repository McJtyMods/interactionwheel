package mcjty.intwheel.network;


import mcjty.intwheel.InteractionWheel;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {

    public static void registerMessages(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(InteractionWheel.MODID)
                .versioned("1.0")
                .optional();

        registrar.playToClient(PacketInventoriesToClient.TYPE, PacketInventoriesToClient.CODEC, PacketInventoriesToClient::handle);
        registrar.playToClient(PacketSyncConfigToClient.TYPE, PacketSyncConfigToClient.CODEC, PacketSyncConfigToClient::handle);
        registrar.playToServer(PacketPerformAction.TYPE, PacketPerformAction.CODEC, PacketPerformAction::handle);
        registrar.playToServer(PacketRequestConfig.TYPE, PacketRequestConfig.CODEC, PacketRequestConfig::handle);
        registrar.playToServer(PacketSyncConfigToServer.TYPE, PacketSyncConfigToServer.CODEC, PacketSyncConfigToServer::handle);
    }

    public static <T extends CustomPacketPayload> void sendToPlayer(T packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static <T extends CustomPacketPayload> void sendToServer(T packet) {
        PacketDistributor.sendToServer(packet);
    }
}
