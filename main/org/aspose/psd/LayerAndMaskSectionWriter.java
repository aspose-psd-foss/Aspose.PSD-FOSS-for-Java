package org.aspose.psd;

import org.aspose.psd.coreexceptions.PsdSaveException;
import org.aspose.psd.layers.Layer;
import org.aspose.psd.layers.LayerRecordWriter;
import org.aspose.psd.sections.LayerAndMaskSection;

import java.io.ByteArrayOutputStream;

/**
 * Writes PSD/PSB Layer and Mask Information sections.
 */
public final class LayerAndMaskSectionWriter {

    private LayerAndMaskSectionWriter() {
        // static utility class
    }

    /**
     * Writes the section while preserving raw bytes for no-mutation saves.
     *
     * @param section         the section to write
     * @param writer          the destination writer
     * @param isLargeDocument {@code true} for PSB-sized lengths; otherwise, {@code false}
     */
    public static void save(LayerAndMaskSection section, BigEndianWriter writer, boolean isLargeDocument) throws PsdSaveException {
        if (section.getRawSectionBytes().length > 0
                && !section.isHasLayerCollectionMutated()
                && !any(section.getLayers(), Layer::hasMutated)) {

            writeSectionLength(writer, section.getRawSectionBytes().length, isLargeDocument);
            writer.write(section.getRawSectionBytes());
            return;
        }

        if (section.getLayers().length == 0) {
            writeSectionLength(writer, 0, isLargeDocument);
            return;
        }

        writeLayerSectionWithMutations(section, writer, isLargeDocument);
    }

    /**
     * Rebuilds the layer info payload when at least one parsed layer has been mutated.
     *
     * @param section         the section being rebuilt
     * @param writer          the destination writer positioned at the Layer and Mask Information section
     * @param isLargeDocument {@code true} for PSB-sized section lengths; otherwise, {@code false}
     */
    private static void writeLayerSectionWithMutations(
            LayerAndMaskSection section, BigEndianWriter writer, boolean isLargeDocument) throws PsdSaveException {

        ByteArrayOutputStream layerInfoPayloadStream = new ByteArrayOutputStream();
        BigEndianWriter layerInfoPayloadWriter = new BigEndianWriter(layerInfoPayloadStream);

        short layerCount = section.getLayerCountRaw() < 0
                ? (short) -section.getLayers().length
                : (short) section.getLayers().length;
        layerInfoPayloadWriter.writeShort(layerCount);

        for (Layer layer : section.getLayers()) {
            LayerRecordWriter.write(layer, layerInfoPayloadWriter, isLargeDocument);
        }

        layerInfoPayloadWriter.write(section.getLayerChannelImageDataRaw());
        byte[] layerInfoPayload = layerInfoPayloadStream.toByteArray();

        ByteArrayOutputStream sectionStream = new ByteArrayOutputStream();
        BigEndianWriter sectionWriter = new BigEndianWriter(sectionStream);

        if (isLargeDocument) {
            sectionWriter.writeLong(layerInfoPayload.length);
        } else {
            sectionWriter.writeInt(layerInfoPayload.length);
        }

        sectionWriter.write(layerInfoPayload);
        sectionWriter.write(getLayerGlobalMaskAndTailBytesForWrite(section));

        byte[] sectionBytes = sectionStream.toByteArray();
        writeSectionLength(writer, sectionBytes.length, isLargeDocument);
        writer.write(sectionBytes);
    }

    /**
     * Gets the global mask and trailing bytes to emit when rebuilding the layer section.
     *
     * @param section the source section
     * @return the preserved tail bytes, or an empty global mask block when the original section had no tail
     */
    private static byte[] getLayerGlobalMaskAndTailBytesForWrite(LayerAndMaskSection section) {
        byte[] tail = section.getLayerGlobalMaskAndTailRaw();
        return tail.length > 0 ? tail : new byte[] { 0, 0, 0, 0 };
    }

    /**
     * Writes the outer Layer and Mask Information section length using PSD or PSB field size.
     *
     * @param writer          the destination writer
     * @param length          the section payload length to write
     * @param isLargeDocument {@code true} to write an 8-byte PSB length; otherwise, {@code false}
     */
    private static void writeSectionLength(BigEndianWriter writer, int length, boolean isLargeDocument) {
        if (isLargeDocument) {
            writer.writeLong(Integer.toUnsignedLong(length));
        } else {
            writer.writeInt(length);
        }
    }

    /**
     * Returns {@code true} if at least one element in the array satisfies the predicate.
     * A LINQ {@code Any(predicate)} analogue that avoids requiring a {@link java.util.List}.
     *
     * @param items     the array to scan (may be null)
     * @param predicate the condition to test
     * @param <T>       element type
     * @return {@code true} if any element matches; {@code false} for an empty/null array
     */
    private static <T> boolean any(T[] items, java.util.function.Predicate<T> predicate) {
        if (items == null) {
            return false;
        }
        for (T item : items) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }
}