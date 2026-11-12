package org.aspose.psd.layers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.aspose.psd.BigEndianReader;
import org.aspose.psd.Rectangle;
import org.aspose.psd.BigEndianWriter;
import org.aspose.psd.coreexceptions.PsdLoadException;
import org.aspose.psd.coreexceptions.PsdSaveException;

/**
 * Represents a single PSD layer with basic metadata used by the FOSS library.
 */
public class Layer {

    /**
     * The fixed-size byte count of the layer record trailer fields.
     */
    public static final int LAYER_TRAILER_SIZE = 16;

    /**
     * Stores the current layer name.
     */
    private String _name = "";

    /**
     * Stores the current layer visibility flag.
     */
    private boolean _isVisible = true;

    /**
     * Stores the current layer opacity value.
     */
    private byte _opacity = (byte) 255;

    /**
     * Stores raw PSD layer record data required for byte-preserving saves.
     */
    private LayerRawData _rawData = LayerRawData.EMPTY;

    /**
     * Stores the current layer bounds in document coordinates.
     */
    private Rectangle _bounds = new Rectangle();

    /**
     * Stores the PSD clipping value for the layer.
     */
    private byte _clipping;

    /**
     * Stores the PSD blend mode exposed by the layer record.
     */
    private BlendMode _blendMode;

    /**
     * Gets or sets the Pascal layer name stored in the layer record.
     */
    public String getName() {
        return _name;
    }

    public void setName(String value) {
        if (!_name.equals(value)) {
            _name = value;
            setHasMutated(true);
        }
    }

    /**
     * Gets the Aspose.PSD-compatible local layer rectangle.
     */
    public Rectangle getBounds() {
        return new Rectangle(0, 0, _bounds.getWidth(), _bounds.getHeight());
    }

    /**
     * Gets the layer width in pixels.
     */
    public int getWidth() {
        return _bounds.getWidth();
    }

    /**
     * Gets the layer height in pixels.
     */
    public int getHeight() {
        return _bounds.getHeight();
    }

    /**
     * Gets the top edge of the layer bounds.
     */
    public int getTop() {
        return _bounds.getTop();
    }

    public void setTop(int value) {
        if (_bounds.getTop() != value) {
            setBounds(Rectangle.fromLTRB(_bounds.getLeft(), value, _bounds.getRight(), _bounds.getBottom()));
        }
    }

    /**
     * Gets the left edge of the layer bounds.
     */
    public int getLeft() {
        return _bounds.getLeft();
    }

    public void setLeft(int value) {
        if (_bounds.getLeft() != value) {
            setBounds(Rectangle.fromLTRB(value, _bounds.getTop(), _bounds.getRight(), _bounds.getBottom()));
        }
    }

    /**
     * Gets the bottom edge of the layer bounds.
     */
    public int getBottom() {
        return _bounds.getBottom();
    }

    public void setBottom(int value) {
        if (_bounds.getBottom() != value) {
            setBounds(Rectangle.fromLTRB(_bounds.getLeft(), _bounds.getTop(), _bounds.getRight(), value));
        }
    }

    /**
     * Gets the right edge of the layer bounds.
     */
    public int getRight() {
        return _bounds.getRight();
    }

    public void setRight(int value) {
        if (_bounds.getRight() != value) {
            setBounds(Rectangle.fromLTRB(_bounds.getLeft(), _bounds.getTop(), value, _bounds.getBottom()));
        }
    }

    /**
     * Gets or sets a value indicating whether the layer is visible.
     */
    public boolean isVisible() {
        return _isVisible;
    }

    public void setVisible(boolean value) {
        if (_isVisible != value) {
            _isVisible = value;
            setHasMutated(true);
        }
    }

    /**
     * Gets or sets the layer opacity in the 0-255 range.
     */
    public byte getOpacity() {
        return _opacity;
    }

    public void setOpacity(byte value) {
        if (_opacity != value) {
            _opacity = value;
            setHasMutated(true);
        }
    }

    /**
     * Gets the PSD clipping value for the layer.
     */
    public byte getClipping() {
        return _clipping;
    }

    public void setClipping(byte value) {
        if (_clipping != value) {
            _clipping = value;
            setHasMutated(true);
        }
    }

    /**
     * Gets or sets the layer blend mode key.
     */
    public BlendMode getBlendModeKey() {
        return _blendMode;
    }

    public void setBlendModeKey(BlendMode value) {
        setBlendModeKeyInternal(value);
    }

    /**
     * Gets the layer's channels count.
     */
    public int getChannelsCount() {
        return getChannelInfo().length;
    }

    /**
     * Gets or sets the channel information.
     */
    public ChannelInformation[] getChannelInformation() {
        LayerChannelInfo[] info = getChannelInfo();
        ChannelInformation[] result = new ChannelInformation[info.length];
        for (int i = 0; i < info.length; i++) {
            result[i] = ChannelInformation.fromLayerChannelInfo(info[i]);
        }
        return result;
    }

    public void setChannelInformation(ChannelInformation[] value) {
        throw new UnsupportedOperationException(
                "Changing layer channel information is not supported by this FOSS build.");
    }

