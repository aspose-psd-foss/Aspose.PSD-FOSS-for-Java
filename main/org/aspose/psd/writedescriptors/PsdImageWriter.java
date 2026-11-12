package org.aspose.psd.writedescriptors;

import org.aspose.psd.sections.PsdHeader;
import org.aspose.psd.sections.ColorData;
import org.aspose.psd.sections.LayerAndMaskSection;
import org.aspose.psd.sections.PsdImageDocumentState;
import org.aspose.psd.BigEndianWriter;
import org.aspose.psd.BigEndianReader;
import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.coreexceptions.PsdSaveException;
import java.io.IOException;

public class PsdImageWriter {
    // TODO: Implement the writer logic using the imported classes.

    public static void save(PsdImageDocumentState document, java.io.OutputStream stream, boolean leaveOpen) throws java.io.IOException {
        try (BigEndianWriter writer = new BigEndianWriter(stream)) {
            writer.writeUInt(PsdHeader.PSD_SIGNATURE);
            document.getHeader().save(writer);
            document.getColorData().save(writer);
            try {
                document.getImageResourcesSection().save(writer);
            } catch (PsdSaveException e) {
                throw new IOException(e);
            }
            document.getLayerAndMaskSection().save(writer, document.getHeader().isLargeDocument());
            document.getImageData().save(writer);
        } catch (PsdSaveException e) {
            throw new RuntimeException(e);
        }
    }
}
