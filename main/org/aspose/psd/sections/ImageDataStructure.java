package org.aspose.psd.sections;

/**
 * Describes the validated structural layout of the PSD Image Data payload.
 */
public final class ImageDataStructure {
    /**
     * Gets the structural kind implied by the compression mode.
     */
    private final ImageDataKind kind;
    /**
     * Gets the size of one RLE row-length entry in bytes.
     */
    private final int rowLengthFieldSize;
    /**
     * Gets the parsed row byte counts for RLE payloads.
     */
    private final long[] rowByteCounts;
    /**
     * Gets the number of payload bytes after structural headers such such as the RLE row-length table.
     */
    private final int compressedPayloadLength;
    /**
     * Gets a value indicating whether ZIP prediction is in effect.
     */
    private final boolean usesPrediction;

    /**
     * Initializes a new instance of the {@code ImageDataStructure} class.
     *
     * @param kind                     The structural kind implied by the compression method.
     * @param rowLengthFieldSize       The size of each RLE row-length entry, or zero for non-RLE payloads.
     * @param rowByteCounts            The parsed row byte counts for RLE payloads.
     * @param compressedPayloadLength  The number of bytes after any structural headers.
     * @param usesPrediction           Whether ZIP prediction is in effect.
     */
    public ImageDataStructure(
            ImageDataKind kind,
            int rowLengthFieldSize,
            long[] rowByteCounts,
            int compressedPayloadLength,
            boolean usesPrediction) {
        this.kind = kind;
        this.rowLengthFieldSize = rowLengthFieldSize;
        this.rowByteCounts = rowByteCounts;
        this.compressedPayloadLength = compressedPayloadLength;
        this.usesPrediction = usesPrediction;
    }

    public ImageDataKind getKind() {
        return kind;
    }

    public int getRowLengthFieldSize() {
        return rowLengthFieldSize;
    }

    public long[] getRowByteCounts() {
        return rowByteCounts;
    }

    public int getCompressedPayloadLength() {
        return compressedPayloadLength;
    }

    public boolean getUsesPrediction() {
        return usesPrediction;
    }

    /**
     * Creates a structure descriptor for raw image data.
     *
     * @param payloadLength The stored payload length.
     * @return The structure descriptor.
     */
    public static ImageDataStructure createRaw(int payloadLength) {
        return new ImageDataStructure(
                ImageDataKind.Raw,
                0,
                new long[0],
                payloadLength,
                false);
    }

    /**
     * Creates a structure descriptor for RLE image data.
     *
     * @param rowByteCounts           The parsed per-row byte counts.
     * @param rowLengthFieldSize      The size of one row-length entry in bytes.
     * @param compressedPayloadLength The payload length after the row-length table.
     * @return The structure descriptor.
     */
    public static ImageDataStructure createRle(long[] rowByteCounts, int rowLengthFieldSize, int compressedPayloadLength) {
        return new ImageDataStructure(
                ImageDataKind.Rle,
                rowLengthFieldSize,
                rowByteCounts,
                compressedPayloadLength,
                false);
    }

    /**
     * Creates a structure descriptor for ZIP-based image data.
     *
     * @param payloadLength   The stored payload length.
     * @param usesPrediction  Whether ZIP prediction is used.
     * @return The structure descriptor.
     */
    public static ImageDataStructure createZip(int payloadLength, boolean usesPrediction) {
        return new ImageDataStructure(
                ImageDataKind.Zip,
                0,
                new long[0],
                payloadLength,
                usesPrediction);
    }

    /**
     * Creates a structure descriptor for unsupported compression values while preserving the payload.
     *
     * @param payloadLength The stored payload length.
     * @return The structure descriptor.
     */
    public static ImageDataStructure createUnknown(int payloadLength) {
        return new ImageDataStructure(
                ImageDataKind.Unknown,
                0,
                new long[0],
                payloadLength,
                false);
    }

    public boolean usesPrediction() {
        return getUsesPrediction();
    }
}
