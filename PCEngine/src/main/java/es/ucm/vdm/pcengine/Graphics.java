package es.ucm.vdm.pcengine;

import java.awt.Color;

import es.ucm.vdm.engine.AbstractGraphics;
import es.ucm.vdm.engine.Rect;

public class Graphics extends AbstractGraphics {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    Window _win;

    /**
     * Graphics constructor.
     *
     * @param w (Window) Window instance
     */
    public Graphics(Window w) {
        _win = w;
        _can = new Rect(0, 0, 0, 0);
        _refCan = new Rect(0, 0, 0, 0);
    } // Graphics

    /**
     * This function receives a color and paints the hole screen with that color (white recommended)
     * to clean it from the last painting.
     *
     * @param color (int) Flat color received to paint the screen
     */
    @Override
    public void clear(int color) {
        Color c = new Color(color);

        // Set color to paint in the Swing Graphics.
        _win.getJGraphics().setColor(c);
        // Paint the hole screen with it.
        _win.getJGraphics().fillRect(0, 0, _win.getWidth(), _win.getHeight());
    } // clear

    @Override
    public void drawLine(int y1, int x1, int y2, int x2, int color) {
        _win.getJGraphics().setColor(new Color(color));
        _win.getJGraphics().drawLine(x1, y1, x2, y2);
    }

    /**
     * Return width of the window.
     *
     * @return (int) Window Width
     */
    @Override
    public int getWidth() {
        return _win.getWidth();
    }

    /**
     * Return height of the window.
     *
     * @return (int) Window height
     */
    @Override
    public int getHeight() {
        return _win.getHeight();
    }
}
