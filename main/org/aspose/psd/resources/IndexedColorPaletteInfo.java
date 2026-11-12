package org.aspose.psd.resources;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides a read-only view over an indexed-color PSD palette.
 */
public final class IndexedColorPaletteInfo {
    /** The decoded palette entries. */
    private final List<Color> entries;

    /**
     * Initializes a new instance of the {@code IndexedColorPaletteInfo} class.
     *
     * @param entries The decoded palette entries.
     */
    public IndexedColorPaletteInfo(Color[] entries) {
        Color[] copy = entries.clone();
        List<Color> list = new ArrayList<>(Arrays.asList(copy));
        this.entries = Collections.unmodifiableList(list);
    }

    /**
     * Gets the decoded palette entries.
     *
     * @return an unmodifiable list of palette entries
     */
    public List<Color> getEntries() {
        return entries;
    }
}
