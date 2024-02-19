package mcjty.intwheel.network;


import mcjty.intwheel.InteractionWheel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static int ID = 12;
    private static int packetId = 0;

    private static SimpleChannel INSTANCE = null;

    public static int nextPacketID() {
        return packetId++;
    }

    public PacketHandler() {
    }

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages(String channelName) {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(InteractionWheel.MODID, channelName))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;
        registerMessages();
    }

    public static void registerMessages() {
        // Server side
        int idx = 1;
        INSTANCE.registerMessage(idx++, PacketPerformAction.class, PacketPerformAction::toBytes, PacketPerformAction::new, PacketPerformAction::handle);
        INSTANCE.registerMessage(idx++, PacketSyncConfigToServer.class, PacketSyncConfigToServer::toBytes, PacketSyncConfigToServer::new, PacketSyncConfigToServer::handle);
        INSTANCE.registerMessage(idx++, PacketRequestConfig.class, PacketRequestConfig::toBytes, PacketRequestConfig::new, PacketRequestConfig::handle);

        // Client side
        INSTANCE.registerMessage(idx++, PacketInventoriesToClient.class, PacketInventoriesToClient::toBytes, PacketInventoriesToClient::new, PacketInventoriesToClient::handle);
        INSTANCE.registerMessage(idx++, PacketSyncConfigToClient.class, PacketSyncConfigToClient::toBytes, PacketSyncConfigToClient::new, PacketSyncConfigToClient::handle);
    }

    public static <T> void sendToPlayer(T packet, Player player) {
        INSTANCE.sendTo(packet, ((ServerPlayer)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T> void sendToServer(T packet) {
        INSTANCE.sendToServer(packet);
    }
}
