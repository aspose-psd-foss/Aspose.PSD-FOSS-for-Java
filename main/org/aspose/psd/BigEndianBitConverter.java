package org.aspose.psd;

/**
 * Provides helper methods for reading big-endian primitive values from byte arrays.
 */
final class BigEndianBitConverter {

    private BigEndianBitConverter() {
        // Prevent instantiation
    }

    /**
     * Reads a 32-bit signed integer in big-endian format from the specified byte array.
     *
     * @param bytes  The source byte array.
     * @param offset The zero-based offset of the integer.
     * @return The parsed 32-bit signed integer.
     */
    public static int toInt32(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 24) |
               ((bytes[offset + 1] & 0xFF) << 16) |
               ((bytes[offset + 2] & 0xFF) << 8) |
               (bytes[offset + 3] & 0xFF);
    }
}
