package org.aspose.psd.layers;

import org.aspose.psd.Rectangle;
import org.aspose.psd.layers.LayerMaskFlags;

/**
 * Defines the base class for PSD layer mask data.
 */
public abstract class LayerMaskData {
    /**
     * Stores the layer mask image data.
     */
    private byte[] imageData = new byte[0];

    /**
     * Stores the layer mask rectangle.
     */
    private Rectangle maskRectangle = new Rectangle();

    /**
     * Gets or sets the bottom layer mask position.
     */
    public int getBottom() {
        return maskRectangle.getBottom();
    }

    public void setBottom(int value) {
        maskRectangle.setBottom(value);
    }

    /**
     * Gets the size of the layer mask data.
     */
    public int getDataSize() {
        return imageData.length;
    }

    /**
     * Gets or sets the default layer mask color.
     */
    private byte defaultColor;

    public byte getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(byte value) {
        this.defaultColor = value;
    }

    /**
     * Gets or sets the layer mask flags.
     */
    private LayerMaskFlags flags;

    public LayerMaskFlags getFlags() {
        return flags;
    }

    public void setFlags(LayerMaskFlags value) {
        this.flags = value;
    }

    /**
     * Gets or sets the layer mask image data.
     */
    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        this.imageData = value;
    }

    /**
     * Gets or sets the left layer mask position.
     */
    public int getLeft() {
        return maskRectangle.getLeft();
    }

    public void setLeft(int value) {
        maskRectangle.setLeft(value);
    }

    /**
     * Gets or sets the mask rectangle.
     */
    public Rectangle getMaskRectangle() {
        return maskRectangle;
    }

    public void setMaskRectangle(Rectangle value) {
        this.maskRectangle = value;
    }

    /**
     * Gets or sets the right layer mask position.
     */
    public int getRight() {
        return maskRectangle.getRight();
    }

    public void setRight(int value) {
        maskRectangle.setRight(value);
    }

    /**
     * Gets or sets the top layer mask position.
     */
    public int getTop() {
        return maskRectangle.getTop();
    }

    public void setTop(int value) {
        maskRectangle.setTop(value);
    }
}

