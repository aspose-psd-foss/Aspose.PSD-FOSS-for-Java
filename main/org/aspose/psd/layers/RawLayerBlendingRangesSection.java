package org.aspose.psd.layers;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.PsdSectionReader;
import org.aspose.psd.coreexceptions.PsdLoadException;

import java.io.IOException;

/**
 * Stores the raw blending ranges subsection including its leading length field.
 */
final class RawLayerBlendingRangesSection {
    /**
     * An empty blending ranges subsection.
     */
    public static final RawLayerBlendingRangesSection EMPTY =
            new RawLayerBlendingRangesSection(new byte[0]);

    private final byte[] rawData;

    /**
     * Initializes a new instance of the {@link RawLayerBlendingRangesSection} class.
     *
     * @param rawData the raw subsection bytes including the length field
     */
    public RawLayerBlendingRangesSection(byte[] rawData) {
        this.rawData = rawData;
    }

    /**
     * Gets the raw subsection bytes including the length field.
     *
     * @return the raw subsection bytes
     */
    public byte[] getRawData() {
        return rawData;
    }

    /**
     * Loads the blending ranges subsection from the reader.
     *
     * @param reader     the reader positioned at the subsection length field
     * @param sectionEnd the byte position of the end of the enclosing layer extra data
     * @return the loaded {@link RawLayerBlendingRangesSection} instance
     * @throws PsdLoadException if the subsection is too large to preserve in memory
     */
    public static RawLayerBlendingRangesSection load(BigEndianReader reader, long sectionEnd) throws IOException, PsdLoadException {
        long length = reader.readUInt32();
        int payloadLength = PsdSectionReader.getNestedMemoryBackedLength(
                reader, length, sectionEnd, "Layer blending ranges subsection");
        if (payloadLength > Integer.MAX_VALUE - 4) {
            throw new PsdLoadException(
                    "Layer blending ranges subsection is too large to preserve in memory with its length field.");
        }

        byte[] rawData = new byte[4 + payloadLength];
        writeUInt32BigEndian(rawData, 0, length);
        if (payloadLength > 0) {
            byte[] payload = reader.readBytes(payloadLength);
            System.arraycopy(payload, 0, rawData, 4, payloadLength);
        }

        return new RawLayerBlendingRangesSection(rawData);
    }

    /**
     * Writes a 32-bit unsigned integer into a byte buffer in big-endian byte order.
     *
     * @param buffer the target buffer
     * @param offset the destination offset in the buffer
     * @param value  the value to encode (treated as unsigned 32-bit)
     */
    private static void writeUInt32BigEndian(byte[] buffer, int offset, long value) {
        buffer[offset] = (byte) ((value >>> 24) & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 3] = (byte) (value & 0xFF);
    }
}