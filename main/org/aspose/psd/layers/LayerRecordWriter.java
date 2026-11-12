package org.aspose.psd.layers;

import org.aspose.psd.BigEndianWriter;
import org.aspose.psd.coreexceptions.PsdSaveException;

/**
 * Writes PSD/PSB layer records from the in-memory {@link Layer} model.
 */
public final class LayerRecordWriter {

    /**
     * Stores the Adobe layer record signature value "8BIM".
     */
    private static final int ADOBE_LAYER_SIGNATURE = 0x3842494D;

    /**
     * Stores the PSD flag bit that marks a layer as hidden when set.
     */
    private static final byte LAYER_INVISIBLE_FLAG = 0x02;

    /**
     * Stores the reserved trailing byte in the fixed layer record fields.
     */
    private static final byte LAYER_RECORD_RESERVED_BYTE = 0;

    private LayerRecordWriter() {
        // Utility class
    }

    /**
     * Writes one layer record using PSD- or PSB-sized channel lengths.
     *
     * @param layer           the layer to write
     * @param writer          the destination writer
     * @param isLargeDocument {@code true} for PSB-sized layer channel lengths; otherwise, {@code false}
     */
    public static void write(Layer layer, BigEndianWriter writer, boolean isLargeDocument) throws PsdSaveException {
        writer.writeInt(layer.getTop());
        writer.writeInt(layer.getLeft());
        writer.writeInt(layer.getBottom());
        writer.writeInt(layer.getRight());
        writer.writeShort((short) layer.getChannelInfo().length);

        for (int i = 0; i < layer.getChannelInfo().length; i++) {
            writer.writeShort(layer.getChannelInfo()[i].getChannelId());
            if (isLargeDocument) {
                writer.writeLong(layer.getChannelInfo()[i].getDataLength());
            } else {
                writer.writeInt((int) layer.getChannelInfo()[i].getDataLength());
            }
        }

        writer.writeInt(ADOBE_LAYER_SIGNATURE);
        writer.write(layer.getRawBlendModeKey().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        writer.write(layer.getOpacity());
        writer.write(layer.getClipping());
        writer.write(getFlagsForWrite(layer));
        writer.write(LAYER_RECORD_RESERVED_BYTE);
        writer.writeInt(getExtraDataLength(layer));
        writer.write(layer.getRawLayerMaskSection().getRawData());
        writer.write(layer.getRawLayerBlendingRangesSection().getRawData());
        writer.writePascalStringAlignedTo4(layer.getName());
        writer.write(layer.getAdditionalLayerData());
    }

    /**
     * Combines the original layer flags with the current public visibility state.
     *
     * @param layer the layer whose flags should be written
     * @return the PSD layer flags byte to write
     */
    private static byte getFlagsForWrite(Layer layer) {
        byte flags = layer.getRawFlags();
        return layer.isVisible()
                ? (byte) (flags & ~LAYER_INVISIBLE_FLAG)
                : (byte) (flags | LAYER_INVISIBLE_FLAG);
    }

    /**
     * Calculates the layer extra data byte count written after the fixed layer record fields.
     *
     * @param layer the layer whose extra data should be measured
     * @return the extra data byte count
     */
    private static int getExtraDataLength(Layer layer) {
        return layer.getRawLayerMaskSection().getRawData().length
                + layer.getRawLayerBlendingRangesSection().getRawData().length
                + BigEndianWriter.getPascalStringStorageLengthAlignedTo4(layer.getName())
                + layer.getAdditionalLayerData().length;
    }
}