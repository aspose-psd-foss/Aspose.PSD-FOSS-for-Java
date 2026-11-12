package org.aspose.psd.layers;

/**
 * Defines the blend modes used for layers in PSD files.
 */
public enum BlendMode {
    /**
     * Normal blend mode (no blending).
     */
    Normal(1852797549L),

    /**
     * Multiply blend mode (multiply colors, darkens image).
     */
    Multiply(1836411936L),

    /**
     * Screen blend mode (screen colors, lightens image).
     */
    Screen(1935897198L),

    /**
     * Overlay blend mode (combines multiply and screen).
     */
    Overlay(1870030194L),

    /**
     * Darken blend mode (keeps darker pixels).
     */
    Darken(1684107883L),

    /**
     * Lighten blend mode (keeps lighter pixels).
     */
    Lighten(1818850405L),

    /**
     * Color Dodge blend mode (dodges colors).
     */
    ColorDodge(1684633120L),

    /**
     * Color Burn blend mode (burns colors).
     */
    ColorBurn(1768188278L),

    /**
     * Hard Light blend mode (hard light blending).
     */
    HardLight(1749838196L),

    /**
     * Soft Light blend mode (soft light blending).
     */
    SoftLight(1934387572L),

    /**
     * Difference blend mode (subtract colors).
     */
    Difference(1684629094L),

    /**
     * Exclusion blend mode (exclude colors).
     */
    Exclusion(1936553316L),

    /**
     * Hue blend mode (apply hue).
     */
    Hue(1752524064L),

    /**
     * Saturation blend mode (apply saturation).
     */
    Saturation(1935766560L),

    /**
     * Color blend mode (apply color).
     */
    Color(1668246642L),

    /**
     * Luminosity blend mode (apply luminosity).
     */
    Luminosity(1819634976L),

    /**
     * Darker Color blend mode (darker of colors).
     */
    DarkerColor(1684751212L),

    /**
     * Lighter Color blend mode (lighter of colors).
     */
    LighterColor(1818706796L),

    /**
     * Linear Burn blend mode (linear burn).
     */
    LinearBurn(1818391150L),

    /**
     * Linear Dodge blend mode (linear dodge).
     */
    LinearDodge(1818518631L),

    /**
     * Linear Light blend mode (linear light).
     */
    LinearLight(1816947060L),

    /**
     * Vivid Light blend mode (vivid light).
     */
    VividLight(1984719220L),

    /**
     * Pin Light blend mode (pin light).
     */
    PinLight(1884055924L),

    /**
     * Hard Mix blend mode (hard mix).
     */
    HardMix(1749903736L),

    /**
     * Subtract blend mode (subtract colors).
     */
    Subtract(1718842722L),

    /**
     * Divide blend mode (divide colors).
     */
    Divide(1717856630L),

    /**
     * Dissolve blend mode.
     */
    Dissolve(1684632435L),

    /**
     * Pass-through blend mode.
     */
    PassThrough(1885434739L),

    /**
     * Blend mode is absent or not set yet.
     */
    Absent(0L);

    private final long value;

    BlendMode(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
