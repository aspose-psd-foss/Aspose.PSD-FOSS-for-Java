package samples.basic;

import org.aspose.psd.Image;
import org.aspose.psd.PsdImage;
import org.aspose.psd.layers.Layer;
import samples.common.SamplePathHelper;
import samples.common.SampleOutputHelper;

/**
 * Demonstrates loading a PSD/PSB file and printing document‑level metadata.
 */
public final class Program {

    public static void main(String[] args) {
        String inputPath = SamplePathHelper.resolveInputPath(args);
        if (inputPath == null) {
            printUsage();
            return;
        }
        runDocumentInspection(inputPath);
    }

    private static void runDocumentInspection(String inputPath) {
        try (PsdImage image = (PsdImage) Image.load(inputPath)) {
            printSampleDescription(inputPath);
            printHeaderInfo(image);
            printDocumentState(image);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }

    private static void printSampleDescription(String inputPath) {
        System.out.println("Aspose.PSD Basic Sample");
        System.out.println("Description: Loads a PSD/PSB file and prints document‑level metadata without rendering.");
        System.out.println("Input: " + inputPath);
        System.out.println();
    }

    private static void printHeaderInfo(PsdImage image) {
        System.out.println("Header");
        System.out.println("  Width: " + image.getWidth());
        System.out.println("  Height: " + image.getHeight());
        System.out.println("  Size: " + image.getSize());
        System.out.println("  ChannelsCount: " + image.getChannelsCount());
        System.out.println("  BitsPerChannel: " + image.getBitsPerChannel());
        System.out.println("  ColorMode: " + image.getColorMode());
        System.out.println("  Version: " + image.getVersion());
        System.out.println();
    }

    private static void printDocumentState(PsdImage image) {
        System.out.println("Document State");
        System.out.println("  Layers: " + image.getLayers().length);
        System.out.println("  ImageResources: " + image.getImageResources().length);
        System.out.println("  Compression: " + image.getCompression());
        System.out.println("  IsFlatten: " + image.isFlatten());
        System.out.println("  ActiveLayer: " + (image.getActiveLayer() != null ? image.getActiveLayer().getName() : "<none>"));
    }

    private static void printUsage() {
        System.out.println("Usage: java -cp <classpath> samples.basic.Program [input.psd]");
        System.out.println("If no input path is provided, the sample tries to use the repository test fixture.");
    }
}
