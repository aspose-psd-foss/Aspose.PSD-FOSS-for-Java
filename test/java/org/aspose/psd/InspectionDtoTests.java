package org.aspose.psd;

import org.aspose.psd.layers.Layer;
import org.aspose.psd.resources.PsdResourceInfo;
import org.aspose.psd.resources.PsdResourceKind;
import org.aspose.psd.sections.ImageDataKind;
import org.aspose.psd.sections.PsdColorDataKind;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class InspectionDtoTests extends PsdTestFixtureBase {

    @Test
    public void loadDocumentDtosReadValues() throws Exception {
        try (PsdImage image = PsdImage.load(
                Paths.get(getTestDataPath("test.psd")).toString())) {

            List<PsdResourceInfo> resources = image.getResources();
            assertEquals(26, resources.size());

            assertEquals(PsdResourceKind.Unknown, resources.get(0).getKind());
            assertEquals(PsdResourceKind.Unknown, resources.get(1).getKind());
            assertEquals(PsdResourceKind.Unknown, resources.get(2).getKind());

            assertNull(resources.get(0).getGlobalAngle());
            assertNull(resources.get(1).getGlobalAngle());
            assertNull(resources.get(2).getGlobalAngle());

            assertFalse(image.hasIccProfile());
            assertNull(image.isIccProfileUntagged());
            assertEquals(0, image.getGlobalAngle());

            assertEquals(PsdColorDataKind.NONE, image.getColorDataInfo().getKind());
            assertEquals(0, image.getColorDataInfo().getRawDataLength());
            assertNull(image.getIndexedPalette());

            assertEquals(ImageDataKind.Rle, image.getImageDataInfo().getKind());
            assertEquals(2, image.getImageDataInfo().getRowLengthFieldSize());
            assertEquals(300, image.getImageDataInfo().getRowByteCounts().size());
            assertTrue(image.getImageDataInfo().getCompressedPayloadLength() > 0);
            assertFalse(image.getImageDataInfo().isUsesPrediction());
        }
    }

    @Test
    public void loadLayerDtosReadValues() throws Exception {
        try (PsdImage image = PsdImage.load(
                Paths.get(getTestDataPath("test.psd")).toString())) {
            Layer layer = image.getLayers()[0];

            assertEquals(4, layer.getChannels().size());
            assertEquals((short) -1, layer.getChannels().get(0).getChannelId());
            assertEquals((short) 0, layer.getChannels().get(1).getChannelId());
            assertEquals((short) 1, layer.getChannels().get(2).getChannelId());
            assertEquals((short) 2, layer.getChannels().get(3).getChannelId());
            assertTrue(layer.getChannels().get(0).getDataLength() > 0L);

            assertFalse(layer.getMaskInfo().isPresent());
            assertEquals(4, layer.getMaskInfo().getRawDataLength());

            assertTrue(layer.getBlendingRangesInfo().isPresent());
            assertEquals(44, layer.getBlendingRangesInfo().getRawDataLength());
        }
    }
}
