package samples.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides shared path resolution helpers for sample projects.
 */
public final class SamplePathHelper {

    // Repository root is assumed to be two levels up from this src directory (src/main/java -> project root)
    private static final Path REPOSITORY_ROOT = Paths.get("..", "..").toAbsolutePath().normalize();

    /**
     * Resolves the input PSD path from command-line arguments or the repository fixture.
     *
     * @param args command‑line arguments
     * @return absolute input path or {@code null} if none found
     */
    public static String resolveInputPath(String[] args) {
        if (args.length > 0) {
            Path candidate = Paths.get(args[0]);
            if (Files.exists(candidate)) {
                return candidate.toAbsolutePath().toString();
            }
        }
        Path fixture = REPOSITORY_ROOT.resolve(Paths.get("testdata", "test.psd"));
        return Files.exists(fixture) ? fixture.toAbsolutePath().toString() : null;
    }

    /**
     * Resolves the output PSD path from command-line arguments or a default file name.
     *
     * @param args                command‑line arguments
     * @param outputArgumentIndex zero‑based index for the output argument
     * @param defaultFileName     default file name when not provided
     * @return absolute output path
     */
    public static String resolveOutputPath(String[] args, int outputArgumentIndex, String defaultFileName) {
        if (args.length > outputArgumentIndex && args[outputArgumentIndex] != null && !args[outputArgumentIndex].trim().isEmpty()) {
            return Paths.get(args[outputArgumentIndex]).toAbsolutePath().toString();
        }
        Path outputDir = REPOSITORY_ROOT.resolve("tmp");
        try {
            Files.createDirectories(outputDir);
        } catch (Exception ignored) {}
        return outputDir.resolve(defaultFileName).toAbsolutePath().toString();
    }
}
