package es.ucm.vdm.logic;

import es.ucm.vdm.engine.GameObject;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Vector2;

import static es.ucm.vdm.logic.Utils.rotVect;

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

    public Enemy(double x, double y, int c, int length, float angle, float angularSpeed,
                 Vector2 linearSpeed, float waitTime, Vector2 direction){
        super(x, y,c);
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
     * @return (Vector2[])
     */
    public Vector2[] get_segment(){
        // Create a vector array with 2 positions
        Vector2[] seg = new Vector2[2];

        // First calculate init point rotating the lower half of the enemy
        Vector2 init = rotVect(new Vector2(_pos._x - (_len/2), _pos._y), _rot);

        // then calculate ending point rotating the upper half of the enemy
        Vector2 end = rotVect(new Vector2(_pos._x + (_len/2), _pos._y), -_rot);

        seg[0] = init;
        seg[1] = end;

        return seg;
    } // get_segment

    @Override
    public void update(double t) {
        _rot += _aSp * t;

        if(_rot >= 360){
            _rot = 0;
        } // if

        if(_dir != null){
            if(!_waiting) {
                _pos._x += ((_dir._unit._x * _lSp._x) * t);
                _pos._y += ((_dir._unit._y * _lSp._y) * t);
                distX += ((_dir._unit._x * _lSp._x) * t);
                distY += ((_dir._unit._y * _lSp._y) * t);

                if(Math.abs(distX) >= Math.abs(_dir._x) && _lSp._x != 0){
                    System.out.println("Max pos reached in X: " + distX + " ");
                    _dir._unit._x *= -1;
                    distX = 0;
                    waited = _wait * 1000;
                    _waiting = true;
                } // if
                if(Math.abs(distY) >= Math.abs(_dir._y) && _lSp._y != 0){
                    System.out.println("Max pos reached in Y: " + distY + " ");
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

        g.drawLine(-g.repositionX(_len/ 2),  0, g.repositionX(_len / 2), 0);

        // Reset canvas after drawing
        g.restore();
    } // render
} // Enemy
