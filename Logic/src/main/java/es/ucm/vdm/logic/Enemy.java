package es.ucm.vdm.logic;

// UCM
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.VDMColor;

/**
 * Enemy class and object. Inherits from GameObject and can rotate,
 * move or stay still. Kills the player when it touches.
 */
public class Enemy extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    int _len; // Length of the enemy
    float _aSp; // Rotation velocity if it has
    Vector2 _lSp; // Linear speed
    float _wait;
    float waited;
    boolean _waiting;
    Vector2 _dir; // Direction in which enemy is moving
    double distX, distY;
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------

    /**
     * Creates a new enemy with the parameters specified.
     *
     * @param x (double) X position at the center of the segment.
     * @param y (double) Y position at the center of the segment.
     * @param c (VDMColor) Color to paint the enemy
     * @param thickness (int) Thickness of the line.
     * @param length (int) Length of the enemy.
     * @param angle (float) Angle that the enemy is rotated.
     * @param angularSpeed (float) Rotation speed.
     * @param linearSpeed (Vector2) Linear speed.
     * @param waitTime (float) Time to wait to move again.
     * @param direction (Vector2) Direction in which the enemy moves.
     */
    public Enemy(double x, double y, VDMColor c, int thickness, int length, float angle,
                 float angularSpeed, Vector2 linearSpeed, float waitTime, Vector2 direction){
        super(x, y, c, thickness);
        _len = length;
        _rot = angle;
        _aSp = angularSpeed;
        _lSp = linearSpeed;
        _dir = direction;
        if(_dir != null) {
            _dir.normalize();
        } // if
        distX = distY = 0;
        _wait = waitTime;
        waited = 0;
        _waiting = false;
    } // Enemy

    /**
     * Returns the segment made by this enemy.
     *
     * @return (Vector2[]) Init and End position of the enemy's segment.
     */
    public Vector2[] getSegment(){
        // Create a vector array with 2 positions
        Vector2[] seg = new Vector2[2];
        Vector2 init = new Vector2(0, 0);
        Vector2 end = new Vector2(0, 0);

        double m = Math.round(1/Math.tan(Math.toRadians(_rot)));

        if(m == 0){ // If 0, segment is vertical
            init = new Vector2(_pos._x, _pos._y - (_len/2));
            end = new Vector2(_pos._x, _pos._y + (_len/2));
        } // if
        else{
            // Calculate init
            double a = (_len/2) * Math.cos(Math.toRadians(_rot));
            double b = (_len/2) * Math.sin(Math.toRadians(_rot));

            init._x = _pos._x - a;
            init._y = _pos._y - b;

            // Calculate end
            end._x = _pos._x + a;
            end._y = _pos._y + b;
        } // else

        seg[0] = init;
        seg[1] = end;

        return seg;
    } // get_segment

    /**
     * Update the position and rotation of the enemy using the time elapsed between frames as
     * a reference.
     *
     * @param t (double) Time elapsed
     */
    @Override
    public void update(double t) {
        _rot += _aSp * t;

        // Return rotation to 0
        if(_rot >= 360){
            _rot = 0;
        } // if

        // Check if it moves in any direction
        if(_dir != null){
            // Check if it has to wait.
            if(!_waiting) {
                _pos._x += ((_dir._unit._x * _lSp._x) * t);
                _pos._y += ((_dir._unit._y * _lSp._y) * t);
                distX += ((_dir._unit._x * _lSp._x) * t);
                distY += ((_dir._unit._y * _lSp._y) * t);

                if(Math.abs(distX) >= Math.abs(_dir._x) && _lSp._x != 0){
                    _dir._unit._x *= -1;
                    distX = 0;
                    waited = _wait * 1000;
                    _waiting = true;
                } // if
                if(Math.abs(distY) >= Math.abs(_dir._y) && _lSp._y != 0){
                    _dir._unit._y *= -1;
                    distY = 0;
                    waited = _wait * 1000;
                    _waiting = true;
                } // if
            } // if
            else{
                waited -= t * 1000;
                if(waited <= 0){
                    _waiting = false;
                } // if
            } // else
        } // if
    } // update

    /**
     * Renders the enemy rotating the matrix and moving it.
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
} // Enemy
