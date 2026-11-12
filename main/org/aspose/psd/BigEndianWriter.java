package org.aspose.psd;

import org.aspose.psd.coreexceptions.PsdSaveException;
import org.aspose.psd.sections.ImageResourcesSection;
import org.aspose.psd.sections.SeekOrigin;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Writes big-endian data to a stream.
 * Used for writing PSD file format which uses big-endian byte order.
 */
public final class BigEndianWriter implements AutoCloseable {

    /**
     * Stores the underlying destination stream.
     */
    private final OutputStream stream;

    /**
     * Indicates whether disposing the writer should leave the stream open.
     */
    private final boolean leaveOpen;

    /**
     * Tracks whether the writer has already been disposed.
     */
    private boolean disposed;

    /**
     * Gets the current position within the stream.
     * <p>
     * Note: {@link OutputStream} does not expose a position; this port tracks
     * the number of bytes written. Seeking is only supported when the underlying
     * stream supports it (see {@link #seek(long, SeekOrigin)}).
     */
    private long position;

    /**
     * Initializes a new instance of the {@link BigEndianWriter} class.
     *
     * @param stream    the stream to write to.
     * @param leaveOpen true to keep the stream open; otherwise, false.
     */
    public BigEndianWriter(OutputStream stream, boolean leaveOpen) {
        this.stream = stream;
        this.leaveOpen = leaveOpen;
    }

    /**
     * Initializes a new instance of the {@link BigEndianWriter} class that
     * closes the underlying stream on dispose.
     *
     * @param stream the stream to write to.
     */
    public BigEndianWriter(OutputStream stream) {
        this(stream, false);
    }

    /**
     * Gets the current position within the stream.
     */
    public long getPosition() {
        return position;
    }

    /**
     * Seeks to a position in the stream.
     * <p>
     * Only supported if the underlying stream is seekable (e.g. a
     * {@link java.io.RandomAccessFile} or {@link java.nio.channels.SeekableByteChannel}
     * backed stream). Otherwise an {@link UnsupportedOperationException} is thrown.
     *
     * @param offset the byte offset relative to the origin.
     * @param origin the reference point used to obtain the new position.
     */
    public void seek(long offset, SeekOrigin origin) {
        throw new UnsupportedOperationException(
                "Seeking is not supported by a plain OutputStream; wrap a seekable stream instead.");
    }

