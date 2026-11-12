package org.aspose.psd;

import org.aspose.psd.layers.BlendMode;
import org.aspose.psd.sections.PsdHeader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains FixtureDocument tests.
 */
public final class FixtureDocumentTests extends PsdTestFixtureBase {

    /**
     * Tests that the basic RGB fixture exposes expected PSD document metadata, layers, resources, and compression.
     */
    @Test
    public void load_BasicRgb_ReadsMetadata() throws IOException {
        try (PsdImage image = PsdImage.load(getTestDataPath("basic-rgb.psd"))) {
            assertEquals(200, image.getWidth());
            assertEquals(200, image.getHeight());
            assertEquals(3, image.getChannels());
            assertEquals(8, image.getBitsPerChannel());
            assertEquals(ColorModes.Rgb, image.getColorMode());
            assertEquals(6, image.getVersion());
            assertEquals(PsdHeader.PSD_VERSION, image.getHeader().getVersion());
            assertEquals(3, image.getLayerCount());
            assertEquals(26, image.getResourceCount());
            assertEquals(CompressionMethod.RLE, image.getCompression());
        }
    }

    /**
     * Tests that saving the basic RGB fixture without mutations preserves the file byte-for-byte.
     */
    @Test
    public void save_BasicRgb_IsByteExact() throws Exception {
        assertByteExactRoundTrip("basic-rgb.psd");
    }

    /**
     * Tests that renaming a layer in the basic RGB fixture persists after save and reload.
     */
    @Test
    public void save_BasicRgb_RenamesLayer() throws IOException {
        assertRenameSave("basic-rgb.psd", 1, "Renamed Rectangle");
    }

    /**
     * Tests that the basic CMYK fixture exposes expected channels, resources, layers, compression, and row metadata.
     */
    @Test
    public void load_BasicCmyk_ReadsMetadata() {
        try (PsdImage image = PsdImage.load(getTestDataPath("basic-cmyk.psd"))) {
            assertEquals(200, image.getWidth());
            assertEquals(200, image.getHeight());
            assertEquals(4, image.getChannels());
            assertEquals(ColorModes.Cmyk, image.getColorMode());
            assertEquals(3, image.getLayerCount());
            assertEquals(28, image.getResourceCount());
            assertEquals(CompressionMethod.RLE, image.getCompression());
            assertEquals(800, image.getImageDataInfo().getRowByteCounts().size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests that saving the basic CMYK fixture without mutations preserves the file byte-for-byte.
     */
    @Test
    public void save_BasicCmyk_IsByteExact() throws Exception {
        assertByteExactRoundTrip("basic-cmyk.psd");
    }

    /**
     * Tests that the basic PSB fixture exposes PSB version, large-document state, resources, and RLE row-length sizing.
     */
    @Test
    public void load_BasicPsb_ReadsMetadata() throws IOException {
        try (PsdImage image = PsdImage.load(getTestDataPath("basic.psb"))) {
            assertEquals(6, image.getVersion());
            assertEquals(PsdHeader.PSB_VERSION, image.getHeader().getVersion());
            assertTrue(image.isLargeDocument());
            assertTrue(image.isPsb());
            assertEquals(200, image.getWidth());
            assertEquals(200, image.getHeight());
            assertEquals(0, image.getLayerCount());
            assertEquals(24, image.getResourceCount());
            assertEquals(CompressionMethod.RLE, image.getCompression());
            assertEquals(Integer.BYTES, image.getImageDataInfo().getRowLengthFieldSize());
        }
    }

    /**
     * Tests that saving the basic PSB fixture without mutations preserves the file byte-for-byte.
     */
    @Test
    public void save_BasicPsb_IsByteExact() throws Exception {
        assertByteExactRoundTrip("basic.psb");
    }

    /**
     * Tests that the layered PSB fixture exposes expected layer names, layer count, and opacity metadata.
     */
    @Test
    public void load_LayeredPsb_ReadsLayers() throws IOException {
        try (PsdImage image = PsdImage.load(getTestDataPath("layered.psb"))) {
            assertEquals(6, image.getVersion());
            assertEquals(PsdHeader.PSB_VERSION, image.getHeader().getVersion());
            assertEquals(3, image.getLayerCount());
            assertEquals("Background", image.getLayers()[0].getName());
            assertEquals("Rectangle 1", image.getLayers()[1].getName());
            assertEquals("Ellipse 1", image.getLayers()[2].getName());
            assertEquals((byte)191, image.getLayers()[2].getOpacity());
        }
    }

    /**
     * Tests that renaming a layer in the layered PSB fixture persists after save and reload.
     */
    @Test
    public void save_LayeredPsb_RenamesLayer() throws IOException {
        assertRenameSave("layered.psb", 1, "Renamed Rectangle");
    }

    /**
     * Tests that changing blend mode in the layered PSB fixture persists with the expected PSD blend key.
     */
    @Test
    public void save_LayeredPsb_ChangesBlendMode() throws IOException {
        String testFile = getTestDataPath("layered.psb");
        String outputFile = getPersistentArtifactPath("layered_psb_blend_mode_test.psb");

        try (PsdImage image = PsdImage.load(testFile)) {
            image.getLayers()[1].setBlendModeKey(BlendMode.Multiply);
            image.save(outputFile);
        }
        logArtifactDirectory(outputFile);

        try (PsdImage reloaded = PsdImage.load(outputFile)) {
            assertEquals(BlendMode.Multiply, reloaded.getLayers()[1].getBlendModeKey());
        }
    }

    /**
     * Tests that the layer variants fixture exposes visibility, unknown blend key, clipping, and additional data flags.
     */
    @Test
    public void load_LayerVariants_ReadsFlags() {
        try (PsdImage image = PsdImage.load(getTestDataPath("layer-variants.psd"))) {
            assertEquals(4, image.getLayerCount());
            assertFalse(image.getLayers()[1].isVisible());
            assertEquals(BlendMode.LinearBurn, image.getLayers()[2].getBlendModeKey());
            assertEquals("lbrn", image.getLayers()[2].getRawBlendModeKey());
            assertEquals(1, image.getLayers()[3].getClipping());
            assertFalse(image.getLayers()[3].getAdditionalLayerData().length == 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests that changing clipping in the layer variants fixture persists after save and reload.
     */
    @Test
    public void save_LayerVariants_Clipping() throws IOException {
        String testFile = getTestDataPath("layer-variants.psd");
        String outputFile = getPersistentArtifactPath("layer_variants_clipping_test.psd");

        try (PsdImage image = PsdImage.load(testFile)) {
            image.getLayers()[3].setClipping((byte) 0);
            image.save(outputFile);
        }
        logArtifactDirectory(outputFile);

        try (PsdImage reloaded = PsdImage.load(outputFile)) {
            assertEquals(0, reloaded.getLayers()[3].getClipping());
        }
    }

    /**
     * Tests that mutating a layer in the variants fixture preserves the layer-and-mask tail bytes.
     */
    @Test
    public void save_LayerVariants_PreservesTail() throws IOException {
        String testFile = getTestDataPath("layer-variants.psd");
        byte[] originalBytes = Files.readAllBytes(Path.of(testFile));
        String outputFile = getPersistentArtifactPath("layer_variants_tail_test.psd");

        try (PsdImage image = PsdImage.load(testFile)) {
            image.getLayers()[2].setName("Ellipse 1 Updated");
            image.save(outputFile);
        }
        logArtifactDirectory(outputFile);

        byte[] savedBytes = Files.readAllBytes(Path.of(outputFile));
        assertArrayEquals(readLayerAndMaskTail(originalBytes), readLayerAndMaskTail(savedBytes));
    }
}