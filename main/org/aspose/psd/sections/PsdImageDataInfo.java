package org.aspose.psd.sections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a read-only summary of the PSD merged image data structure.
 */
public final class PsdImageDataInfo {
    private final ImageDataKind kind;
    private final int rowLengthFieldSize;
    private final List<Long> rowByteCounts;
    private final int compressedPayloadLength;
    private final boolean usesPrediction;

    /**
     * Initializes a new instance of the {@code PsdImageDataInfo} class.
     *
     * @param kind                    The structural kind of the payload.
     * @param rowLengthFieldSize      The size of one row-length entry in bytes.
     * @param rowByteCounts           The parsed row byte counts for RLE payloads.
     * @param compressedPayloadLength The payload length after any structural headers.
     * @param usesPrediction          Whether ZIP prediction is in effect.
     */
    public PsdImageDataInfo(ImageDataKind kind,
                     int rowLengthFieldSize,
                     long[] rowByteCounts,
                     int compressedPayloadLength,
                     boolean usesPrediction) {
        this.kind = kind;
        this.rowLengthFieldSize = rowLengthFieldSize;
        List<Long> list = new ArrayList<>(rowByteCounts.length);
        for (long v : rowByteCounts) {
            list.add(v);
        }
        this.rowByteCounts = Collections.unmodifiableList(list);
        this.compressedPayloadLength = compressedPayloadLength;
        this.usesPrediction = usesPrediction;
    }

    /**
     * Gets the structural kind of the payload.
     *
     * @return the kind
     */
    public ImageDataKind getKind() {
        return kind;
    }

    /**
     * Gets the size of one RLE row-length entry in bytes.
     *
     * @return the row length field size
     */
    public int getRowLengthFieldSize() {
        return rowLengthFieldSize;
    }

    /**
     * Gets the parsed row byte counts for RLE payloads.
     *
     * @return an unmodifiable list of row byte counts
     */
    public List<Long> getRowByteCounts() {
        return rowByteCounts;
    }

    /**
     * Gets the payload length after structural headers such as the RLE row-length table.
     *
     * @return the compressed payload length
     */
    public int getCompressedPayloadLength() {
        return compressedPayloadLength;
    }

    /**
     * Gets a value indicating whether ZIP prediction is in effect.
     *
     * @return true if ZIP prediction is used; otherwise false
     */
    public boolean isUsesPrediction() {
        return usesPrediction;
    }
}
