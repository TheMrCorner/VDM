package es.ucm.vdm.logic;

import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Rect;
import es.ucm.vdm.engine.VDMColor;

/**
 * GameObject that represents a Life on screen. It has two positions, active or inactive, with
 * it's unique render option (cross or square).
 */
public class Life extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    int _w, _h; // Width and height
    VDMColor _color2; // Second color (or cross color)
    boolean _lost; // Flag to know if life is lost or not
    double _diag; // Diagonal to paint the cross
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------

    /**
     * Constructor for a Life object. Receives all data necessary to paint a life, setting
     * it's colors and etc.
     *
     * @param x (double) X position of the Object.
     * @param y (double) Y position of the Object.
     * @param c (VDMColor) Color to paint the Object.
     * @param secondColor (VDMColor) Color to paint the Object in it's second position.
     * @param w (int) Width of the square.
     * @param h (int) Height of the square.
     * @param crossRot (int) Rotation angle fot the Cross figure.
     * @param thickness (int) Thickness of the lines.
     */
    public Life(double x, double y, VDMColor c, VDMColor secondColor, int w, int h,
                int crossRot, int thickness) {
        super(x, y, c, thickness);
        _w = w;
        _h = h;
        _color2 = secondColor;
        _diag = Math.sqrt((Math.pow(_w, 2) + Math.pow(_h, 2)));
        _rot = crossRot;
    } // constructor

    /**
     * Mark this life as lost.
     */
    public void loseLife(){
        _lost = true;
    } // lose_life

    /**
     * Draw the life applying position.
     *
     * @param g (Graphics) Graphics instance to paint.
     */
    @Override
    public void render(Graphics g) {
        Rect o = new Rect(_w, 0, 0, _h);
        Rect n = g.scale(o, g.getCanvas());

        // Set color to paint life depending on state
        if(_lost){
            g.setColor(_color2);
        } // if
        else {
            g.setColor(_c);
        } // else

        // Save actual canvas Transformation matrix
        g.save();

        // Change transformation matrix
        g.translate(g.getCanvas().getLeft() + (int)_pos._x,
                g.getCanvas().getTop() + (int)_pos._y);

        // Draw square or cross
        if(_lost){
            // Cross
            for(int i = 1; i >= -1; i-=2){
                g.save();
                g.rotate(_rot * i);
                g.drawLine(-g.repositionX((int)_diag/ 2),  0,
                        g.repositionX((int)_diag / 2), 0, _lineThickness);
                g.restore();
            }
        } // if
        else {
            // Square
            g.drawLine(-n.width / 2, -n.height / 2,
                    n.width / 2, -n.height / 2, _lineThickness);
            g.drawLine(-n.width / 2, -n.height / 2,
                    -n.width / 2, n.height / 2, _lineThickness);
            g.drawLine(n.width / 2, -n.height / 2,
                    n.width / 2, n.height / 2, _lineThickness);
            g.drawLine(-n.width / 2, n.height / 2,
                    n.width / 2, n.height / 2, _lineThickness);
        } // else

        // Reset canvas after drawing
        g.restore();
    } // render
} // Life
