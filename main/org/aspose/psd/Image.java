package org.aspose.psd;

import org.aspose.psd.coreexceptions.PsdLoadException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Provides the Aspose.PSD-compatible base image entry point for loading PSD/PSB documents.
 */
public abstract class Image implements AutoCloseable {
    /**
     * Gets the image width in pixels.
     */
    public abstract int getWidth();

    /**
     * Gets the image height in pixels.
     */
    public abstract int getHeight();

    /**
     * Gets the image bounds.
     */
    public Rectangle getBounds() {
        return new Rectangle(0, 0, getWidth(), getHeight());
    }

    /**
     * Loads a new image from the specified file path.
     *
     * @param filePath The file path to load image from.
     * @return The loaded image.
     */
    public static Image load(String filePath) throws IOException {
        return (Image) ImageLoader.load(filePath);
    }

    /**
     * Loads a new image from the specified stream.
     *
     * @param stream The stream to load image from.
     * @return The loaded image.
     */
    public static Image load(InputStream stream) throws IOException, PsdLoadException {
        return (Image) ImageLoader.load(stream);
    }

    /**
     * Saves the image data to the specified file path.
     *
     * @param filePath The destination file path.
     * @throws IOException if an I/O error occurs while saving
     */
    public abstract void save(String filePath) throws IOException;

    /**
     * Saves the image data to the specified stream.
     *
     * @param stream The destination stream.
     * @throws IOException if an I/O error occurs while saving
     */
    public abstract void save(OutputStream stream) throws IOException;

    /**
     * Releases resources used by the image.
     */
    public abstract void dispose();

    @Override
    public void close() {
        dispose();
    }
}