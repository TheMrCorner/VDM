package es.ucm.vdm.pcengine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Class that implements the base Font for its use on PC platforms
 */
public class PCFont implements es.ucm.vdm.engine.Font {
    /**
     * Font object
     */
    protected Font _font = null;

    /**
     * Attributes that control the appearance of the text
     */
    String _contents = "";
    int _fontSize = 1;
    Color _fontColor = Color.white;
    int _x = 0, _y = 0;

    /**
     * Graphics component for rendering purposes
     */
    Graphics _graphics = null;

    @Override
    public boolean initializeFont(String contents, int fontSize, int fontColor) {
        // Loading the font from the .ttf file
        Font baseFont;

        try (InputStream is = new FileInputStream(FONT_FILE)) {
            baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
        }
        catch (Exception e) {
            // Font could not be loaded
            System.err.println("Error loading the font file: " + e);
            return false;
        }

        _font = baseFont.deriveFont(Font.BOLD, fontSize);

        _contents = contents;
        _fontSize = fontSize;
        int r = (fontColor & 0xFF0000) >> 16;
        int g = (fontColor & 0xFF00) >> 8;
        int b = (fontColor & 0xFF);
        _fontColor = new Color(r, g, b);

        return true;
    }

    @Override
    public void render() {
        // check for nulls before trying to render
        if (_graphics != null && _font != null) {
            _graphics.setColor(_fontColor);
            _graphics.setFont(_font);
            _graphics.drawString(_contents, _x, _y);
        }
    }

    @Override
    public void setPosition(int x, int y) {
        _x = x;
        _y = y;
    }

    /**
     * Sets the Graphics attribute used for rendering
     * @param g (Graphics) Graphics component for rendering purposes
     */
    void getCanvas(Graphics g) { _graphics = g;}
}
