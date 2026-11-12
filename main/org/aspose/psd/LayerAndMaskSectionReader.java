package org.aspose.psd;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.layers.Layer;
import org.aspose.psd.layers.LayerRecordReader;
import org.aspose.psd.sections.LayerAndMaskSection;

/**
 * Reads PSD/PSB Layer and Mask Information sections.
 */
public final class LayerAndMaskSectionReader {

    private LayerAndMaskSectionReader() {
        // Utility class
    }

    /**
     * Loads the section using PSD- or PSB-sized outer and layer-info lengths.
     *
     * @param reader           the reader positioned at the outer section length field
     * @param isLargeDocument  {@code true} for PSB-sized lengths; otherwise {@code false}
     * @return the loaded section
     * @throws PsdLoadException if the PSD structure is invalid
     * @throws IOException      on I/O failure
     */
    public static org.aspose.psd.sections.LayerAndMaskSection load(BigEndianReader reader, boolean isLargeDocument) throws IOException, PsdLoadException {
        long sectionLength = readSectionLength(reader, isLargeDocument);
        if (sectionLength == 0L) {
            return LayerAndMaskSection.EMPTY;
        }

        byte[] rawSectionBytes = PsdSectionReader.readBytes(
                reader, sectionLength, "Layer and Mask Information section");

        try (BigEndianReader memReader = new BigEndianReader(
                new ByteArrayInputStream(rawSectionBytes), true)) {

            long layerInfoLength = PsdSectionReader.validateSignedLength(
                    isLargeDocument ? memReader.readInt64() : memReader.readInt32(),
                    "Layer Info section");

            if (layerInfoLength > rawSectionBytes.length - memReader.getPosition()) {
                throw new PsdLoadException(
                        "Layer Info section length exceeds the enclosing Layer and Mask Information section.");
            }

            long layerInfoEnd = (isLargeDocument ? Long.BYTES : Integer.BYTES) + layerInfoLength;

            if (layerInfoLength == 0L) {
                long remaining = rawSectionBytes.length - memReader.getPosition();
                byte[] tail = remaining > 0
                        ? memReader.readBytes((int) remaining)
                        : new byte[0];
                return new LayerAndMaskSection(
                        rawSectionBytes, new byte[0], tail, (short) 0, new Layer[0]);
            }

            if (layerInfoLength < Short.BYTES) {
                throw new PsdLoadException(
                        "Layer Info section is too short to contain the layer count field.");
            }

            short layerCountRaw = memReader.readInt16();
            short layerCount = layerCountRaw < 0 ? (short) -layerCountRaw : layerCountRaw;

            Layer[] layers = readLayers(memReader, layerCount, layerInfoEnd, isLargeDocument);

            int channelImageDataLength = (int) (layerInfoEnd - memReader.getPosition());
            byte[] layerChannelImageDataRaw = channelImageDataLength > 0
                    ? memReader.readBytes(channelImageDataLength)
                    : new byte[0];

            int globalMaskAndTailLength =
                    Math.max(0, rawSectionBytes.length - (int) memReader.getPosition());
            byte[] layerGlobalMaskAndTailRaw = globalMaskAndTailLength > 0
                    ? memReader.readBytes(globalMaskAndTailLength)
                    : new byte[0];

            return new LayerAndMaskSection(
                    rawSectionBytes,
                    layerChannelImageDataRaw,
                    layerGlobalMaskAndTailRaw,
                    layerCountRaw,
                    layers);
        }
    }

    /**
     * Reads layer records and validates the declared Layer Info boundary.
     *
     * @param reader          reader positioned after the layer count field
     * @param layerCount      absolute layer count to read
     * @param layerInfoEnd    absolute end offset of the Layer Info subsection
     * @param isLargeDocument {@code true} for PSB-sized layer channel lengths
     * @return the parsed layer records
     * @throws IOException on I/O failure
     */
    private static Layer[] readLayers(BigEndianReader reader, short layerCount,
                                      long layerInfoEnd, boolean isLargeDocument) throws IOException, PsdLoadException {
        Layer[] layers = new Layer[0];
        if (layerCount > 0) {
            layers = new Layer[layerCount];
            for (int i = 0; i < layerCount; i++) {
                layers[i] = LayerRecordReader.load(reader, isLargeDocument);
            }
        }

        if (reader.getPosition() > layerInfoEnd) {
            throw new PsdLoadException(
                    "Layer records exceed the declared Layer Info section length.");
        }

        return layers;
    }

    /**
     * Reads the outer Layer and Mask Information section length using the
     * PSD (4-byte) or PSB (8-byte) field size.
     *
     * @param reader          reader positioned at the outer section length field
     * @param isLargeDocument {@code true} to read an 8-byte PSB length
     * @return the declared section payload length
     * @throws IOException on I/O failure
     */
    private static long readSectionLength(BigEndianReader reader, boolean isLargeDocument)
            throws IOException {
        // Java longs are signed; PSD section lengths never exceed Long.MAX_VALUE
        // in practice, so the unsigned read is mapped to a non-negative long.
        return isLargeDocument ? reader.readUInt64() : reader.readUInt32();
    }
}