package org.aspose.psd.resources;

import org.aspose.psd.PsdVersion;
import org.aspose.psd.ResourceBlock;

import java.io.OutputStream;

/**
 * Adapts a raw-preserved image resource to the official {@link ResourceBlock} surface.
 */
public final class PreservedResourceBlock extends ResourceBlock {

    /**
     * Stores the preserved resource payload.
     */
    private final byte[] data;

    /**
     * Initializes a new instance of the {@link PreservedResourceBlock} class.
     *
     * @param resource the parsed raw resource block
     */
    public PreservedResourceBlock(UnknownResource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("resource must not be null");
        }
        setId((short) resource.getResourceId());
        setName(resource.getName());
        byte[] src = resource.getData();
        // Defensive copy so the block is immutable; drop the clone if aliasing is desired.
        this.data = (src != null) ? src.clone() : new byte[0];
    }

    /**
     * Gets the resource data size in bytes.
     */
    @Override
    public int getDataSize() {
        return data.length;
    }

    /**
     * Gets the minimal required PSD version.
     */
    @Override
    public int getMinimalVersion() {
        return PsdVersion.PSD.getValue();
    }

    /**
     * Saves the resource block to the specified stream container.
     *
     * @param stream the stream container to save to
     * @throws UnsupportedOperationException always, in this FOSS build
     */
    @Override
    public void save(OutputStream stream) {
        throw new UnsupportedOperationException(
                "Saving individual image resource blocks is not supported by this FOSS build.");
    }
}