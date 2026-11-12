package org.aspose.psd;

import java.util.Objects;

/**
 * Represents an ordered pair of integer x- and y-coordinates that defines a point in a two-dimensional plane.
 */
public final class Point {
    /**
     * Represents a {@link Point} with coordinates left uninitialized.
     */
    public static final Point EMPTY = new Point();

    private int x;
    private int y;

    /**
     * Initializes a new instance of the {@link Point} structure with the specified coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Default constructor leaving coordinates uninitialized (0,0).
     */
    public Point() {
    }

    /**
     * Gets the x-coordinate of this {@link Point}.
     *
     * @return the x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of this {@link Point}.
     *
     * @param x the x-coordinate to set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of this {@link Point}.
     *
     * @return the y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of this {@link Point}.
     *
     * @param y the y-coordinate to set.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets a value indicating whether this {@link Point} has coordinates left uninitialized.
     *
     * @return {@code true} if both coordinates are zero; otherwise, {@code false}.
     */
    public boolean isEmpty() {
        return x == 0 && y == 0;
    }

    /**
     * Determines whether the specified point is equal to this point.
     *
     * @param other The point to compare.
     * @return {@code true} if the points are equal; otherwise, {@code false}.
     */
    public boolean equals(Point other) {
        if (other == null) {
            return false;
        }
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Point && equals((Point) obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "X=" + x + ", Y=" + y;
    }
}
