package org.aspose.psd.resources;

import java.awt.Color;

public final class IndexedColorPalette {
    public static final int EXPECTED_RAW_LENGTH = 768;

    private final Color[] entries;

    public IndexedColorPalette(Color[] entries) {
        this.entries = entries;
    }

    public Color[] getEntries() {
        return entries;
    }

    public static IndexedColorPalette parse(byte[] rawData) {
        if (rawData == null || rawData.length != EXPECTED_RAW_LENGTH) {
            throw new IllegalArgumentException("Invalid raw data length for IndexedColorPalette");
        }
        Color[] entries = new Color[256];
        for (int i = 0; i < entries.length; i++) {
            int r = rawData[i] & 0xFF;
            int g = rawData[i + 256] & 0xFF;
            int b = rawData[i + 512] & 0xFF;
            entries[i] = new Color(r, g, b);
        }
        return new IndexedColorPalette(entries);
    }

    public IndexedColorPaletteInfo toPublicInfo() {
        return new IndexedColorPaletteInfo(entries);
    }
}
