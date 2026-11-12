package org.aspose.psd.layers;

/**
 * Defines layer mask data for layers that have only a raster or vector mask.
 */
public class LayerMaskDataShort extends LayerMaskData {
    /**
     * Gets or sets the layer mask padding.
     */
    private short padding;

    /**
     * Initializes a new instance of the {@link LayerMaskDataShort} class.
     */
    public LayerMaskDataShort() {
        super();
    }

    public short getPadding() {
        return padding;
    }

    public void setPadding(short padding) {
        this.padding = padding;
    }
}
