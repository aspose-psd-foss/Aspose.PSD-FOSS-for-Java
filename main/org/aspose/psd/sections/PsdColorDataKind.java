package org.aspose.psd.sections;

/**
 * Describes how the Color Mode Data payload was interpreted for the loaded document.
 */
public enum PsdColorDataKind {
    /**
     * No color mode payload was present.
     */
    NONE,

    /**
     * The payload was parsed as a standard indexed palette.
     */
    INDEXED_PALETTE,

    /**
     * The payload belongs to an RGB document.
     */
    RGB_PAYLOAD,

    /**
     * The payload belongs to a CMYK document.
     */
    CMYK_PAYLOAD,

    /**
     * The payload is preserved as opaque raw data.
     */
    RAW_PRESERVED
}
