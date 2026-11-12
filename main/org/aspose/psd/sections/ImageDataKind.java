package org.aspose.psd.sections;

/**
 * Identifies the structural shape of the PSD Image Data payload.
 */
public enum ImageDataKind {
    /**
     * The payload is raw pixel data.
     */
    Raw,

    /**
     * The payload starts with an RLE row-length table followed by compressed scan data.
     */
    Rle,

    /**
     * The payload is ZIP-compressed scan data, optionally with prediction.
     */
    Zip,

    /**
     * The payload uses an unrecognized compression code and is preserved as-is.
     */
    Unknown,

    /**
     * The payload is preserved as raw bytes without semantic parsing.
     */
    RAW_PRESERVED
}
