package org.aspose.psd;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.layers.GlobalLayerMaskInfo;
import org.aspose.psd.layers.Layer;
import org.aspose.psd.layers.LayerResource;
import org.aspose.psd.loaders.PsdImageLoader;
import org.aspose.psd.resources.IndexedColorPaletteInfo;
import org.aspose.psd.resources.PreservedResourceBlock;
import org.aspose.psd.resources.PsdResourceInfo;
import org.aspose.psd.resources.UnknownResource;
import org.aspose.psd.sections.ColorData;
import org.aspose.psd.sections.ImageDataKind;
import org.aspose.psd.sections.PsdColorDataInfo;
import org.aspose.psd.sections.PsdHeader;
import org.aspose.psd.sections.PsdImageDataInfo;
import org.aspose.psd.sections.PsdImageDocumentState;
import org.aspose.psd.writedescriptors.PsdImageWriter;

/**
 * Represents a PSD image that can be loaded, inspected, and saved without rendering.
 */
public final class PsdImage extends Image {

    /** Stores the underlying source or destination stream. */
    private final InputStream stream;

    /** Indicates whether the underlying stream must remain open after disposal. */
    private final boolean leaveOpen;

    /** Tracks whether the instance has already been disposed. */
    private boolean disposed;

    /** Stores the parsed PSD/PSB document sections. */
    private PsdImageDocumentState document = PsdImageDocumentState.EMPTY;

    /** Global angle (stored as a simple auto-property equivalent). */
    private int globalAngle;

    /**
     * Initializes a new instance of the {@link PsdImage} class over an internal working stream.
     */
    private PsdImage(InputStream stream, boolean leaveOpen) {
        this.stream = stream;
        this.leaveOpen = leaveOpen;
    }

    // ---------------------------------------------------------------------
    // Basic document properties
    // ---------------------------------------------------------------------

    /** Gets the document width in pixels. */
    @Override
    public int getWidth() {
        return document.getHeader() != null ? document.getHeader().getWidth() : 0;
    }

    /** Gets the document height in pixels. */
    @Override
    public int getHeight() {
        return document.getHeader() != null ? document.getHeader().getHeight() : 0;
    }

    /** Gets the document channel count from the PSD header. */
    int getChannels() {
        return document.getHeader() != null ? document.getHeader().getChannels() : 0;
    }

    /** Gets the number of bits stored per channel. */
    public int getBitsPerChannel() {
        return document.getHeader() != null ? document.getHeader().getBitDepth() : 0;
    }

    /** Gets the PSD color mode reported by the header. */
    public ColorModes getColorMode() {
        return document.getHeader() != null
                ? document.getHeader().getColorMode()
                : ColorModes.Rgb;
    }

    public void setColorMode(ColorModes value) {
        getHeader().setColorMode(value);
    }

    /** Gets or sets the Aspose.PSD-compatible API version number. */
    public int getVersion() {
        return 6;
    }

    public void setVersion(int value) {
        if (value != 6) {
            throw new IllegalArgumentException(
                    "Supported Aspose.PSD-compatible API version is 6.");
        }
    }

    /** Gets the parsed PSD/PSB header object. */
    PsdHeader getHeader() {
        if (document.getHeader() == null) {
            throw new IllegalStateException("PSD/PSB header is not loaded.");
        }
        return document.getHeader();
    }

    /** Indicates whether the loaded document uses the PSB large-document container. */
    boolean isLargeDocument() {
        return document.getHeader() != null && document.getHeader().isLargeDocument();
    }

    /** Equivalent to {@link #isLargeDocument()}. */
    boolean isPsb() {
        return isLargeDocument();
    }

    // ---------------------------------------------------------------------
    // Layers
    // ---------------------------------------------------------------------

    /** Gets the parsed layer collection. */
    public Layer[] getLayers() {
        return document.getLayerAndMaskSection().getLayers().clone();
    }

    public void setLayers(Layer[] value) {
        document = document.withLayerAndMaskSection(
                document.getLayerAndMaskSection()
                        .withLayers(value != null ? value : new Layer[0]));
    }

