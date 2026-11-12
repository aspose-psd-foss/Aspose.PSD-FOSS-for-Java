package org.aspose.psd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.coreexceptions.PsdLoadException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public final class PascalStringTests extends PsdTestFixtureBase {

    /**
     * Tests that 2-byte-aligned PSD Pascal strings consume padding even when the payload is empty.
     */
    @Test
    public void readAligned2ConsumesPadding() throws IOException {
        byte[] data = new byte[] { 0x00, 0x00, 0x7F };
        try (InputStream stream = new ByteArrayInputStream(data);
             BigEndianReader reader = new BigEndianReader(stream, true)) {

            String value = reader.readPascalStringAlignedTo2();

            assertTrue(value.isEmpty());
            assertEquals(2, reader.getPosition());
            assertEquals(0x7F, reader.readByte());
        } catch (PsdLoadException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests that 4-byte-aligned PSD Pascal strings consume padding even when the payload is empty.
     */
    @Test
    public void readAligned4ConsumesPadding() throws IOException {
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x7F };
        try (InputStream stream = new ByteArrayInputStream(data);
             BigEndianReader reader = new BigEndianReader(stream, true)) {

            String value = reader.readPascalStringAlignedTo4();

            assertTrue(value.isEmpty());
            assertEquals(4, reader.getPosition());
            assertEquals(0x7F, reader.readByte());
        } catch (PsdLoadException e) {
            throw new RuntimeException(e);
        }
    }
}
