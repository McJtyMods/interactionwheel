package mcjty.intwheel.api;

public enum StandardWheelActions {
    SEARCH(StandardWheelActions.ID_SEARCH, "Search item", 96, 128),                       // Search item
    ROTATE(StandardWheelActions.ID_ROTATE, "Rotate block", 64, 128),                      // Rotate block
    DUMP(StandardWheelActions.ID_DUMP, "Dump to container", 0, 128),                      // Dump all possible items in players inventory into container
    EXTRACT(StandardWheelActions.ID_EXTRACT, "Fetch from container", 32, 128);            // Extract all possible items from container into players inventory

    public static final String ID_DUMP = "std.dump";
    public static final String ID_EXTRACT = "std.extract";
    public static final String ID_ROTATE = "std.rotate";
    public static final String ID_SEARCH = "std.search";

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

    public WheelActionElement createWheelAction() {
        return new WheelActionElement(id, desc, "intwheel:textures/gui/wheel_hilight.png", u, v, u, v+64, 256, 256);
    }
}
