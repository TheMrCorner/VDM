package es.ucm.vdm.androidengine;

// Android
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;

// UCM
import es.ucm.vdm.engine.AbstractGraphics;
import es.ucm.vdm.engine.Font;

/**
 * Graphics class that implements the interface from the engine and uses android API to draw lines
 * and squares. It also draws text in screen. Extends AbstractGraphics to use scaling methods and
 * common variables.
 */
public class Graphics extends AbstractGraphics {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    SurfaceView _sView;
    AssetManager _aMan;
    Paint _pnt;
    Canvas _cnv;

    /**
     * Constructor. Receives and saves an instance of the SurfaceView to paint there later. Also
     * receives and saves an instance of the AssetManager to load new Images and  create them .
     * Initializes Paint to use it to show images on the screen.
     *
     * @param sv (SurfaceView) SurfaceView instance
     * @param am (AssetManager) AssetManager instance
     */
    public Graphics(SurfaceView sv, AssetManager am){
        _sView = sv;
        _aMan = am;
        _pnt = new Paint();
    } // Graphics

    /**
     * Initialize the frame. Receives a new canvas and sets it as the canvas it will use to Paint
     * later. Called once per frame.
     *
     * @param c (Canvas) New canvas.
     */
    public void startFrame(Canvas c) {
        _cnv = c;
    } // startFrame

    /**
     * Clears the screen with a specific color. Draws the hole screen with that color.
     *
     * @param color (int) Color to paint
     */
    @Override
    public void clear(int color) {
        // Set color
        setColor(color);

        // Paint screen
        _cnv.drawRect((float)_can.getX(), (float)_can.getY(),
                (float)(_can.getX() + _can.getWidth()), (float)(_can.getY() + _can.getHeight()),
                _pnt);
    } // clear

    @Override
    public void setColor(int color) {
        int c = 0xFF000000 + color;
        _pnt.setColor(color);
    } // setColor

    /**
     * Draws a line with a given color.
     *
     * @param x1 (int) X position of the beginning point
     * @param y1 (int) Y position of the beginning point
     * @param x2 (int) X position of the ending point
     * @param y2 (int) Y position of the ending point
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        _cnv.setDensity(1);
        _cnv.drawLine(x1, y1, x2, y2, _pnt);
    } // drawLine

    /**
     * Draws a rectangle with given coordinates. (Top left corner and bottom right corner).
     *
     * @param x1 (int) Top left X coordinate
     * @param y1 (int) Top left Y coordinate
     * @param x2 (int) Bottom right X coordinate
     * @param y2 (int) Bottom right Y coordinate
     */
    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        _cnv.setDensity(1);
        Rect temp = new Rect(x1, y1, (x2 - x1), (y2 - y1));
        _cnv.drawRect(temp, _pnt);
    } // fillRect

    @Override
    public void drawText(String text, int x, int y) {
        // TODO: Implementar
    } // drawText

    @Override
    public Font newFont(String filename, int size, boolean isBold) {
        // TODO: Implementar
        return null;
    } // newFont

    /**
     * Return width of the SurfaceView for calculations.
     *
     * @return (int) Width of the surface view.
     */
    @Override
    public int getWidth() {
        return _sView.getWidth();
    } // getWidth

    /**
     * Return width of the SurfaceView for calculations.
     *
     * @return (int) Height of the surface view.
     */
    @Override
    public int getHeight() {
        return _sView.getHeight();
    } // getHeight

    /**
     * Saves actual canvas state for restoring it later.
     */
    @Override
    public void save() {
        _cnv.save();
    } // save

    /**
     * Restores canvas using the last saved state
     */
    @Override
    public void restore() {
        _cnv.restore();
    } // restore

    /**
     * Rotates transformation matrix for painting rotated objects.
     *
     * @param angle (float) Angle to rotate object
     */
    @Override
    public void rotate(float angle) {
        _cnv.rotate(angle);
    } // rotate

    /**
     * Changes the origin coordinates of transformation matrix.
     *
     * @param x (int) X position to set as origin
     * @param y (int) Y position to set as origin
     */
    @Override
    public void translate(int x, int y) {
        x = _can.getX() + repositionX(x);
        y = _can.getY() + repositionY(y);

        _cnv.translate(x, y);
    } // translate
} // Graphics
