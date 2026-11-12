package org.aspose.psd;

/**
 * Defines the compression methods used for image data in PSD files.
 */
public enum CompressionMethod {
    /**
     * Raw (uncompressed) data.
     */
    Raw((short) 0),

    /**
     * RLE (Run-Length Encoded) compression.
     */
    RLE((short) 1),

    /**
     * ZIP (lossless) compression.
     */
    ZipWithoutPrediction((short) 2),

    /**
     * ZIP compression with prediction.
     */
    ZipWithPrediction((short) 3);

    private final short value;

    CompressionMethod(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public static CompressionMethod fromValue(short value) {
        for (CompressionMethod method : CompressionMethod.values()) {
            if (method.value == value) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown CompressionMethod value: " + value);
    }
}
