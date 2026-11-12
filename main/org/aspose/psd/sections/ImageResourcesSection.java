package org.aspose.psd.sections;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.BigEndianWriter;
import org.aspose.psd.PsdSectionReader;
import org.aspose.psd.resources.UnknownResource;
import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.coreexceptions.PsdSaveException;

public final class ImageResourcesSection {
    public static final ImageResourcesSection EMPTY = new ImageResourcesSection(new byte[0], new UnknownResource[0]);

    private final byte[] rawData;
    private final UnknownResource[] resources;

    private ImageResourcesSection(final byte[] rawData, final UnknownResource[] resources) {
        this.rawData = rawData;
        this.resources = resources;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public UnknownResource[] getResources() {
        return resources;
    }

    public boolean hasResources() {
        return rawData.length > 0 || resources.length > 0;
    }

    public static ImageResourcesSection load(final BigEndianReader reader) throws IOException, PsdLoadException {
        long resourcesLength = reader.readUInt32();
        if (resourcesLength == 0) {
            return EMPTY;
        }

        byte[] rawData = PsdSectionReader.readBytes(reader, resourcesLength, "Image Resources section");
        long resourcesEnd = rawData.length;
        BigEndianReader resourcesReader = new BigEndianReader(new ByteArrayInputStream(rawData));
        List<UnknownResource> resourcesList = new ArrayList<>();

        while (resourcesReader.getPosition() < resourcesEnd) {
            UnknownResource resource = UnknownResource.load(resourcesReader, resourcesEnd);
            if (resource == null) {
                break;
            }
            resourcesList.add(resource);
        }

        return new ImageResourcesSection(rawData, resourcesList.toArray(new UnknownResource[0]));
    }

    public void save(final BigEndianWriter writer) throws IOException, PsdSaveException {
        if (rawData.length > 0) {
            writer.writeUInt(rawData.length);
            writer.write(rawData);
            return;
        }

        if (resources.length == 0) {
            writer.writeUInt(0);
            return;
        }

        // Write resources to a temporary buffer to calculate length without seeking
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        BigEndianWriter tempWriter = new BigEndianWriter(buffer);

        for (UnknownResource resource : resources) {
            tempWriter.writeUInt(0x3842494D);
            tempWriter.writeUShort((short) resource.getResourceId());
            tempWriter.writePascalStringAlignedTo2(resource.getName());
            tempWriter.writeUInt(resource.getData().length);
            tempWriter.write(resource.getData());

            if (resource.getData().length % 2 == 1) {
                tempWriter.write((byte) 0);
            }
        }

        byte[] resourcesBlock = buffer.toByteArray();
        writer.writeUInt(resourcesBlock.length);
        writer.write(resourcesBlock);
    }
}
