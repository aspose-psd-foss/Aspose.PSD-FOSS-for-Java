package org.aspose.psd;

import org.aspose.psd.layers.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains source-compatibility tests for the documented Aspose.PSD API subset
 * implemented by the FOSS package.
 */
public final class AsposeCompatibilityTests extends PsdTestFixtureBase {

    /**
     * Tests that official-style code can load a PSD through {@link Image#load(String)}
     * and cast it to {@link PsdImage}.
     */
    @Test
    public void imageLoad_Official_ReturnsPsd() throws IOException {
        try (Image image = Image.load(getTestDataPath("test.psd"))) {
            PsdImage psdImage = (PsdImage) image;

            assertEquals(100, psdImage.getWidth());
            assertEquals(3, psdImage.getChannelsCount());
            assertEquals(2, psdImage.getLayers().length);
        }
    }

    /**
     * Tests that official-style layer metadata access uses Aspose.PSD
     * Rectangle and BlendMode types.
     */
    @Test
    public void metadata_OfficialTypes_Reads() throws IOException {
        try (PsdImage image = (PsdImage) Image.load(getTestDataPath("test.psd"))) {
            Layer layer = image.getLayers()[0];

            Rectangle bounds = layer.getBounds();
            BlendMode blendModeKey = layer.getBlendModeKey();

            assertEquals(new Rectangle(0, 0, 100, 100), bounds);
            assertEquals(BlendMode.Normal, blendModeKey);
            assertEquals((byte)255, layer.getOpacity());
            assertTrue(layer.isVisible());
        }
    }

    /**
     * Tests that official-style layer channel properties expose parsed channel
     * metadata and reject unsupported channel rewrites.
     */
    @Test
    public void channels_OfficialShape_ReadsOnly() throws IOException {
        try (PsdImage image = (PsdImage) Image.load(getTestDataPath("test.psd"))) {
            Layer layer = image.getLayers()[0];

            ChannelInformation[] channelInformation = layer.getChannelInformation();

            assertEquals(channelInformation.length, layer.getChannelsCount());
            assertNotEquals(0, channelInformation.length);
            assertTrue(channelInformation[0].getLength() > 0);
            assertThrows(UnsupportedOperationException.class,
                    () -> layer.setChannelInformation(channelInformation));
        }
    }

    /**
     * Tests that official-style layer mask and blending range properties expose
     * supported metadata and reject unsupported semantic rewrites.
     */
    @Test
    public void layerMask_OfficialShape_Reads() {
        try (PsdImage image = (PsdImage) Image.load(getTestDataPath("test.psd"))) {
            Layer layer = image.getLayers()[0];

            assertNull(layer.getLayerMaskData());
            assertEquals(40, layer.getLayerBlendingRangesData().getLength());
            assertThrows(UnsupportedOperationException.class,
                    () -> layer.setLayerMaskData(null));
            assertThrows(UnsupportedOperationException.class,
                    () -> layer.setLayerBlendingRangesData(new LayerBlendingRangesData()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests that the Aspose.PSD-compatible rectangle value types expose
     * mutable location and size members.
     */
    @Test
    public void rectangle_MutableShape_Updates() {
        Rectangle rectangle = new Rectangle(new Point(10, 20), new Size(30, 40));

        rectangle.setLocation(new Point(15, 25));
        rectangle.setSize(new Size(35, 45));
        rectangle.setRight(60);
        rectangle.setBottom(80);

        assertEquals(15, rectangle.getX());
        assertEquals(25, rectangle.getY());
        assertEquals(45, rectangle.getWidth());
        assertEquals(55, rectangle.getHeight());
        assertTrue(rectangle.contains(new Point(30, 40)));
        assertTrue(rectangle.intersectsWith(Rectangle.fromLeftTopRightBottom(50, 70, 70, 90)));

        rectangle.offset(new Point(5, -5));
        rectangle.inflate(new Size(1, 2));

        assertEquals(Rectangle.fromLeftTopRightBottom(19, 18, 66, 77), rectangle);
    }

    /**
     * Tests that official-style PSD image setters compile and update the
     * implemented structural metadata subset.
     */
    @Test
    public void imageSetters_UpdateMetadata() throws IOException {
        try (PsdImage image = (PsdImage) Image.load(getTestDataPath("test.psd"))) {
            Layer[] layers = image.getLayers();
            int version = image.getVersion();

            image.setColorMode(ColorModes.Rgb);
            image.setLayers(layers);
            image.setGlobalAngle(45);
            image.setVersion(version);

            assertEquals(ColorModes.Rgb, image.getColorMode());
            assertEquals(layers.length, image.getLayers().length);
            assertEquals(45, image.getGlobalAngle());
            assertEquals(version, image.getVersion());
        }
    }

    /**
     * Tests that official-style PSD image resource and document state properties
     * expose the supported compatibility subset.
     */
    @Test
    public void resources_OfficialShape_Reads() {
        try (PsdImage image = (PsdImage) Image.load(getTestDataPath("test.psd"))) {
            ResourceBlock[] imageResources = image.getImageResources();
            LayerResource[] globalLayerResources = image.getGlobalLayerResources();
            GlobalLayerMaskInfo globalLayerMaskInfo = image.getGlobalLayerMaskInfo();
            Layer activeLayer = image.getActiveLayer();

            assertEquals(new Size(image.getWidth(), image.getHeight()), image.getSize());
            assertNotEquals(0, imageResources.length);
            assertNotEquals(0, imageResources[0].getId());
            assertTrue(imageResources[0].getDataSize() >= 0);
            assertEquals(0, globalLayerResources.length);
            assertNotNull(globalLayerMaskInfo);
            assertSame(image.getLayers()[0], activeLayer);
            assertFalse(image.isFlatten());
            assertFalse(image.getHasTransparencyData());
            assertThrows(UnsupportedOperationException.class,
                    () -> image.setImageResources((ResourceBlock[]) imageResources));
            assertThrows(UnsupportedOperationException.class,
                    () -> image.setGlobalLayerResources(globalLayerResources));
            assertThrows(UnsupportedOperationException.class,
                    () -> image.setActiveLayer(activeLayer));
            assertThrows(UnsupportedOperationException.class,
                    () -> image.setHasTransparencyData(true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests that official-style layer mutation and save flow works through the
     * implemented compatibility subset.
     */
    @Test
    public void save_OfficialMutation_Persists() throws IOException {
        String outputFile = getPersistentArtifactPath("official_style_layer_mutation.psd");

        try (PsdImage image = (PsdImage) Image.load(getTestDataPath("test.psd"))) {
            image.getLayers()[0].setName("Official style");
            image.getLayers()[0].setLeft(10);
            image.getLayers()[0].setTop(20);
            image.getLayers()[0].setRight(40);
            image.getLayers()[0].setBottom(60);
            image.getLayers()[0].setBlendModeKey(BlendMode.Multiply);
            image.save(outputFile);
        }

        try (PsdImage reloaded = (PsdImage) Image.load(outputFile)) {
            assertEquals("Official style", reloaded.getLayers()[0].getName());
            assertEquals(new Rectangle(0, 0, 30, 40), reloaded.getLayers()[0].getBounds());
            assertEquals(10, reloaded.getLayers()[0].getLeft());
            assertEquals(20, reloaded.getLayers()[0].getTop());
            assertEquals(40, reloaded.getLayers()[0].getRight());
            assertEquals(60, reloaded.getLayers()[0].getBottom());
            assertEquals(BlendMode.Multiply, reloaded.getLayers()[0].getBlendModeKey());
        }
    }
}