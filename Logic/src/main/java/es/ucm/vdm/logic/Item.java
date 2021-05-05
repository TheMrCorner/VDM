package es.ucm.vdm.logic;

import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Rect;

public class Item extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    int _w, _h; // Width and Height
    float _av; // Angular velocity
    boolean _taken; // Flag to control if the player has taken this item
    float _centerAv; // Angular velocity for rotating over one point
    float _totalRot;
    float _distanceCenter;
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------


    //---------------------------------------------------------------
    //------------------Constructor and Management-------------------
    //---------------------------------------------------------------

    /**
     * Constructor of an item. Receives the position of the item and the color that will be used
     * to draw it. Also receives width and height with an angular velocity for rotation.
     *
     * @param x (double) X position in canvas
     * @param y (double) Y position in canvas
     * @param c (int) Color to draw this GO
     * @param width (int) Width of the square
     * @param height (int) Height of the square
     * @param angVel (float) Angular velocity the Object will have for rotation
     */
    public Item(double x, double y, int c, int width, int height, float angVel, int thickness) {
        super(x, y, c, thickness);
        _w = width;
        _h = height;
        _av = angVel;
        _taken = false;
        _centerAv = -1;
    } // Item

    public Item(double x, double y, int c, int width, int height, float angVel, int thickness,
                float tAngVel, float rotRefer, float distCenter){
        super(x, y, c, thickness);
        _w = width;
        _h = height;
        _av = angVel;
        _taken = false;
        _centerAv = tAngVel;
        _totalRot = rotRefer;
        _distanceCenter = distCenter;
    } // Item rotating with a point

    //---------------------------------------------------------------
    //------------------Constructor and Management-------------------
    //---------------------------------------------------------------

    /**
     * Function called when an item is taken by the player.
     */
    public void itemTaken(){
        _taken = true;
    } // item_taken

    /**
     * Item update. Rotates the object and scales it if it is taken.
     *
     * @param t (double) Time elapsed
     */
    @Override
    public void update(double t) {
        _rot += _av * t;

        if(_rot >= 360){
            _rot = 0;
        }

        if(_centerAv != -1){
            _totalRot += _centerAv * t;

            if(_totalRot >= 360){
                _totalRot = 0;
            }

            _pos._x = _coordOrigin._x + _distanceCenter *Math.sin(Math.toRadians(_totalRot));
            _pos._y = _coordOrigin._y + _distanceCenter * Math.cos(Math.toRadians(_totalRot));
        }
    } // update

    /**
     * Renders the item with the color preset and in the position asignated.
     *
     * @param g (Graphics) Graphics instance to paint it
     */
    @Override
    public void render(Graphics g) {
        Rect o = new Rect(_w, 0, 0, _h);
        Rect n = g.scale(o, g.getCanvas());

        // Set the color to paint the coin/item
        g.setColor(_c);
        // Save the actual canvas transformation matrix
        g.save();

        if(_centerAv == -1) {
            g.translate((int) _coordOrigin._x + (int) _pos._x,
                    (int) _coordOrigin._y - (int) _pos._y);
        }
        else{
            g.translate((int) _pos._x,
                    (int) _pos._y);
        }

        g.rotate(_rot);

        // Draw square
        g.drawLine(-n.width/2, -n.height/2,
                n.width/2, -n.height/2, _lineThickness);
        g.drawLine(-n.width/2, -n.height/2,
                -n.width/2, n.height/2, _lineThickness);
        g.drawLine(n.width/2, -n.height/2,
                n.width/2, n.height/2, _lineThickness);
        g.drawLine(-n.width/2, n.height/2,
                n.width/2, n.height/2, _lineThickness);

        // Reset canvas after drawing
        g.restore();
    } // render
} // Item
