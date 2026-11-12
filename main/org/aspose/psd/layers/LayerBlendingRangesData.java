package org.aspose.psd.layers;

import org.aspose.psd.layers.BlendRange;

/**
 * Represents PSD layer blending ranges data.
 */
public class LayerBlendingRangesData {

    /**
     * Stores the PSD blending-ranges subsection length prefix size.
     */
    private static final int LENGTH_FIELD_SIZE = Integer.BYTES;

    private BlendRange compositeBlendRange = new BlendRange();

    private BlendRange[] channelBlendRanges = new BlendRange[0];

    private int length;

    /**
     * Initializes a new instance of the {@link LayerBlendingRangesData} class.
     */
    public LayerBlendingRangesData() {
    }

    /**
     * Initializes a new instance of the {@link LayerBlendingRangesData} class with a known raw length.
     *
     * @param length The raw blending ranges subsection length in bytes.
     */
    private LayerBlendingRangesData(int length) {
        this.length = length;
    }

    /**
     * Gets or sets the composite blend range.
     *
     * @return The composite blend range.
     */
    public BlendRange getCompositeBlendRange() {
        return compositeBlendRange;
    }

    /**
     * Sets the composite blend range.
     *
     * @param compositeBlendRange The composite blend range.
     */
    public void setCompositeBlendRange(BlendRange compositeBlendRange) {
        this.compositeBlendRange = compositeBlendRange;
    }

    /**
     * Gets or sets the per-channel blend ranges.
     *
     * @return The per-channel blend ranges.
     */
    public BlendRange[] getChannelBlendRanges() {
        return channelBlendRanges;
    }

    /**
     * Sets the per-channel blend ranges.
     *
     * @param channelBlendRanges The per-channel blend ranges.
     */
    public void setChannelBlendRanges(BlendRange[] channelBlendRanges) {
        this.channelBlendRanges = channelBlendRanges;
    }

    /**
     * Gets the blending ranges data length in bytes.
     *
     * @return The blending ranges data length in bytes.
     */
    public int getLength() {
        return length;
    }

    /**
     * Creates public blending ranges data from a preserved raw subsection length.
     *
     * @param length The raw subsection length in bytes.
     * @return The public blending ranges data.
     */
    static LayerBlendingRangesData fromRawLength(int length) {
        return new LayerBlendingRangesData(Math.max(0, length - LENGTH_FIELD_SIZE));
    }
}