package org.aspose.psd.layers;

/**
 * Holds raw layer data parsed from a PSD file.
 * This class is a simple data holder used by {@link Layer} and related classes.
 */
public class LayerRawData {
    private final byte flags;
    private final String blendModeKey;
    private final LayerChannelInfo[] channelInfo;
    private final RawLayerMaskSection layerMaskSection;
    private final RawLayerBlendingRangesSection blendingRangesSection;
    private final byte[] additionalLayerData;

    /**
     * Empty instance used when no raw data is available.
     */
    public static final LayerRawData EMPTY = new LayerRawData();

    /**
     * Default constructor used for the {@link #EMPTY} instance.
     * Initializes all fields with safe default values.
     */
    public LayerRawData() {
        this.flags = 0;
        this.blendModeKey = "";
        this.channelInfo = new LayerChannelInfo[0];
        this.layerMaskSection = RawLayerMaskSection.EMPTY;
        this.blendingRangesSection = RawLayerBlendingRangesSection.EMPTY;
        this.additionalLayerData = new byte[0];
    }

    /**
     * Full‑argument constructor matching the usage in {@link LayerRecordReader}.
     *
     * @param flags               Layer flags.
     * @param blendModeKey        Original blend mode key string.
     * @param channelInfo         Array of channel information.
     * @param layerMaskSection    Raw mask section.
     * @param blendingRangesSection Raw blending‑ranges section.
     * @param additionalLayerData Additional layer data bytes.
     */
    public LayerRawData(byte flags,
                        String blendModeKey,
                        LayerChannelInfo[] channelInfo,
                        RawLayerMaskSection layerMaskSection,
                        RawLayerBlendingRangesSection blendingRangesSection,
                        byte[] additionalLayerData) {
        this.flags = flags;
        this.blendModeKey = blendModeKey != null ? blendModeKey : "";
        this.channelInfo = channelInfo != null ? channelInfo : new LayerChannelInfo[0];
        this.layerMaskSection = layerMaskSection != null ? layerMaskSection : RawLayerMaskSection.EMPTY;
        this.blendingRangesSection = blendingRangesSection != null ? blendingRangesSection : RawLayerBlendingRangesSection.EMPTY;
        this.additionalLayerData = additionalLayerData != null ? additionalLayerData : new byte[0];
    }

    public byte getFlags() {
        return flags;
    }

    public String getBlendModeKey() {
        return blendModeKey;
    }

    public LayerChannelInfo[] getChannelInfo() {
        return channelInfo;
    }

    public RawLayerMaskSection getLayerMaskSection() {
        return layerMaskSection;
    }

    public RawLayerBlendingRangesSection getBlendingRangesSection() {
        return blendingRangesSection;
    }

    public byte[] getAdditionalLayerData() {
        return additionalLayerData;
    }

    /**
     * Returns a new {@code LayerRawData} instance with the same data except for an updated blend mode key.
     *
     * @param key New blend mode key.
     * @return New {@code LayerRawData} with the provided blend mode key.
     */
    public LayerRawData withBlendModeKey(String key) {
        return new LayerRawData(this.flags,
                                 key,
                                 this.channelInfo,
                                 this.layerMaskSection,
                                 this.blendingRangesSection,
                                 this.additionalLayerData);
    }

    /**
     * Returns the original blend mode key string.
     *
     * @return the original blend mode key.
     */
    public String getOriginalBlendModeKey() {
        return this.blendModeKey;
    }

    /**
     * Returns the raw flags byte.
     *
     * @return the raw flags.
     */
    public byte getRawFlags() {
        return this.flags;
    }
}
