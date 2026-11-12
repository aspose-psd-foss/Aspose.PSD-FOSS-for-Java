package org.aspose.psd;

import org.aspose.psd.PsdTestFixtureBase;
import org.aspose.psd.PsdImage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public final class PsdImageSaveTests extends PsdTestFixtureBase {

    @Test
    public void Save_RoundTrip_LoadsAgain() throws IOException {
        Path testFile = Paths.get(getTestDataPath("test.psd"));
        String outputFile = getPersistentArtifactPath("roundtrip_test.psd");

        try (PsdImage image1 = PsdImage.load(testFile.toString())) {
            int originalLength = image1.getLayers().length;
            String originalLayerName = image1.getLayers().length > 0 ? image1.getLayers()[0].getName() : "";
            image1.save(outputFile);
            logArtifactDirectory(outputFile);

            try (PsdImage image2 = PsdImage.load(outputFile)) {
                assertEquals(image1.getWidth(), image2.getWidth());
                assertEquals(image1.getHeight(), image2.getHeight());
                assertEquals(image1.getChannels(), image2.getChannels());
                assertEquals(image1.getBitsPerChannel(), image2.getBitsPerChannel());
                assertEquals(originalLength, image2.getLayers().length);
                if (image2.getLayers().length > 0) {
                    assertEquals(originalLayerName, image2.getLayers()[0].getName());
                }
            }
        }
    }

    @Test
    public void Save_RoundTrip_IsByteExact() throws IOException {
        Path testFile = Paths.get(getTestDataPath( "test.psd"));
        String outputFile = getPersistentArtifactPath("roundtrip_byteexact_test.psd");

        byte[] originalBytes = Files.readAllBytes(testFile);

        try (PsdImage image = PsdImage.load(testFile.toString())) {
            image.save(outputFile);
            logArtifactDirectory(outputFile);
        }

        byte[] savedBytes = Files.readAllBytes(Paths.get(outputFile));

        assertEquals(originalBytes.length, savedBytes.length,
                "Round‑trip output file length must match original file length.");

        for (int i = 0; i < originalBytes.length; i++) {
            assertEquals(originalBytes[i], savedBytes[i],
                    String.format("Byte mismatch at position %d: expected %02X, got %02X", i, originalBytes[i], savedBytes[i]));
        }
    }

    @Test
    public void Save_ChangesLayerName() throws IOException {
        Path testFile = Paths.get(getTestDataPath( "test.psd"));
        String outputFile = getPersistentArtifactPath("layer_name_test.psd");

        try (PsdImage image = PsdImage.load(testFile.toString())) {
            assertTrue(image.getLayers().length > 0);

            image.getLayers()[0].setName("Test Layer Name");
            image.save(outputFile);
            logArtifactDirectory(outputFile);
        }

        try (PsdImage reloaded = PsdImage.load(outputFile)) {
            assertEquals("Test Layer Name", reloaded.getLayers()[0].getName());
        }
    }

    @Test
    public void Save_ChangesLayerVisibility() throws IOException {
        Path testFile = Paths.get(getTestDataPath( "test.psd"));
        String outputFile = getPersistentArtifactPath("layer_visible_test.psd");

        boolean originalVisible;
        try (PsdImage image = PsdImage.load(testFile.toString())) {
            assertTrue(image.getLayers().length > 0);
            originalVisible = image.getLayers()[0].isVisible();
            image.getLayers()[0].setVisible(!originalVisible);
            image.save(outputFile);
            logArtifactDirectory(outputFile);
        }

        try (PsdImage reloaded = PsdImage.load(outputFile)) {
            assertEquals(!originalVisible, reloaded.getLayers()[0].isVisible());
        }
    }

    @Test
    public void Save_ChangesLayerOpacity() throws IOException {
        Path testFile = Paths.get(getTestDataPath( "test.psd"));
        String outputFile = getPersistentArtifactPath("layer_opacity_test.psd");

        byte newOpacity;
        try (PsdImage image = PsdImage.load(testFile.toString())) {
            assertTrue(image.getLayers().length > 0);
            byte originalOpacity = image.getLayers()[0].getOpacity();
            newOpacity = (byte) Math.max(0, originalOpacity - 50);
            image.getLayers()[0].setOpacity(newOpacity);
            image.save(outputFile);
            logArtifactDirectory(outputFile);
        }

        try (PsdImage reloaded = PsdImage.load(outputFile)) {
            assertEquals(newOpacity, reloaded.getLayers()[0].getOpacity());
        }
    }

    @Test
    public void Save_Mutation_PreservesFlags() throws IOException {
        Path testFile = Paths.get(getTestDataPath( "test.psd"));
        String outputFile = getPersistentArtifactPath("preserve_flags_and_blend.psd");

        try (PsdImage image = PsdImage.load(testFile.toString())) {
            image.getLayers()[0].setName("Renamed");
            image.save(outputFile);
            logArtifactDirectory(outputFile);
        }

        try (PsdImage reloaded = PsdImage.load(outputFile)) {
            assertEquals("norm", reloaded.getLayers()[0].getRawBlendModeKey());
            assertTrue(reloaded.getLayers()[0].isVisible());
        }
    }
}
