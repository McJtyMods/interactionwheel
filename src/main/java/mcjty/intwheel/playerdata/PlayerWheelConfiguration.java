package mcjty.intwheel.playerdata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PlayerWheelConfiguration {

    private Map<String, Integer> hotkeys = new HashMap<>();
    private Map<String, Boolean> enabledActions = new HashMap<>();

    public PlayerWheelConfiguration() {
    }

    public Map<String, Integer> getHotkeys() {
        return hotkeys;
    }

    public void addHotkey(int key, String id) {
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
    }


    public void saveNBTData(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<String, Integer> entry : hotkeys.entrySet()) {
            NBTTagCompound tc = new NBTTagCompound();
            tc.setString("id", entry.getKey());
            tc.setInteger("key", entry.getValue());
            list.appendTag(tc);
        }
        compound.setTag("hotkeys", list);

        list = new NBTTagList();
        for (Map.Entry<String, Boolean> entry : enabledActions.entrySet()) {
            NBTTagCompound tc = new NBTTagCompound();
            tc.setString("id", entry.getKey());
            tc.setBoolean("enabled", entry.getValue());
            list.appendTag(tc);
        }
        compound.setTag("enabled", list);
    }

    public void loadNBTData(NBTTagCompound compound) {
        hotkeys = new HashMap<>();
        NBTTagList list = compound.getTagList("hotkeys", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < list.tagCount() ; i++) {
            NBTTagCompound tc = (NBTTagCompound) list.get(i);
            hotkeys.put(tc.getString("id"), tc.getInteger("key"));
        }

        enabledActions = new HashMap<>();
        list = compound.getTagList("enabled", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < list.tagCount() ; i++) {
            NBTTagCompound tc = (NBTTagCompound) list.get(i);
            enabledActions.put(tc.getString("id"), tc.getBoolean("enabled"));
        }
    }
}
