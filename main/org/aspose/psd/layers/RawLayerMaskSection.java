package org.aspose.psd.layers;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.PsdSectionReader;
import org.aspose.psd.coreexceptions.PsdLoadException;
import java.io.EOFException;
import java.io.IOException;

public final class RawLayerMaskSection {
    public static final RawLayerMaskSection EMPTY = new RawLayerMaskSection(new byte[0]);

    public static RawLayerMaskSection empty() {
        return EMPTY;
    }

    private final byte[] rawData;

    public RawLayerMaskSection(byte[] rawData) {
        this.rawData = rawData;
    }

    public byte[] getRawData() {
        return rawData;
    }

    /** Returns a defensive copy of the raw mask section bytes. */
    public byte[] getRawBytes() {
        return rawData.clone();
    }

    public static RawLayerMaskSection load(BigEndianReader reader, long sectionEnd) throws PsdLoadException {
        long length;
        try {
            length = reader.readUInt32();
        } catch (IOException e) {
            throw new PsdLoadException("Unexpected end of file while reading layer mask length", e);
        }
        if (length > Integer.MAX_VALUE) {
            throw new PsdLoadException("Layer mask length exceeds int range");
        }
        int lengthInt = (int) length;
        int payloadLength = PsdSectionReader.getNestedMemoryBackedLength(reader, lengthInt, sectionEnd, "Layer mask subsection");
        if (payloadLength > Integer.MAX_VALUE - Integer.BYTES) {
            throw new PsdLoadException("Layer mask subsection is too large to preserve in memory with its length field.");
        }

        byte[] rawData = new byte[Integer.BYTES + payloadLength];
        writeUInt32BigEndian(rawData, 0, lengthInt);
        if (payloadLength > 0) {
            try {
                byte[] payload = reader.readBytes(payloadLength);
                System.arraycopy(payload, 0, rawData, Integer.BYTES, payloadLength);
            } catch (EOFException e) {
                throw new PsdLoadException("Unexpected end of file while reading layer mask payload", e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new RawLayerMaskSection(rawData);
    }

    private static void writeUInt32BigEndian(byte[] buffer, int offset, int value) {
        buffer[offset] = (byte) ((value >>> 24) & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 3] = (byte) (value & 0xFF);
    }
}
