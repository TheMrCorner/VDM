package es.ucm.vdm.logic;

import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Rect;
import es.ucm.vdm.engine.VDMColor;

public class Item extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    private int _w, _h; // Width and Height
    private double _cW, _cH; // Current width and height
    private float _av; // Angular velocity
    private float _exv; // Expansion velocity
    private int _transpVel; // alpha change velocity
    private VDMColor _transpCol; // color change variable
    private boolean _taken; // Flag to control if the player has taken this item
    private float _centerAv; // Angular velocity for rotating over one point
    private float _totalRot;
    private float _distanceCenter;
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
    public Item(double x, double y, VDMColor c, int width, int height, float angVel, int thickness,
                float expansionVel, int transparencyVel) {
        super(x, y, c, thickness);
        _w = width;
        _h = height;
        _cW = _w;
        _cH = _h;
        _av = angVel;
        _taken = false;
        _centerAv = -1;
        _exv = expansionVel;
        _transpVel = transparencyVel;
        _transpCol = new VDMColor(c._r, c._g, c._b, c._a);
    } // Item

    public Item(double x, double y, VDMColor c, int width, int height, float angVel, int thickness,
                float tAngVel, float rotRefer, float distCenter,
                float expansionVel, int transparencyVel){
        super(x, y, c, thickness);
        _w = width;
        _h = height;
        _cW = _w;
        _cH = _h;
        _av = angVel;
        _taken = false;
        _centerAv = tAngVel;
        _totalRot = rotRefer;
        _distanceCenter = distCenter;
        _exv = expansionVel;
        _transpVel = transparencyVel;
        _transpCol = new VDMColor(c._r, c._g, c._b, c._a);
    } // Item rotating with a point

    //---------------------------------------------------------------
    //------------------Constructor and Management-------------------
    //---------------------------------------------------------------

    /**
     * Function to reset the Item to it's initial value.
     */
    public void resetItem(){
        _taken = false;
        _act = true;
        _rot = 0;
        _cW = _w;
        _cH = _h;
        _transpCol = new VDMColor(_c._r, _c._g, _c._b, _c._a);
    } // resetItem

    /**
     * Function called when an item is taken by the player.
     */
    public void itemTaken(){
        _taken = true;
    } // item_taken

    /**
     * Function to check whether the has been taken or not.
     *
     * @return (boolean) _taken
     */
    public boolean isTaken(){
        return _taken;
    } // isTaken

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
        } // if

        if(_centerAv != -1){
            _totalRot -= _centerAv * t;

            if(_totalRot <= 0){
                _totalRot = 360;
            } // if

            _pos._x = _distanceCenter * Math.sin(Math.toRadians(_totalRot));
            _pos._y = _distanceCenter * Math.cos(Math.toRadians(_totalRot));
        } // if

        if(_taken) {
            if((_cW < (_w * 3)) && (_cH < (_h * 3))) {
                _cW += _exv * t;
                _cH += _exv * t;
            }

            _transpCol._a -= (_transpVel * t);

            if(_transpCol._a <= 0){
                _act = false;
            }

        } // if
    } // update

    /**
     * Renders the item with the color preset and in the position assigned.
     *
     * @param g (Graphics) Graphics instance to paint it
     */
    @Override
    public void render(Graphics g) {
        if(_act) {
            Rect o;
            Rect n = null;
            int lineThickness = _lineThickness;
            VDMColor c;

            if(!_taken) {
                o = new Rect(_w, 0, 0, _h);

                c = _c;
            }
            else{
                o = new Rect((int)_cW, 0, 0, (int)_cH);

                lineThickness = (int)(_cW * _lineThickness / _w);

                c = _transpCol;
            } // else

            n = g.scale(o, g.getCanvas());
            // Set the color to paint the coin/item
            g.setColor(c);
            // Save the actual canvas transformation matrix
            g.save();

            g.translate((int) _coordOrigin._x + (int) _pos._x,
                    (int) _coordOrigin._y + ((int) _pos._y * (-1)));

            g.rotate(_rot);

            // Draw square
            g.drawLine(-n.width / 2, -n.height / 2,
                    n.width / 2, -n.height / 2, lineThickness);
            g.drawLine(-n.width / 2, -n.height / 2,
                    -n.width / 2, n.height / 2, lineThickness);
            g.drawLine(n.width / 2, -n.height / 2,
                    n.width / 2, n.height / 2, lineThickness);
            g.drawLine(-n.width / 2, n.height / 2,
                    n.width / 2, n.height / 2, lineThickness);

            // Reset canvas after drawing
            g.restore();
        } // if
    } // render
} // Item
