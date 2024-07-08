package mcjty.intwheel.playerdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcjty.intwheel.network.PacketHandler;
import mcjty.intwheel.network.PacketSyncConfigToServer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PlayerWheelConfiguration(Map<String, String> hotkeys, Map<String, Boolean> enabledActions, List<String> orderedActions) {

    public static final Codec<PlayerWheelConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("hotkeys").forGetter(l -> l.hotkeys),
                    Codec.unboundedMap(Codec.STRING, Codec.BOOL).fieldOf("enabled").forGetter(l -> l.enabledActions),
                    Codec.list(Codec.STRING).fieldOf("order").forGetter(l -> l.orderedActions)
            ).apply(instance, PlayerWheelConfiguration::new));

    public static final StreamCodec<FriendlyByteBuf, PlayerWheelConfiguration> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.STRING_UTF8), PlayerWheelConfiguration::hotkeys,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.BOOL), PlayerWheelConfiguration::enabledActions,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), PlayerWheelConfiguration::orderedActions,
            PlayerWheelConfiguration::new
    );

    public PlayerWheelConfiguration() {
        this(new HashMap<>(), new HashMap<>(), new ArrayList<>());
    }

    public void addHotkey(String key, String id) {
        hotkeys.put(id, key);
    }

    public void removeHotkey(String id) {
        hotkeys.remove(id);
    }

    public void enable(String id) {
        enabledActions.put(id, Boolean.TRUE);
    }

    public void disable(String id) {
        enabledActions.put(id, Boolean.FALSE);
    }

    public void setOrderActions(List<String> actions) {
        orderedActions.clear();
        orderedActions.addAll(actions);
    }

    /**
     * Can return null if the status is not known yet for this player
     * @param id
     * @return
     */
    public Boolean isEnabled(String id) {
        return enabledActions.get(id);
    }

    public void copyFrom(PlayerWheelConfiguration source) {
        hotkeys.clear();
        hotkeys.putAll(source.hotkeys);
        enabledActions.clear();
        enabledActions.putAll(source.enabledActions);
        orderedActions.clear();
        orderedActions.addAll(source.orderedActions);
    }


    public void sendToServer() {
        PacketHandler.sendToServer(new PacketSyncConfigToServer(this));
    }
}
