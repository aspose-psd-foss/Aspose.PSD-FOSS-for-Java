package org.aspose.psd;

import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.sections.ImageResourcesSection;
import org.aspose.psd.sections.SeekOrigin;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Reads big-endian data from a stream.
 * Used for parsing PSD file format which uses big-endian byte order.
 */
public final class BigEndianReader implements AutoCloseable {

    /**
     * Stores the underlying source stream data in memory.
     */
    private final byte[] data;

    /**
     * Indicates whether closing the reader should leave the stream open.
     */
    private final boolean leaveOpen;

    /**
     * The current position within the data array.
     */
    private long position;

    /**
     * Initializes a new instance of the {@link BigEndianReader} class.
     *
     * @param stream    The stream to read from.
     * @param leaveOpen true to keep the stream open; otherwise, false.
     * @throws IOException if an I/O error occurs while reading the stream.
     */
    public BigEndianReader(InputStream stream, boolean leaveOpen) throws IOException {
        this.leaveOpen = leaveOpen;
        
        // Read the entire stream into memory to support position tracking and length queries
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] tmp = new byte[8192];
        int read;
        while ((read = stream.read(tmp)) != -1) {
            buffer.write(tmp, 0, read);
        }
        this.data = buffer.toByteArray();
        this.position = 0;
    }

    public BigEndianReader(InputStream stream) throws IOException {
        this(stream, false);
    }

    /**
     * Seeks to a position in the stream.
     *
     * @param offset The byte offset relative to the origin.
     * @param origin The reference point used to obtain the new position.
     */
    public void seek(long offset, SeekOrigin origin) {
        switch (origin) {
            case BEGIN:
                position = offset;
                break;
            case CURRENT:
                position += offset;
                break;
            case END:
                position = data.length - offset;
                break;
            default:
                throw new IllegalArgumentException("Unknown SeekOrigin: " + origin);
        }
    }

    /**
     * Reads a single byte from the stream.
     *
     * @return The byte read from the stream.
     * @throws IOException if the end of the stream is reached or an I/O error occurs.
     */
    public byte readByte() throws IOException {
        if (position >= data.length) {
            throw new EOFException();
        }
        return data[(int) position++];
    }

    /**
     * Reads a signed byte (sbyte) from the stream.
     *
     * @return The sbyte read from the stream.
     * @throws IOException if the end of the stream is reached or an I/O error occurs.
     */
    public byte readSByte() throws IOException {
        return readByte();
    }

    /**
     * Reads a specified number of bytes from the stream.
     *
     * @param count The number of bytes to read.
     * @return The byte array containing the data read from the stream.
     * @throws IOException if the end of the stream is reached before reading the requested count.
     */
    public byte[] readBytes(int count) throws IOException {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        if (position + count > data.length) {
            throw new EOFException();
        }
        byte[] buffer = new byte[count];
        System.arraycopy(data, (int) position, buffer, 0, count);
        position += count;
        return buffer;
    }

    /**
     * Reads a 16-bit signed integer in big-endian format.
     *
     * @return The 16-bit signed integer read from the stream.
     * @throws IOException if an I/O error occurs.
     */
    public short readInt16() throws IOException {
        byte[] buffer = readBytes(2);
        return (short) (((buffer[0] & 0xFF) << 8) | (buffer[1] & 0xFF));
    }

    /**
     * Reads a 16-bit unsigned integer in big-endian format.
     *
     * @return The 16-bit unsigned integer read from the stream.
     * @throws IOException if an I/O error occurs.
     */
    public int readUInt16() throws IOException {
        return readInt16() & 0xFFFF;
    }

    /**
     * Reads a 32-bit signed integer in big-endian format.
     *
     * @return The 32-bit signed integer read from the stream.
     * @throws IOException if an I/O error occurs.
     */
    public int readInt32() throws IOException {
        byte[] buffer = readBytes(4);
        return ((buffer[0] & 0xFF) << 24)
                | ((buffer[1] & 0xFF) << 16)
                | ((buffer[2] & 0xFF) << 8)
                | (buffer[3] & 0xFF);
    }

    /**
     * Reads a 32-bit unsigned integer in big-endian format.
     *
     * @return The 32-bit unsigned integer read from the stream.
     * @throws IOException if an I/O error occurs.
     */
    public long readUInt32() throws IOException {
        return readInt32() & 0xFFFFFFFFL;
    }

    /**
     * Reads a 64-bit signed integer in big-endian format.
     *
     * @return The 64-bit signed integer read from the stream.
     * @throws IOException if an I/O error occurs.
     */
    public long readInt64() throws IOException {
        long high = readInt32() & 0xFFFFFFFFL;
        long low = readInt32() & 0xFFFFFFFFL;
        return (high << 32) | low;
    }

    /**
     * Reads a 64-bit unsigned integer in big-endian format. Since Java lacks an unsigned
     * 64-bit primitive, the result is returned as a signed {@code long}. Use
     * {@link Long#toUnsignedString(long)} or {@link Long#compareUnsigned(long, long)}
     * when unsigned semantics are required.
     *
     * @return The 64-bit value read from the stream (interpreted as unsigned).
     * @throws IOException if an I/O error occurs.
     */
    public long readUInt64() throws IOException {
        return readInt64();
    }

    /**
     * Reads a 32-bit floating-point number in big-endian format.
     *
     * @return The 32-bit floating-point number read from the stream.
     * @throws IOException if an I/O error occurs.
     */
    public float readSingle() throws IOException {
        return Float.intBitsToFloat(readInt32());
    }

    /**
     * Reads a 64-bit floating-point number in big-endian format.
     *
     * @return The 64-bit floating-point number read from the stream.
     * @throws IOException if an I/O error occurs.
     */
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readInt64());
    }

    /**
     * Reads a PSD Pascal string whose length byte and payload are padded to a 4-byte boundary.
     *
     * @param maxLength The maximum length of the string to read.
     * @return The string read from the stream.
     * @throws IOException if an I/O error occurs.
     */
    public String readPascalString(int maxLength) throws IOException, PsdLoadException {
        return readPascalStringAlignedTo4(maxLength);
    }

    public String readPascalString() throws IOException, PsdLoadException {
        return readPascalString(256);
    }

    /**
     * Reads a PSD resource Pascal string whose length byte and payload are padded to a 2-byte boundary.
     *
     * @param maxLength The maximum payload length to accept.
     * @return The decoded ASCII string.
     * @throws IOException      if an I/O error occurs.
     * @throws PsdLoadException if the stored string length exceeds {@code maxLength}.
     */
    public String readPascalStringAlignedTo2(int maxLength) throws IOException, PsdLoadException {
        return readPascalStringAlignedTo(2, maxLength);
    }

    public String readPascalStringAlignedTo2() throws IOException, PsdLoadException {
        return readPascalStringAlignedTo2(0xFF);
    }

    /**
     * Reads a PSD layer Pascal string whose length byte and payload are padded to a 4-byte boundary.
     *
     * @param maxLength The maximum payload length to accept.
     * @return The decoded ASCII string.
     * @throws IOException      if an I/O error occurs.
     * @throws PsdLoadException if the stored string length exceeds {@code maxLength}.
     */
    public String readPascalStringAlignedTo4(int maxLength) throws IOException, PsdLoadException {
        return readPascalStringAlignedTo(4, maxLength);
    }

    public String readPascalStringAlignedTo4() throws IOException, PsdLoadException {
        return readPascalStringAlignedTo4(0xFF);
    }

    /**
     * Reads a Pascal string and consumes alignment padding according to the enclosing PSD structure.
     *
     * @param boundary  The byte boundary used by the enclosing structure.
     * @param maxLength The maximum payload length to accept.
     * @return The decoded ASCII string.
     * @throws IOException      if an I/O error occurs.
     * @throws PsdLoadException if the stored string length exceeds {@code maxLength}.
     */
    private String readPascalStringAlignedTo(int boundary, int maxLength) throws IOException, PsdLoadException {
        int length = readByte() & 0xFF;
        if (length > maxLength) {
            throw new PsdLoadException(
                    "PSD Pascal string length " + length + " exceeds the supported maximum " + maxLength + ".");
        }

        String result = "";
        if (length > 0) {
            byte[] bytes = readBytes(length);
            result = new String(bytes, StandardCharsets.US_ASCII);
        }

        int padding = getPaddingLength(length, boundary);
        if (padding > 0) {
            readBytes(padding);
        }

        return result;
    }

    /**
     * Calculates padding after a Pascal string length byte and payload.
     *
     * @param payloadLength The stored Pascal string payload length.
     * @param boundary      The byte boundary used by the enclosing structure.
     * @return The number of padding bytes to consume.
     */
    private static int getPaddingLength(int payloadLength, int boundary) {
        return (boundary - ((payloadLength + 1) % boundary)) % boundary;
    }

    /**
     * Skips the specified number of bytes in the stream.
     *
     * @param count The number of bytes to skip.
     * @throws IOException if an I/O error occurs or the end of the stream is reached.
     */
    public void skip(int count) throws IOException {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        if (position + count > data.length) {
            throw new EOFException();
        }
        position += count;
    }

    /**
     * Releases the stream resources.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        // Data is already in memory; nothing to close
    }

    public long getPosition() {
        return position;
    }

    public long getLength() {
        return data.length;
    }
}