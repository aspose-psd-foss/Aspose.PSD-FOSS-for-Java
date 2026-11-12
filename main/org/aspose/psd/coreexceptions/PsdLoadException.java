package org.aspose.psd.coreexceptions;

/**
 * Exception that is thrown when an error occurs while loading a PSD file.
 */
public class PsdLoadException extends Exception {
    /**
     * Initializes a new instance of the {@code PsdLoadException} class with a specified error message.
     *
     * @param message The error message that explains the reason for the exception.
     */
    public PsdLoadException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@code PsdLoadException} class with a specified error message and inner exception.
     *
     * @param message        The error message that explains the reason for the exception.
     * @param innerException The exception that caused the current exception.
     */
    public PsdLoadException(String message, Throwable innerException) {
        super(message, innerException);
    }
}
