package org.aspose.psd.sections;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.BigEndianWriter;
import org.aspose.psd.ColorModes;
import org.aspose.psd.PsdVersion;
import org.aspose.psd.coreexceptions.PsdLoadException;

import java.io.IOException;

/**
 * Contains the header information of a Photoshop document.
 */
public final class PsdHeader {

    /**
     * Shared PSD/PSB file signature value (0x38425053 = '8BPS').
     */
    public static final int PSD_SIGNATURE = 0x38425053;

    /**
     * Shared PSD/PSB file signature value (0x38425053 = '8BPS').
     */
    public static final int PSB_SIGNATURE = PSD_SIGNATURE;

    /**
     * PSD version value.
     */
    public static final short PSD_VERSION = 1;

    /**
     * PSB large-document version value.
     */
    public static final short PSB_VERSION = 2;

    private int width;
    private int height;
    private int channels;
    private int bitDepth;
    private ColorModes colorMode;
    private PsdVersion formatVersion;

    private PsdHeader() {
    }

    /**
     * Gets the width of the image in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the image in pixels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the number of channels in the image (e.g., 3 for RGB).
     */
    public int getChannels() {
        return channels;
    }

    /**
     * Gets the bit depth per channel (e.g., 8, 16, 32).
     */
    public int getBitDepth() {
        return bitDepth;
    }

    /**
     * Gets the color mode of the image.
     */
    public ColorModes getColorMode() {
        return colorMode;
    }

    /**
     * Gets the raw PSD container version: 1 for PSD and 2 for PSB.
     */
    public int getVersion() {
        return formatVersion.getValue();
    }

    /**
     * Gets the strongly typed PSD container version.
     */
    PsdVersion getFormatVersion() {
        return formatVersion;
    }

    /**
     * Gets a value indicating whether the document uses the PSB large-document container.
     */
    public boolean isLargeDocument() {
        return formatVersion == PsdVersion.PSB;
    }

    /**
     * Loads the fixed PSD/PSB file header from the reader.
     *
     * @param reader The reader positioned at the start of the file header.
     * @return The parsed {@link PsdHeader} instance.
     */
    public static PsdHeader load(BigEndianReader reader) throws IOException, PsdLoadException {
        long signature = reader.readUInt32();
        if (signature != (PSD_SIGNATURE & 0xFFFFFFFFL)) {
            throw new PsdLoadException("Invalid PSD signature. Expected '8BPS' (0x38425053).");
        }

        int rawVersion = reader.readUInt16();
        if (rawVersion != (PSD_VERSION & 0xFFFF) && rawVersion != (PSB_VERSION & 0xFFFF)) {
            throw new PsdLoadException(
                    "Unsupported PSD version: " + rawVersion
                            + ". Supported versions: " + PSD_VERSION
                            + " (PSD) and " + PSB_VERSION + " (PSB).");
        }

        PsdVersion version = PsdVersion.fromValue(rawVersion);

        byte[] reserved = reader.readBytes(6);
        for (byte value : reserved) {
            if (value != 0) {
                throw new PsdLoadException("PSD header reserved bytes must be zero.");
            }
        }

        int channels = reader.readUInt16();
        int height = reader.readInt32();
        int width = reader.readInt32();
        int bitDepth = reader.readUInt16();

        try {
            ColorModes colorMode = ColorModes.fromValue((short) reader.readUInt16());
            validateHeaderFields(version, channels, height, width, bitDepth, colorMode);

            PsdHeader header = new PsdHeader();
            header.width = width;
            header.height = height;
            header.channels = channels;
            header.bitDepth = bitDepth;
            header.colorMode = colorMode;
            header.formatVersion = version;
            return header;
        } catch (IllegalArgumentException ex) {
            throw new PsdLoadException(ex.toString());
        }


    }

    /**
     * Validates PSD/PSB header invariants before exposing the parsed document state.
     *
     * @param version   The parsed PSD container version.
     * @param channels  The declared channel count.
     * @param height    The declared document height.
     * @param width     The declared document width.
     * @param bitDepth  The declared bits per channel.
     * @param colorMode The declared PSD color mode.
     * @throws PsdLoadException Thrown when a header field is outside the supported PSD/PSB range.
     */
    private static void validateHeaderFields(
            PsdVersion version,
            int channels,
            int height,
            int width,
            int bitDepth,
            ColorModes colorMode) throws PsdLoadException {

        if (channels < 1 || channels > 56) {
            throw new PsdLoadException(
                    "PSD header channel count " + channels
                            + " is outside the supported range 1-56.");
        }

        int maxDimension = version == PsdVersion.PSB ? 300000 : 30000;
        if (height < 1 || height > maxDimension) {
            throw new PsdLoadException(
                    "PSD header height " + height
                            + " is outside the supported range 1-" + maxDimension
                            + " for this document version.");
        }

        if (width < 1 || width > maxDimension) {
            throw new PsdLoadException(
                    "PSD header width " + width
                            + " is outside the supported range 1-" + maxDimension
                            + " for this document version.");
        }

        if (bitDepth != 1 && bitDepth != 8 && bitDepth != 16 && bitDepth != 32) {
            throw new PsdLoadException(
                    "PSD header bit depth " + bitDepth
                            + " is not supported. Supported values are 1, 8, 16, and 32.");
        }

        if (colorMode == null) {
            throw new PsdLoadException(
                    "PSD header color mode " + colorMode
                            + " is not recognized by this implementation.");
        }
    }

    /**
     * Writes the fixed PSD/PSB file header to the writer.
     *
     * @param writer The destination writer.
     */
    public void save(BigEndianWriter writer) {
        writer.writeUShort(formatVersion.getValue());
        writer.write(new byte[6]);
        writer.writeUShort(channels);
        writer.writeInt(height);
        writer.writeInt(width);
        writer.writeUShort(bitDepth);
        writer.writeUShort(colorMode.getValue());
    }

    /**
     * Updates the header color mode for the compatibility subset that rewrites header metadata.
     *
     * @param colorMode The color mode to store.
     */
    public void setColorMode(ColorModes colorMode) {
        this.colorMode = colorMode;
    }

    /**
     * Updates the PSD container version.
     *
     * @param version The PSD container version to store.
     */
    void setVersion(int version) {
        if (version != PSD_VERSION && version != PSB_VERSION) {
            throw new IllegalArgumentException(
                    "Supported PSD versions are 1 (PSD) and 2 (PSB). Got: " + version);
        }
        this.formatVersion = PsdVersion.fromValue(version);}
}