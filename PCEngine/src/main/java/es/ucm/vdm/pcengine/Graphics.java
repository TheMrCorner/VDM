package es.ucm.vdm.pcengine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Stack;

import es.ucm.vdm.engine.AbstractGraphics;
import es.ucm.vdm.engine.Font;
import es.ucm.vdm.engine.Rect;
import es.ucm.vdm.engine.VDMColor;

public class Graphics extends AbstractGraphics {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    Window _win;
    Stack<AffineTransform> _saveStack; // Stack to save transformation matrix states

    PCFont _font;

    /**
     * Graphics constructor.
     *
     * @param w (Window) Window instance
     */
    public Graphics(Window w) {
        _win = w;
        _can = new Rect(0, 0, 0, 0);
        _refCan = new Rect(0, 0, 0, 0);
        _saveStack = new Stack<AffineTransform>();
    } // Graphics

    /**
     * Translate canvas to its position in screen for painting correctly.
     *
     * @param x (int) X coordinate
     * @param y (int) Y coordinate
     */
    @Override
    public void setCanvasPos(int x, int y) {
        _can.setPosition(x, y);
        ((Graphics2D)_win.getJGraphics()).translate(x, y);
    } // setCanvasPos

    /**
     * This function receives a color and paints the hole screen with that color (white recommended)
     * to clean it from the last painting.
     *
     * @param color (int) Flat color received to paint the screen
     */
    @Override
    public void clear(VDMColor color) {
        // Paint the hole screen with it.
        setColor(color);
        save();
        ((Graphics2D)_win.getJGraphics()).translate(0, 0);
        ((Graphics2D)(_win.getJGraphics())).fillRect(0, 0, _win.getWidth(), _win.getHeight());
        restore();
    } // clear

    /**
     * Sets color to paint on screen.
     *
     * @param color (int) Color to set for drawing
     */
    @Override
    public void setColor(VDMColor color) {
        Color c = new Color(color._r, color._g, color._b, color._a);

        // Set color to paint in the Swing Graphics.
        ((Graphics2D)(_win.getJGraphics())).setColor(c);
    } // setColor

    /**
     * Draws a line between two given points.
     *
     * @param x1 (int) X position of the beginning point
     * @param y1 (int) Y position of the beginning point
     * @param x2 (int) X position of the ending point
     * @param y2 (int) Y position of the ending point
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int thickness) {
        try{
            ((Graphics2D)(_win.getJGraphics())).setStroke(new BasicStroke(thickness));
            ((Graphics2D)(_win.getJGraphics())).drawLine(x1, y1, x2, y2);
        } // try
        catch (Exception e){
            e.printStackTrace();
        } // catch
    } // drawLine

    /**
     * Draws a rectangle with given coordinates and fills it with specified color.
     *
     * @param x1 (int) X position of top left corner
     * @param y1 (int) Y position of top left corner
     * @param x2 (int) X position of bottom right corner
     * @param y2 (int) Y position of bottom right corner
     */
    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        try {
            ((Graphics2D)(_win.getJGraphics())).fillRect(x1, y1, x2, y2);
        } // try
        catch(Exception e){
            e.printStackTrace();
        } // catch
    } // fillRect


    @Override
    public void drawText(String text, int x, int y) {
        // TODO: Implement
        _font.setContents(text);
        _font.setPosition(x, y);
        _font.render();
    } // drawText

    @Override
    public Font newFont(String filename, int size, boolean isBold) {
        _font = new PCFont();
        _font.initializeFont(filename, size, 0xffffff, isBold);
        _font.setCanvas((Graphics2D)_win.getJGraphics());
        return _font;
    } // newFont

    /**
     * Return width of the window.
     *
     * @return (int) Window Width
     */
    @Override
    public int getWidth() {
        return _win.getWidth();
    } // getWidth

    /**
     * Return height of the window.
     *
     * @return (int) Window height
     */
    @Override
    public int getHeight() {
        return _win.getHeight();
    } // getHeight

    /**
     * Saves actual transformation matrix's state for restoring it later.
     */
    @Override
    public void save() {
        AffineTransform t = ((Graphics2D)_win.getJGraphics()).getTransform();
        _saveStack.push(t);
    } // save

    /**
     * Restores last saved Transformation matrix instance.
     */
    @Override
    public void restore() {
        ((Graphics2D)_win.getJGraphics()).setTransform(_saveStack.pop());
    } // restore

    /**
     * Rotates transformation matrix to paint objects rotated.
     *
     * @param angle (float) Angle to rotate object
     */
    @Override
    public void rotate(float angle) {
        ((Graphics2D)_win.getJGraphics()).rotate(Math.toRadians(angle));
    } // rotate

    /**
     * Change origin coordinates of transformation matrix to paint objects in
     * given position.
     *
     * @param x (int) X position to set as origin
     * @param y (int) Y position to set as origin
     */
    @Override
    public void translate(int x, int y) {
        try {
            x = _can.getX() + repositionX(x);
            y = _can.getY() + repositionY(y);

            ((Graphics2D)_win.getJGraphics()).translate(x, y);
        } // try
        catch(Exception e){
            e.printStackTrace();
        }
    } // translate
} // Graphics
