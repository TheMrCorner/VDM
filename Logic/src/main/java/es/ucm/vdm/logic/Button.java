package es.ucm.vdm.logic;

import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Rect;
import es.ucm.vdm.engine.VDMColor;

public class Button extends GameObject{
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    private int _w, _h; // Width and Height
    private Text _t;

    /**
     * Constructor of the GameObject. Creates a new GameObject and assigns the position, the color
     * and the rotation that the object will have. To make this generic, initializes _rot to 0.
     *
     * @param x         (double) X coordinate.
     * @param y         (double) Y coordinate
     * @param width (int) Width of the button
     * @param height (int) Height of the button
     * @param c         (int) Color
     * @param thickness
     */
    public Button(double x, double y, int width, int height, VDMColor c, int thickness, Text t) {
        super(x, y, c, thickness);
        _w = width;
        _h = height;
        _t = t;
    }


    public void setWidthHeight(int width, int height) {
        _w = width;
        _h = height;
    }

    @Override
    public void render(Graphics g) {
        // for debug only
        /*Rect o = new Rect(_w, 0, 0, _h);
        Rect n = g.scale(o, g.getCanvas());

        g.setColor(_c);
        // Save actual canvas Transformation matrix
        g.save();

        Vector2 v = new Vector2((int) _coordOrigin._x + (int) _pos._x,
                (int) _coordOrigin._y + ((int) _pos._y * (-1)));

        // Change transformation matrix
        g.translate((int) _coordOrigin._x + (int) _pos._x,
                (int) _coordOrigin._y + ((int) _pos._y * (-1)));

        // Draw square
        g.drawLine(-n.width / 2, -n.height / 2,
                n.width / 2, -n.height / 2, _lineThickness);
        g.drawLine(-n.width / 2, -n.height / 2,
                -n.width / 2, n.height / 2, _lineThickness);
        g.drawLine(n.width / 2, -n.height / 2,
                n.width / 2, n.height / 2, _lineThickness);
        g.drawLine(-n.width / 2, n.height / 2,
                n.width / 2, n.height / 2, _lineThickness);

        // Reset canvas after drawing
        g.restore();*/

        _t.render(g);
    }

    /**
     * Function that checks if a button is pressed. Returns true when that happens, false if not.
     *
     * @param x X position of the pointer
     * @param y Y position of the pointer
     * @return Returns true if button is pressed, false if not
     */
    public boolean isPressed(int x, int y){
        // If the cursor is inside the rectangle of the sprite.
        double leftX, leftY;
        double rightX, rightY;

        leftX = _pos._x - (_w / 2);
        leftY = _pos._y - (_h / 2);
        rightX = _pos._x + (_w / 2);
        rightY = _pos._y + (_h / 2);

        // Translate to coordOriginPos

        // x
        if(x < _coordOrigin._x) {
            x = (((int)_coordOrigin._x - x) * -1);
        } // if
        else {
            x = (((int)_coordOrigin._x -((2 * (int)_coordOrigin._x) - x)));
        } // else

        // y
        if(y < _coordOrigin._y) {
            y = (((int)_coordOrigin._y - y));
        } // if
        else {
            y = (((int)_coordOrigin._y -((2 * (int)_coordOrigin._y) - y)) * -1);
        } // else

        // Check inside button
        if( ((x >= leftX) && (x < rightX))
                && ((y >= leftY) && (y < rightY))) {
            return true;
        } // if
        else{ // If not, return Button not Pressed
            return false;
        } // else
    } // isPressed
}
