package mcjty.intwheel.playerdata;

import mcjty.intwheel.network.PacketHandler;
import mcjty.intwheel.network.PacketSyncConfigToServer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerWheelConfiguration {

    private Map<String, Character> hotkeys = new HashMap<>();
    private Map<String, Boolean> enabledActions = new HashMap<>();
    private List<String> orderedActions = new ArrayList<>();

    public PlayerWheelConfiguration() {
    }

    public Map<String, Character> getHotkeys() {
        return hotkeys;
    }

    public void addHotkey(Character key, String id) {
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

    public List<String> getOrderedActions() {
        return orderedActions;
    }

    public void setOrderActions(List<String> actions) {
        orderedActions = new ArrayList<>(actions);
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
        hotkeys = new HashMap<>(source.hotkeys);
        enabledActions = new HashMap<>(source.enabledActions);
        orderedActions = new ArrayList<>(source.orderedActions);
    }


    public void saveNBTData(CompoundTag compound) {
        ListTag list = new ListTag();
        for (Map.Entry<String, Character> entry : hotkeys.entrySet()) {
            CompoundTag tc = new CompoundTag();
            tc.putString("id", entry.getKey());
            tc.putString("key", entry.getValue().toString());
            list.add(tc);
        }
        compound.put("hotkeys", list);

        list = new ListTag();
        for (Map.Entry<String, Boolean> entry : enabledActions.entrySet()) {
            CompoundTag tc = new CompoundTag();
            tc.putString("id", entry.getKey());
            tc.putBoolean("enabled", entry.getValue());
            list.add(tc);
        }
        compound.put("enabled", list);

        list = new ListTag();
        for (String action : orderedActions) {
            list.add(StringTag.valueOf(action));
        }
        compound.put("order", list);

    }

    public void loadNBTData(CompoundTag compound) {
        hotkeys = new HashMap<>();
        ListTag list = compound.getList("hotkeys", Tag.TAG_COMPOUND);
        for (Tag tag : list) {
            CompoundTag tc = (CompoundTag) tag;
            hotkeys.put(tc.getString("id"), tc.getString("key").charAt(0));
        }

        enabledActions = new HashMap<>();
        list = compound.getList("enabled", Tag.TAG_COMPOUND);
        for (Tag tag : list) {
            CompoundTag tc = (CompoundTag) tag;
            enabledActions.put(tc.getString("id"), tc.getBoolean("enabled"));
        }

        orderedActions = new ArrayList<>();
        list = compound.getList("order", Tag.TAG_STRING);
        for (Tag tag : list) {
            StringTag tc = (StringTag) tag;
            orderedActions.add(tc.getAsString());
        }
    }

    public void sendToServer() {
        CompoundTag tc = new CompoundTag();
        saveNBTData(tc);
        PacketHandler.INSTANCE.sendToServer(new PacketSyncConfigToServer(tc));
    }
}
