package es.ucm.vdm.logic;

// Utils
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

// UCM
import es.ucm.vdm.engine.Engine;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Input;
import sun.security.ssl.Debug;

import static es.ucm.vdm.logic.Utils.parseDouble;
import static es.ucm.vdm.logic.Utils.segmentsIntersection;

public class PlayGameState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------

    //---------------------Object Pools-------------------------------
    //ArrayList<GameObject> _go; // GameObject pool
    ArrayList<GameObject> _it;
    ArrayList<GameObject> _en;
    ArrayList<GameObject> _lf;
    Path _paths;
    Player _player;

    //---------------------------------------------------------------
    Logic _l; // For changing gamestate
    String _name; // Name of the actual level
    JSONObject _level;
    int _nLevel; // Number of level, for writing
    int _diffLevel; // Difficulty
    int _posOrX; // Pos of coord origin X
    int _posOrY; // Pos of coord origin Y
    boolean _dead; // Flag to check that player is dead
    double _countdown = 0; // Variable to count


    // Constant values for colors and etc.
    int _playerC = 0xFF0000FF; // Player color ARGB
    int _itemC = 0xFFFFF200; // Item color ARGB
    int _enemyC = 0xFFFF0000; // Enemy color ARGB
    int _lineThickness = 4;
    int _playerWidth = 12; // Player width
    int _itemWidth = 8; // Item width
    int _itemDist = 20;
    float _aVel = 180; // Angular velocity for all player and items
    int _currLife = 0; // Current life (last life lost)
    int _playerVel;
    double _countdownInit = 1; // Count down to wait for dead resolution or win resolution (1 second)
    float _expansionVel = 5;
    float _transpVel = 1;

    /**
     * Generates a new level from a JSONObject provided in the constructor.
     *
     * @param level (JSONObject) Level to be loaded.
     * @param nlev (int) Number of the actual level.
     * @param diff (boolean) Difficulty level
     */
    public PlayGameState(JSONObject level, int nlev, int diff, Logic l){
        // Init GameObject pool
        //_go = new ArrayList<GameObject>();
        _lf = new ArrayList<GameObject>();
        _diffLevel = diff;
        _l = l;

        _posOrY = (int)_l._cnv.height/2;
        _posOrX = (int)_l._cnv.width/2;

        // Parse all data from the level
        _nLevel = nlev + 1;

        // Create lifes
        // Lifes
        int numLifes, lifeXposition, lifeYposition, vel;
        // Check number of lifes depending on game difficulty
        if(_diffLevel == 0){
            numLifes = 10;
            _playerVel = 250;
        } // if
        else{
            numLifes = 5;
            _playerVel = 400;
        } // else

        // Then create lifes and place them in scene.
        Life nLife;
        lifeYposition = 20; // Set Y position
        for(int lf = 0; lf < numLifes; lf++){
            // Set X position relative to number of lifes and canvas width
            lifeXposition = _l.getCanvasSize().width - (20 * (lf + 1));
            nLife = new Life(lifeXposition, lifeYposition, _playerC, _enemyC,
                    _playerWidth, _playerWidth, 45, _lineThickness);
            _lf.add(nLife);
        } // for

        // Save level for reseting
        _level = level;

        parseLevel(level);
    } // PlayGameState

    /**
     * Function that parses all information relative to the level and builds it with the new
     * information, placing all the different objects
     *
     * @param level (JSONObject) And Object with all the information about the actual level
     */
    private void parseLevel(JSONObject level){
        _it = new ArrayList<GameObject>();
        _en = new ArrayList<GameObject>();
        _name = (String) level.get("name"); // Name of the level
        JSONArray p = (JSONArray) level.get("paths"); // Paths of the level
        JSONArray i = (JSONArray) level.get("items"); // Items
        JSONArray e = (JSONArray) level.get("enemies"); // Enemies

        // Create Path
        _paths = new Path(_posOrX, _posOrY, 0xFFFFFFFF, _lineThickness, p); // Path object

        createItems(i);

        // Create enemies, if there are enemies
        if(e != null){
            createEnemies(e);
        } // if

        // Player
        _player =  new Player(_paths.getInitPos()._x, _paths.getInitPos()._y, _playerC,
                _playerWidth, _playerWidth, _lineThickness, _playerVel, _aVel, this);
        _player.setPath(_paths.getPaths().get(0), 0, 1);
        _player.setCoordOrigin(new Vector2(_posOrX, _posOrY));
        _paths.setActivePath(0);
    } // parse_level

    /**
     * Function to reset the level and put everything to how it was at the beginning
     */
    private void resetLevel(){
        // Reset items
        for(int i = 0; i < _it.size(); i++){
            ((Item)_it.get(i)).resetItem();
        } // for

        // Reset player position
        _player.resetPlayer(_paths.getInitPos(), _paths.getPaths().get(0));
        _paths.setActivePath(0);
        _dead = false;
    } // resetLevel

    /**
     * Function that parses and creates all the items in the scene.
     *
     * @param i (JSONArray) An array with all the different items.
     */
    private void createItems(JSONArray i){
        // Create items
        Item nItem;
        JSONObject coord;
        double coordX, coordY;

        for(int j = 0; j < i.size(); j++){
            // Get object with coordinates
            coord = (JSONObject)i.get(j);

            // Get each coordinate value
            Object nx = coord.get("x");
            Object ny = coord.get("y");

            // First parse coordinates
            // Check value
            coordX = parseDouble(nx);
            coordY = parseDouble(ny);

            // Then check if it from rotating type
            if(coord.containsKey("radius")){
                // If it is from rotating type
                Object ang = coord.get("angle");
                Object mag = coord.get("radius");
                Object angVel = coord.get("speed");
                double trueAng;
                double magnitude;
                double totalAngularVelocity;

                // First parse the angle to get the position
                trueAng = parseDouble(ang);

                // Then parse the radius to get the magnitude of the vector (distance really)
                magnitude = parseDouble(mag);

                // Finally, parse the angular velocity that all items will have.
                totalAngularVelocity = parseDouble(angVel);

                // Now, using simple trigonometry, get the position at which the object should be
                // With multiplication of magnitude by cos and sin we get how much will add to
                // each coordinate to get the position. Due to Java Math.cos/sin use the angle
                // value as radians, it is important to convert angle to radians. Then round the
                // result to get real result, because when cos/sin return values too near to 0
                // they give a number like 1.89423678*10^-16, too small.
                coordX += magnitude * Math.round(Math.sin(Math.toRadians(trueAng)));
                coordY += magnitude * Math.round(Math.cos(Math.toRadians(trueAng)));

                nItem = new Item(coordX, coordY, _itemC, _itemWidth,
                        _itemWidth, _aVel, _lineThickness, (float)totalAngularVelocity,
                        (float)trueAng, (float)magnitude, _expansionVel, _transpVel);
            } // if
            else{
                // Else create a normal Item
                nItem = new Item(coordX, coordY, _itemC, _itemWidth,
                        _itemWidth, _aVel, _lineThickness, _expansionVel, _transpVel);
            } // else

            nItem.setCoordOrigin(new Vector2(_posOrX, _posOrY));
            _it.add(nItem);
        } // for
    } // create_items

    /**
     * Function to create and parse the information of the enemies.
     *
     * @param e (JSONArray) An array with all the enemies.
     */
    private void createEnemies(JSONArray e) {
        double coordX, coordY;
        JSONObject enemyData;
        double length, angle, angSpeed, waitTime;
        Vector2 direction, linearSpeed;
        Enemy nEnemy;
        for(int en = 0; en < e.size(); en++) {
            enemyData = (JSONObject) e.get(en);

            Object nx = enemyData.get("x");
            Object ny = enemyData.get("y");
            Object le = enemyData.get("length");
            Object an = enemyData.get("angle");

            // First parse coordinates
            // Check value
            coordX = parseDouble(nx);
            coordY = parseDouble(ny);
            // Parse length
            length = parseDouble(le);

            // Parse angle
            angle = parseDouble(an);

            // Parse angular speed
            if (enemyData.containsKey("speed")) {
                Object sp = enemyData.get("speed");
                angSpeed = parseDouble(sp);
            } // if
            else {
                angSpeed = 0;
            } // else

            if (enemyData.containsKey("offset")) {
                Object offX = ((JSONObject) enemyData.get("offset")).get("x");
                Object offY = ((JSONObject) enemyData.get("offset")).get("y");
                Object time1 = enemyData.get("time1");
                Object time2 = enemyData.get("time2");

                // Calculate vector between positions
                direction = new Vector2(parseDouble(offX), parseDouble(offY));

                double timeMove = parseDouble(time1);

                // Calculate linear speed
                linearSpeed = new Vector2(Math.abs(direction._x / timeMove),
                        Math.abs(direction._y / timeMove));

                waitTime = parseDouble(time2);
            } // if
            else {
                linearSpeed = null;
                waitTime = 0;
                direction = null;
            } // else

            nEnemy = new Enemy(coordX, coordY, _enemyC, _lineThickness, (int) length - 2, (int) angle,
                    (float) angSpeed, linearSpeed, (float) waitTime, direction);
            nEnemy.setCoordOrigin(new Vector2(_posOrX, _posOrY));
            _en.add(nEnemy);
        } // for
    } // create_enemies

    /**
     * Sets a logic instance in this GameState for indicating ending of game, levels, get a new
     * level and indicate changes of state.
     *
     * @param l (Logic) Logic's instance.
     */
    public void setLogic(Logic l){
        _l = l;
    } // set_logic

    /**
     * Function to check the collisions between tha player and the rest of the Objects on screen.
     *
     * @param segINIT (Vector2) Initial point of the segment to check.
     * @param segEND (Vector2) Ending point of the segment to check.
     */
    public void checkCollisions(Vector2 segINIT, Vector2 segEND){
        // First check enemies
        Vector2 collision;
        for(int i = 0; i < _en.size(); i++){
            Vector2[] enSeg = ((Enemy)_en.get(i)).getSegment();
            collision = segmentsIntersection(segINIT, segEND, enSeg[0], enSeg[1]);
            if(collision != null){
                // KILL PLAYER
                killPlayer();
            } // if
        } // for

        // ONLY WHEN FLYING AND NOT DEAD
        if(_player.isFlying() && !_dead) {
            // Then check items
            for(int i = 0; i < _it.size(); i++) {
                double dist = Math.sqrt(Utils.sqrDistancePointSegment(_player.getJumpPos(), segEND, _it.get(i).getPos()));

                if (dist != -1) {
                    if (dist < _itemDist) {
                        ((Item) _it.get(i)).itemTaken();
                    } // if
                } // if
            } // for

            // Check paths
            for(int i = 0; i < _paths.getPaths().size(); i++){
                for(int j = 0; j < _paths.getPaths().get(i).size(); j++){
                    int next = j + 1;
                    if(next == _paths.getPaths().get(i).size()){
                        next = 0;
                    } // if toroid
                    collision = segmentsIntersection(_player.getJumpPos(), segEND,
                            _paths.getPaths().get(i).get(j), _paths.getPaths().get(i).get(next));

                    if(collision != null){
                        _player.pathCollide(collision, _paths.getPaths().get(i), j, next);
                        _paths.setActivePath(i);
                    } // if
                    // If no collision, keep running till infinite
                } // Vertex for
            } // Paths for

            // Check boundaries
            int posX = _posOrX + (int) segEND._x;
            int posY = _posOrY + ((int) segEND._y * (-1));
            if(_l.checkWindowBoundaries(posX, posY)){
                killPlayer();
            } // if
        } // is_flying
    } // check_collisions

    private void killPlayer(){
        _player.setActive(false);
        _dead = true;
        ((Life)_lf.get(_currLife)).loseLife();
        _currLife++;

        _countdown = _countdownInit;
    } // kill_player

    /**
     * Update all the objects in this state and check the collisions between Player and the rest of
     * the objects.
     *
     * @param t (double) Time elapsed since the last frame.
     */
    @Override
    public void update(double t) {
        // First update all items
        for (int i = 0; i < _it.size(); i++) {
            _it.get(i).update(t);
        } // for

        // Then update enemies
        for (int i = 0; i < _en.size(); i++) {
            _en.get(i).update(t);
        } // for

        // Update player
        if(_countdown <= 0) {
            _player.update(t);
        }
        else{
            _countdown -= t;

            if(_countdown <= 0){
                // !!!!Esto se debería hacer de otra manera,
                // pero no me da tiempo a tenerlo para ahora
                resetLevel();
            } // if
        } // else
    } // update

    @Override
    public void render(Graphics g) {
        // Render paths first
        _paths.render(g);

        // Then render items
        for(int i = 0; i < _it.size(); i++) {
            _it.get(i).render(g);
        } // for

        // Next render enemies
        for(int i = 0; i < _en.size(); i++) {
            _en.get(i).render(g);
        } // for

        // Then render Player
        _player.render(g);

        // Render lives
        for(int i = 0; i < _lf.size(); i++) {
            _lf.get(i).render(g);
        } // for

        //TODO: change this to actual level info
        /*g.newFont("Resources/Fonts/Bungee-Regular.ttf", g.repositionX(20), false);

        g.save();

        g.translate(_posOrX, _posOrY);

        g.drawText("test", 0, 0);

        g.restore();*/
        // Render UI

    } // render


    /**
     * Process all input incoming form the Input class. If mouse clicked or screen touched, throw
     * player to his right until it hits a new line or leaves the play zone.
     *
     * @param e (List<Input.TouchEvent>) Event list taken from the Input class
     */
    @Override
    public void processInput(List<Input.TouchEvent> e) {
        // int ptr = e.size() - 1; // Pointer to roam the list
        int ptr = 0;

        while(ptr < e.size()){ // While list is not empty...
            Input.TouchEvent te = e.get(ptr); // Get touch event at pointers position

            switch(te.getType()){
                case PRESSED_DOWN:
                case KEY_PRESSED:
                    // Same mechanism as with mouse or touchscreen but with keyboard.
                    // Tell the _player to begin flight
                    _player.fly(_paths.getJumpDir(_paths.getActivePath(),
                            _player.getActualPoint()));
                    break;
                default:
                    // Ignore the rest
                    break;
            } // switch

            ptr++;
        } // while
    } // processInput
} // PlayGameState