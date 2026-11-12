package org.aspose.psd;

import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.resources.ImageResourceIds;
import org.aspose.psd.resources.PsdResourceKind;
import org.aspose.psd.resources.UnknownResource;
import org.aspose.psd.sections.ImageResourcesSection;
import org.aspose.psd.sections.SeekOrigin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Port of {@code ImageResourcesSectionTests} from the C# test suite.
 *
 * <p>Tests covering the Image Resources section of PSD/PSB files: raw block
 * loading, round-tripping, malformed-envelope rejection, and byte-exact saves.</p>
 */
public final class ImageResourcesSectionTests extends PsdTestFixtureBase {

    // ---------------------------------------------------------------------
    // Load tests
    // ---------------------------------------------------------------------

    /**
     * Tests that image resources are loaded as unknown blocks without
     * ID-specific semantics.
     */
    @Test
    public void load_Resources_ReadsBlocks() throws IOException {

        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{0x00, 0x00, 0x00, 0x2D}),
                new TestResourceBlock(ImageResourceIds.ICC_PROFILE, "icc", new byte[]{0x49, 0x43, 0x43, 0x50}),
                new TestResourceBlock(ImageResourceIds.ICC_UNTAGGED_PROFILE, "", new byte[]{0x01})
        );

        byte[] resourcesPayload = buildResourcesPayload(blocks);

        try (InputStream stream = new ByteArrayInputStream(resourcesPayload);
             BigEndianReader reader = new BigEndianReader(stream, /*leaveOpen=*/true)) {

            long length = resourcesPayload.length;
            List<UnknownResource> resources = new ArrayList<>();
            while (reader.getPosition() < length) {
                UnknownResource resource = UnknownResource.load(reader, length);
                assertNotNull(resource);
                resources.add(resource);
            }

            assertEquals(3, resources.size());
            assertEquals(ImageResourceIds.GLOBAL_ANGLE, resources.get(0).getResourceId());
            assertArrayEquals(new byte[]{0x00, 0x00, 0x00, 0x2D}, resources.get(0).getData());
            assertEquals("glba", resources.get(0).getName());

            assertEquals(ImageResourceIds.ICC_PROFILE, resources.get(1).getResourceId());
            assertArrayEquals(new byte[]{0x49, 0x43, 0x43, 0x50}, resources.get(1).getData());
            assertEquals("icc", resources.get(1).getName());

            assertEquals(ImageResourceIds.ICC_UNTAGGED_PROFILE, resources.get(2).getResourceId());
            assertArrayEquals(new byte[]{0x01}, resources.get(2).getData());
            assertEquals("", resources.get(2).getName());
        } catch (PsdLoadException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests that saving a document with image resources preserves the raw
     * Image Resources section bytes.
     */
    @Test
    public void save_Resources_PreservesRaw() throws IOException {
        String testFile = getTestDataPath("test.psd");
        byte[] originalBytes = Files.readAllBytes(Path.of(testFile));
        String outputFile = getPersistentArtifactPath("resources_roundtrip_test.psd");

        try (PsdImage image = PsdImage.load(testFile)) {
            image.save(outputFile);
        }
        logArtifactDirectory(outputFile);

        byte[] savedBytes = Files.readAllBytes(Path.of(outputFile));
        byte[] originalResourcesSection = extractImageResourcesSection(originalBytes);
        byte[] savedResourcesSection = extractImageResourcesSection(savedBytes);

        assertArrayEquals(originalResourcesSection, savedResourcesSection);

        try (PsdImage reloaded = PsdImage.load(outputFile)) {
            assertTrue(reloaded.hasImageResources());
            assertEquals(26, reloaded.getResourceCount());
        }
    }

    /**
     * Tests that the resources fixture exposes unknown resource summaries
     * and expected layer presence.
     */
    @Test
    public void load_ResourcesFixture_Reads() throws IOException {
        try (PsdImage image = PsdImage.load(getTestDataPath("resources.psd"))) {
            assertEquals(28, image.getResourceCount());
            assertEquals(PsdResourceKind.Unknown, image.getResources().get(0).getKind());
            assertTrue(image.hasImageResources());
            assertEquals(3, image.getLayers().length);
        }
    }

    /**
     * Tests that saving the resources fixture preserves the raw Image
     * Resources section bytes.
     */
    @Test
    public void save_ResourcesFixture_Raw() throws IOException {
        String testFile = getTestDataPath("resources.psd");
        byte[] originalBytes = Files.readAllBytes(Path.of(testFile));
        String outputFile = getPersistentArtifactPath("resources_fixture_roundtrip_test.psd");

        try (PsdImage image = PsdImage.load(testFile)) {
            image.save(outputFile);
        }
        logArtifactDirectory(outputFile);

        byte[] savedBytes = Files.readAllBytes(Path.of(outputFile));
        assertArrayEquals(extractImageResourcesSection(originalBytes),
                extractImageResourcesSection(savedBytes));
    }

    // ---------------------------------------------------------------------
    // Malformed envelope tests
    // ---------------------------------------------------------------------

    /**
     * Rejects a truncated resource signature or resource ID at the block offset.
     */
    @ParameterizedTest(name = "availableBytes={0}, reason={1}")
    @CsvSource({
            "1, signature",
            "2, signature",
            "3, signature",
            "4, header",
            "5, header"
    })
    public void load_Resources_TruncatedHeader_Throws(int availableBytes, String reason) {

        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1})
        );

        byte[] block = buildResourcesPayload(blocks);

        block = Arrays.copyOf(block, availableBytes);
        assertMalformedBlock(block, block.length, 0, reason);
    }

    /**
     * Rejects a complete but invalid signature without another damaged field.
     */
    @Test
    public void load_Resources_InvalidSignature_Throws() {
        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1})
        );

        byte[] block = buildResourcesPayload(blocks);
        block[0] = 0;
        assertMalformedBlock(block, block.length, 0, "signature");
    }

    /**
     * Rejects a missing Pascal name length byte after a complete resource ID.
     */
    @Test
    public void load_Resources_MissingNameLength_Throws() {
        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1})
        );

        byte[] block = buildResourcesPayload(blocks);
        block = Arrays.copyOf(block, 6);
        assertMalformedBlock(block, block.length, 0, "name length");
    }

    /**
     * Rejects a Pascal name that crosses the logical section boundary.
     */
    @Test
    public void load_Resources_NameCrossesSection_Throws() {
        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1})
        );

        byte[] block = buildResourcesPayload(blocks);
        assertMalformedBlock(block, 9, 0, "name");
    }

    /**
     * Rejects missing 2-byte alignment after an even-length Pascal name.
     */
    @Test
    public void load_Resources_MissingNamePadding_Throws() {
        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1})
        );

        byte[] block = buildResourcesPayload(blocks);
        assertMalformedBlock(block, 11, 0, "name padding");
    }

    /**
     * Rejects a payload length field that crosses the section boundary.
     */
    @ParameterizedTest(name = "lengthBytes={0}")
    @CsvSource({"1", "2", "3"})
    public void load_Resources_TruncatedLength_Throws(int lengthBytes) {
        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1})
        );

        byte[] block = buildResourcesPayload(blocks);
        assertMalformedBlock(block, 12 + lengthBytes, 0, "payload length");
    }

    /**
     * Rejects a negative signed big-endian payload length.
     */
    @Test
    public void load_Resources_NegativeLength_Throws() {
        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1})
        );

        byte[] block = buildResourcesPayload(blocks);
        writeUInt32BigEndian(block, 12, 0xFFFFFFFFL);
        assertMalformedBlock(block, block.length, 0, "negative payload length");
    }

    /**
     * Rejects a declared payload length beyond the logical section boundary.
     */
    @Test
    public void load_Resources_PayloadCrossesSection_Throws() {
        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1})
        );

        byte[] block = buildResourcesPayload(blocks);
        assertMalformedBlock(block, 12, 0, "payload length");
        assertMalformedBlock(block, 16, 0, "payload");
    }

    /**
     * Rejects a physically truncated payload even when the declared section
     * end is larger.
     */
    @Test
    public void load_Resources_PhysicallyTruncatedPayload_Throws() {
        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1, 2, 3, 4})
        );

        byte[] block = buildResourcesPayload(blocks);
        block = Arrays.copyOf(block, 18);
        assertMalformedBlock(block, 20, 0, "payload");
    }

    /**
     * Rejects a missing alignment byte after a one-byte payload.
     */
    @Test
    public void load_Resources_MissingPayloadPadding_Throws() {
        List<TestResourceBlock> blocks = Arrays.asList(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "x", new byte[]{1})
        );
        byte[] block = buildResourcesPayload(blocks);
        block = Arrays.copyOf(block, block.length - 1);
        assertMalformedBlock(block, block.length, 0, "payload padding");
    }

    /**
     * Reports the start of the second block after a valid first block.
     */
    @Test
    public void load_Resources_SecondBlockOffset_Throws() throws IOException {
        byte[] first = buildResourcesPayload(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, "glba", new byte[]{1}));
        byte[] second = buildResourcesPayload(
                new TestResourceBlock(ImageResourceIds.ICC_PROFILE, "icc", new byte[]{2}));
        second[0] = 0;
        byte[] section = new byte[4 + first.length + second.length];
        writeUInt32BigEndian(section, 0, first.length + second.length);
        System.arraycopy(first, 0, section, 4, first.length);
        System.arraycopy(second, 0, section, 4 + first.length, second.length);

        try (InputStream stream = new ByteArrayInputStream(section);
             BigEndianReader reader = new BigEndianReader(stream, /*leaveOpen=*/true)) {

            PsdLoadException exception = assertThrows(PsdLoadException.class,
                    () -> ImageResourcesSection.load(reader));
            assertTrue(exception.getMessage().contains("offset " + first.length),
                    "expected message to contain 'offset " + first.length + "', was: "
                            + exception.getMessage());
            assertTrue(exception.getMessage().contains("signature"),
                    "expected message to contain 'signature', was: " + exception.getMessage());
        }
    }

    // ---------------------------------------------------------------------
    // Maximum-length name
    // ---------------------------------------------------------------------

    /**
     * Accepts the maximum-length Pascal resource name without crossing the
     * section boundary.
     */
    @Test
    public void load_Resources_MaximumLengthName_ReadsBlock() throws IOException {
        char[] chars = new char[255];
        Arrays.fill(chars, 'n');
        String name = new String(chars);
        byte[] block = buildResourcesPayload(
                new TestResourceBlock(ImageResourceIds.GLOBAL_ANGLE, name, new byte[]{1}));

        try (InputStream stream = new ByteArrayInputStream(block);
             BigEndianReader reader = new BigEndianReader(stream, /*leaveOpen=*/true)) {

            UnknownResource resource = UnknownResource.load(reader, block.length);

            assertEquals(name, resource.getName());
            assertArrayEquals(new byte[]{1}, resource.getData());
            assertEquals(block.length, reader.getPosition());
        } catch (PsdLoadException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------------------------------------------------------
    // Malformed resource in document
    // ---------------------------------------------------------------------

    /**
     * Rejects a malformed resource block through the public PSD and PSB load
     * paths.
     */
    @ParameterizedTest(name = "fileName={0}")
    @CsvSource({"test.psd", "basic.psb"})
    public void load_MalformedResourceInDocument_Throws(String fileName) throws IOException {
        byte[] document = Files.readAllBytes(Path.of(getTestDataPath(fileName)));
        int colorModeLength = BigEndianBitConverter.toInt32(document, 26);
        int sectionLengthOffset = 30 + colorModeLength;
        int sectionLength = BigEndianBitConverter.toInt32(document, sectionLengthOffset);
        int blockOffset = sectionLengthOffset + 4;
        assertTrue(sectionLength >= 4);
        assertEquals((byte) '8', document[blockOffset]);
        assertEquals((byte) 'B', document[blockOffset + 1]);
        document[blockOffset] = 0;

        try (InputStream stream = new ByteArrayInputStream(document)) {
            PsdLoadException exception = assertThrows(PsdLoadException.class,
                    () -> {
                        try (PsdImage image = PsdImage.load(stream)) {
                            // no-op; loading should throw
                        }
                    });
            assertTrue(exception.getMessage().contains("offset 0"),
                    "expected message to contain 'offset 0', was: " + exception.getMessage());
            assertTrue(exception.getMessage().contains("signature"),
                    "expected message to contain 'signature', was: " + exception.getMessage());
        }
    }

    // ---------------------------------------------------------------------
    // Byte-exact save
    // ---------------------------------------------------------------------

    /**
     * Preserves the full PSD and PSB bytes when saving without mutation.
     */
    @ParameterizedTest(name = "fileName={0}")
    @CsvSource({"resources.psd", "basic.psb"})
    public void save_Resources_ByteExact(String fileName) throws Exception {
        assertByteExactRoundTrip(fileName);
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    /**
     * Asserts the error type, block offset, and damaged envelope field.
     *
     * @param bytes       the physical bytes of the resource section
     * @param sectionEnd  the declared end of the resource section
     * @param blockOffset the start of the damaged block
     * @param reason      the field named in the error
     */
    private static void assertMalformedBlock(byte[] bytes, long sectionEnd,
                                             long blockOffset, String reason) {
        try (InputStream stream = new ByteArrayInputStream(bytes);
             BigEndianReader reader = new BigEndianReader(stream, /*leaveOpen=*/true)) {

            reader.seek(blockOffset, SeekOrigin.BEGIN);
            PsdLoadException exception = assertThrows(PsdLoadException.class,
                    () -> UnknownResource.load(reader, sectionEnd));
            assertTrue(exception.getMessage().contains("offset " + blockOffset),
                    "expected message to contain 'offset " + blockOffset + "', was: "
                            + exception.getMessage());
            assertTrue(exception.getMessage().contains(reason),
                    "expected message to contain '" + reason + "', was: "
                            + exception.getMessage());
        } catch (IOException e) {
            throw new AssertionError("Unexpected I/O failure in assertMalformedBlock", e);
        }
    }
}