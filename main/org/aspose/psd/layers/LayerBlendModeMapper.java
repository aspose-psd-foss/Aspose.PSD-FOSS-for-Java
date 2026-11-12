package org.aspose.psd.layers;

import java.nio.charset.StandardCharsets;

public final class LayerBlendModeMapper {

    public static final String NORMAL_BLEND_MODE_KEY = "norm";

    private LayerBlendModeMapper() {
    }

    public static BlendMode parseBlendModeKey(byte[] key) {
        if (key == null || key.length < 4) {
            return BlendMode.Normal;
        }

        String modeKey = new String(key, StandardCharsets.US_ASCII);
        switch (modeKey) {
            case NORMAL_BLEND_MODE_KEY:
                return BlendMode.Normal;
            case "mul ":
                return BlendMode.Multiply;
            case "scrn":
                return BlendMode.Screen;
            case "diss":
                return BlendMode.Dissolve;
            case "over":
                return BlendMode.Overlay;
            case "dark":
                return BlendMode.Darken;
            case "lite":
                return BlendMode.Lighten;
            case "div ":
                return BlendMode.ColorDodge;
            case "idiv":
                return BlendMode.ColorBurn;
            case "burn":
                return BlendMode.ColorBurn;
            case "hLit":
            case "hlit":
                return BlendMode.HardLight;
            case "sLit":
            case "slit":
                return BlendMode.SoftLight;
            case "diff":
                return BlendMode.Difference;
            case "smud":
                return BlendMode.Exclusion;
            case "hue ":
                return BlendMode.Hue;
            case "sat ":
                return BlendMode.Saturation;
            case "colr":
                return BlendMode.Color;
            case "lum ":
                return BlendMode.Luminosity;
            case "lbrn":
                return BlendMode.LinearBurn;
            case "lddg":
                return BlendMode.LinearDodge;
            case "vLit":
                return BlendMode.VividLight;
            case "lLit":
                return BlendMode.LinearLight;
            case "pLit":
                return BlendMode.PinLight;
            case "hMix":
                return BlendMode.HardMix;
            case "pass":
                return BlendMode.PassThrough;
            case "dkCl":
                return BlendMode.DarkerColor;
            case "lgCl":
                return BlendMode.LighterColor;
            case "fsub":
                return BlendMode.Subtract;
            case "fdiv":
                return BlendMode.Divide;
            default:
                return BlendMode.Normal;
        }
    }

    public static String getBlendModeKey(BlendMode mode) {
        switch (mode) {
            case Normal:
                return NORMAL_BLEND_MODE_KEY;
            case Multiply:
                return "mul ";
            case Screen:
                return "scrn";
            case Dissolve:
                return "diss";
            case Overlay:
                return "over";
            case Darken:
                return "dark";
            case Lighten:
                return "lite";
            case ColorDodge:
                return "div ";
            case ColorBurn:
                return "idiv";
            case HardLight:
                return "hLit";
            case SoftLight:
                return "sLit";
            case Difference:
                return "diff";
            case Exclusion:
                return "smud";
            case Hue:
                return "hue ";
            case Saturation:
                return "sat ";
            case Color:
                return "colr";
            case Luminosity:
                return "lum ";
            case LinearBurn:
                return "lbrn";
            case LinearDodge:
                return "lddg";
            case VividLight:
                return "vLit";
            case LinearLight:
                return "lLit";
            case PinLight:
                return "pLit";
            case HardMix:
                return "hMix";
            case PassThrough:
                return "pass";
            case DarkerColor:
                return "dkCl";
            case LighterColor:
                return "lgCl";
            case Subtract:
                return "fsub";
            case Divide:
                return "fdiv";
            default:
                return NORMAL_BLEND_MODE_KEY;
        }
    }
}
