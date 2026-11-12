package org.aspose.psd;

import java.util.Objects;

/**
 * Stores a set of four integers that represent the location and size of a rectangle.
 */
public class Rectangle {
    /**
     * Represents a {@link Rectangle} structure with its properties left uninitialized.
     */
    public static final Rectangle EMPTY = new Rectangle();

    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * Initializes a new instance of the {@link Rectangle} structure with the specified location and size.
     *
     * @param location The upper-left corner of the rectangle.
     * @param size     The size of the rectangle.
     */
    public Rectangle(Point location, Size size) {
        this(location.getX(), location.getY(), size.getWidth(), size.getHeight());
    }

    /**
     * Initializes a new instance of the {@link Rectangle} structure with the specified coordinates.
     *
     * @param x      The left coordinate.
     * @param y      The top coordinate.
     * @param width  The rectangle width.
     * @param height The rectangle height.
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBottom() {
        return y + height;
    }

    public void setBottom(int bottom) {
        this.height = bottom - y;
    }

    public boolean isEmpty() {
        return x == 0 && y == 0 && width == 0 && height == 0;
    }

    public int getLeft() {
        return x;
    }

    public void setLeft(int left) {
        int right = getRight();
        this.x = left;
        this.width = right - left;
    }

    public Point getLocation() {
        return new Point(x, y);
    }

    public void setLocation(Point location) {
        this.x = location.getX();
        this.y = location.getY();
    }

    public int getRight() {
        return x + width;
    }

    public void setRight(int right) {
        this.width = right - x;
    }

    public Size getSize() {
        return new Size(width, height);
    }

    public void setSize(Size size) {
        this.width = size.getWidth();
        this.height = size.getHeight();
    }

    public int getTop() {
        return y;
    }

    public void setTop(int top) {
        int bottom = getBottom();
        this.y = top;
        this.height = bottom - top;
    }

    /**
     * Converts a {@link RectangleF} structure to a {@link Rectangle} structure by rounding up the coordinates and size.
     *
     * @param value The rectangle to convert.
     * @return The converted rectangle.
     */
    public static Rectangle ceiling(RectangleF value) {
        return new Rectangle(
                (int) Math.ceil(value.getX()),
                (int) Math.ceil(value.getY()),
                (int) Math.ceil(value.getWidth()),
                (int) Math.ceil(value.getHeight()));
    }

    /**
     * Creates a rectangle from edge coordinates.
     *
     * @param left   The left edge.
     * @param top    The top edge.
     * @param right  The right edge.
     * @param bottom The bottom edge.
     * @return The rectangle.
     */
    public static Rectangle fromLeftTopRightBottom(int left, int top, int right, int bottom) {
        return new Rectangle(left, top, right - left, bottom - top);
    }

    /**
     * Creates a rectangle from edge coordinates for internal PSD record parsing.
     *
     * @param left   The left edge.
     * @param top    The top edge.
     * @param right  The right edge.
     * @param bottom The bottom edge.
     * @return The rectangle.
     */
    public static Rectangle fromLTRB(int left, int top, int right, int bottom) {
        return fromLeftTopRightBottom(left, top, right, bottom);
    }

    /**
     * Creates a rectangle that spans the specified points.
     *
     * @param point1 The first point.
     * @param point2 The second point.
     * @return The rectangle that spans the two points.
     */
    public static Rectangle fromPoints(Point point1, Point point2) {
        int left = Math.min(point1.getX(), point2.getX());
        int top = Math.min(point1.getY(), point2.getY());
        int right = Math.max(point1.getX(), point2.getX());
        int bottom = Math.max(point1.getY(), point2.getY());
        return fromLeftTopRightBottom(left, top, right, bottom);
    }

    /**
     * Creates and returns an inflated copy of the specified rectangle.
     *
     * @param rectangle The rectangle to inflate.
     * @param x         The amount to inflate horizontally.
     * @param y         The amount to inflate vertically.
     * @return The inflated rectangle.
     */
    public static Rectangle inflate(Rectangle rectangle, int x, int y) {
        Rectangle result = new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        result.inflate(x, y);
        return result;
    }

    /**
     * Returns a rectangle that represents the intersection of two rectangles.
     *
     * @param a The first rectangle.
     * @param b The second rectangle.
     * @return The intersection rectangle, or {@link #EMPTY} when there is no overlap.
     */
    public static Rectangle intersect(Rectangle a, Rectangle b) {
        int left = Math.max(a.getLeft(), b.getLeft());
        int top = Math.max(a.getTop(), b.getTop());
        int right = Math.min(a.getRight(), b.getRight());
        int bottom = Math.min(a.getBottom(), b.getBottom());

        return (right > left && bottom > top)
                ? fromLeftTopRightBottom(left, top, right, bottom)
                : EMPTY;
    }

