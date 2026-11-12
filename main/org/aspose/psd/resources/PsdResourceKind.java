package org.aspose.psd.resources;

/**
 * Identifies the semantic classification exposed for an image resource block.
 */
public enum PsdResourceKind {
    /**
     * The resource is not parsed semantically and remains raw-preserved only.
     */
    Unknown,

    /**
     * Reserved for future semantic parsing of the global layer-effects lighting angle resource.
     */
    GlobalAngle,

    /**
     * Reserved for future semantic parsing of an embedded ICC profile payload.
     */
    IccProfile,

    /**
     * Reserved for future semantic parsing of the intentionally-untagged ICC profile flag.
     */
    IccUntaggedProfile
}
