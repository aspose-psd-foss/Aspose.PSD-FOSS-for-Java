package org.aspose.psd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.aspose.psd.coreexceptions.PsdSaveException;
import org.aspose.psd.resources.ImageResourceIds;
import org.junit.jupiter.api.Assertions;

import org.aspose.psd.resources.IndexedColorPalette;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PsdTestFixtureBase {
    protected String getTestDataPath(String fileName) {
        String resourcesPath = Paths.get("test", "resources", fileName).toAbsolutePath().toString();
        if (Files.exists(Paths.get(resourcesPath))) {
            return resourcesPath;
        }
        return Paths.get("src", "testdata", fileName).toAbsolutePath().toString();
    }

    protected String getPersistentArtifactPath(String fileName) {
        String testName = sanitizePathSegment(getTestName());
        String artifactDirectory = Paths.get("target", "artifacts", "PsdTestFixtureBase", testName).toString();
        Path artifactPath = Paths.get(artifactDirectory);
        if (!Files.exists(artifactPath)) {
            try {
                Files.createDirectories(artifactPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create artifact directory: " + artifactDirectory, e);
            }
        }
        return Paths.get(artifactDirectory, fileName).toString();
    }

    protected void assertByteExactRoundTrip(String fileName) throws Exception {
        String testFile = getTestDataPath(fileName);
        String outputFile = getPersistentArtifactPath(fileName);
        byte[] originalBytes = Files.readAllBytes(Paths.get(testFile));

        try (PsdImage img = (PsdImage) ImageLoader.load(testFile)) {
            img.save(outputFile);
        }
        logArtifactDirectory(outputFile);

        byte[] savedBytes = Files.readAllBytes(Paths.get(outputFile));
        assertByteArrayEquals(originalBytes, savedBytes);
    }

    /**
     * Asserts that renaming a layer persists after saving and reloading a fixture.
     *
     * @param fileName   The fixture file name.
     * @param layerIndex The zero-based layer index to rename.
     * @param newName    The replacement layer name.
     * @throws IOException If reading or writing the fixture fails.
     */
    protected void assertRenameSave(String fileName, int layerIndex, String newName) throws IOException {
        String testFile = getTestDataPath(fileName);
        String outputFile = getPersistentArtifactPath("renamed_" + fileName);

        try (PsdImage image = PsdImage.load(testFile)) {
            image.getLayers()[layerIndex].setName(newName);
            image.save(outputFile);
        }
        logArtifactDirectory(outputFile);

        try (PsdImage reloaded = PsdImage.load(outputFile)) {
            assertEquals(newName, reloaded.getLayers()[layerIndex].getName());
        }
    }

    private void assertByteArrayEquals(byte[] expected, byte[] actual) {
        Assertions.assertTrue(java.util.Arrays.equals(expected, actual), "Byte arrays differ");
    }

    private String getTestName() {
        return System.getProperty("junit.jupiter.test.method.name", "unknown");
    }

    protected static void writeUInt32BigEndian(byte[] buffer, int offset, long value) {
        buffer[offset] = (byte) ((value >>> 24) & 0xFF);
        buffer[offset + 1] = (byte) ((value >>> 16) & 0xFF);
        buffer[offset + 2] = (byte) ((value >>> 8) & 0xFF);
        buffer[offset + 3] = (byte) (value & 0xFF);
    }

    protected static int toInt32(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xFF) << 24) |
                ((bytes[offset + 1] & 0xFF) << 16) |
                ((bytes[offset + 2] & 0xFF) << 8) |
                (bytes[offset + 3] & 0xFF);
    }

    protected static byte[] buildHeaderBytes(int version) {
        return buildHeaderBytes(version, 3);
    }

    protected static byte[] buildHeaderBytes(int version, int channels) {
        return buildHeaderBytes(version, channels, 1);
    }

    protected static byte[] buildHeaderBytes(int version, int channels, int width) {
        return buildHeaderBytes(version, channels, width, 1);
    }

    protected static byte[] buildHeaderBytes(int version, int channels, int width, int height) {
        return buildHeaderBytes(version, channels, width, height, 8);
    }

    protected static byte[] buildHeaderBytes(int version, int channels, int width, int height, int bitDepth) {
        return buildHeaderBytes(version, channels, width, height, bitDepth, ColorModes.Rgb);
    }

    protected static byte[] buildHeaderBytes(int version, int channels, int width, int height, int bitDepth,
            ColorModes colorMode) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (BigEndianWriter writer = new BigEndianWriter(stream, true)) {
            stream.write("8BPS".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
            writer.writeShort((short) version);
            writer.write(new byte[6]);
            writer.writeShort((short) channels);
            writer.writeInt(height);
            writer.writeInt(width);
            writer.writeShort((short) bitDepth);
            writer.writeShort((short) colorMode.getValue());
        } catch (IOException e) {
            throw new RuntimeException("Failed to build header bytes", e);
        }
        return stream.toByteArray();
    }

    protected static byte[] buildHeaderBytes(int version, int channels, int width, int height, int bitDepth,
                                             short colorMode) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (BigEndianWriter writer = new BigEndianWriter(stream, true)) {
            stream.write("8BPS".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
            writer.writeShort((short) version);
            writer.write(new byte[6]);
            writer.writeShort((short) channels);
            writer.writeInt(height);
            writer.writeInt(width);
            writer.writeShort((short) bitDepth);
            writer.writeShort((short) colorMode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to build header bytes", e);
        }
        return stream.toByteArray();
    }

    protected static byte[] buildColorDataSection(byte[] payload) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (BigEndianWriter writer = new BigEndianWriter(stream, true)) {
            writer.writeUInt(payload.length);
            writer.write(payload);
        }
        return stream.toByteArray();
    }

    protected static byte[] buildImageDataSection(CompressionMethod compression, byte[] payload) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (BigEndianWriter writer = new BigEndianWriter(stream, true)) {
            writer.writeShort((short) compression.getValue());
            writer.write(payload);
        }
        return stream.toByteArray();
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

    protected static byte[] buildResourcesPayload(TestResourceBlock block) {
        List<TestResourceBlock> blocks = Arrays.asList(
                block
        );
        return buildResourcesPayload(blocks);
    }

    protected static byte[] buildResourcesPayload(java.util.List<TestResourceBlock> resources) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (TestResourceBlock resource : resources) {
            stream.write("8BIM".getBytes(java.nio.charset.StandardCharsets.US_ASCII), 0, 4);
            writeShortBE(stream, resource.resourceId);
            byte[] nameBytes = resource.name.getBytes(java.nio.charset.StandardCharsets.US_ASCII);
            stream.write(nameBytes.length);
            stream.write(nameBytes, 0, nameBytes.length);
            if ((nameBytes.length + 1) % 2 != 0) {
                stream.write(0);
            }
            writeIntBE(stream, resource.data.length);
            stream.write(resource.data, 0, resource.data.length);
            if (resource.data.length % 2 == 1) {
                stream.write(0);
            }
        }
        return stream.toByteArray();
    }

    private static void writeShortBE(ByteArrayOutputStream stream, short value) {
        stream.write((byte) ((value >>> 8) & 0xFF));
        stream.write((byte) (value & 0xFF));
    }

    private static void writeIntBE(ByteArrayOutputStream stream, int value) {
        stream.write((byte) ((value >>> 24) & 0xFF));
        stream.write((byte) ((value >>> 16) & 0xFF));
        stream.write((byte) ((value >>> 8) & 0xFF));
        stream.write((byte) (value & 0xFF));
    }

    protected static byte[] buildLayerRecordBytesWithExtraData(byte[] extraData) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (BigEndianWriter writer = new BigEndianWriter(stream, true)) {
            writer.writeInt(0);
            writer.writeInt( 0);
            writer.writeInt( 1);
            writer.writeInt(1);
            writer.writeShort((short) 0);
            writer.writeUInt(0x3842494D);
            writer.write("norm".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
            writer.write((byte) 255);
            writer.write((byte) 0);
            writer.write((byte) 0);
            writer.write((byte) 0);
            writer.writeUInt(extraData.length);
            writer.write(extraData);
        }
        return stream.toByteArray();
    }

    protected static byte[] extractImageResourcesSection(byte[] documentBytes) {
        final int headerLength = 26;
        int colorModeLength = toInt32(documentBytes, headerLength);
        int resourcesLengthOffset = headerLength + 4 + colorModeLength;
        int resourcesPayloadLength = toInt32(documentBytes, resourcesLengthOffset);
        int totalSectionLength = 4 + resourcesPayloadLength;
        byte[] sectionBytes = new byte[totalSectionLength];
        System.arraycopy(documentBytes, resourcesLengthOffset, sectionBytes, 0, totalSectionLength);
        return sectionBytes;
    }

    protected static int readLayerInfoLength(byte[] documentBytes) {
        final int headerLength = 26;
        int colorModeLength = toInt32(documentBytes, headerLength);
        int resourcesLengthOffset = headerLength + 4 + colorModeLength;
        int resourcesPayloadLength = toInt32(documentBytes, resourcesLengthOffset);
        int layerAndMaskLengthOffset = resourcesLengthOffset + 4 + resourcesPayloadLength;
        int layerInfoLengthOffset = layerAndMaskLengthOffset + 4;
        return toInt32(documentBytes, layerInfoLengthOffset);
    }

    protected static byte[] readLayerAndMaskTail(byte[] documentBytes) {
        final int headerLength = 26;
        int colorModeLength = toInt32(documentBytes, headerLength);
        int resourcesLengthOffset = headerLength + 4 + colorModeLength;
        int resourcesPayloadLength = toInt32(documentBytes, resourcesLengthOffset);
        int layerAndMaskLengthOffset = resourcesLengthOffset + 4 + resourcesPayloadLength;
        int layerAndMaskLength = toInt32(documentBytes, layerAndMaskLengthOffset);
        int layerInfoLength = readLayerInfoLength(documentBytes);
        int tailOffset = layerAndMaskLengthOffset + 4 + 4 + layerInfoLength;
        int tailLength = (layerAndMaskLengthOffset + 4 + layerAndMaskLength) - tailOffset;

        if (tailLength <= 0) {
            return new byte[0];
        }

        byte[] tailBytes = new byte[tailLength];
        System.arraycopy(documentBytes, tailOffset, tailBytes, 0, tailLength);
        return tailBytes;
    }

    protected static byte[] buildPsbLayerRecordBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (BigEndianWriter writer = new BigEndianWriter(stream, true)) {
            writer.writeInt(0);
            writer.writeInt(0);
            writer.writeInt(1);
            writer.writeInt(1);
            writer.writeUShort((short) 4);
            writer.writeShort((short) -1);
            writer.writeULong(3L);
            writer.writeShort((short) 0);
            writer.writeULong(3L);
            writer.writeShort((short) 1);
            writer.writeULong(3);
            writer.writeShort((short) 2);
            writer.writeULong(3);
            writer.writeUInt(0x3842494D);
            writer.write("norm".getBytes(java.nio.charset.StandardCharsets.US_ASCII));
            writer.write((byte) 200);
            writer.write((byte) 0);
            writer.write((byte) 0);
            writer.write((byte) 0);

            ByteArrayOutputStream extraDataStream = new ByteArrayOutputStream();
            try (BigEndianWriter extraDataWriter = new BigEndianWriter(extraDataStream, true))
            {
                extraDataWriter.writeUInt(0);
                extraDataWriter.writeUInt(0);
                extraDataWriter.writePascalString("Layer 1");
            } catch (PsdSaveException e) {
                throw new RuntimeException(e);
            }
            byte[] extraData = extraDataStream.toByteArray();
            writer.writeUInt(extraData.length);
            writer.write(extraData);
        }
        return stream.toByteArray();
    }

    protected static void logArtifactDirectory(String outputFile) {
        String outputDirectory = Paths.get(outputFile).getParent().toString();
        System.out.println("Saved test artifact directory: " + outputDirectory);
    }

    protected static String sanitizePathSegment(String value) {
        Pattern invalidPattern = Pattern.compile("[^a-zA-Z0-9._-]");
        return invalidPattern.matcher(value).replaceAll("_");
    }

    protected static class TestResourceBlock {
        protected final short resourceId;
        protected final String name;
        protected final byte[] data;

        public TestResourceBlock(short resourceId, String name, byte[] data) {
            this.resourceId = resourceId;
            this.name = name;
            this.data = data;
        }
    }
}