    /**
     * Converts a {@link RectangleF} structure to a {@link Rectangle} structure by rounding the coordinates and size.
     *
     * @param value The rectangle to convert.
     * @return The converted rectangle.
     */
    public static Rectangle round(RectangleF value) {
        return new Rectangle(
                (int) Math.round(value.getX()),
                (int) Math.round(value.getY()),
                (int) Math.round(value.getWidth()),
                (int) Math.round(value.getHeight()));
    }

    /**
     * Converts a {@link RectangleF} structure to a {@link Rectangle} structure by truncating the coordinates and size.
     *
     * @param value The rectangle to convert.
     * @return The converted rectangle.
     */
    public static Rectangle truncate(RectangleF value) {
        return new Rectangle((int) value.getX(), (int) value.getY(),
                (int) value.getWidth(), (int) value.getHeight());
    }

    /**
     * Returns a rectangle that contains the union of two rectangles.
     *
     * @param a The first rectangle.
     * @param b The second rectangle.
     * @return The union rectangle.
     */
    public static Rectangle union(Rectangle a, Rectangle b) {
        int left = Math.min(a.getLeft(), b.getLeft());
        int top = Math.min(a.getTop(), b.getTop());
        int right = Math.max(a.getRight(), b.getRight());
        int bottom = Math.max(a.getBottom(), b.getBottom());
        return fromLeftTopRightBottom(left, top, right, bottom);
    }

    /**
     * Determines whether the specified point is contained within this {@link Rectangle}.
     *
     * @param point The point to test.
     * @return {@code true} if the point is contained in this rectangle; otherwise, {@code false}.
     */
    public boolean contains(Point point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * Determines whether the rectangular region represented by the specified rectangle is entirely contained within this {@link Rectangle}.
     *
     * @param rectangle The rectangle to test.
     * @return {@code true} if the rectangle is contained in this rectangle; otherwise, {@code false}.
     */
    public boolean contains(Rectangle rectangle) {
        return getLeft() <= rectangle.getLeft()
                && rectangle.getRight() <= getRight()
                && getTop() <= rectangle.getTop()
                && rectangle.getBottom() <= getBottom();
    }

    /**
     * Determines whether the specified point is contained within this {@link Rectangle}.
     *
     * @param x The x-coordinate of the point to test.
     * @param y The y-coordinate of the point to test.
     * @return {@code true} if the point is contained in this rectangle; otherwise, {@code false}.
     */
    public boolean contains(int x, int y) {
        return x >= getLeft() && x < getRight() && y >= getTop() && y < getBottom();
    }

    /**
     * Determines whether the specified rectangle is equal to this rectangle.
     *
     * @param other The rectangle to compare.
     * @return {@code true} if the rectangles are equal; otherwise, {@code false}.
     */
    public boolean equals(Rectangle other) {
        return other != null
                && x == other.x
                && y == other.y
                && width == other.width
                && height == other.height;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Rectangle && equals((Rectangle) obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height);
    }

    /**
     * Inflates this rectangle by the specified size.
     *
     * @param size The amount to inflate.
     */
    public void inflate(Size size) {
        inflate(size.getWidth(), size.getHeight());
    }

    /**
     * Inflates this rectangle by the specified horizontal and vertical amounts.
     *
     * @param width  The amount to inflate horizontally.
     * @param height The amount to inflate vertically.
     */
    public void inflate(int width, int height) {
        x -= width;
        y -= height;
        this.width += width * 2;
        this.height += height * 2;
    }

    /**
     * Replaces this rectangle with its intersection with the specified rectangle.
     *
     * @param rectangle The rectangle to intersect with this rectangle.
     */
    public void intersect(Rectangle rectangle) {
        Rectangle r = intersect(this, rectangle);
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
    }

    /**
     * Determines whether this rectangle intersects with the specified rectangle.
     *
     * @param rectangle The rectangle to test.
     * @return {@code true} if the rectangles intersect; otherwise, {@code false}.
     */
    public boolean intersectsWith(Rectangle rectangle) {
        return rectangle.getLeft() < getRight()
                && getLeft() < rectangle.getRight()
                && rectangle.getTop() < getBottom()
                && getTop() < rectangle.getBottom();
    }

    /**
     * Normalizes this rectangle so that width and height are non-negative.
     */
    public void normalize() {
        if (width < 0) {
            x += width;
            width = -width;
        }
        if (height < 0) {
            y += height;
            height = -height;
        }
    }

    /**
     * Moves this rectangle by the specified point.
     *
     * @param point The offset to apply.
     */
    public void offset(Point point) {
        offset(point.getX(), point.getY());
    }

    /**
     * Moves this rectangle by the specified horizontal and vertical amounts.
     *
     * @param x The amount to move horizontally.
     * @param y The amount to move vertically.
     */
    public void offset(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public String toString() {
        return String.format("{X=%d, Y=%d, Width=%d, Height=%d}", x, y, width, height);
    }
}
