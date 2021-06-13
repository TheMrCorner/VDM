package es.ucm.vdm.logic;

// UCM
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.VDMColor;

/**
 * Particle class and object to make the explosion effect for the player when it collides with
 * an enemy.
 */
public class Particle extends GameObject {

    // Variables
    int _len; // Length of the line particle
    double _dist; // distance to origin of ParticleSystem.
    private Vector2 _dir; // Direction the particle is moving
    float _av; // Angular velocity
    float _lv; // Linear velocity

    /**
     * Particle constructor. Creates and object of Type Particle with the different parameters
     * provided.
     *
     * @param x (double) X position (of the ParticleSystem).
     * @param y (double) Y position (of the ParticleSystem).
     * @param c (VDMColor) Color to paint the Particle.
     * @param thickness (int) Thickness of the line of the Particle.
     * @param length (int) Length of the line of the particle.
     * @param angVel (float) Angular velocity for rotating the particle from it's center.
     * @param linVel (float) Linear speed for moving the particle.
     * @param dir (Vector2) Direction in which the particle is moving.
     * @param rot (float) Initial rotation of the particle.
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
    } // Constructor

    /**
     * Update the position and rotation of the particle using the time elapsed.
     *
     * @param t (double) Time elapsed
     */
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

    /**
     * Render the particle current state on screen.
     *
     * @param g (Graphics) Graphics instance to paint it
     */
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
