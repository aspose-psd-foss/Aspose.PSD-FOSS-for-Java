package org.aspose.psd;

import org.aspose.psd.resources.IndexedColorPalette;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.sections.ColorData;
import org.aspose.psd.sections.PsdColorDataKind;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public final class ColorDataTests extends PsdTestFixtureBase {

    @Test
    public void loadTooLongColorModeThrows() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(getTestDataPath("test.psd")));
        writeUInt32BigEndian(bytes, 26, 100000);
        try (InputStream stream = new ByteArrayInputStream(bytes)) {
            assertThrows(PsdLoadException.class, () -> PsdImage.load(stream));
        }
    }

    @Test
    public void loadIndexedDataParsesPalette() throws Exception {
        byte[] payload = buildIndexedPalettePayload();
        try (InputStream stream = new ByteArrayInputStream(BuildColorDataSection(payload));
                BigEndianReader reader = new BigEndianReader(stream, true)) {

            ColorData colorData = ColorData.load(reader, ColorModes.Indexed);

            assertEquals(PsdColorDataKind.INDEXED_PALETTE, colorData.getKind());
            assertByteArrayEquals(payload, colorData.getRawData());
            assertNotNull(colorData.getIndexedPalette());
            assertEquals(256, colorData.getIndexedPalette().getEntries().length);
            assertEquals(new Color(0x00, 0xFF, 0x80), colorData.getIndexedPalette().getEntries()[0]);
            assertEquals(new Color(0x11, 0xEE, 0x91), colorData.getIndexedPalette().getEntries()[17]);
        }
    }

    @Test
    public void loadCmykPayloadClassifies() throws Exception {
        byte[] payload = new byte[] { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE };
        try (InputStream stream = new ByteArrayInputStream(BuildColorDataSection(payload));
                BigEndianReader reader = new BigEndianReader(stream, true)) {

            ColorData colorData = ColorData.load(reader, ColorModes.Cmyk);

            assertEquals(PsdColorDataKind.CMYK_PAYLOAD, colorData.getKind());
            assertByteArrayEquals(payload, colorData.getRawData());
            assertNull(colorData.getIndexedPalette());
        }
    }

    @Test
    public void loadIndexedFixtureReadsData() throws Exception {
        try (PsdImage image = PsdImage.load(getTestDataPath("basic-indexed.psd"))) {

            assertEquals(200, image.getWidth());
            assertEquals(200, image.getHeight());
            assertEquals(1, image.getChannelsCount());
            assertEquals(ColorModes.Indexed, image.getColorMode());
        }
    }

    @Test
    public void saveIndexedFixtureIsByteExact() throws Exception {
        assertByteExactRoundTrip("basic-indexed.psd");
    }

    // Utility methods used only by this test class

    private static void writeUInt32BigEndian(byte[] buffer, int offset, int value) {
        buffer[offset] = (byte) ((value >>> 24) & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 3] = (byte) (value & 0xFF);
    }

    private static byte[] BuildColorDataSection(byte[] payload) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeUInt32BigEndian(baos, payload.length);
        baos.write(payload);
        return baos.toByteArray();
    }

    private static void writeUInt32BigEndian(ByteArrayOutputStream out, int value) throws IOException {
        out.write((value >>> 24) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>> 8) & 0xFF);
        out.write(value & 0xFF);
    }

    protected static byte[] buildIndexedPalettePayload() {
        byte[] payload = new byte[IndexedColorPalette.EXPECTED_RAW_LENGTH];
        for (int i = 0; i < 256; i++) {
            payload[i] = (byte) i;
            payload[i + 256] = (byte) (255 - i);
            payload[i + 512] = (byte) (128 + (i % 64));
        }
        return payload;
    }


    private static void assertByteArrayEquals(byte[] expected, byte[] actual) {
        assertTrue(Arrays.equals(expected, actual), "Byte arrays are not equal");
    }

    /**
     * Tests that a malformed color mode length field is rejected with {@link PsdLoadException}.
     */
    @Test
    public void load_TooLongColorMode_Throws() throws Exception {
        byte[] bytes =Files.readAllBytes(Paths.get(getTestDataPath("test.psd")));
        writeUInt32BigEndian(bytes, 26, 100_000L);

        try (InputStream stream = new ByteArrayInputStream(bytes)) {
            assertThrows(PsdLoadException.class, () -> PsdImage.load(stream));
        }
    }

    /**
     * Tests that unexpected RGB color mode data is classified explicitly and preserved.
     */
    @Test
    public void load_RgbPayload_Classifies() throws Exception {
            byte[] payload = new byte[] { 0x10, 0x20, 0x30, 0x40 };
        try (InputStream stream = new ByteArrayInputStream(BuildColorDataSection(payload));
             BigEndianReader reader = new BigEndianReader(stream, true)) {

            ColorData colorData = ColorData.load(reader, ColorModes.Rgb);

            assertEquals(PsdColorDataKind.RGB_PAYLOAD, colorData.getKind());
            assertByteArrayEquals(payload, colorData.getRawData());
            assertNull(colorData.getIndexedPalette());
        }
    }

    /**
     * Tests that ` color mode data is parsed into a structured 256-color palette.
     */
    @Test
    public void load_IndexedData_ParsesPalette() throws Exception {
        byte[] payload = buildIndexedPalettePayload();

        try (InputStream stream = new ByteArrayInputStream(buildColorDataSection(payload));
             BigEndianReader reader = new BigEndianReader(stream, true)) {

            ColorData colorData = ColorData.load(reader, ColorModes.Indexed);

            assertEquals(PsdColorDataKind.INDEXED_PALETTE, colorData.getKind());
            assertArrayEquals(payload, colorData.getRawData());

            assertNotNull(colorData.getIndexedPalette());
            Color[] entries = colorData.getIndexedPalette().getEntries();
            assertEquals(256, entries.length);
            assertEquals(new Color(0x00, 0xFF, 0x80), entries[0]);
            assertEquals(new Color(0x11, 0xEE, 0x91), entries[17]);
        }
    }

    /**
     * Tests that CMYK color mode data is classified explicitly and preserved.
     */
    @Test
    public void load_CmykPayload_Classifies() throws Exception {
        byte[] payload = new byte[] { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE };

        try (InputStream stream = new ByteArrayInputStream(buildColorDataSection(payload));
             BigEndianReader reader = new BigEndianReader(stream, true)) {

            ColorData colorData = ColorData.load(reader, ColorModes.Cmyk);

            assertEquals(PsdColorDataKind.CMYK_PAYLOAD, colorData.getKind());
            assertArrayEquals(payload, colorData.getRawData());
            assertNull(colorData.getIndexedPalette());
        }
    }

    /**
     * Tests that the basic indexed fixture exposes indexed color mode data and palette metadata.
     */
    @Test
    public void load_IndexedFixture_ReadsData() throws Exception {
        try (PsdImage image = PsdImage.load(getTestDataPath("basic-indexed.psd"))) {
            assertEquals(200, image.getWidth());
            assertEquals(200, image.getHeight());
            assertEquals(1, image.getChannels());
            assertEquals(ColorModes.Indexed, image.getColorMode());
            assertTrue(image.hasColorModeData());
            assertEquals(PsdColorDataKind.INDEXED_PALETTE, image.getColorDataInfo().getKind());
            assertEquals(768, image.getColorDataInfo().getRawDataLength());
            assertEquals(0, image.getLayerCount());
            assertEquals(24, image.getResourceCount());
        }
    }

    // ---------------------------------------------------------------------
    // Helpers (ported from the C# test project)
    // ---------------------------------------------------------------------

}
