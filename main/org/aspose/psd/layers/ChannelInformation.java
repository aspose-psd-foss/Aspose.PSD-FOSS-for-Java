package org.aspose.psd.layers;

import org.aspose.psd.CompressionMethod;
import org.aspose.psd.PsdVersion;
import org.aspose.psd.layers.LayerChannelInfo;

public class ChannelInformation {
    private short channelId;
    private CompressionMethod compressionMethod;
    private final int length;

    public ChannelInformation(CompressionMethod compressionMethod, int bitDepth, int psdVersion) {
        this.compressionMethod = compressionMethod;
        this.length = getHeaderLength(psdVersion);
    }

    private ChannelInformation(short channelId, int length) {
        this.channelId = channelId;
        this.compressionMethod = CompressionMethod.Raw;
        this.length = length;
    }

    public short getChannelId() {
        return channelId;
    }

    public void setChannelId(short channelId) {
        this.channelId = channelId;
    }

    public CompressionMethod getCompressionMethod() {
        return compressionMethod;
    }

    public void setCompressionMethod(CompressionMethod compressionMethod) {
        this.compressionMethod = compressionMethod;
    }

    public int getLength() {
        return length;
    }

    static ChannelInformation fromLayerChannelInfo(LayerChannelInfo channelInfo) {
        int length = channelInfo.getDataLength() > Integer.MAX_VALUE
                ? Integer.MAX_VALUE
                : (int) channelInfo.getDataLength();
        return new ChannelInformation(channelInfo.getChannelId(), length);
    }

    private static int getHeaderLength(int psdVersion) {
        return psdVersion == PsdVersion.PSB.ordinal() ? Long.BYTES : Short.BYTES;
    }
}
