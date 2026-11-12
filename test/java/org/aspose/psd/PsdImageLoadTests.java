package org.aspose.psd;

import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.layers.BlendMode;
import org.aspose.psd.layers.Layer;
import org.aspose.psd.sections.ImageDataKind;
import org.aspose.psd.sections.PsdHeader;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

public final class PsdImageLoadTests extends PsdTestFixtureBase {

    @Test
    public void Load_Document_ReadsProperties() throws IOException {
        String testFile = Paths.get(getTestDataPath("test.psd")).toString();

        assertTrue(new File(testFile).exists(), "Test file not found");

        try (PsdImage image = PsdImage.load(testFile)) {
            assertTrue(image.getWidth() > 0);
            assertTrue(image.getHeight() > 0);
            assertTrue(image.getChannels() > 0);
            assertEquals(8, image.getBitsPerChannel());
            assertEquals(ColorModes.Rgb, image.getColorMode());
            assertTrue(image.getVersion() > 0 && image.getVersion() <= 6);
            assertTrue(image.getLayers().length > 0);
        }
    }

    @Test
    public void Load_Layer_ReadsProperties() throws IOException {
        String testFile = Paths.get(getTestDataPath("test.psd")).toString();

        try (PsdImage image = PsdImage.load(testFile)) {
            assertTrue(image.getLayers().length > 0);

            Layer firstLayer = image.getLayers()[0];

            assertEquals("Background copy", firstLayer.getName());
            assertNotEquals(new Rectangle(), firstLayer.getBounds());
            assertTrue(firstLayer.isVisible());
            assertTrue((int)(firstLayer.getOpacity() & 0xFF) >= 0 && (int)(firstLayer.getOpacity() & 0xFF) <= 255);
            assertEquals(BlendMode.Normal, firstLayer.getBlendModeKey());

            assertEquals("Pattern Fill 1", image.getLayers()[1].getName());
        }
    }

    @Test
    public void Load_Layers_Indexable() throws IOException {
        String testFile = Paths.get(getTestDataPath("test.psd")).toString();

        try (PsdImage image = PsdImage.load(testFile)) {
            assertNotNull(image.getLayers()[0]);
            assertNotNull(image.getLayers()[1]);
        }
    }

    @Test
    public void Load_Seekable_PreservesPosition() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(getTestDataPath("test.psd")));
        byte[] prefix = new byte[]{1, 2, 3, 4, 5};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(prefix);
        outputStream.write(bytes);
        byte[] combinedBytes = outputStream.toByteArray();

        try (ByteArrayInputStream stream = new ByteArrayInputStream(combinedBytes)) {
            long skipped = stream.skip(prefix.length);
            assertEquals(prefix.length, skipped);

            try (PsdImage image = (PsdImage) Image.load(stream)) {
                int currentPosition = combinedBytes.length - stream.available();
                assertEquals(prefix.length, currentPosition);
                assertTrue(image.getWidth() > 0);
            }
        }
    }

    @Test
    public void Load_NonSeekableStream_Succeeds() throws Exception {
        byte[] bytes = readAllBytes(Paths.get(getTestDataPath("test.psd")));
        try (NonSeekableReadStream stream = new NonSeekableReadStream(bytes);
             PsdImage image = PsdImage.load(stream)) {
            assertTrue(image.getWidth() > 0);
            assertTrue(image.getLayers().length > 0);
        }
    }

    @Test
    public void Load_NullStream_Throws() {
        assertThrows(IllegalArgumentException.class, () -> PsdImage.load((InputStream) null));
    }

    @Test
    public void Load_MissingFile_Throws() {
        String missingFile = String.valueOf(Paths.get(getTestDataPath("missing.psd")));
        assertThrows(FileNotFoundException.class, () -> PsdImage.load(missingFile));
    }

    @Test
    public void Load_InvalidSignature_Throws() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(buildHeaderBytes(PsdHeader.PSD_VERSION));
        byte[] headerBytes = baos.toByteArray();
        headerBytes[0] = (byte) 'B';
        ByteArrayInputStream stream = new ByteArrayInputStream(headerBytes);
        try (BigEndianReader reader = new BigEndianReader(stream, true)) {
            assertThrows(PsdLoadException.class, () -> PsdHeader.load(reader));
        }
    }

    @Test
    public void Load_DocInspection_Reads() throws IOException {
        String testFile = Paths.get(getTestDataPath("test.psd")).toString();

        try (PsdImage image = PsdImage.load(testFile)) {
            assertNotNull(image.getHeader());
            assertFalse(image.isLargeDocument());
            assertFalse(image.isPsb());
            assertEquals(2, image.getLayerCount());
            assertTrue(image.hasImageResources());
            assertEquals(26, image.getResourceCount());
            assertFalse(image.hasColorModeData());
            assertTrue(image.hasMergedImageData());
            assertEquals(CompressionMethod.RLE, image.getCompression());
            assertEquals(ImageDataKind.Rle, image.getImageDataKind());
            assertFalse(image.usesPrediction());
            assertEquals(6, image.getVersion());
            assertEquals(PsdHeader.PSD_VERSION, image.getHeader().getVersion());
            assertEquals(image.getColorMode(), image.getHeader().getColorMode());
        }
    }

    private static byte[] readAllBytes(java.nio.file.Path path) throws IOException {
        try (InputStream is = new FileInputStream(path.toFile())) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] tmp = new byte[8192];
            int n;
            while ((n = is.read(tmp)) != -1) {
                buffer.write(tmp, 0, n);
            }
            return buffer.toByteArray();
        }
    }
}
