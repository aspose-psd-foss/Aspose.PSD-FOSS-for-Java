package org.aspose.psd;

import java.util.Objects;

/**
 * Represents an ordered pair of integer width and height values that defines a size.
 */
public class Size {
    /**
     * Represents a {@link Size} with width and height left uninitialized.
     */
    public static final Size EMPTY = new Size();

    private int width;
    private int height;

    /**
     * Initializes a new instance of the {@link Size} class with the specified dimensions.
     *
     * @param width  The width component.
     * @param height The height component.
     */
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Default constructor creates a size with width and height set to 0.
     */
    public Size() {
        this.width = 0;
        this.height = 0;
    }

    /**
     * Gets the width component of this {@link Size}.
     *
     * @return the width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width component of this {@link Size}.
     *
     * @param width the width to set.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height component of this {@link Size}.
     *
     * @return the height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height component of this {@link Size}.
     *
     * @param height the height to set.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets a value indicating whether this {@link Size} has width and height left uninitialized.
     *
     * @return {@code true} if width and height are both zero; otherwise {@code false}.
     */
    public boolean isEmpty() {
        return width == 0 && height == 0;
    }

    /**
     * Determines whether the specified size is equal to this size.
     *
     * @param other the size to compare.
     * @return {@code true} if the sizes are equal; otherwise {@code false}.
     */
    public boolean equals(Size other) {
        if (other == null) {
            return false;
        }
        return this.width == other.width && this.height == other.height;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Size && equals((Size) obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return "{Width=" + width + ", Height=" + height + "}";
    }
}
