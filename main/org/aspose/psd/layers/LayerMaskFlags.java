package org.aspose.psd.layers;

public enum LayerMaskFlags {
    /** No layer mask flags are defined. */
    None((byte) 0),

    /** The layer mask position is relative to the layer. */
    RelativeToLayer((byte) 1),

    /** The layer mask is disabled. */
    Disabled((byte) 2),

    /** The layer mask is inverted when blending. */
    InvertedWhenBlending((byte) 4),

    /** The user mask comes from rendering other data. */
    UserMaskFromRenderingOtherData((byte) 8),

    /** The user or vector masks have parameters applied. */
    UserOrVectorMasksHaveParameters((byte) 16);

    private final byte value;

    private LayerMaskFlags(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
