package org.aspose.psd.resources;

/**
 * Provides a read-only summary of one parsed PSD image resource block.
 */
public final class PsdResourceInfo {
    private final short resourceId;
    private final String name;
    private final PsdResourceKind kind;
    private final int dataLength;
    private final Integer globalAngle;
    private final Boolean isIccProfileUntagged;

    /**
     * Initializes a new instance of the {@code PsdResourceInfo} class.
     *
     * @param resourceId               The PSD resource identifier.
     * @param name                     The decoded Pascal resource name.
     * @param kind                     The semantic classification exposed for the resource.
     * @param dataLength               The raw payload length in bytes.
     * @param globalAngle              The parsed global angle, when available.
     * @param isIccProfileUntagged    The parsed untagged-profile flag, when available.
     */
    public PsdResourceInfo(short resourceId, String name, PsdResourceKind kind, int dataLength, Integer globalAngle, Boolean isIccProfileUntagged) {
        this.resourceId = resourceId;
        this.name = name;
        this.kind = kind;
        this.dataLength = dataLength;
        this.globalAngle = globalAngle;
        this.isIccProfileUntagged = isIccProfileUntagged;
    }

    /**
     * Gets the PSD resource identifier.
     *
     * @return the resource identifier
     */
    public short getResourceId() {
        return resourceId;
    }

    /**
     * Gets the decoded Pascal resource name.
     *
     * @return the resource name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the semantic classification exposed for the resource.
     *
     * @return the resource kind
     */
    public PsdResourceKind getKind() {
        return kind;
    }

    /**
     * Gets the raw payload length in bytes.
     *
     * @return the data length
     */
    public int getDataLength() {
        return dataLength;
    }

    /**
     * Gets the parsed global angle, when this resource carries that value.
     * The current unknown-only parser leaves this value unset.
     *
     * @return the global angle, or {@code null} if not set
     */
    public Integer getGlobalAngle() {
        return globalAngle;
    }

    /**
     * Gets the parsed untagged-profile flag, when this resource carries that value.
     * The current unknown-only parser leaves this value unset.
     *
     * @return the ICC profile untagged flag, or {@code null} if not set
     */
    public Boolean getIsIccProfileUntagged() {
        return isIccProfileUntagged;
    }

    /**
     * Initializes a new instance with minimal data for unknown resources.
     */
    public PsdResourceInfo(int resourceId, String name, byte[] data) {
        this((short) resourceId, name, PsdResourceKind.Unknown, data != null ? data.length : 0, null, null);
    }
}