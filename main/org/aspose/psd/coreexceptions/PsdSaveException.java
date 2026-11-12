package org.aspose.psd.coreexceptions;

/**
 * Exception that is thrown when an error occurs while saving a PSD file.
 */
public class PsdSaveException extends Exception {
    /**
     * Initializes a new instance of the {@code PsdSaveException} class with a specified error message.
     *
     * @param message The error message that explains the reason for the exception.
     */
    public PsdSaveException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@code PsdSaveException} class with a specified error message and inner exception.
     *
     * @param message The error message that explains the reason for the exception.
     * @param cause   The exception that caused the current exception.
     */
    public PsdSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
