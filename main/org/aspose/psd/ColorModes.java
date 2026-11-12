package org.aspose.psd;

/**
 * Defines the color modes supported by PSD files.
 */
public enum ColorModes {
    /**
     * Bitmap color mode.
     */
    Bitmap((short) 0),

    /**
     * Grayscale color mode.
     */
    Grayscale((short) 1),

    /**
     * Indexed color mode (using a color palette).
     */
    Indexed((short) 2),

    /**
     * RGB color mode (Red, Green, Blue).
     */
    Rgb((short) 3),

    /**
     * CMYK color mode (Cyan, Magenta, Yellow, Black).
     */
    Cmyk((short) 4),

    /**
     * Multichannel color mode.
     */
    Multichannel((short) 7),

    /**
     * Duotone color mode (two-color gradient).
     */
    Duotone((short) 8),

    /**
     * LAB color mode.
     */
    Lab((short) 9);

    private final short value;

    ColorModes(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public static ColorModes fromValue(short value) {
        for (ColorModes mode : ColorModes.values()) {
            if (mode.value == value) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown color mode: " + value);
    }

}