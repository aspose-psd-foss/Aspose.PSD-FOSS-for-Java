package org.aspose.psd.resources;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.coreexceptions.PsdLoadException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Represents a parsed PSD image resource block without any ID-specific semantics.
 */
public final class UnknownResource {

    /**
     * The PSD resource identifier.
     */
    private final short resourceId;

    /**
     * The decoded Pascal resource name.
     */
    private final String name;

    /**
     * The raw resource payload bytes.
     */
    private final byte[] data;

    /**
     * Initializes a new instance of the {@link UnknownResource} class.
     *
     * @param resourceId The PSD resource identifier.
     * @param name       The decoded Pascal resource name.
     * @param data       The raw resource payload.
     */
    public UnknownResource(short resourceId, String name, byte[] data) {
        this.resourceId = resourceId;
        this.name = name;
        this.data = data;
    }

    /**
     * Gets the PSD resource identifier.
     *
     * @return The resource identifier.
     */
    public short getResourceId() {
        return resourceId;
    }

    /**
     * Gets the decoded Pascal resource name.
     *
     * @return The resource name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the raw resource payload bytes.
     *
     * @return The raw payload bytes.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Loads one resource block from the reader, validating its envelope within the section.
     *
     * @param reader     The reader positioned at the start of a resource block.
     * @param sectionEnd The byte position of the end of the resources section.
     * @return An {@link UnknownResource} instance.
     * @throws PsdLoadException Thrown when the resource block is malformed.
     */
    public static UnknownResource load(BigEndianReader reader, long sectionEnd) throws PsdLoadException {
        long startPos = reader.getPosition();
        String[] field = {"signature"};

        try {
            requireBytes(reader, sectionEnd, startPos, field, 4, "signature");
            long signature = reader.readUInt32() & 0xFFFFFFFFL;
            if (signature != 0x3842494DL) { // "8BIM"
                throw new PsdLoadException(String.format(
                        "Malformed image resource block at offset %d: invalid signature 0x%08X.",
                        startPos, signature));
            }

            requireBytes(reader, sectionEnd, startPos, field, 2, "header resource ID");
            short resourceId = reader.readInt16();

            requireBytes(reader, sectionEnd, startPos, field, 1, "name length");
            int nameLength = reader.readByte() & 0xFF;
            requireBytes(reader, sectionEnd, startPos, field, nameLength, "name");
            byte[] nameBytes = reader.readBytes(nameLength);
            String name = new String(nameBytes, StandardCharsets.US_ASCII);
            if ((nameLength + 1) % 2 != 0) {
                requireBytes(reader, sectionEnd, startPos, field, 1, "name padding");
                reader.readByte();
            }

            requireBytes(reader, sectionEnd, startPos, field, 4, "payload length");
            int dataLength = reader.readInt32();
            if (dataLength < 0) {
                throw new PsdLoadException(String.format(
                        "Malformed image resource block at offset %d: negative payload length %d.",
                        startPos, dataLength));
            }

            requireBytes(reader, sectionEnd, startPos, field, dataLength, "payload");
            byte[] data = reader.readBytes(dataLength);
            if (dataLength % 2 != 0) {
                requireBytes(reader, sectionEnd, startPos, field, 1, "payload padding");
                reader.readByte();
            }

            return new UnknownResource(resourceId, name, data);
        } catch (IOException ex) {
            // IOException covers EOF-like conditions from the underlying stream.
            throw new PsdLoadException(String.format(
                    "Malformed image resource block at offset %d: truncated %s.",
                    startPos, field[0]), ex);
        }
    }

    /**
     * Ensures that the current resource field fits within the declared section and the input stream.
     *
     * @param reader     The reader positioned at the field.
     * @param sectionEnd The byte position of the end of the resources section.
     * @param startPos   The byte position of the resource block start.
     * @param field      The field reported if a subsequent read reaches the end of the stream.
     * @param count      The number of bytes required for the field.
     * @param reason     The field name used in malformed-block errors.
     * @throws PsdLoadException Thrown when the field exceeds the section or stream boundary.
     */
    private static void requireBytes(BigEndianReader reader, long sectionEnd, long startPos,
                                     String[] field, int count, String reason) throws PsdLoadException {
        field[0] = reason;
        long position = reader.getPosition();
        long length = reader.getLength();
        if (position > sectionEnd || sectionEnd - position < count
                || position > length || length - position < count) {
            throw new PsdLoadException(String.format(
                    "Malformed image resource block at offset %d: truncated %s.",
                    startPos, reason));
        }
    }

    /**
     * Creates a public read-only summary for this resource block.
     *
     * @return The public {@link PsdResourceInfo} summary with {@link PsdResourceKind}.
     */
    public PsdResourceInfo toPublicInfo() {
        return new PsdResourceInfo(resourceId, name, PsdResourceKind.Unknown, data.length, null, null);
    }
}