package org.aspose.psd.sections;

import java.io.EOFException;
import java.io.IOException;
import org.aspose.psd.BigEndianReader;
import org.aspose.psd.BigEndianWriter;
import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.resources.IndexedColorPalette;
import org.aspose.psd.ColorModes;

/**
 * Represents the Color Mode Data section of a PSD file.
 */
public final class ColorData {
    public static final ColorData EMPTY = new ColorData(new byte[0], PsdColorDataKind.NONE, null);

    private final PsdColorDataKind kind;
    private final IndexedColorPalette indexedPalette;
    private final byte[] rawData;

    private ColorData(byte[] rawData, PsdColorDataKind kind, IndexedColorPalette indexedPalette) {
        this.kind = kind;
        this.indexedPalette = indexedPalette;
        this.rawData = rawData;
    }

    private ColorData(byte[] rawData, PsdColorDataKind kind) {
        this.kind = kind;
        this.indexedPalette = null;
        this.rawData = rawData;
    }


    /**
     * Loads the Color Mode Data section from the provided reader.
     *
     * @param reader    the reader positioned at the start of the Color Mode Data section
     * @param colorMode the color mode value from the PSD header
     * @return a {@code ColorData} instance representing the parsed data
     * @throws PsdLoadException if an I/O error occurs while reading
     */
    public static ColorData load(BigEndianReader reader, ColorModes colorMode) throws PsdLoadException {
        try {
            long length = reader.readUInt32();
            if (length == 0) {
                return EMPTY;
            }

            // Guard against excessively large lengths (should be safe due to in‑memory parser limits)
            if (length > Integer.MAX_VALUE) {
                throw new PsdLoadException("Color Data length exceeds maximum supported size.");
            }

            int dataLen = (int) length;
            byte[] data = reader.readBytes(dataLen);
            ColorData colorData;
            // Determine kind based on color mode and payload size
            switch (colorMode) {
                case Indexed:
                    if (data.length == IndexedColorPalette.EXPECTED_RAW_LENGTH) {
                        colorData = new ColorData(data, PsdColorDataKind.INDEXED_PALETTE,
                                IndexedColorPalette.parse(data));
                    } else {
                        colorData = new ColorData(data, PsdColorDataKind.RAW_PRESERVED);
                    }
                    break;
                case Rgb:
                    colorData = new ColorData(data, PsdColorDataKind.RGB_PAYLOAD);
                    break;
                case Cmyk:
                    colorData = new ColorData(data, PsdColorDataKind.CMYK_PAYLOAD);
                    break;
                default:
                    colorData = new ColorData(data, PsdColorDataKind.RAW_PRESERVED);
                    break;
            }
            return colorData;
        } catch (EOFException e) {
            throw new PsdLoadException("Unexpected end of file while reading Color Data.", e);
        } catch (Exception e) {
            if (e instanceof PsdLoadException) {
                throw (PsdLoadException) e;
            }
            throw new PsdLoadException("Failed to load Color Data.", e);
        }
    }

    public PsdColorDataKind getKind() {
        return kind;
    }

    public IndexedColorPalette getIndexedPalette() {
        return indexedPalette;
    }

    public byte[] getRawData() {
        return rawData;
    }

    /**
     * Converts this ColorData instance to a public PsdColorDataInfo instance.
     *
     * @return a PsdColorDataInfo instance representing this color data
     */
    public PsdColorDataInfo toPublicInfo() {
        return new PsdColorDataInfo(kind, indexedPalette, rawData);
    }

    /**
     * Saves the Color Mode Data section to the provided writer.
     *
     * @param writer the writer to which the color data should be written
     * @throws IOException if an I/O error occurs while writing
     */
    public void save(BigEndianWriter writer) throws IOException {
        if (rawData == null || rawData.length == 0) {
            writer.writeUInt(0);
            return;
        }
        writer.writeUInt(rawData.length);
        writer.write(rawData);
    }
}