    /**
     * Writes a single byte to the stream.
     *
     * @param value the byte value to write.
     */
    public void write(byte value) {
        try {
            stream.write(value & 0xFF);
            position++;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes a byte array to the stream.
     *
     * @param buffer the byte array to write.
     */
    public void write(byte[] buffer) {
        write(buffer, 0, buffer.length);
    }

    /**
     * Writes a subarray of bytes to the stream.
     *
     * @param buffer the byte array containing the data to write.
     * @param offset the zero-based byte offset in buffer at which to begin writing bytes.
     * @param count  the number of bytes to write.
     */
    public void write(byte[] buffer, int offset, int count) {
        try {
            stream.write(buffer, offset, count);
            position += count;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes a signed byte (sbyte) to the stream.
     *
     * @param value the sbyte value to write.
     */
    public void writeSByte(byte value) {
        write(value);
    }

    /**
     * Writes a 16-bit signed integer in big-endian format.
     *
     * @param value the 16-bit signed integer to write.
     */
    public void writeShort(short value) {
        write(new byte[]{
                (byte) ((value >>> 8) & 0xFF),
                (byte) (value & 0xFF)
        });
    }

    /**
     * Writes a 16-bit unsigned integer in big-endian format.
     *
     * @param value the 16-bit unsigned integer to write.
     */
    public void writeUShort(int value) {
        writeShort((short) value);
    }

    /**
     * Writes a 32-bit signed integer in big-endian format.
     *
     * @param value the 32-bit signed integer to write.
     */
    public void writeInt(int value) {
        write(new byte[]{
                (byte) ((value >>> 24) & 0xFF),
                (byte) ((value >>> 16) & 0xFF),
                (byte) ((value >>> 8) & 0xFF),
                (byte) (value & 0xFF)
        });
    }

    /**
     * Writes a 32-bit unsigned integer in big-endian format.
     *
     * @param value the 32-bit unsigned integer to write.
     */
    public void writeUInt(long value) {
        writeInt((int) value);
    }

    /**
     * Writes a 64-bit signed integer in big-endian format.
     *
     * @param value the 64-bit signed integer to write.
     */
    public void writeLong(long value) {
        write(new byte[]{
                (byte) ((value >>> 56) & 0xFF),
                (byte) ((value >>> 48) & 0xFF),
                (byte) ((value >>> 40) & 0xFF),
                (byte) ((value >>> 32) & 0xFF),
                (byte) ((value >>> 24) & 0xFF),
                (byte) ((value >>> 16) & 0xFF),
                (byte) ((value >>> 8) & 0xFF),
                (byte) (value & 0xFF)
        });
    }

    /**
     * Writes a 64-bit unsigned integer in big-endian format.
     *
     * @param value the 64-bit unsigned integer to write.
     */
    public void writeULong(long value) {
        writeLong(value);
    }

    /**
     * Writes a PSD Pascal string whose length byte and payload are padded to a 4-byte boundary.
     *
     * @param value the string to write.
     */
    public void writePascalString(String value) throws PsdSaveException {
        writePascalStringAlignedTo4(value);
    }

    /**
     * Writes a PSD resource Pascal string whose length byte and payload are padded to a 2-byte boundary.
     *
     * @param value the ASCII string to write.
     * @throws PsdSaveException Thrown when the encoded string is too long for a PSD Pascal string.
     */
    public void writePascalStringAlignedTo2(String value) throws PsdSaveException {
        writePascalStringAlignedTo(value, 2);
    }

    /**
     * Writes a PSD layer Pascal string whose length byte and payload are padded to a 4-byte boundary.
     *
     * @param value the ASCII string to write.
     * @throws PsdSaveException Thrown when the encoded string is too long for a PSD Pascal string.
     */
    public void writePascalStringAlignedTo4(String value) throws PsdSaveException {
        writePascalStringAlignedTo(value, 4);
    }

    /**
     * Returns the stored byte count for a PSD Pascal string aligned to a 4-byte boundary.
     *
     * @param value the ASCII string to measure.
     * @return the length byte, payload, and padding byte count.
     */
    public static int getPascalStringStorageLengthAlignedTo4(String value) {
        int length = (value == null || value.isEmpty())
                ? 0
                : value.getBytes(StandardCharsets.US_ASCII).length;
        return 1 + length + getPaddingLength(length, 4);
    }

    /**
     * Writes a Pascal string and pads it according to the enclosing PSD structure.
     *
     * @param value    the ASCII string to write.
     * @param boundary the byte boundary used by the enclosing structure.
     */
    private void writePascalStringAlignedTo(String value, int boundary) throws PsdSaveException {
        byte[] bytes = (value == null ? "" : value).getBytes(StandardCharsets.US_ASCII);
        if (bytes.length > 0xFF) {
            throw new PsdSaveException(
                    "PSD Pascal string length " + bytes.length
                            + " exceeds the supported maximum " + 0xFF + ".");
        }

        write((byte) bytes.length);
        if (bytes.length > 0) {
            write(bytes);
        }

        int padding = getPaddingLength(bytes.length, boundary);
        for (int i = 0; i < padding; i++) {
            write((byte) 0);
        }
    }

    /**
     * Calculates padding after a Pascal string length byte and payload.
     *
     * @param payloadLength the stored Pascal string payload length.
     * @param boundary      the byte boundary used by the enclosing structure.
     * @return the number of padding bytes to write.
     */
    private static int getPaddingLength(int payloadLength, int boundary) {
        return (boundary - ((payloadLength + 1) % boundary)) % boundary;
    }

    /**
     * Writes all bytes from the specified array to the stream.
     *
     * @param data the byte array to write.
     */
    public void writeAllBytes(byte[] data) {
        write(data);
    }

    /**
     * Releases the stream resources.
     */
    @Override
    public void close() {
        if (disposed) {
            return;
        }
        disposed = true;

        if (!leaveOpen) {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}