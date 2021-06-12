package es.ucm.vdm.logic;

import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.VDMColor;

public class Particle extends GameObject {

    int _len; // Length of the line particle
    double _dist; // distance to origin
    private Vector2 _dir; // Direction the particle is moving
    float _av; // Angular velocity
    float _lv; // Linear velocity

    /**
     * Constructor of the GameObject. Creates a new GameObject and assigns the position, the color
     * and the rotation that the object will have. To make this generic, initializes _rot to 0.
     *
     * @param x         (double) X coordinate.
     * @param y         (double) Y coordinate
     * @param c         (int) Color
     * @param thickness
     */
    public Particle(double x, double y, VDMColor c, int thickness, int length, float angVel,
                    float linVel, Vector2 dir, float rot) {
        super(x, y, c, thickness);
        _len = length;
        _av = angVel;
        _lv = linVel;
        _dist = 0.7;
        _dir = new Vector2(dir._x, dir._y);
        _rot = rot;
    }

    @Override
    public void update(double t) {
        if(_act) {
            _dist += _lv * t;

            _pos._x = _initPos._x + (_dist * _dir._x);
            _pos._y = _initPos._y + (_dist * _dir._y);

            // Rotation is constant
            _rot += _av * t;

            if (_rot >= 360) {
                _rot = 0;
            } // if

            if(_lv > 0) {
                _lv -= 0.01;
            } // if
            else{
                _lv = 0.0f;
            }

            if(_av > 0) {
                _av -= 1;
            } // if
            else{
                _av = 0.0f;
            }
        } // if
    } // update

    @Override
    public void render(Graphics g) {
        // Set color to paint the player
        g.setColor(_c);
        // Save actual canvas Transformation matrix
        g.save();

        // Change transformation matrix
        g.translate((int)_coordOrigin._x + (int)_pos._x,
                (int)_coordOrigin._y + ((int)_pos._y *(-1)));

        g.rotate(-_rot);

        g.drawLine(-g.repositionX(_len/ 2),  0,
                g.repositionX(_len / 2), 0, _lineThickness);

        // Reset canvas after drawing
        g.restore();
    } // render
} // Particle
