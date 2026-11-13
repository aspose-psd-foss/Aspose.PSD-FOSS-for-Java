package samples.common;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helper for opening the folder that contains a saved output file.
 */
public final class SampleOutputHelper {

    /**
     * Opens the folder (or selects the file on Windows) that contains the given output path.
     *
     * @param outputPath the path to the output PSD or PSB file
     */
    public static void openOutputFolder(String outputPath) {
        try {
            Path fullPath = Paths.get(outputPath).toAbsolutePath();
            File file = fullPath.toFile();
            File directory = file.getParentFile();
            if (directory == null) {
                directory = new File(".");
            }
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null) {
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    try {
                        desktop.browseFileDirectory(file);
                        System.out.println("Opened output folder (selected file): " + directory);
                        return;
                    } catch (Throwable ignored) {
                        // fallback
                    }
                }
                desktop.open(directory);
                System.out.println("Opened output folder: " + directory);
            }
        } catch (IOException e) {
            System.out.println("Failed to open output folder: " + e.getMessage());
        }
    }
}
