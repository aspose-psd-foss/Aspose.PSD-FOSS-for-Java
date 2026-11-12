package org.aspose.psd;

import java.io.InputStream;
import java.util.Objects;

/**
 * Represents a stream container used by resource save APIs.
 */
public class StreamContainer {
    /**
     * Initializes a new instance of the {@code StreamContainer} class.
     *
     * @param stream the wrapped stream.
     */
    public StreamContainer(InputStream stream) {
        this.stream = Objects.requireNonNull(stream, "stream");
    }

    /**
     * Gets the wrapped stream.
     *
     * @return the wrapped stream.
     */
    public InputStream getStream() {
        return stream;
    }

    private final InputStream stream;
}
