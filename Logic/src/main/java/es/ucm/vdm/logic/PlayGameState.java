package es.ucm.vdm.logic;

// JSON
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// JAVA
import java.util.ArrayList;
import java.util.List;

// UCM
import es.ucm.vdm.engine.Font;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Input;
import es.ucm.vdm.engine.VDMColor;
import static es.ucm.vdm.logic.Utils.parseDouble;
import static es.ucm.vdm.logic.Utils.segmentsIntersection;

/**
 * PlayGameState. This GameState controls the Game Scenario, checking collisions between objects,
 * checking when a level is completed, checking when the player is dead and processing all inputs.
 * Displays different banners for different situations (GameOver and Game complete). Displays the
 * UI and calls for all the different methods that are necessary for the game to work (updates,
 * renders and etc.)
 */
public class PlayGameState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------

    //---------------------Object Pools-------------------------------
    ArrayList<GameObject> _it; // Items
    ArrayList<GameObject> _en; // Enemies
    ArrayList<GameObject> _lf; // Lifes
    Path _paths; // Paths
    Player _player; // Player GO
    Text _levelText; // Level text
    ParticleSystem _part = null; // Particle system

    //---------------------------------------------------------------
    Logic _l; // For changing gamestate
    String _name; // Name of the actual level
    JSONObject _level; // Current level data
    int _nLevel; // Number of level, for writing
    int _diffLevel; // Difficulty
    boolean _dead = false; // Flag to check that player is dead
    boolean _gameOver = false; // Flag used to display the Game Over banner
    boolean _completed = false; // Flag to check if level is complete
    double _countdown = 0; // Variable to count
    boolean _pathCollision = false; // Collision with the path, for animation
    boolean _allLevelsCleared = false; // Check if all levels are complete
    boolean _yellowCongratulations = false; // Change Congratulations text
    double _timeInColor = 1.0; // Time in which the text will be of one color
    double _colorChangeCounter = 0.0; // Counter for the time


    // Constant values for colors and etc.
    VDMColor _playerC; // Player color ARGB
    VDMColor _itemC; // Item color ARGB
    VDMColor _enemyC; // Enemy color ARGB
    Vector2 _coordOr; // Coord origin
    int _coordOrX; // Coord origin X value
    int _coordOrY; // Coord origin Y value
    int _lineThickness = 4; // Line thickness
    int _playerWidth = 12; // Player width
    int _itemWidth = 8; // Item width
    int _itemDist = 20; // Item distance to take them
    float _aVel = 180; // Angular velocity for all player and items
    int _currLife = 0; // Current life (last life lost)
    int _playerVel; // Player speed
    double _countdownInit = 1; // Count down to wait for dead resolution or win resolution (1 second)
    float _expansionVel = 100; // Item expansion speed
    int _transpVel = 230; // Transparent speed
    float _collisionDesp = 0.03f; // Distance moved when player collides with the path.

    /**
     * Generates a new level from a JSONObject provided in the constructor.
     *
     * @param level (JSONObject) Level to be loaded.
     * @param nlev (int) Number of the actual level.
     * @param diff (boolean) Difficulty level.
     * @param l (Logic) Logic of the game.
     */
    public PlayGameState(JSONObject level, int nlev, int diff, Logic l){
        // Init GameObject pool
        //_go = new ArrayList<GameObject>();
        VDMColor colorPicker = new VDMColor();
        _playerC = colorPicker.getPlayerColor();
        _enemyC = colorPicker.getEnemyColor();
        _itemC = colorPicker.getItemColor();
        _lf = new ArrayList<GameObject>();
        _diffLevel = diff;
        _l = l;

        _coordOrX = _l._cnv.width/2;
        _coordOrY = _l._cnv.height/2;

        _coordOr = new Vector2(_coordOrX, _coordOrY);

        // Parse all data from the level
        _nLevel = nlev + 1;

        // Create lifes
        // Lifes
        int numLifes, lifeXposition, lifeYposition;
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
        lifeYposition = 25; // Set Y position
        for(int lf = 0; lf < numLifes; lf++){
            // Set X position relative to number of lifes and canvas width
            lifeXposition = _l.getCanvasSize().width - (20 * (lf + 1));
            nLife = new Life(lifeXposition, lifeYposition, _playerC, _enemyC,
                    _playerWidth, _playerWidth, 45, _lineThickness);
            _lf.add(nLife);
        } // for

        newLevel(level, nlev);
    } // PlayGameState

    /**
     * Load new level (if it is possible).
     *
     * @param level (JSONObject) Level to parse.
     * @param nlev (int) Number of level.
     */
    public void newLevel(JSONObject level, int nlev){
        if(level != null && nlev != -1) {
            _nLevel = nlev + 1;

            // Save level for reseting
            _level = level;

            parseLevel(level);
        } // if
        else{
            _allLevelsCleared = true;
        } // else
    } // newLevel

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
        _paths = new Path((int)_coordOr._x, (int)_coordOr._y, new VDMColor(255, 255, 255, 255), _lineThickness, p); // Path object

        createItems(i);

        // Create enemies, if there are enemies
        if(e != null){
            createEnemies(e);
        } // if

        // Player
        _player =  new Player(_paths.getInitPos()._x, _paths.getInitPos()._y, _playerC,
                _playerWidth, _playerWidth, _lineThickness, _playerVel, _aVel, this);
        _player.setPath(_paths.getPaths().get(0), 0, 1);
        _player.setCoordOrigin(_coordOr);
        _paths.setActivePath(0);

        String text = String.format("LEVEL %s - ", _nLevel) + _name;

        _levelText = new Text(-300, 200, _playerC.getWhite(),
                15, text, false, Font.FONT_BUNGEE_HAIRLINE);
        _levelText.setCoordOrigin(_coordOr);
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
        _gameOver = false;
        _part = null;
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
                double trueAng = parseDouble(ang);
                double magnitude = parseDouble(mag);
                double totalAngularVelocity = parseDouble(angVel);

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

            nItem.setCoordOrigin(_coordOr);
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
            nEnemy.setCoordOrigin(_coordOr);
            _en.add(nEnemy);
        } // for
    } // create_enemies

    /**
     * Used to render the game over banner over the level
     */
    private void renderGameOverBanner(Graphics g) {
        VDMColor colorPicker = new VDMColor();

        // Draw the background
        g.save();
        g.setColor(new VDMColor(60, 60, 60, 255));
        g.translate((int)_coordOr._x, (int)_coordOr._y);
        g.fillRect((-g.getWidth() / 2), (-g.getHeight()/3), (g.getWidth() / 2), 0);
        g.restore();

        // Draw Game Over text
        g.save();
        g.setColor(colorPicker.getRed());
        g.newFont(Font.FONT_BUNGEE_REGULAR, g.repositionX(45), true);
        g.translate((int) _coordOr._x - 140, ((int) _coordOr._y - 110));
        g.drawText("GAME OVER", 0, 0);
        g.restore();

        g.save();
        // Draw Difficulty text
        g.setColor(colorPicker.getWhite());
        g.newFont(Font.FONT_BUNGEE_REGULAR, g.repositionX(30), false);
        g.translate((int) _coordOr._x - 90, ((int) _coordOr._y - 70));
        if (_diffLevel == 0)
            g.drawText("EASY MODE", 0, 0);
        else
            g.drawText("HARD MODE", 0, 0);
        g.restore();

        g.save();
        g.setColor(colorPicker.getWhite());
        g.newFont(Font.FONT_BUNGEE_REGULAR, g.repositionX(30), false);
        g.translate((int) _coordOr._x - 80, ((int) _coordOr._y - 40));
        // Draw Score text
        g.drawText("SCORE: " + _nLevel , 0, 0);
        g.restore();
    }

    /**
     * Used to render the banner that tells the player they won.
     *
     * @param g (Graphics) Graphics object to paint everything.
     */
    private void renderGameWinBanner(Graphics g){
        VDMColor colorPicker = new VDMColor();

        g.save();
        g.setColor(new VDMColor(60, 60, 60, 255));
        g.translate((int)_coordOr._x, (int)_coordOr._y);
        g.fillRect((-g.getWidth() / 2), (-g.getHeight()/3), (g.getWidth() / 2), 0);
        g.restore();

        g.save();
        // Draw Winning text
        if(_yellowCongratulations){
            g.setColor(colorPicker.getItemColor());
        } // if
        else{
            g.setColor(colorPicker.getPlayerColor());
        } // else
        g.newFont(Font.FONT_BUNGEE_REGULAR, g.repositionX(45), true);
        g.translate((int) _coordOr._x - 245, ((int) _coordOr._y - 110));
        g.drawText("CONGRATULATIONS", 0, 0);
        g.restore();

        g.save();
        // Draw Difficulty text
        g.setColor(colorPicker.getWhite());
        g.newFont(Font.FONT_BUNGEE_REGULAR, g.repositionX(25), false);
        g.translate((int) _coordOr._x - 150, ((int) _coordOr._y - 70));
        if (_diffLevel == 0)
            g.drawText("EASY MODE COMPLETE", 0, 0);
        else
            g.drawText("HARD MODE COMPLETE", 0, 0);
        g.restore();

        g.save();
        g.setColor(colorPicker.getWhite());
        g.newFont(Font.FONT_BUNGEE_REGULAR, g.repositionX(25), false);
        g.translate((int) _coordOr._x - 190, ((int) _coordOr._y - 40));
        // Draw Score text
        g.drawText("CLICK TO QUIT TO MAIN MENU", 0, 0);
        g.restore();
    }

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
                _part = new ParticleSystem(collision._x, collision._y,
                        _playerC, _lineThickness, _coordOr);

                killPlayer();
            } // if
        } // for

        // ONLY WHEN FLYING AND NOT DEAD
        if(_player.isFlying() && !_dead) {
            // Then check items
            for(int i = 0; i < _it.size(); i++) {
                if(!((Item)_it.get(i)).isTaken()) {
                    double dist = Utils.sqrDistancePointSegment(segINIT, segEND, _it.get(i).getPos());

                    if (dist != -1.0) {
                        dist = Math.sqrt(dist);
                        if (dist < _itemDist) {
                            ((Item) _it.get(i)).itemTaken();
                        } // if
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
                    collision = segmentsIntersection(segINIT, segEND,
                            _paths.getPaths().get(i).get(j), _paths.getPaths().get(i).get(next));

                    if(collision != null){
                        Vector2 dir = _player.getDir(); // Moving effect

                        _coordOr._x += _collisionDesp * dir._x;
                        _coordOr._y -= _collisionDesp * dir._y;

                        _player.pathCollide(collision, _paths.getPaths().get(i), j, next);
                        _paths.setActivePath(i);
                        checkItems();
                        _pathCollision = true;
                    } // if
                    // If no collision, keep running till infinite
                } // Vertex for
            } // Paths for

            // Check boundaries
            int posX = (int)_coordOr._x + (int) segEND._x;
            int posY = (int)_coordOr._y + ((int) segEND._y * (-1));
            if(_l.checkWindowBoundaries(posX, posY)){
                killPlayer();
            } // if
        } // is_flying
    } // check_collisions

    /**
     * Function to check if all items are collected.
     */
    private void checkItems(){
        boolean completed = true;

        for(int i = 0; i < _it.size() && completed; i++) {
            if(!((Item)_it.get(i)).isTaken()){
                completed = false;
            } // if
        } // for

        if(completed){
            _countdown = _countdownInit;
        } // if

        _completed = completed;
    } // checkItems

    /**
     * Function called when the player hits an enemy or exists the boundaries of the game/window.
     * Kills the player and reduces lives, then starts a countdown to reset the level or display
     * the GameOver UI.
     */
    private void killPlayer(){
        _player.setActive(false);
        _dead = true;
        ((Life)_lf.get(_currLife)).loseLife();
        _currLife++;

        _countdown = _countdownInit;
    } // killPlayer

    /**
     * Update all the objects in this state and check the collisions between Player and the rest of
     * the objects.
     *
     * @param t (double) Time elapsed since the last frame.
     */
    @Override
    public void update(double t) {
        // Update time counter for congrats animation
        if(_allLevelsCleared){
            _colorChangeCounter += t;

            if(_colorChangeCounter >= _timeInColor) {
                _yellowCongratulations = !_yellowCongratulations;
                _colorChangeCounter = 0.0;
            } // if
        } // if

        // Update particles
        if(_part != null){
            _part.update(t);
        } // if

        // First update all items
        for (int i = 0; i < _it.size(); i++) {
            _it.get(i).update(t);
        } // for

        // Then update enemies
        for (int i = 0; i < _en.size(); i++) {
            _en.get(i).update(t);
        } // for

        // Update player
        if(!_dead) {
            _player.update(t);
        }

        // If it's game over, skip the rest of the checks
        if (!_gameOver) {
            if (_countdown > 0) {
                _countdown -= t;
            } // if
            else {
                if (_dead) {
                    if (_currLife >= _lf.size()) {
                        _gameOver = true;
                    } // All lives lost
                    else {
                        resetLevel();
                    } // else
                } // if
                else if (_completed) {
                    _completed = false;
                    _l.levelComplete();
                } // else if
            } // else
        } // if !_gameover
    } // update

    /**
     * Render the current scene in it's current position.
     *
     * @param g (Graphics) Instance of Graphics
     */
    @Override
    public void render(Graphics g) {
        // Render paths first
        _paths.render(g);

        // render particles
        if(_part != null){
            _part.render(g);
        } // if

        // Then render items
        for(int i = 0; i < _it.size(); i++) {
            _it.get(i).render(g);
        } // for

        // Next render enemies
        for(int i = 0; i < _en.size(); i++) {
            _en.get(i).render(g);
        } // for

        // Then render Player
        if(!_dead) {
            _player.render(g);
        } // if

        if(_pathCollision){
            _coordOr._x = _coordOrX;
            _coordOr._y = _coordOrY;
            _pathCollision = false;
        }

        // Render lives
        for(int i = 0; i < _lf.size(); i++) {
            _lf.get(i).render(g);
        } // for

        _levelText.render(g);

        // If it's a game over, render the game over screen
        if (_gameOver)
            renderGameOverBanner(g);

        if(_allLevelsCleared){
            renderGameWinBanner(g);
        } // if
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
                case CLICKED:
                case PRESSED_DOWN:
                case KEY_PRESSED:
                    // Same mechanism as with mouse or touchscreen but with keyboard.
                    // Tell the _player to begin flight
                    if(!_player.isFlying())
                        _player.fly(_paths.getJumpDir(_paths.getActivePath(),
                                _player.getActualPoint()));

                    if (_gameOver || _allLevelsCleared)
                        _l.setGameState(Logic.GameStates.MENU, 0);
                    break;
                case KEY_EXIT:
                    _l.closeGame();
                    break;
                default:
                    // Ignore the rest
                    break;
            } // switch

            ptr++;
        } // while
    } // processInput
} // PlayGameState