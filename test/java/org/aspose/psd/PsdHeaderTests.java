package org.aspose.psd;

import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.sections.PsdHeader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class PsdHeaderTests extends PsdTestFixtureBase {

    @Test
    public void Load_NonZeroReserved_Throws() throws IOException {
        byte[] bytes = buildHeaderBytes(PsdHeader.PSD_VERSION);
        bytes[6] = 1;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
             BigEndianReader reader = new BigEndianReader(stream, true)) {
            assertThrows(PsdLoadException.class, () -> PsdHeader.load(reader));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,8,0",
            "57,1,1,8,0",
            "3,0,1,8,0",
            "3,1,0,8,0",
            "3,30001,1,8,0",
            "3,1,1,12,0",
            "3,1,1,8,99"
    })
    public void Load_InvalidHeaderField_Throws(int channels, int width, int height, int bitDepth, int colorMode) throws IOException {
        //ColorModes mode = ColorModes.fromValue((short) colorMode);
        byte[] bytes = buildHeaderBytes(PsdHeader.PSD_VERSION, (short) channels, width, height, (short) bitDepth, (short) colorMode);
        try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
             BigEndianReader reader = new BigEndianReader(stream, true)) {
            assertThrows(PsdLoadException.class, () -> PsdHeader.load(reader));
        }
    }

    @Test
    public void Load_PsdHeader_ReadsMetadata() throws IOException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(buildHeaderBytes(PsdHeader.PSD_VERSION));
             BigEndianReader reader = new BigEndianReader(stream, true)) {
            PsdHeader header = PsdHeader.load(reader);
            assertEquals(PsdHeader.PSD_VERSION, header.getVersion());
            assertEquals(1, header.getWidth());
            assertEquals(1, header.getHeight());
            assertEquals(3, header.getChannels());
            assertEquals(8, header.getBitDepth());
            assertEquals(ColorModes.Rgb, header.getColorMode());
        } catch (PsdLoadException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void Load_PsbHeader_ReadsMetadata() throws IOException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(buildHeaderBytes(PsdHeader.PSB_VERSION));
             BigEndianReader reader = new BigEndianReader(stream, true)) {
            PsdHeader header = PsdHeader.load(reader);
            assertEquals(PsdHeader.PSB_VERSION, header.getVersion());
            assertTrue(header.isLargeDocument());
            assertEquals(1, header.getWidth());
            assertEquals(1, header.getHeight());
            assertEquals(3, header.getChannels());
        } catch (PsdLoadException e) {
            throw new RuntimeException(e);
        }
    }
}
