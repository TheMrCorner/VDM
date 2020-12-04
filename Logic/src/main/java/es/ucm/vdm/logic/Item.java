package es.ucm.vdm.logic;

import es.ucm.vdm.engine.GameObject;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Rect;

public class Item extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    int _w, _h; // Width and Height
    float _av; // Angular velocity
    boolean _taken; // Flag to control if the player has taken this item

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
     * @param x (int) X position in canvas
     * @param y (int) Y position in canvas
     * @param c (int) Color to draw this GO
     * @param width (int) Width of the square
     * @param height (int) Height of the square
     * @param angVel (float) Angular velocity the Object will have for rotation
     */
    public Item(int x, int y, int c, int width, int height, float angVel) {
        super(x, y, c);
        _w = width;
        _h = height;
        _av = angVel;
        _taken = false;
    } // Item

    //---------------------------------------------------------------
    //------------------Constructor and Management-------------------
    //---------------------------------------------------------------

    /**
     * Function called when an item is taken by the player.
     */
    public void item_taken(){
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

        // Change transformation matrix

        // TODO: Esto no se situa donde debe
        g.translate(g.getCanvas().getLeft() + (int)_pos._x,
                g.getCanvas().getTop() + ((int)_pos._y * (-1)));

        g.rotate(_rot);

        // Draw square
        g.drawLine(-n.width/2, -n.height/2, n.width/2, -n.height/2);
        g.drawLine(-n.width/2, -n.height/2, -n.width/2, n.height/2);
        g.drawLine(n.width/2, -n.height/2, n.width/2, n.height/2);
        g.drawLine(-n.width/2, n.height/2, n.width/2, n.height/2);

        // Reset canvas after drawing
        g.restore();
    } // render
} // Item
