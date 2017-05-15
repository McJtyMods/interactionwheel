package mcjty.intwheel.api;

public enum StandardWheelActions {
    GENERIC("?", "?", null, 128, 128),
    SEARCH(StandardWheelActions.ID_SEARCH, "Search item", null, 96, 128),
    ROTATE(StandardWheelActions.ID_ROTATE, "Rotate block", null, 64, 128),
    DUMP(StandardWheelActions.ID_DUMP, "Dump to container", "Dump (including hotbar)", 0, 128),
    DUMP1(StandardWheelActions.ID_DUMP1, "Dump current to container", null, 160, 128),
    EXTRACT(StandardWheelActions.ID_EXTRACT, "Fetch from container", null, 32, 128),
    DUMPSIMILAR(StandardWheelActions.ID_DUMPSIMILAR, "Dump current and equal to container", null, 224, 128),
    DUMPSIMILARINV(StandardWheelActions.ID_DUMPSIMILARINV, "Dump all matching container contents", null, 96, 160),
    DUMPORES(StandardWheelActions.ID_DUMPORES, "Dump all ores to container", null, 32, 160),
    DUMPBLOCKS(StandardWheelActions.ID_DUMPBLOCKS, "Dump all blocks to container", null, 64, 160),
    PICKTOOL(StandardWheelActions.ID_PICKTOOL, "Select the right tool", null, 0, 160);


    public static final String ID_DUMP = "std.dump";
    public static final String ID_DUMP1 = "std.dump1";
    public static final String ID_DUMPSIMILAR = "std.dumpsimilar";
    public static final String ID_DUMPSIMILARINV = "std.dumpsimilarinv";
    public static final String ID_DUMPORES = "std.dumpores";
    public static final String ID_DUMPBLOCKS = "std.dumpblocks";
    public static final String ID_EXTRACT = "std.extract";
    public static final String ID_ROTATE = "std.rotate";
    public static final String ID_SEARCH = "std.search";
    public static final String ID_PICKTOOL = "std.picktool";

    private final String id;
    private final String desc;
    private final String sneakDesc;
    private final int u;
    private final int v;

    StandardWheelActions(String id, String desc, String sneakDesc, int u, int v) {
        this.id = id;
        this.desc = desc;
        this.sneakDesc = sneakDesc;
        this.u = u;
        this.v = v;
    }

    // Create a standard wheel action element
    public WheelActionElement createElement() {
        return new WheelActionElement(id).texture("intwheel:textures/gui/wheel_hilight.png", u, v, u, v+64, 256, 256).description(desc, sneakDesc);
    }
}
