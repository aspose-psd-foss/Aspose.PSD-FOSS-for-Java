package org.aspose.psd.layers;

/**
 * Provides a read-only summary of one parsed layer channel record.
 */
public final class PsdLayerChannelInfo {
    private final short channelId;
    private final long dataLength;

    /**
     * Initializes a new instance of the {@code PsdLayerChannelInfo} class.
     *
     * @param channelId  The PSD channel identifier.
     * @param dataLength The declared payload length in bytes.
     */
    public PsdLayerChannelInfo(short channelId, long dataLength) {
        this.channelId = channelId;
        this.dataLength = dataLength;
    }

    /**
     * Gets the PSD channel identifier.
     *
     * @return the channel identifier.
     */
    public short getChannelId() {
        return channelId;
    }

    /**
     * Gets the declared payload length in bytes.
     *
     * @return the data length.
     */
    public long getDataLength() {
        return dataLength;
    }
}
