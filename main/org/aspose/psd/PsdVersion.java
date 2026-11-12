package org.aspose.psd;

/**
 * Represents the supported PSD container versions.
 */
public enum PsdVersion {
    PSD(1),
    PSB(2);

    private final int value;

    PsdVersion(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PsdVersion fromValue(int value) {
        for (PsdVersion v : values()) {
            if (v.value == value) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unsupported PSD version value: " + value);
    }
}
