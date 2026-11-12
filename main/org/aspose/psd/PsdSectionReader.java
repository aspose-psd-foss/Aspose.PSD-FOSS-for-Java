package org.aspose.psd;

import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.BigEndianReader;
import java.io.EOFException;
import java.io.IOException;

/**
 * Provides bounded reads for PSD/PSB sections that are stored in memory by this implementation.
 */
public final class PsdSectionReader {

    private PsdSectionReader() {
        // Prevent instantiation
    }

    /**
     * Reads a declared section payload after validating stream boundaries and the in-memory size limit.
     *
     * @param reader      The reader positioned at the start of the section payload.
     * @param length      The declared payload length.
     * @param sectionName The PSD/PSB section name used in error messages.
     * @return The section payload bytes.
     * @throws PsdLoadException Thrown when the declared length cannot be read safely.
     * @throws EOFException     Thrown when the end of the stream is reached unexpectedly.
     */
    public static byte[] readBytes(BigEndianReader reader, long length, String sectionName) throws IOException, PsdLoadException {
        int boundedLength = getMemoryBackedLength(reader, length, sectionName);
        return boundedLength == 0 ? new byte[0] : reader.readBytes(boundedLength);
    }

    /**
     * Validates a declared section length and returns the equivalent in-memory array size.
     *
     * @param reader      The reader positioned at the start of the section payload.
     * @param length      The declared payload length.
     * @param sectionName The PSD/PSB section name used in error messages.
     * @return The validated length as an {@code int}.
     * @throws PsdLoadException Thrown when the declared length cannot be represented or exceeds available bytes.
     */
    public static int getMemoryBackedLength(BigEndianReader reader, long length, String sectionName) throws PsdLoadException {
        if (length > Integer.MAX_VALUE) {
            throw new PsdLoadException(sectionName + " length " + length +
                    " exceeds the current in-memory parser limit of " + Integer.MAX_VALUE + " bytes.");
        }

        long remaining = getRemainingBytes(reader, sectionName);
        if (length > remaining) {
            throw new PsdLoadException(sectionName + " length " + length +
                    " exceeds the remaining stream data (" + remaining + " bytes).");
        }

        return (int) length;
    }

    /**
     * Validates a signed length field that belongs to a bounded section.
     *
     * @param length      The declared signed length.
     * @param sectionName The PSD/PSB section name used in error messages.
     * @return The non-negative length.
     * @throws PsdLoadException Thrown when the length is negative.
     */
    public static long validateSignedLength(long length, String sectionName) throws PsdLoadException {
        if (length < 0) {
            throw new PsdLoadException(sectionName + " length cannot be negative.");
        }
        return length;
    }

    /**
     * Validates that a nested payload stays inside the already-bounded enclosing section.
     *
     * @param reader      The reader positioned at the nested payload start.
     * @param length      The nested payload length.
     * @param sectionEnd  The byte position of the enclosing section end.
     * @param sectionName The PSD/PSB subsection name used in error messages.
     * @return The validated length as an {@code int}.
     * @throws PsdLoadException Thrown when the nested payload exceeds its enclosing section.
     */
    public static int getNestedMemoryBackedLength(BigEndianReader reader, long length, long sectionEnd, String sectionName) throws PsdLoadException {
        if (length > Integer.MAX_VALUE) {
            throw new PsdLoadException(sectionName + " length " + length +
                    " exceeds the current in-memory parser limit of " + Integer.MAX_VALUE + " bytes.");
        }

        long remaining = sectionEnd - reader.getPosition();
        if (remaining < 0 || length > remaining) {
            throw new PsdLoadException(sectionName + " length " + length + " exceeds the enclosing section boundary.");
        }

        return (int) length;
    }

    /**
     * Returns the remaining bytes in a seekable reader stream.
     *
     * @param reader      The reader to inspect.
     * @param sectionName The PSD/PSB section name used in error messages.
     * @return The number of remaining bytes.
     * @throws PsdLoadException Thrown when the reader position is outside the stream.
     */
    private static long getRemainingBytes(BigEndianReader reader, String sectionName) throws PsdLoadException {
        long remaining = reader.getLength() - reader.getPosition();
        if (remaining < 0) {
            throw new PsdLoadException(sectionName + " reader position is beyond the stream length.");
        }
        return remaining;
    }
}
