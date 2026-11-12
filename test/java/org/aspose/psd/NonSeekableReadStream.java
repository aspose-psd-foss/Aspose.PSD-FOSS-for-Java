package org.aspose.psd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Wraps a readable in-memory stream and intentionally disables seeking.
 */
final class NonSeekableReadStream extends InputStream {

    /** Stores the wrapped in-memory stream that provides the readable payload. */
    private final ByteArrayInputStream innerStream;

    /** The total length of the stream. */
    private final long length;

    /**
     * Initializes a new instance of the {@code NonSeekableReadStream} class.
     *
     * @param data The bytes exposed by the stream.
     */
    public NonSeekableReadStream(byte[] data) {
        Objects.requireNonNull(data);
        this.innerStream = new ByteArrayInputStream(data);
        this.length = data.length;
    }

    /** Gets a value indicating whether the stream supports reading. */
    @Override
    public int read() throws IOException {
        return innerStream.read();
    }

    /** Reads bytes from the stream. */
    @Override
    public int read(byte[] buffer, int offset, int len) throws IOException {
        return innerStream.read(buffer, offset, len);
    }

    /** Gets the total length of the stream. */
    public long length() {
        return length;
    }

    /** Seeking is not supported. */
    @Override
    public long skip(long n) throws IOException {
        throw new UnsupportedOperationException();
    }

    /** Changing the stream length is not supported. */
    public void setLength(long value) {
        throw new UnsupportedOperationException();
    }

    /** Writes are not supported. */
    public void write(byte[] buffer, int offset, int len) {
        throw new UnsupportedOperationException();
    }

    /** Flushes buffered state (no operation). */
    @Override
    public void close() throws IOException {
        innerStream.close();
    }

    /** Indicates that mark/reset are not supported. */
    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public synchronized void mark(int readlimit) {
        // No-op: marking not supported
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
}
