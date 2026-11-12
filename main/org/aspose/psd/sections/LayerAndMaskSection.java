package org.aspose.psd.sections;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.BigEndianWriter;
import org.aspose.psd.LayerAndMaskSectionReader;
import org.aspose.psd.LayerAndMaskSectionWriter;
import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.coreexceptions.PsdSaveException;
import org.aspose.psd.layers.Layer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the PSD/PSB Layer and Mask Information section and its raw-preserved save state.
 */
public final class LayerAndMaskSection {

    /**
     * An empty Layer and Mask Information section.
     */
    public static final LayerAndMaskSection EMPTY = new LayerAndMaskSection(
            new byte[0], new byte[0], new byte[0], (short) 0, new Layer[0], false);

    private final byte[] rawSectionBytes;
    private final byte[] layerChannelImageDataRaw;
    private final byte[] layerGlobalMaskAndTailRaw;
    private final short layerCountRaw;
    private final Layer[] layers;
    private final boolean hasLayerCollectionMutated;

    /**
     * Creates a new instance of the {@link LayerAndMaskSection} class.
     *
     * @param rawSectionBytes           the raw section payload without the outer length field
     * @param layerChannelImageDataRaw  the raw channel image data payload after layer records
     * @param layerGlobalMaskAndTailRaw the raw global mask and trailing section bytes
     * @param layerCountRaw             the original signed layer count value
     * @param layers                    the parsed layer records
     * @param hasLayerCollectionMutated true when the public layer collection was replaced
     */
    public LayerAndMaskSection(
            byte[] rawSectionBytes,
            byte[] layerChannelImageDataRaw,
            byte[] layerGlobalMaskAndTailRaw,
            short layerCountRaw,
            Layer[] layers,
            boolean hasLayerCollectionMutated) {
        this.rawSectionBytes = rawSectionBytes;
        this.layerChannelImageDataRaw = layerChannelImageDataRaw;
        this.layerGlobalMaskAndTailRaw = layerGlobalMaskAndTailRaw;
        this.layerCountRaw = layerCountRaw;
        this.layers = layers;
        this.hasLayerCollectionMutated = hasLayerCollectionMutated;
    }

    /**
     * Convenience constructor mirroring the C# default parameter.
     */
    public LayerAndMaskSection(
            byte[] rawSectionBytes,
            byte[] layerChannelImageDataRaw,
            byte[] layerGlobalMaskAndTailRaw,
            short layerCountRaw,
            Layer[] layers) {
        this(rawSectionBytes, layerChannelImageDataRaw, layerGlobalMaskAndTailRaw,
                layerCountRaw, layers, false);
    }

    /**
     * Gets the raw section payload without the outer length field.
     */
    public byte[] getRawSectionBytes() {
        return rawSectionBytes;
    }

    /**
     * Gets the raw layer channel image data payload after layer records.
     */
    public byte[] getLayerChannelImageDataRaw() {
        return layerChannelImageDataRaw;
    }

    /**
     * Gets the raw global mask info and trailing section bytes.
     */
    public byte[] getLayerGlobalMaskAndTailRaw() {
        return layerGlobalMaskAndTailRaw;
    }

    /**
     * Gets the original signed layer count so transparency-protected layers can preserve sign.
     */
    public short getLayerCountRaw() {
        return layerCountRaw;
    }

    /**
     * Gets the parsed layer records.
     */
    public Layer[] getLayers() {
        return layers;
    }

    /**
     * Gets a value indicating whether the public layer collection was replaced.
     */
    public boolean isHasLayerCollectionMutated() {
        return hasLayerCollectionMutated;
    }

    /**
     * Loads the section using PSD- or PSB-sized outer and layer-info lengths.
     *
     * @param reader          the reader positioned at the outer section length field
     * @param isLargeDocument true for PSB-sized lengths; otherwise, false
     * @return the loaded section
     */
    public static LayerAndMaskSection load(BigEndianReader reader, boolean isLargeDocument) throws IOException, PsdLoadException {
        return LayerAndMaskSectionReader.load(reader, isLargeDocument);
    }

    /**
     * Writes the section while preserving raw bytes for no-mutation saves.
     *
     * @param writer          the destination writer
     * @param isLargeDocument true for PSB-sized lengths; otherwise, false
     */
    public void save(BigEndianWriter writer, boolean isLargeDocument) throws PsdSaveException {
        LayerAndMaskSectionWriter.save(this, writer, isLargeDocument);
    }

    /**
     * Creates a section copy with a replaced layer collection and marks it for rewriting on save.
     *
     * @param layers the replacement layer collection
     * @return the section copy
     */
    public LayerAndMaskSection withLayers(Layer[] layers) {
        return new LayerAndMaskSection(
                rawSectionBytes,
                layerChannelImageDataRaw,
                layerGlobalMaskAndTailRaw,
                layerCountRaw,
                Arrays.copyOf(layers, layers.length),
                true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LayerAndMaskSection)) return false;
        LayerAndMaskSection that = (LayerAndMaskSection) o;
        return layerCountRaw == that.layerCountRaw
                && hasLayerCollectionMutated == that.hasLayerCollectionMutated
                && Arrays.equals(rawSectionBytes, that.rawSectionBytes)
                && Arrays.equals(layerChannelImageDataRaw, that.layerChannelImageDataRaw)
                && Arrays.equals(layerGlobalMaskAndTailRaw, that.layerGlobalMaskAndTailRaw)
                && Arrays.equals(layers, that.layers);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(layerCountRaw, hasLayerCollectionMutated);
        result = 31 * result + Arrays.hashCode(rawSectionBytes);
        result = 31 * result + Arrays.hashCode(layerChannelImageDataRaw);
        result = 31 * result + Arrays.hashCode(layerGlobalMaskAndTailRaw);
        result = 31 * result + Arrays.hashCode(layers);
        return result;
    }

    @Override
    public String toString() {
        return "LayerAndMaskSection{"
                + "layerCountRaw=" + layerCountRaw
                + ", layers=" + Arrays.toString(layers)
                + ", hasLayerCollectionMutated=" + hasLayerCollectionMutated
                + '}';
    }
}