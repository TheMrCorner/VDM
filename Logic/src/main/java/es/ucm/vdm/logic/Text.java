package es.ucm.vdm.logic;

// UCM
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.VDMColor;

/**
 * Text GameObject that stores the font with which to paint the text, the text to write and the
 * different parameters.
 */
public class Text extends GameObject {

    private boolean _b;
    private String _t;
    private String _f;

    /**
     * Constructor of Text GameObject. Creates a new Text object with all the parameters provided
     * to apply it for the text.
     *
     * @param x (double) X position of the text.
     * @param y (double) Y position of the text.
     * @param c (VDMColor) Color to paint the text.
     * @param thickness (int) Thickness of the text. (Font size)
     * @param text (String) Text to write
     * @param bold (boolean) Boolean to check if text is bold or not.
     * @param font (String) Font path that will use to paint itself.
     */
    public Text(double x, double y, VDMColor c, int thickness,
                String text, boolean bold, String font) {
        super(x, y, c, thickness);

        _b = bold;
        _t = text;
        _f = font;
    } // Constructor

    /**
     * Renders the text in the current position.
     *
     * @param g (Graphics) Graphics instance to paint it.
     */
    @Override
    public void render(Graphics g) {

        int x = g.repositionX((int) _coordOrigin._x + (int) _pos._x);
        int y = g.repositionY((int) _coordOrigin._y + ((int) _pos._y * (-1)));

        g.newFont(_f, g.repositionX(_lineThickness), _b);
        g.setColor(_c);
        g.drawText(_t, g.getCanvas().getX() + x, g.getCanvas().getY() + y);
    } // render
} // Text