    /** Gets the PSD channels count. */
    public int getChannelsCount() {
        return getChannels();
    }

    /** Gets the image size. */
    public Size getSize() {
        return new Size(getWidth(), getHeight());
    }

    /** Gets or sets the active layer. */
    public Layer getActiveLayer() {
        Layer[] layers = getLayers();
        return layers.length > 0 ? layers[0] : null;
    }

    public void setActiveLayer(Layer value) {
        throw new UnsupportedOperationException(
                "Changing the active layer is not supported by this FOSS build.");
    }

    /** Gets the number of parsed layers in the document. */
    int getLayerCount() {
        return document.getLayerAndMaskSection().getLayers().length;
    }

    /** Indicates whether the document contains at least one parsed layer. */
    boolean hasLayers() {
        return document.getLayerAndMaskSection().getLayers().length > 0;
    }

    // ---------------------------------------------------------------------
    // Image resources
    // ---------------------------------------------------------------------

    /** Indicates whether the document contains any parsed image resources. */
    boolean hasImageResources() {
        return document.getImageResourcesSection().hasResources();
    }

    /** Gets the number of parsed image resource blocks. */
    int getResourceCount() {
        return document.getImageResourcesSection().getResources().length;
    }

    /** Gets or sets the PSD image resources. */
    public ResourceBlock[] getImageResources() {
        return Arrays.stream(document.getImageResourcesSection().getResources())
                .map(PreservedResourceBlock::new)
                .toArray(ResourceBlock[]::new);
    }

    public void setImageResources(ResourceBlock[] value) {
        throw new UnsupportedOperationException(
                "Changing image resources is not supported by this FOSS build.");
    }

    /** Gets or sets the global layer resources. */
    public LayerResource[] getGlobalLayerResources() {
        return new LayerResource[0];
    }

    public void setGlobalLayerResources(LayerResource[] value) {
        throw new UnsupportedOperationException(
                "Changing global layer resources is not supported by this FOSS build.");
    }

    /** Gets the global layer mask info. */
    public GlobalLayerMaskInfo getGlobalLayerMaskInfo() {
        return GlobalLayerMaskInfo.empty();
    }

    /** Indicates whether the PSD image is flattened. */
    public boolean isFlatten() {
        return document.getLayerAndMaskSection().getLayers().length == 0;
    }

    /**
     * Gets or sets whether the first alpha channel contains transparency data for the
     * merged result when specifying layers data.
     */
    public boolean getHasTransparencyData() {
        return false;
    }

    public void setHasTransparencyData(boolean value) {
        throw new UnsupportedOperationException(
                "Changing transparency data semantics is not supported by this FOSS build.");
    }

