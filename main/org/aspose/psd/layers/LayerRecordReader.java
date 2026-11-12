package org.aspose.psd.layers;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.PsdSectionReader;
import org.aspose.psd.Rectangle;

import java.io.IOException;

public final class LayerRecordReader {
    private static final int ADOBE_LAYER_SIGNATURE = 0x3842494D;
    private static final String ADOBE_LAYER_SIGNATURE_TEXT = "8BIM";
    private static final byte LAYER_INVISIBLE_FLAG = 0x02;

    public static Layer load(BigEndianReader reader, boolean isLargeDocument) throws PsdLoadException, IOException {
        int top = reader.readInt32();
        int left = reader.readInt32();
        int bottom = reader.readInt32();
        int right = reader.readInt32();

        int actualChannelCount = (int)reader.readUInt16();
        LayerChannelInfo[] channelInfoArray = new LayerChannelInfo[actualChannelCount];

        for (int i = 0; i < actualChannelCount; i++) {
            short channelId = reader.readInt16();
            long dataLength = isLargeDocument ? reader.readUInt64() : reader.readUInt32();

            LayerChannelInfo info = new LayerChannelInfo();
            info.setChannelId(channelId);
            info.setDataLength(dataLength);
            channelInfoArray[i] = info;
        }

        int signature = reader.readInt32();
        if (signature != ADOBE_LAYER_SIGNATURE) {
            throw new PsdLoadException("Invalid layer blend mode signature. Expected '" + ADOBE_LAYER_SIGNATURE_TEXT + "'.");
        }

        byte[] blendModeKey = reader.readBytes(4);
        String originalBlendModeKey = new String(blendModeKey, java.nio.charset.StandardCharsets.US_ASCII);
        BlendMode blendMode = LayerBlendModeMapper.parseBlendModeKey(blendModeKey);

        byte opacity = reader.readByte();
        byte clipping = reader.readByte();
        byte flags = reader.readByte();
        reader.readByte(); // padding

        int extraLength = reader.readInt32();
        if (extraLength < 0) {
            throw new PsdLoadException("Layer extra data length cannot be negative.");
        }

        String layerName = "";
        RawLayerMaskSection layerMaskData = RawLayerMaskSection.empty();
        RawLayerBlendingRangesSection blendingRangesData = RawLayerBlendingRangesSection.EMPTY;
        byte[] additionalLayerData = new byte[0];

        if (extraLength > 0) {
            long extraStart = reader.getPosition();
            int extraEnd = (int) (extraStart + extraLength);

            layerMaskData = RawLayerMaskSection.load(reader, (long) extraEnd);
            if (reader.getPosition() + Integer.BYTES > extraEnd) {
                throw new PsdLoadException("Layer extra data is truncated before the blending ranges length field.");
            }

            blendingRangesData = RawLayerBlendingRangesSection.load(reader, (long) extraEnd);
            layerName = reader.readPascalStringAlignedTo4();

            long remaining = (long) extraEnd - reader.getPosition();
            if (remaining < 0) {
                throw new PsdLoadException("Layer extra data parser read beyond the declared extra data boundary.");
            }

            if (remaining > 0) {
                additionalLayerData = reader.readBytes(
                    PsdSectionReader.getNestedMemoryBackedLength(reader, remaining, (long) extraEnd, "Additional layer data"));
            }
        }

        Rectangle bounds = Rectangle.fromLTRB(left, top, right, bottom);
        boolean visible = (flags & LAYER_INVISIBLE_FLAG) == 0;

        LayerRawData rawData = new LayerRawData(
            flags,
            originalBlendModeKey,
            channelInfoArray,
            layerMaskData,
            blendingRangesData,
            additionalLayerData
        );

        return Layer.createParsed(
            layerName,
            bounds,
            visible,
            opacity,
            clipping,
            blendMode,
            rawData
        );
    }
}
