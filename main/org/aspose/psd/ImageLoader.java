package org.aspose.psd;

import java.io.InputStream;
import java.io.IOException;
import org.aspose.psd.PsdImage;
import org.aspose.psd.coreexceptions.PsdLoadException;

/**
 * Provides the Load functionality for Images
 */
public abstract class ImageLoader {
    /**
     * Loads a new image from the specified file path.
     *
     * @param filePath The file path to load image from.
     * @return The loaded image.
     * @throws IOException if an I/O error occurs while loading
     */
    public static Object load(String filePath) throws IOException {
        return PsdImage.load(filePath);
    }

    /**
     * Loads a new image from the specified stream.
     *
     * @param stream The stream to load image from.
     * @return The loaded image.
     * @throws IOException if an I/O error occurs while loading
     */
    public static Object load(InputStream stream) throws IOException, PsdLoadException {
        return PsdImage.load(stream);
    }
}
