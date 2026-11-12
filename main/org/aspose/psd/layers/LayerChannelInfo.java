package org.aspose.psd.layers;

/**
 * Stores one channel metadata entry from a layer record.
 */
public class LayerChannelInfo {
    /**
     * PSD channel identifier.
     */
    private short channelId;

    /**
     * Declared byte length of the channel data payload.
     */
    private long dataLength;

    public LayerChannelInfo() {
    }

    public LayerChannelInfo(short channelId, long dataLength) {
        this.channelId = channelId;
        this.dataLength = dataLength;
    }

    public short getChannelId() {
        return channelId;
    }

    public void setChannelId(short channelId) {
        this.channelId = channelId;
    }

    public long getDataLength() {
        return dataLength;
    }

    public void setDataLength(long dataLength) {
        this.dataLength = dataLength;
    }

    /**
     * Returns the declared byte length of the channel data payload.
     *
     * @return the channel data length
     */
    public long getChannelLength() {
        return dataLength;
    }

    /**
     * Returns the PSD channel identifier.
     *
     * @return the channel identifier
     */
    public short getId() {
        return channelId;
    }

    /**
     * Returns the declared length of the channel data payload.
     *
     * @return the data length
     */
    public long getLength() {
        return dataLength;
    }
}
