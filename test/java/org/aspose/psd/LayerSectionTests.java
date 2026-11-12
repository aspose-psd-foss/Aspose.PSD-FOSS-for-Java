package org.aspose.psd;

import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.layers.BlendMode;
import org.aspose.psd.layers.Layer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class LayerSectionTests extends PsdTestFixtureBase {

    @Test
    public void loadTooLongLayerMaskThrows() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(getTestDataPath("test.psd")));
        int colorModeLength = BigEndianBitConverter.toInt32(bytes, 26);
        int resourcesLengthOffset = 26 + 4 + colorModeLength;
        int resourcesPayloadLength = BigEndianBitConverter.toInt32(bytes, resourcesLengthOffset);
        int layerAndMaskLengthOffset = resourcesLengthOffset + 4 + resourcesPayloadLength;
        writeUInt32BigEndian(bytes, layerAndMaskLengthOffset, 100000L);
        try (InputStream stream = new ByteArrayInputStream(bytes)) {
            assertThrows(PsdLoadException.class, () -> PsdImage.load(stream));
        }
    }

    @Test
    public void loadTooLongLayerExtraThrows() throws Exception {
        byte[] layerBytes = buildLayerRecordBytesWithExtraData(new byte[] {
                0x00, 0x00, 0x00, 0x10,
                0x00, 0x00, 0x00, 0x00
        });
        try (InputStream stream = new ByteArrayInputStream(layerBytes);
                BigEndianReader reader = new BigEndianReader(stream, true)) {
            assertThrows(PsdLoadException.class, () -> Layer.load(reader, false));
        }
    }

    @Test
    public void loadNegativeLayerExtraThrows() throws Exception {
        byte[] layerBytes = buildLayerRecordBytesWithExtraData(new byte[0]);
        writeUInt32BigEndian(layerBytes, 30, 0xFFFFFFFFL);
        try (InputStream stream = new ByteArrayInputStream(layerBytes);
                BigEndianReader reader = new BigEndianReader(stream, true)) {
            assertThrows(PsdLoadException.class, () -> Layer.load(reader, false));
        }
    }

    @Test
    public void loadLayerInspectionReadsValues() throws Exception {
        PsdImage image = PsdImage.load(Paths.get(getTestDataPath("test.psd")).toString());
        Layer firstLayer = image.getLayers()[0];

        assertEquals(new Rectangle(0, 0, 100, 100), firstLayer.getBounds());
        assertEquals(firstLayer.getWidth(), firstLayer.getBounds().getWidth());
        assertEquals(firstLayer.getHeight(), firstLayer.getBounds().getHeight());
        assertEquals(0, firstLayer.getTop());
        assertEquals(0, firstLayer.getLeft());
        assertEquals(100, firstLayer.getBottom());
        assertEquals(100, firstLayer.getRight());
        assertTrue(firstLayer.getChannelsCount() > 0);
        assertEquals(4, firstLayer.getRawBlendModeKey().length());
        assertNull(firstLayer.getLayerMaskData());
        assertEquals(firstLayer.getLayerBlendingRangesData().getLength(),
                firstLayer.getBlendingRangesInfo().getRawDataLength() - Integer.BYTES);
        assertFalse(firstLayer.getAdditionalLayerData().length <= 0);
    }

    @Test
    public void saveChangesBlendMode() throws Exception {
        String testFile = Paths.get(getTestDataPath("test.psd")).toString();
        String outputFile = getPersistentArtifactPath("layer_blend_mode_test.psd");

        PsdImage image = PsdImage.load(testFile);
        image.getLayers()[0].setBlendModeKey(BlendMode.Multiply);
        image.save(outputFile);
        logArtifactDirectory(outputFile);

        PsdImage reloaded = PsdImage.load(outputFile);
        assertEquals(BlendMode.Multiply, reloaded.getLayers()[0].getBlendModeKey());

        byte[] originalBytes = Files.readAllBytes(Paths.get(testFile));
        byte[] savedBytes = Files.readAllBytes(Paths.get(outputFile));
        assertEquals(readLayerInfoLength(originalBytes), readLayerInfoLength(savedBytes));
    }

    @Test
    public void saveChangesClipping() throws Exception {
        String testFile = getTestDataPath("test.psd").toString();
        String outputFile = getPersistentArtifactPath("layer_clipping_test.psd");
        byte[] originalBytes = Files.readAllBytes(Paths.get(testFile));

        PsdImage image = PsdImage.load(testFile);
        var layers = image.getLayers();
        layers[0].setClipping((byte) 1);
        image.setLayers(layers);
        image.save(outputFile);
        logArtifactDirectory(outputFile);

        PsdImage reloaded = PsdImage.load(outputFile);
        assertEquals(1, reloaded.getLayers()[0].getClipping());

        byte[] savedBytes = Files.readAllBytes(Paths.get(outputFile));
        assertArrayEquals(readLayerAndMaskTail(originalBytes), readLayerAndMaskTail(savedBytes));
    }

    @Test
    public void saveChangesBounds() throws Exception {
        String testFile = Paths.get(getTestDataPath("test.psd")).toString();
        String outputFile = getPersistentArtifactPath("layer_bounds_test.psd");

        PsdImage image = PsdImage.load(testFile);
        Rectangle newBounds = Rectangle.fromLeftTopRightBottom(10, 20, 40, 60);
        Layer layer = image.getLayers()[0];
        layer.setLeft(newBounds.getLeft());
        layer.setTop(newBounds.getTop());
        layer.setRight(newBounds.getRight());
        layer.setBottom(newBounds.getBottom());
        image.save(outputFile);
        logArtifactDirectory(outputFile);

        PsdImage reloaded = PsdImage.load(outputFile);
        assertEquals(new Rectangle(0, 0, newBounds.getWidth(), newBounds.getHeight()),
                reloaded.getLayers()[0].getBounds());
        assertEquals(newBounds.getLeft(), reloaded.getLayers()[0].getLeft());
        assertEquals(newBounds.getTop(), reloaded.getLayers()[0].getTop());
        assertEquals(newBounds.getRight(), reloaded.getLayers()[0].getRight());
        assertEquals(newBounds.getBottom(), reloaded.getLayers()[0].getBottom());
    }

    @Test
    public void saveChangesCoordinates() throws Exception {
        String testFile = Paths.get(getTestDataPath("test.psd")).toString();
        String outputFile = getPersistentArtifactPath("layer_coordinates_test.psd");

        PsdImage image = PsdImage.load(testFile);
        Layer layer = image.getLayers()[0];
        layer.setTop(10);
        layer.setLeft(20);
        layer.setBottom(30);
        layer.setRight(50);
        image.save(outputFile);
        logArtifactDirectory(outputFile);

        PsdImage reloaded = PsdImage.load(outputFile);
        assertEquals(new Rectangle(0, 0, 30, 20), reloaded.getLayers()[0].getBounds());
        assertEquals(20, reloaded.getLayers()[0].getLeft());
        assertEquals(10, reloaded.getLayers()[0].getTop());
        assertEquals(50, reloaded.getLayers()[0].getRight());
        assertEquals(30, reloaded.getLayers()[0].getBottom());
    }

    @Test
    public void loadPsbLayerRecordReads() throws Exception {
        try (InputStream stream = new ByteArrayInputStream(buildPsbLayerRecordBytes());
                BigEndianReader reader = new BigEndianReader(stream, true)) {

            Layer layer = Layer.load(reader, true);

            assertEquals("Layer 1", layer.getName());
            assertEquals(new Rectangle(0, 0, 1, 1), layer.getBounds());
            assertEquals((byte) 200, layer.getOpacity());
            assertTrue(layer.isVisible());
        }
    }
}
