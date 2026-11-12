package org.aspose.psd;

import java.io.OutputStream;

/**
 * Represents a PSD image resource block.
 */
public abstract class ResourceBlock {

    /**
     * The regular Photoshop resource signature.
     */
    public static final int RESOURCE_BLOCK_SIGNATURE = 0x3842494D;

    /**
     * The ImageReady resource signature.
     */
    public static final int RESOURCE_BLOCK_ME_SA_SIGNATURE = 0x3842494D;

    private short id;
    private String name = "";

    /**
     * Represents resource block state.
     */
    public enum ResourceBlockState {
        /**
         * The resource block is ready.
         */
        Ready,

        /**
         * The resource block is disposed.
         */
        Disposed
    }

    /**
     * Gets the resource data size in bytes.
     *
     * @return the resource data size in bytes
     */
    public abstract int getDataSize();

    /**
     * Gets the unique identifier for the resource.
     *
     * @return the resource ID
     */
    public short getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the resource.
     *
     * @param id the resource ID
     */
    public void setId(short id) {
        this.id = id;
    }

    /**
     * Gets the minimal required PSD version.
     *
     * @return the minimal required PSD version
     */
    public abstract int getMinimalVersion();

    /**
     * Gets the resource name.
     *
     * @return the resource name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the resource name.
     *
     * @param name the resource name
     */
    public void setName(String name) {
        this.name = (name != null) ? name : "";
    }

    /**
     * Gets the resource signature.
     *
     * @return the resource signature
     */
    public int getSignature() {
        return RESOURCE_BLOCK_SIGNATURE;
    }

    /**
     * Gets the resource block size in bytes including its data.
     *
     * @return the resource block size
     */
    public int getSize() {
        return getDataSize();
    }

    /**
     * Saves the resource block to the specified stream.
     *
     * @param stream the output stream to save to
     */
    public abstract void save(OutputStream stream);

    /**
     * Validates the resource values.
     */
    public void validateValues() {
        // default implementation does nothing
    }
}