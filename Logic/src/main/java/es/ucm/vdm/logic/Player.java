package es.ucm.vdm.logic;

import es.ucm.vdm.engine.GameObject;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Rect;
import es.ucm.vdm.engine.Vector2;

public class Player extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    int _w, _h;
    Vector2 _dir; // Direction, points at where the Player is moving
    float _lv; // Linear velocity
    float _av; // Angular velocity
    Vector2[] _path; // Path that the player has to follow
    int _pathDir; // Direction of the path
    int _lastPoint; // Last point that Player went through


    public Player(int x, int y, int c, int w, int h, float lv, float av){
        super(x, y, c);
        _dir = new Vector2(0.35, 0);
        _w = w;
        _h = h;
        _lv = lv;
        _av = av;
    } // Player

    /**
     * Function to call when the path has changed. Sets a coordinate path for the player to follow.
     *
     * @param p (Vector2[]) Array with all the different positions.
     */
    public void set_path(Vector2[] p){
        _path = p;
    } // set_path

    /**
     * Updates the position and rotation of the player using time elapsed between frames to
     * calculate the next step.
     *
     * @param t (double) Time elapsed
     */
    @Override
    public void update(double t) {
        // Update position
        _pos._x += ((_dir._x * _lv) * t);
        _pos._y += ((_dir._y * _lv) * t);

        _rot += _av * t;

        if(_rot >= 360){
            _rot = 0;
        }
    } // update

    /**
     * Draw the player applying the rotation and new position.
     *
     * @param g (Graphics) Graphics instance to paint Player
     */
    @Override
    public void render(Graphics g) {
        Rect o = new Rect(_w, 0, 0, _h);
        Rect n = g.scale(o, g.getCanvas());

        // Set color to paint the player
        g.setColor(_c);
        // Save actual canvas Transformation matrix
        g.save();

        // Change transformation matrix
        g.translate(g.getCanvas().getLeft() + (int)_pos._x,
                g.getCanvas().getTop() + (int)_pos._y);

        g.rotate(_rot);

        // Draw square
        g.drawLine(-n.width/2, -n.height/2, n.width/2, -n.height/2);
        g.drawLine(-n.width/2, -n.height/2, -n.width/2, n.height/2);
        g.drawLine(n.width/2, -n.height/2, n.width/2, n.height/2);
        g.drawLine(-n.width/2, n.height/2, n.width/2, n.height/2);

        // Reset canvas after drawing
        g.restore();
    } // render
} // Player
