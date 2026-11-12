package org.aspose.psd.layers;

import org.aspose.psd.StreamContainer;

/**
 * Represents a PSD layer resource.
 */
public abstract class LayerResource {
    /**
     * The common layer resource signature.
     */
    public static final int RESOURCE_SIGNATURE = 0x3842494D;

    /**
     * The PSB-specific layer resource signature.
     */
    public static final int PSB_RESOURCE_SIGNATURE = 0x38425053;

    /**
     * Gets the layer resource key.
     */
    public abstract int getKey();

    /**
     * Gets the layer resource length in bytes.
     */
    public abstract int getLength();

    /**
     * Gets the minimal PSD version required for the layer resource.
     */
    public int getPsdVersion() {
        return 0;
    }

    /**
     * Gets the layer resource signature.
     */
    public int getSignature() {
        return RESOURCE_SIGNATURE;
    }

    /**
     * Saves the layer resource to the specified stream container.
     *
     * @param streamContainer The stream container to save to.
     * @param psdVersion The PSD version.
     */
    public abstract void save(StreamContainer streamContainer, int psdVersion);
}
