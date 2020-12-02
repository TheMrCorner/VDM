package es.ucm.vdm.logic;

import es.ucm.vdm.engine.GameObject;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Vector2;

public class Player extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    int _w, _h;
    Vector2 _dir; // Direction, points at where the Player is moving
    float _lv; // Linear velocity
    float _av; // Angular velocity


    public Player(int x, int y, int c, int w, int h, float lv, float av){
        super(x, y, c);
        _w = w;
        _h = h;
        _lv = lv;
        _av = av;
    } // Player

    /**
     * Updates the position and rotation of the player using time elapsed between frames to
     * calculate the next step.
     *
     * @param t (double) Time elapsed
     */
    @Override
    public void update(double t) {
        // Update position
        _pos._x += (_dir._x * _lv) * t;
        _pos._y += (_dir._y * _lv) * t;
    } // update

    /**
     * Draw the player applying the rotation and new position.
     *
     * @param g (Graphics) Graphics instance to paint Player
     */
    @Override
    public void render(Graphics g) {
        // Set color to paint the player
        g.setColor(_c);
        // Save actual canvas Transformation matrix
        g.save();

        // Change transformation matrix
        g.translate((int)_pos._x, (int)_pos._y);
        g.rotate(_rot);

        // Draw square
        g.drawLine(-_w/2, -_h/2, _w/2, -_h/2);
        g.drawLine(-_w/2, -_h/2, -_w/2, _h/2);
        g.drawLine(_w/2, -_h/2, _w/2, _h/2);
        g.drawLine(-_w/2, _h/2, _w/2, _h/2);

        // Reset canvas after drawing
        g.restore();
    } // render
} // Player
