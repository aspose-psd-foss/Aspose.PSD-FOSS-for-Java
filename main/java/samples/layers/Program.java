package samples.layers;

import org.aspose.psd.Image;
import org.aspose.psd.PsdImage;
import org.aspose.psd.layers.Layer;
import org.aspose.psd.layers.BlendMode;
import samples.common.SamplePathHelper;
import samples.common.SampleOutputHelper;

/**
 * Demonstrates loading a PSD/PSB file and printing supported layer metadata.
 */
public final class Program {

    public static void main(String[] args) {
        String inputPath = SamplePathHelper.resolveInputPath(args);
        if (inputPath == null) {
            printUsage();
            return;
        }
        runLayerInspection(inputPath);
    }

    private static void runLayerInspection(String inputPath) {
        try (PsdImage image = (PsdImage) Image.load(inputPath)) {
            printSampleDescription(inputPath, image.getLayers().length);
            for (int i = 0; i < image.getLayers().length; i++) {
                printLayer(image.getLayers()[i], i);
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }

    private static void printSampleDescription(String inputPath, int layerCount) {
        System.out.println("Aspose.PSD Layer Sample");
        System.out.println("Description: Loads a PSD/PSB file and prints supported layer metadata.");
        System.out.println("Input: " + inputPath);
        System.out.println("LayerCount: " + layerCount);
        System.out.println();
    }

    private static void printLayer(Layer layer, int index) {
        System.out.println("Layer [" + index + "]");
        System.out.println("  Name: " + layer.getName());
        System.out.println("  Bounds: " + layer.getBounds());
        System.out.println("  Width: " + layer.getWidth());
        System.out.println("  Height: " + layer.getHeight());
        System.out.println("  Top: " + layer.getTop());
        System.out.println("  Left: " + layer.getLeft());
        System.out.println("  Bottom: " + layer.getBottom());
        System.out.println("  Right: " + layer.getRight());
        System.out.println("  IsVisible: " + layer.isVisible());
        System.out.println("  Opacity: " + (layer.getOpacity() & 0xFF));
        System.out.println("  Clipping: " + layer.getClipping());
        System.out.println("  BlendModeKey: " + layer.getBlendModeKey());
        System.out.println("  ChannelsCount: " + layer.getChannelsCount());
        System.out.println("  HasLayerMaskData: " + (layer.getLayerMaskData() != null));
        System.out.println("  BlendingRangesLength: " + layer.getLayerBlendingRangesData().getLength());
        System.out.println();
    }

    private static void printUsage() {
        System.out.println("Usage: java -cp <classpath> samples.layers.Program [input.psd]");
        System.out.println("If no input path is provided, the sample tries to use the repository test fixture.");
    }
}
