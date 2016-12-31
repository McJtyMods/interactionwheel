package mcjty.intwheel.api;

import java.util.ArrayList;
import java.util.List;

public enum StandardWheelActions {
    DUMP(StandardWheelActions.ID_DUMP, "Dump to container", 0, 128),                      // Dump all possible items in players inventory into container
    EXTRACT(StandardWheelActions.ID_EXTRACT, "Fetch from container", 32, 128);            // Extract all possible items from container into players inventory

    public static final String ID_DUMP = "std.dump";
    public static final String ID_EXTRACT = "std.extract";

    private final String id;
    private final String desc;
    private final int u;
    private final int v;

    StandardWheelActions(String id, String desc, int u, int v) {
        this.id = id;
        this.desc = desc;
        this.u = u;
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public static List<WheelActionElement> createStandardActions() {
        List<WheelActionElement> actions = new ArrayList<>();
        for (StandardWheelActions action : StandardWheelActions.values()) {
            actions.add(new WheelActionElement(action.getId(), action.getDesc(), "intwheel:textures/gui/wheel_hilight.png", action.getU(), action.getV()));
        }
        return actions;
    }
}
