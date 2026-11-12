package org.aspose.psd.loaders;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.sections.PsdImageDocumentState;
import org.aspose.psd.sections.ColorData;
import org.aspose.psd.sections.ImageData;
import org.aspose.psd.sections.ImageResourcesSection;
import org.aspose.psd.sections.LayerAndMaskSection;
import org.aspose.psd.sections.PsdHeader;

public final class PsdImageLoader {
    private PsdImageLoader() {
    }

    public static PsdImageDocumentState load(InputStream stream, boolean leaveOpen) throws PsdLoadException {
        try (BigEndianReader reader = new BigEndianReader(stream, leaveOpen)) {
            PsdHeader header = PsdHeader.load(reader);
            ColorData colorData = ColorData.load(reader, header.getColorMode());
            ImageResourcesSection imageResourcesSection = ImageResourcesSection.load(reader);
            LayerAndMaskSection layerAndMaskSection = LayerAndMaskSection.load(reader, header.isLargeDocument());
            ImageData imageData = ImageData.load(
                    reader,
                    header.isLargeDocument(),
                    header.getHeight(),
                    header.getChannels());

            return new PsdImageDocumentState(
                    header,
                    colorData,
                    imageResourcesSection,
                    layerAndMaskSection,
                    imageData);
        } catch (EOFException exception) {
            throw new PsdLoadException("Unexpected end of PSD/PSB data while reading the file structure.", exception);
        } catch (IOException exception) {
            throw new PsdLoadException("Failed to read PSD/PSB data from the source stream.", exception);
        }
    }
}
