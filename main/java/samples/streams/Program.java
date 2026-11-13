package samples.streams;

import org.aspose.psd.Image;
import org.aspose.psd.PsdImage;
import samples.common.SamplePathHelper;
import samples.common.SampleOutputHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Demonstrates loading a PSD/PSB from a stream and saving it to another stream.
 */
public final class Program {

    public static void main(String[] args) {
        String inputPath = SamplePathHelper.resolveInputPath(args);
        if (inputPath == null) {
            printUsage();
            return;
        }
        String outputPath = SamplePathHelper.resolveOutputPath(args, 1, "streams-sample-output.psd");
        runStreamRoundTrip(inputPath, outputPath);
    }

    private static void runStreamRoundTrip(String inputPath, String outputPath) {
        try {
            byte[] inputBytes = Files.readAllBytes(Paths.get(inputPath));
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes)) {
                long originalPosition = inputStream.available();
                try (PsdImage image = (PsdImage) Image.load(inputStream)) {
                    long currentPosition = inputStream.available();
                    printSampleDescription(inputPath, outputPath, inputBytes.length, originalPosition, currentPosition);
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        image.save(outputStream);
                        Files.write(Paths.get(outputPath), outputStream.toByteArray());
                    }
                }
            }
            System.out.println("SavedThroughStream: " + outputPath);
            SampleOutputHelper.openOutputFolder(outputPath);
        } catch (IOException e) {
            System.out.println("Error during stream round‑trip: " + e.getMessage());
        }
    }

    private static void printSampleDescription(String inputPath, String outputPath, int inputByteCount,
                                               long originalPosition, long currentPosition) {
        System.out.println("Aspose.PSD Stream Sample");
        System.out.println("Description: Demonstrates Load(Stream) and Save(Stream) using an in‑memory round‑trip without rendering.");
        System.out.println("Input: " + inputPath);
        System.out.println("Output: " + outputPath);
        System.out.println("InputBytes: " + inputByteCount);
        System.out.println("OriginalStreamPosition: " + originalPosition);
        System.out.println("StreamPositionAfterLoad: " + currentPosition);
        System.out.println();
    }

    private static void printUsage() {
        System.out.println("Usage: java -cp <classpath> samples.streams.Program [input.psd] [output.psd]");
        System.out.println("If no input is provided, the repository fixture is used. If no output is provided, a file in the tmp folder is used.");
    }
}