    /** Gets a read-only summary of the parsed image resource blocks. */
    List<PsdResourceInfo> getResources() {
        return Arrays.stream(document.getImageResourcesSection().getResources())
                .map(UnknownResource::toPublicInfo)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------
    // Color mode data / image data
    // ---------------------------------------------------------------------

    /** Indicates whether the document contains Color Mode Data bytes. */
    boolean hasColorModeData() {
        return document.getColorData().getRawData().length > 0;
    }

    /** Gets a read-only summary of the parsed Color Mode Data section. */
    PsdColorDataInfo getColorDataInfo() {
        return document.getColorData().toPublicInfo();
    }

    /** Gets the parsed indexed palette summary, when present. */
    IndexedColorPaletteInfo getIndexedPalette() {
        return getColorDataInfo().getIndexedPalette();
    }

    /** Indicates whether the document contains merged image data payload bytes. */
    boolean hasMergedImageData() {
        return document.getImageData().getRawData().length > 0;
    }

    /** Gets the compression method used by the merged image data section. */
    public CompressionMethod getCompression() {
        return document.getImageData().getCompression();
    }

    /** Gets a read-only summary of the parsed merged image data structure. */
    PsdImageDataInfo getImageDataInfo() {
        return document.getImageData().toPublicInfo();
    }

    /** Gets the structural kind of the merged image data payload. */
    ImageDataKind getImageDataKind() {
        return document.getImageData().getStructure().getKind();
    }

    /** Indicates whether ZIP prediction is used by the merged image data payload. */
    boolean usesPrediction() {
        return document.getImageData().getStructure().usesPrediction();
    }

    // ---------------------------------------------------------------------
    // Misc
    // ---------------------------------------------------------------------

    /** Gets or sets the parsed global angle (auto-property equivalent). */
    public int getGlobalAngle() {
        return globalAngle;
    }

    public void setGlobalAngle(int value) {
        this.globalAngle = value;
    }

    /** Indicates whether an embedded ICC profile resource is present. */
    boolean hasIccProfile() {
        return false;
    }

    /** Gets the parsed untagged ICC profile flag, when present. */
    Boolean isIccProfileUntagged() {
        return null;
    }

    /** Gets the parsed color mode data details for internal verification and tests. */
    ColorData getParsedColorData() {
        return document.getColorData();
    }

    /** Gets the parsed image resources for internal verification and tests. */
    UnknownResource[] getParsedResources() {
        return document.getImageResourcesSection().getResources();
    }

    // ---------------------------------------------------------------------
    // Loading
    // ---------------------------------------------------------------------

    /**
     * Loads a PSD image from a file path.
     */
    public static PsdImage load(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("filePath");
        }
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        try (InputStream in = Files.newInputStream(path)) {
            return load(in);
        } catch (PsdLoadException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a PSD image from a readable stream. The whole stream is consumed into an
     * internal buffer; if the stream is seekable, its original position is restored
     * afterwards (matching the C# behaviour).
     */
    public static PsdImage load(InputStream stream) throws IOException, PsdLoadException {
        if (stream == null) {
            throw new IllegalArgumentException("stream");
        }

        long originalPosition = 0L;
        boolean restorePosition = stream.markSupported();
        if (restorePosition) {
            stream.mark(Integer.MAX_VALUE);
        }

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] tmp = new byte[8192];
            int read;
            while ((read = stream.read(tmp)) != -1) {
                buffer.write(tmp, 0, read);
            }
            ByteArrayInputStream bufferedStream =
                    new ByteArrayInputStream(buffer.toByteArray());
            return load(bufferedStream, false);
        } finally {
            if (restorePosition) {
                try {
                    stream.reset();
                } catch (IOException ignored) {
                    // best effort, mirroring C# restore in finally
                }
            }
        }
    }

    /**
     * Creates a loaded image instance over the provided (already buffered) stream.
     */
    private static PsdImage load(InputStream stream, boolean leaveOpen) throws IOException, PsdLoadException {
        PsdImage image = new PsdImage(stream, leaveOpen);
        image.document = PsdImageLoader.load(stream, leaveOpen);
        return image;
    }

    // ---------------------------------------------------------------------
    // Saving
    // ---------------------------------------------------------------------

    /** Saves the image to a file path. */
    @Override
    public void save(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("filePath");
        }
        try (OutputStream out = Files.newOutputStream(Paths.get(filePath))) {
            save(out, false);
        }
    }

    /** Saves the image to a writable stream. */
    @Override
    public void save(OutputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("stream");
        }
        save(stream, true);
    }

    /** Saves the current document to a stream with configurable stream ownership. */
    private void save(OutputStream stream, boolean leaveOpen) throws IOException {
        if (disposed) {
            throw new IllegalStateException("PsdImage is disposed");
        }
        PsdImageWriter.save(document, stream, leaveOpen);
    }

    // ---------------------------------------------------------------------
    // Disposal
    // ---------------------------------------------------------------------

    /** Releases the image and optionally the underlying stream. */
    @Override
    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;
        if (!leaveOpen) {
            try {
                stream.close();
            } catch (IOException ignored) {
                // silent, matching C# Dispose semantics for non-critical failures
            }
        }
    }
}