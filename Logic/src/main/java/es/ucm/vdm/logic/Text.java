package es.ucm.vdm.logic;

import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.VDMColor;

public class Text extends GameObject {

    private boolean _b;
    private String _t;
    private String _f;

    /**
     * Constructor of the GameObject. Creates a new GameObject and assigns the position, the color
     * and the rotation that the object will have. To make this generic, initializes _rot to 0.
     *
     * @param x         (double) X coordinate.
     * @param y         (double) Y coordinate
     * @param c         (int) Color
     * @param thickness
     */
    public Text(double x, double y, VDMColor c, int thickness,
                String text, boolean bold, String font) {
        super(x, y, c, thickness);

        _b = bold;
        _t = text;
        _f = font;
    } // Constructor

    @Override
    public void render(Graphics g) {

        int x = g.repositionX((int) _coordOrigin._x + (int) _pos._x);
        int y = g.repositionY((int) _coordOrigin._y + ((int) _pos._y * (-1)));

        g.newFont(_f, g.repositionX(_lineThickness), _b);
        g.setColor(_c);
        g.drawText(_t, g.getCanvas().getX() + x, g.getCanvas().getY() + y);
    } // render
} // Text
