package es.ucm.vdm.logic;

import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.VDMColor;

public class Button extends GameObject{
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    private int _w, _h; // Width and Height

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
    public Button(double x, double y, int width, int height, VDMColor c, int thickness) {
        super(x, y, c, thickness);
        _w = width;
        _h = height;
    }


    public void setWidthHeight(int width, int height) {
        _w = width;
        _h = height;
    }

    @Override
    public void render(Graphics g) {
        // for debug only
        g.fillRect((int)_pos._x, (int)_pos._y, _w, _h);
    }

    /**
     * Function that checks if a button is pressed. Returns true when that happens, false if not.
     *
     * @param x X position of the pointer
     * @param y Y position of the pointer
     * @return Returns true if button is pressed, false if not
     */
    public boolean isPressed(int x, int y, Graphics g){
        // If the cursor is inside the rectangle of the sprite.
        //TODO: fix so the coords make sense
        int logicX = g.reverseRepositionX((int)_pos._x);
        int logicY = g.reverseRepositionY((int)_pos._y);
        int logicW = g.reverseRepositionX(_w);
        int logicH = g.reverseRepositionY(_h);

        if( (x >= logicX) && (x < (logicX + logicW))
                && ((y >= logicY) && (y < (logicY +logicH))) ) {
            System.out.println("Button Press");
            return true;
        }
        else{ // If not, return Button not Pressed
            return false;
        }
    }
}
