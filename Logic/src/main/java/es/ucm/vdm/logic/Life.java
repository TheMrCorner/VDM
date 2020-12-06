package es.ucm.vdm.logic;

import es.ucm.vdm.engine.GameObject;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Rect;

public class Life extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    int _w, _h; // Width and height
    int _color2; // Second color (or cross color)
    boolean _lost; // Flag to know if life is lost or not
    double _diag; // Diagonal to paint the cross
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------

    /**
     * Constructor of the GameObject. Creates a new GameObject and assigns the position, the color
     * and the rotation that the object will have. To make this generic, initializes _rot to 0.
     *
     * @param x (double) X coordinate.
     * @param y (double) Y coordinate
     * @param c (int) Color
     */
    public Life(double x, double y, int c, int secondColor, int w, int h, int crossRot) {
        super(x, y, c);
        _w = w;
        _h = h;
        _color2 = secondColor;
        _diag = Math.sqrt((Math.pow(_w, 2) + Math.pow(_h, 2)));
        _rot = crossRot;
    } // constructor

    /**
     * Mark this life as lost.
     */
    public void lose_life(){
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
                g.drawLine(-g.repositionX((int)_diag/ 2),  0, g.repositionX((int)_diag / 2), 0);
                g.restore();
            }
        } // if
        else {
            // Square
            g.drawLine(-n.width / 2, -n.height / 2, n.width / 2, -n.height / 2);
            g.drawLine(-n.width / 2, -n.height / 2, -n.width / 2, n.height / 2);
            g.drawLine(n.width / 2, -n.height / 2, n.width / 2, n.height / 2);
            g.drawLine(-n.width / 2, n.height / 2, n.width / 2, n.height / 2);
        } // else

        // Reset canvas after drawing
        g.restore();
    } // render
} // Life