    /**
     * Gets a read-only summary of the parsed layer channel records.
     */
    public List<PsdLayerChannelInfo> getChannels() {
        LayerChannelInfo[] info = getChannelInfo();
        List<PsdLayerChannelInfo> result = new ArrayList<>(info.length);
        for (LayerChannelInfo channel : info) {
            result.add(new PsdLayerChannelInfo(channel.getChannelId(), channel.getDataLength()));
        }
        return java.util.Collections.unmodifiableList(result);
    }

    /**
     * Gets a value indicating whether the layer contains a non-empty layer mask subsection.
     */
    boolean hasMaskData() {
        return _rawData.getLayerMaskSection().getRawData().length > Integer.BYTES;
    }

    /**
     * Gets a value indicating whether the layer contains a non-empty blending ranges subsection.
     */
    boolean hasBlendingRangesData() {
        return _rawData.getBlendingRangesSection().getRawData().length > Integer.BYTES;
    }

    /**
     * Gets a read-only summary of the parsed layer mask subsection.
     */
    public LayerMaskInfo getMaskInfo() {
        return new LayerMaskInfo(hasMaskData(), _rawData.getLayerMaskSection().getRawData().length);
    }

    /**
     * Gets a read-only summary of the parsed blending ranges subsection.
     */
    public LayerBlendingRangesInfo getBlendingRangesInfo() {
        return new LayerBlendingRangesInfo(hasBlendingRangesData(),
                _rawData.getBlendingRangesSection().getRawData().length);
    }

    /**
     * Stores the parsed per-channel metadata from the layer record.
     */
    LayerChannelInfo[] getChannelInfo() {
        return _rawData.getChannelInfo();
    }

    /**
     * Gets the original PSD layer flags byte used when rewriting the layer record.
     */
    byte getRawFlags() {
        return _rawData.getFlags();
    }

    /**
     * Gets the raw layer mask subsection used by the layer record writer.
     */
    RawLayerMaskSection getRawLayerMaskSection() {
        return _rawData.getLayerMaskSection();
    }

    /**
     * Gets the raw blending ranges subsection used by the layer record writer.
     */
    RawLayerBlendingRangesSection getRawLayerBlendingRangesSection() {
        return _rawData.getBlendingRangesSection();
    }

    /**
     * Gets or sets the layer mask data.
     */
    public LayerMaskData getLayerMaskData() {
        if (!hasMaskData()) {
            return null;
        }
        return new LayerMaskDataShort();
    }

    public void setLayerMaskData(LayerMaskData value) {
        throw new UnsupportedOperationException(
                "Changing layer mask data is not supported by this FOSS build.");
    }

    /**
     * Gets or sets the layer blending ranges data.
     */
    public LayerBlendingRangesData getLayerBlendingRangesData() {
        return LayerBlendingRangesData.fromRawLength(_rawData.getBlendingRangesSection().getRawData().length);
    }

    public void setLayerBlendingRangesData(LayerBlendingRangesData value) {
        throw new UnsupportedOperationException(
                "Changing layer blending ranges data is not supported by this FOSS build.");
    }

    /**
     * Gets the additional layer data bytes that follow the Pascal layer name.
     */
    public byte[] getAdditionalLayerData() {
        return _rawData.getAdditionalLayerData();
    }

    /**
     * Gets the original raw 4-byte PSD blend mode key for diagnostics and raw-preserve verification.
     */
    public String getRawBlendModeKey() {
        return _rawData.getBlendModeKey();
    }

    /**
     * Gets a value indicating whether the layer has a pending in-memory mutation.
     */
    public boolean hasMutated() {
        return _hasMutated;
    }

    private boolean _hasMutated;

    void setHasMutated(boolean value) {
        _hasMutated = value;
    }

    /**
     * Marks the layer as mutated so the save path rewrites the minimal required structures.
     */
    void markMutated() {
        _hasMutated = true;
    }

    /**
     * Updates the layer bounds from coordinate property setters.
     */
    private void setBounds(Rectangle bounds) {
        if (!_bounds.equals(bounds)) {
            _bounds = bounds;
            _hasMutated = true;
        }
    }

    /**
     * Updates the PSD blend mode key.
     */
    private void setBlendModeKeyInternal(BlendMode blendMode) {
        if (_blendMode != blendMode) {
            _blendMode = blendMode;
            _rawData = _rawData.withBlendModeKey(LayerBlendModeMapper.getBlendModeKey(blendMode));
            _hasMutated = true;
        }
    }

    /**
     * Loads a layer record from the current reader position.
     */
    public static Layer load(BigEndianReader reader, boolean isLargeDocument) throws PsdLoadException, IOException {
        return LayerRecordReader.load(reader, isLargeDocument);
    }

    /**
     * Writes the current layer record using PSD- or PSB-sized channel lengths.
     */
    void write(BigEndianWriter writer, boolean isLargeDocument) throws PsdSaveException {
        LayerRecordWriter.write(this, writer, isLargeDocument);
    }

    /**
     * Creates a layer instance from already-parsed PSD layer record fields without marking it as mutated.
     */
    static Layer createParsed(
            String name,
            Rectangle bounds,
            boolean isVisible,
            byte opacity,
            byte clipping,
            BlendMode blendMode,
            LayerRawData rawData) {
        Layer layer = new Layer();
        layer._name = name;
        layer._bounds = bounds;
        layer._isVisible = isVisible;
        layer._opacity = opacity;
        layer._clipping = clipping;
        layer._blendMode = blendMode;
        layer._rawData = rawData;
        return layer;
    }
}