package org.aspose.psd.sections;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.aspose.psd.resources.IndexedColorPalette;
import org.aspose.psd.resources.IndexedColorPaletteInfo;

/**
 * Provides a read-only view over the Color Mode Data section.
 */
public final class PsdColorDataInfo {
    private final PsdColorDataKind kind;
    private final IndexedColorPalette indexedPalette;
    private final byte[] rawData;

    /**
     * Initializes a new instance of the {@code PsdColorDataInfo} class.
     *
     * @param kind        the kind of color data
     * @param indexedPalette the indexed palette, if any
     * @param rawData     the raw color mode data
     */
    PsdColorDataInfo(PsdColorDataKind kind, IndexedColorPalette indexedPalette, byte[] rawData) {
        this.kind = kind;
        this.indexedPalette = indexedPalette;
        this.rawData = rawData;
    }

    /**
     * Gets the kind of color data.
     *
     * @return the kind
     */
    public PsdColorDataKind getKind() {
        return kind;
    }

    /**
     * Gets the raw data length.
     *
     * @return the raw data length
     */
    public int getRawDataLength() {
        return rawData != null ? rawData.length : 0;
    }

    /**
     * Gets the indexed palette, if any.
     *
     * @return the indexed palette or null
     */
    public IndexedColorPaletteInfo getIndexedPalette() {
        if (indexedPalette == null) {
            return null;
        }
        return new IndexedColorPaletteInfo(indexedPalette.getEntries());
    }

    /**
     * Gets the raw color mode data.
     *
     * @return the raw data (clone to prevent modification)
     */
    public byte[] getRawData() {
        if (rawData == null) {
            return new byte[0];
        }
        return rawData.clone();
    }
}
