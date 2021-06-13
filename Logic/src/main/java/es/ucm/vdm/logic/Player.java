package es.ucm.vdm.logic;

// JAVA
import java.util.ArrayList;

// UCM
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Rect;
import es.ucm.vdm.engine.VDMColor;
import static es.ucm.vdm.logic.Utils.rotVect;
import static es.ucm.vdm.logic.Utils.subVect;

/**
 * Player GameObject. Manages all the logic related to the player, like distance moved and rendering
 * process.
 */
public class Player extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    int _w, _h;
    Vector2 _dir; // Direction, points at where the Player is moving
    double _dist; // distance to initial position of the path
    float _lv; // Linear velocity
    final float _flv = 1500; // Flying linear velocity (constant)
    float _av; // Angular velocity
    ArrayList<Vector2> _path; // Path that the player has to follow
    int _pathDir; // Direction of the path
    int _actualPoint; // Last point that Player went through
    boolean _flying; // flag to detect if player is flying
    Vector2 _jumpPos; // Jumping position. Used to calculate collisions
    PlayGameState _pg; // Instance of PlayGameState to communicate with and check collisions
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------

    /**
     * Constructor of a new player, receives the initial position of the player, the color to use
     * to paint the player, the width and height the player has, the linear velocity that will
     * use to update it's position and the angular velocity that will use to update it's rotation.
     *
     * @param x (double) Init X position
     * @param y (double) Init Y position
     * @param c (int) Color
     * @param w (int) Width of the player
     * @param h (int) Height of the player
     * @param lv (float) Linear velocity
     * @param av (float) Angular velocity
     */
    public Player(double x, double y, VDMColor c, int w, int h, int thickness, float lv, float av, PlayGameState pg){
        super(x, y, c, thickness);
        _dir = new Vector2(0, 0);
        _w = w;
        _h = h;
        _lv = lv;
        _av = av;
        _act = true;
        _pg = pg;
        _pathDir = 1;
        _flying = false;
        _dist = 0;
    } // Player

    /**
     * Function to call when the path has changed. Sets a coordinate path for the player to follow.
     *
     * @param p (ArrayList) Array with all the different positions.
     */
    public void setPath(ArrayList p, int initPoint, int endingPoint){
        _path = p;

        _initPos = (Vector2)p.get(initPoint);

        calculateDirection(initPoint, endingPoint);
    } // set_path

    /**
     * Reset Player data: direction, position, distance, flying, etc.
     *
     * @param pos (Vector2) New position of the Player.
     * @param p (ArrayList) New path to place the player.
     */
    public void resetPlayer(Vector2 pos, ArrayList p){
        _dir = new Vector2(0, 0);
        _pos = _initPos = new Vector2(pos._x, pos._y);
        _rot = 0;
        _pathDir = 1;
        _act = true;
        _flying = false;
        _actualPoint = 0;
        _dist = 0;
        setPath(p, 0, 1);
    } // resetPlayer

    /**
     * Function to check if player is flying.
     *
     * @return (boolean) Flying state.
     */
    public boolean isFlying(){
        return _flying;
    } // is_flying

    /**
     * Function to notify player of collision with path. Receives the index of both point that
     * made the segment of collision to calculate new movement direction.
     *
     * @param nPosition (Vector2) Collision point to set the player position
     * @param nPath (ArrayList) The new path
     * @param point1 (int) First point that made the collision
     * @param point2 (int) Second point that made the collision
     */
    public void pathCollide(Vector2 nPosition, ArrayList<Vector2> nPath, int point1, int point2){
        _path = nPath;

        // Now calculate the new direction to fit with the direction that player had before
        if(_pathDir == -1){
            // If moving direction was reverse to the path direction, set moving direction to
            // match path direction
            _pathDir = 1;

            // Calculate direction to follow with the 2 points in order
            calculateDirection(point1, point2);
            _actualPoint = point1;
            _dist = Math.abs(Utils.subVect(_path.get(point1), nPosition).magnitude());
            _initPos = (Vector2)nPath.get(point1);
        } // if
        else{
            // If moving direction was reverse to the path direction, set moving direction to
            // match path direction
            _pathDir = -1;

            // Calculate direction to follow with 2 points in reverse
            calculateDirection(point2, point1);
            _actualPoint = point2;
            _dist = Math.abs(Utils.subVect(_path.get(point2), nPosition).magnitude());
            _initPos = (Vector2)nPath.get(point2);
        } // else

        _flying = false;
    } // path_collide

    /**
     * Function to return the actual direction. Used to calculate the next direction.
     *
     * @return (Vector2) Actual direction.
     */
    public Vector2 getDir(){
        return _dir;
    } // get_dir

    /**
     * Function to calculate the direction from one point to another.
     *
     * @param p (int) Initial point.
     * @param n (int) Next point.
     */
    private void calculateDirection(int p, int n){
        Vector2 init = _path.get(p);
        Vector2 next = _path.get(n);

        _dir = subVect(next, init);

        _dir.normalize();
    } // calculate_direction

    /**
     * Function used to check if player can loop the path.
     *
     * @return (int)
     */
    public int checkLoop(){
        // Toroid
        if(_actualPoint + _pathDir == _path.size()){
            return 0;
        } // if
        else if(_actualPoint + _pathDir < 0){
            return _path.size() - 1;
        } // else if
        else{
            return _actualPoint + _pathDir;
        } // else
    } // check_loop

    /**
     * Function called when the user clicks the screen. The player begins to move in some direction.
     * If direction is provided by the one that calls this function, that means the path had a
     * direction jump preset and use that direction for jumping. If not, is important to calculate
     * the new direction.
     */
    public void fly(Vector2 dir){
        if(!_flying) {
            _flying = true;

            if (dir != null) {
                _dir = dir;
            } // Set jumping direction
            else { // if not, calculate a new one
                if (_pathDir == -1) {
                    _dir = rotVect(_dir, 90);
                } // if
                else {
                    _dir = rotVect(_dir, -90);
                } // else
            } // else

            _dir.normalize();
            _jumpPos = new Vector2(_pos._x + ((_w / 8) * _dir._x),
                                   _pos._y + ((_w / 8) * _dir._y));
        } // if
    } // fly

    /**
     * Gets the index of path which points the actual point.
     *
     * @return (int) Actual point where the player is moving.
     */
    public int getActualPoint(){
        return _actualPoint;
    } // get_actualPoint

    /**
     * Updates the position and rotation of the player using time elapsed between frames to
     * calculate the next step.
     *
     * @param t (double) Time elapsed
     */
    @Override
    public void update(double t) {
        if(_act) {
            // Update position
            if (!_flying) {
                _dist += _lv * t;

                _pos._x = _initPos._x + (_dist * _dir._unit._x);
                _pos._y = _initPos._y + (_dist * _dir._unit._y);

                _pg.checkCollisions(new Vector2(_initPos._x, _initPos._y), new Vector2(_pos._x, _pos._y));

                // Check for looping path
                int next = checkLoop();

                Vector2 pathDist = Utils.subVect(_path.get(_actualPoint), _path.get(next));

                // Check path
                if (_dist > Math.abs(pathDist.magnitude())) {
                    _actualPoint = next;
                    next = checkLoop();

                    _pos._x = _path.get(_actualPoint)._x;
                    _pos._y = _path.get(_actualPoint)._y;

                    _dist = 0;
                    _initPos = _path.get(_actualPoint);
                    calculateDirection(_actualPoint, next);
                } // if distance
            } // if
            else {
                double x = _pos._x + (_dir._unit._x) * 0.1;
                double y = _pos._y + (_dir._unit._y) * 0.1;

                _pos._x = (_pos._x) + (((_dir._unit._x) * _flv) * t) + (_dir._unit._x) * (_w / 9.7);
                _pos._y = (_pos._y) + (((_dir._unit._y) * _flv) * t) + (_dir._unit._y) * (_h / 9.7);

                _pg.checkCollisions(new Vector2(x, y), _pos);
            } // else

            // Rotation is constant
            _rot += _av * t;

            if (_rot >= 360) {
                _rot = 0;
            } // if
        } // if active
    } // update

    /**
     * Draw the player applying the rotation and new position.
     *
     * @param g (Graphics) Graphics instance to paint Player
     */
    @Override
    public void render(Graphics g) {
        if(_act) {
            Rect o = new Rect(_w, 0, 0, _h);
            Rect n = g.scale(o, g.getCanvas());

            // Set color to paint the player
            g.setColor(_c);
            // Save actual canvas Transformation matrix
            g.save();

            // Change transformation matrix
            g.translate((int) _coordOrigin._x + (int) _pos._x,
                    (int) _coordOrigin._y + ((int) _pos._y * (-1)));

            g.rotate(_rot);

            // Draw square
            g.drawLine(-n.width / 2, -n.height / 2,
                    n.width / 2, -n.height / 2, _lineThickness);
            g.drawLine(-n.width / 2, -n.height / 2,
                    -n.width / 2, n.height / 2, _lineThickness);
            g.drawLine(n.width / 2, -n.height / 2,
                    n.width / 2, n.height / 2, _lineThickness);
            g.drawLine(-n.width / 2, n.height / 2,
                    n.width / 2, n.height / 2, _lineThickness);

            // Reset canvas after drawing
            g.restore();
        } // if active
    } // render
} // Player
