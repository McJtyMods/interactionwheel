package mcjty.intwheel.playerdata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerWheelConfiguration {

    private Map<String, Integer> hotkeys = new HashMap<>();
    private Set<String> enabledActions = new HashSet<>();

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
        enabledActions.add(id);
    }

    public void disable(String id) {
        enabledActions.remove(id);
    }

    public boolean isEnabled(String id) {
        return enabledActions.contains(id);
    }

    public void copyFrom(PlayerWheelConfiguration source) {
        hotkeys = new HashMap<>(source.hotkeys);
        enabledActions = new HashSet<>(source.enabledActions);
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
        for (String action : enabledActions) {
            list.appendTag(new NBTTagString(action));
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

        enabledActions = new HashSet<>();
        list = compound.getTagList("enabled", Constants.NBT.TAG_STRING);
        for (int i = 0 ; i < list.tagCount() ; i++) {
            NBTTagString tc = (NBTTagString) list.get(i);
            enabledActions.add(tc.getString());
        }
    }
}
