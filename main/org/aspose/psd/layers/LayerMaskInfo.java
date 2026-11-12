package org.aspose.psd.layers;

/**
 * Provides a read-only summary of the parsed layer mask subsection.
 */
public final class LayerMaskInfo {
    private final boolean isPresent;
    private final int rawDataLength;

    /**
     * Initializes a new instance of the {@code LayerMaskInfo} class.
     *
     * @param isPresent     Whether the subsection contains payload bytes.
     * @param rawDataLength The raw subsection length including the leading length field.
     */
    public LayerMaskInfo(boolean isPresent, int rawDataLength) {
        this.isPresent = isPresent;
        this.rawDataLength = rawDataLength;
    }

    /**
     * Gets a value indicating whether the subsection contains payload bytes.
     *
     * @return {@code true} if the subsection contains payload bytes; otherwise {@code false}.
     */
    public boolean isPresent() {
        return isPresent;
    }

    /**
     * Gets the raw subsection length including the leading length field.
     *
     * @return The raw subsection length.
     */
    public int getRawDataLength() {
        return rawDataLength;
    }
}
