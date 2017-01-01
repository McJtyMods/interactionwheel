package mcjty.intwheel.api;

/**
 * A graphical description of a wheel element. Describes what the element does as well
 * as the icon for it
 */
public class WheelActionElement {
    private final String id;
    private final String description;
    private final String sneakDescription;
    private final String texture;
    private final int uhigh;
    private final int vhigh;
    private final int ulow;
    private final int vlow;
    private final int txtw;
    private final int txth;

    /**
     * The wheel icon should be a 32x32 image embedded in a larger texture
     * @param id is the id for the wheel action
     * @param description a short (max about 20 chars) description of the wheel element
     * @param sneakDescription an optional description to use when sneaking
     * @param texture a string representation of a texture resource. Default is "intwheel:textures/gui/wheel_hilight.png"
     * @param uhigh the texture 'u' location of the image within the texture (selected version)
     * @param vhigh the texture 'v' location of the image within the texture (selected version)
     * @param ulow the texture 'u' location of the image within the texture (non-selected version)
     * @param vlow the texture 'v' location of the image within the texture (non-selected version)
     * @param txtw the total size of the source texture (often 256)
     * @param txth the total size of the source texture (often 256)
     */
    public WheelActionElement(String id, String description, String sneakDescription, String texture, int uhigh, int vhigh, int ulow, int vlow, int txtw, int txth) {
        this.id = id;
        this.description = description;
        this.sneakDescription = sneakDescription;
        this.texture = texture;
        this.uhigh = uhigh;
        this.vhigh = vhigh;
        this.ulow = ulow;
        this.vlow = vlow;
        this.txtw = txtw;
        this.txth = txth;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getSneakDescription() {
        return sneakDescription;
    }

    public String getTexture() {
        return texture;
    }

    public int getUhigh() {
        return uhigh;
    }

    public int getVhigh() {
        return vhigh;
    }

    public int getUlow() {
        return ulow;
    }

    public int getVlow() {
        return vlow;
    }

    public int getTxtw() {
        return txtw;
    }

    public int getTxth() {
        return txth;
    }
}
