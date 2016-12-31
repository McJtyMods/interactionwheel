package mcjty.intwheel.api;

import net.minecraft.util.ResourceLocation;

public class WheelActionElement {
    private final String id;
    private final String description;
    private final String texture;
    private final int u;
    private final int v;

    public WheelActionElement(String id, String description, String texture, int u, int v) {
        this.id = id;
        this.description = description;
        this.texture = texture;
        this.u = u;
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTexture() {
        return texture;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }
}
