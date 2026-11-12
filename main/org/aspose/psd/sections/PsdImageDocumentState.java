package org.aspose.psd.sections;

import org.aspose.psd.CompressionMethod;
import org.aspose.psd.PsdImage;
import org.aspose.psd.coreexceptions.PsdLoadException;

/**
 * Stores parsed PSD/PSB document sections used by {@link PsdImage}.
 */
public final class PsdImageDocumentState {

    /**
     * An empty document state before a PSD/PSB stream has been parsed.
     */
    public static final PsdImageDocumentState EMPTY;

    static {
        try {
            EMPTY = new PsdImageDocumentState(
                    null,
                    ColorData.EMPTY,
                    ImageResourcesSection.EMPTY,
                    LayerAndMaskSection.EMPTY,
                    new ImageData(CompressionMethod.Raw, new byte[0]));
        } catch (PsdLoadException e) {
            throw new RuntimeException(e);
        }
    }

    private final PsdHeader header;
    private final ColorData colorData;
    private final ImageResourcesSection imageResourcesSection;
    private final LayerAndMaskSection layerAndMaskSection;
    private final ImageData imageData;

    /**
     * Initializes a new instance of the {@link PsdImageDocumentState} class.
     *
     * @param header                The parsed PSD/PSB header (may be {@code null}).
     * @param colorData             The parsed Color Mode Data section.
     * @param imageResourcesSection The parsed Image Resources section.
     * @param layerAndMaskSection   The parsed Layer and Mask Information section.
     * @param imageData             The parsed merged Image Data section.
     */
    public PsdImageDocumentState(
            PsdHeader header,
            ColorData colorData,
            ImageResourcesSection imageResourcesSection,
            LayerAndMaskSection layerAndMaskSection,
            ImageData imageData) {
        this.header = header;
        this.colorData = colorData;
        this.imageResourcesSection = imageResourcesSection;
        this.layerAndMaskSection = layerAndMaskSection;
        this.imageData = imageData;
    }

    /**
     * Gets the parsed PSD/PSB header, if present.
     */
    public PsdHeader getHeader() {
        return header;
    }

    /**
     * Gets the parsed Color Mode Data section.
     */
    public ColorData getColorData() {
        return colorData;
    }

    /**
     * Gets the parsed and raw-preserved Image Resources section.
     */
    public ImageResourcesSection getImageResourcesSection() {
        return imageResourcesSection;
    }

    /**
     * Gets the parsed and raw-preserved Layer and Mask Information section.
     */
    public LayerAndMaskSection getLayerAndMaskSection() {
        return layerAndMaskSection;
    }

    /**
     * Gets the parsed merged Image Data section.
     */
    public ImageData getImageData() {
        return imageData;
    }

    /**
     * Creates a state copy with a replaced Layer and Mask Information section.
     *
     * @param layerAndMaskSection The replacement Layer and Mask Information section.
     * @return The updated document state.
     */
    public PsdImageDocumentState withLayerAndMaskSection(LayerAndMaskSection layerAndMaskSection) {
        return new PsdImageDocumentState(
                header,
                colorData,
                imageResourcesSection,
                layerAndMaskSection,
                imageData);
    }
}