package mcjty.intwheel.api;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WheelActionElement that = (WheelActionElement) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
