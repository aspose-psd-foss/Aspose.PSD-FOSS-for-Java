package org.aspose.psd.test;

import org.aspose.psd.CompressionMethod;
import org.aspose.psd.BigEndianReader;
import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.BigEndianWriter;
import org.aspose.psd.sections.ImageDataStructure;

import java.io.EOFException;
import java.io.IOException;

/**
 * Represents image data within a PSD file.
 */
class ImageDataTest {
    private final CompressionMethod compression;
    private final byte[] data;
    private final ImageDataStructure structure;

    public ImageDataTest(CompressionMethod compression, byte[] data, ImageDataStructure structure) {
        this.compression = compression;
        this.data = data;
        this.structure = structure;
    }

    public ImageDataTest(CompressionMethod compression, byte[] data) {
        this(compression, data, null);
    }

    public CompressionMethod getCompression() {
        return compression;
    }

    public byte[] getData() {
        return data;
    }

    public ImageDataStructure getStructure() {
        return structure;
    }

    public byte[] getRawData() {
        return data;
    }

    /**
     * Loads image data from the given reader.
     */
    public static ImageDataTest load(BigEndianReader reader, boolean isLargeDocument, int height, int channels)
            throws PsdLoadException {
        CompressionMethod compression;
        try {
            compression = CompressionMethod.fromValue(reader.readInt16());
        } catch (EOFException e) {
            throw new PsdLoadException("Unexpected end of file while reading compression method.", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ImageDataStructure structure = null;
        byte[] payload = new byte[0];

        try {
            switch (compression) {
                case RLE: {
                    int rowFieldSize = isLargeDocument ? Integer.BYTES : Short.BYTES;
                    int rowCount = height * channels;
                    long[] rowByteCounts = new long[rowCount];

                    for (int i = 0; i < rowCount; i++) {
                        if (isLargeDocument) {
                            rowByteCounts[i] = reader.readUInt32();
                        } else {
                            rowByteCounts[i] = reader.readUInt16();
                        }
                    }

                    int remaining = (int) (reader.getLength() - reader.getPosition());
                    if (remaining < 0) {
                        throw new PsdLoadException("Negative remaining payload length while reading RLE image data.");
                    }
                    payload = remaining > 0 ? reader.readBytes(remaining) : new byte[0];

                    structure = ImageDataStructure.createRle(rowByteCounts, rowFieldSize, payload.length);
                    break;
                }
                case ZipWithPrediction:
                case ZipWithoutPrediction:
                case Raw: {
                    int remaining = (int) (reader.getLength() - reader.getPosition());
                    if (remaining < 0) {
                        throw new PsdLoadException("Negative remaining payload length while reading image data.");
                    }
                    payload = remaining > 0 ? reader.readBytes(remaining) : new byte[0];

                    boolean usesPrediction = compression == CompressionMethod.ZipWithPrediction;
                    structure = ImageDataStructure.createZip(payload.length, usesPrediction);
                    break;
                }
                default: {
                    int remaining = (int) (reader.getLength() - reader.getPosition());
                    payload = remaining > 0 ? reader.readBytes(remaining) : new byte[0];
                    break;
                }
            }
        } catch (EOFException e) {
            throw new PsdLoadException("Unexpected end of file while reading image data payload.", e);
        } catch (IOException e) {
            throw new PsdLoadException("I/O error while reading image data.", e);
        }

        return new ImageDataTest(compression, payload, structure);
    }

    public void save(BigEndianWriter writer) throws IOException {
        writer.writeShort((short) compression.getValue());
        if (data != null && data.length > 0) {
            writer.write(data);
        }
    }
}
