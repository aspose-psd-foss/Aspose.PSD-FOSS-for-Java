package org.aspose.psd.sections;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.BigEndianWriter;
import org.aspose.psd.CompressionMethod;
import org.aspose.psd.PsdSectionReader;
import org.aspose.psd.coreexceptions.PsdLoadException;

import java.io.IOException;
import java.util.Arrays;

/**
 * Stores the PSD Image Data section as a compression header plus raw payload bytes.
 */
public final class ImageData {

    private final ImageDataStructure structure;
    private final CompressionMethod compression;
    private final byte[] rawData;

    /**
     * Initializes a new instance of the {@link ImageData} class for callers that only preserve raw payload bytes.
     *
     * @param compression The PSD image compression method.
     * @param rawData     The raw image data payload after the compression field.
     */
    public ImageData(CompressionMethod compression, byte[] rawData) throws PsdLoadException {
        this(compression, rawData, parseStructure(compression, rawData, false, 0, 0));
    }

    /**
     * Initializes a new instance of the {@link ImageData} class.
     *
     * @param compression The PSD image compression method.
     * @param rawData     The raw image data payload after the compression field.
     * @param structure   The parsed structural view of the payload.
     */
    private ImageData(CompressionMethod compression, byte[] rawData, ImageDataStructure structure) {
        this.compression = compression;
        this.rawData = rawData;
        this.structure = structure;
    }

    /**
     * Gets the parsed structural interpretation of the payload.
     */
    public ImageDataStructure getStructure() {
        return structure;
    }

    /**
     * Gets the stored compression method.
     */
    public CompressionMethod getCompression() {
        return compression;
    }

    /**
     * Gets the raw image data payload after the compression field.
     */
    public byte[] getRawData() {
        return rawData;
    }

    /**
     * Loads the final Image Data section from the reader.
     *
     * @param reader         The reader positioned at the compression field.
     * @param isLargeDocument true for PSB row-length sizing; otherwise, false.
     * @param height         The document height used to interpret row-oriented compression payloads.
     * @param channelCount   The document channel count used to interpret row-oriented compression payloads.
     * @return The loaded {@link ImageData} instance.
     * @throws IOException if an I/O error occurs.
     */
    public static ImageData load(BigEndianReader reader, boolean isLargeDocument, int height, int channelCount)
            throws IOException, PsdLoadException {
        CompressionMethod compression = CompressionMethod.fromValue((short) reader.readUInt16());

        long imageDataStart = reader.getPosition();
        reader.seek(0, SeekOrigin.END);
        long imageDataEnd = reader.getPosition();
        reader.seek(imageDataStart, SeekOrigin.BEGIN);

        long imageDataLength = imageDataEnd - imageDataStart;
        byte[] rawData = PsdSectionReader.readBytes(reader, imageDataLength, "Image Data section");
        ImageDataStructure structure = parseStructure(compression, rawData, isLargeDocument, height, channelCount);
        return new ImageData(compression, rawData, structure);
    }

    /**
     * Writes the Image Data section to the writer.
     *
     * @param writer The destination writer.
     * @throws IOException if an I/O error occurs.
     */
    public void save(BigEndianWriter writer) throws IOException {
        writer.writeUShort((short) compression.getValue());
        writer.write(rawData);
    }

    /**
     * Creates a read-only public summary of the parsed merged image data structure.
     *
     * @return The public image data summary.
     */
    public PsdImageDataInfo toPublicInfo() {
        return new PsdImageDataInfo(
                structure.getKind(),
                structure.getRowLengthFieldSize(),
                structure.getRowByteCounts(),
                structure.getCompressedPayloadLength(),
                structure.usesPrediction());
    }

    /**
     * Builds a structural view over the raw image-data payload without decoding pixels.
     */
    private static ImageDataStructure parseStructure(CompressionMethod compression, byte[] rawData,
                                                     boolean isLargeDocument, int height, int channelCount) throws PsdLoadException {
        switch (compression) {
            case Raw:
                return ImageDataStructure.createRaw(rawData.length);
            case RLE:
                return parseRleStructure(rawData, isLargeDocument, height, channelCount);
            case ZipWithoutPrediction:
                return ImageDataStructure.createZip(rawData.length, false);
            case ZipWithPrediction:
                return ImageDataStructure.createZip(rawData.length, true);
            default:
                return ImageDataStructure.createUnknown(rawData.length);
        }
    }

    /**
     * Parses the RLE row-length table so the payload has a validated structural model.
     */
    private static ImageDataStructure parseRleStructure(byte[] rawData, boolean isLargeDocument,
                                                        int height, int channelCount) throws PsdLoadException {
        if (height < 0 || channelCount < 0) {
            throw new PsdLoadException("PSD image dimensions are invalid for RLE image data.");
        }

        int rowCount;
        try {
            rowCount = Math.multiplyExact(height, channelCount);
        } catch (ArithmeticException e) {
            throw new PsdLoadException("PSD RLE row count overflow.", e);
        }

        int rowLengthFieldSize = isLargeDocument ? Integer.BYTES : Short.BYTES;
        int tableLength;
        try {
            tableLength = Math.multiplyExact(rowCount, rowLengthFieldSize);
        } catch (ArithmeticException e) {
            throw new PsdLoadException("PSD RLE row-length table size overflow.", e);
        }

        if (rawData.length < tableLength) {
            throw new PsdLoadException("PSD RLE image data is truncated before the row-length table completes.");
        }

        if (rowCount == 0) {
            return ImageDataStructure.createRle(new long[0], rowLengthFieldSize, 0);
        }

        int offset = 0;
        long[] rowByteCounts = new long[rowCount];
        for (int i = 0; i < rowCount; i++) {
            rowByteCounts[i] = isLargeDocument
                    ? readUInt32BigEndian(rawData, offset)
                    : readUInt16BigEndian(rawData, offset);
            offset += rowLengthFieldSize;
        }

        return ImageDataStructure.createRle(rowByteCounts, rowLengthFieldSize, rawData.length - tableLength);
    }

    /**
     * Reads a big-endian 16-bit unsigned integer from a byte array.
     */
    private static int readUInt16BigEndian(byte[] buffer, int offset) {
        return ((buffer[offset] & 0xFF) << 8) | (buffer[offset + 1] & 0xFF);
    }

    /**
     * Reads a big-endian 32-bit unsigned integer from a byte array.
     */
    private static long readUInt32BigEndian(byte[] buffer, int offset) {
        return ((long) (buffer[offset] & 0xFF) << 24)
                | ((long) (buffer[offset + 1] & 0xFF) << 16)
                | ((long) (buffer[offset + 2] & 0xFF) << 8)
                | (buffer[offset + 3] & 0xFF);
    }
}