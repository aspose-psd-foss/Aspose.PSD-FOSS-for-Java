package org.aspose.psd;

import java.util.Objects;

/**
 * Stores a set of four floating-point numbers that represent the location and size of a rectangle.
 */
public final class RectangleF  {

    /**
     * Represents a {@link RectangleF} structure with its properties left uninitialized.
     */
    public static final RectangleF EMPTY = new RectangleF();

    private float x;
    private float y;
    private float width;
    private float height;

    /**
     * Initializes a new instance of the {@link RectangleF} structure with the specified coordinates.
     *
     * @param x      The left coordinate.
     * @param y      The top coordinate.
     * @param width  The rectangle width.
     * @param height The rectangle height.
     */
    public RectangleF(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Initializes a new instance of the {@link RectangleF} structure with all properties set to zero.
     */
    public RectangleF() {
        this(0f, 0f, 0f, 0f);
    }

    /**
     * Gets or sets the x-coordinate of the upper-left corner of this {@link RectangleF}.
     */
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets or sets the y-coordinate of the upper-left corner of this {@link RectangleF}.
     */
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets or sets the width of this {@link RectangleF}.
     */
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Gets or sets the height of this {@link RectangleF}.
     */
    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Gets the y-coordinate of the bottom edge of this {@link RectangleF}.
     */
    public float getBottom() {
        return y + height;
    }

    /**
     * Gets a value indicating whether this {@link RectangleF} has no location or size.
     */
    public boolean isEmpty() {
        return x == 0 && y == 0 && width == 0 && height == 0;
    }

    /**
     * Gets the x-coordinate of the left edge of this {@link RectangleF}.
     */
    public float getLeft() {
        return x;
    }

    /**
     * Gets the x-coordinate of the right edge of this {@link RectangleF}.
     */
    public float getRight() {
        return x + width;
    }

    /**
     * Gets the y-coordinate of the top edge of this {@link RectangleF}.
     */
    public float getTop() {
        return y;
    }

    /**
     * Determines whether the specified rectangle is equal to this rectangle.
     *
     * @param other The rectangle to compare.
     * @return {@code true} if the rectangles are equal; otherwise, {@code false}.
     */
    public boolean equals(RectangleF other) {
        return Float.compare(x, other.x) == 0
                && Float.compare(y, other.y) == 0
                && Float.compare(width, other.width) == 0
                && Float.compare(height, other.height) == 0;
    }

    /**
     * Determines whether the specified object is equal to this rectangle.
     *
     * @param obj The object to compare.
     * @return {@code true} if the object is an equal rectangle; otherwise, {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RectangleF)) {
            return false;
        }
        return equals((RectangleF) obj);
    }

    /**
     * Returns a hash code for this rectangle.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height);
    }

    /**
     * Returns a compact coordinate representation.
     *
     * @return The coordinate representation.
     */
    @Override
    public String toString() {
        return "Left=" + getLeft()
                + ", Top=" + getTop()
                + ", Right=" + getRight()
                + ", Bottom=" + getBottom()
                + ", Width=" + width
                + ", Height=" + height;
    }
}