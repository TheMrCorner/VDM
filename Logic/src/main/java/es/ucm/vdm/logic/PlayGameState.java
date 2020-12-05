package es.ucm.vdm.logic;

// Utils
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

// UCM
import es.ucm.vdm.engine.Engine;
import es.ucm.vdm.engine.GameObject;
import es.ucm.vdm.engine.GameState;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Input;
import es.ucm.vdm.engine.Vector2;
import static es.ucm.vdm.logic.Utils.parse_double;

public class PlayGameState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    ArrayList<GameObject> _go; // GameObject pool
    Logic _l; // For changing gamestate
    String _name; // Name of the actual level
    int _nLevel; // Number of level, for writing
    int _diffLevel; // Difficulty
    int _posOrX; // Pos of coord origin X
    int _posOrY; // Pos of coord origin Y

    // Constant values for colors and etc.
    int _playerC = 0xFF0000FF; // Player color ARGB
    int _itemC = 0xFFFFF200; // Item color ARGB
    int _enemyC = 0xFFFF0000; // Enemy color ARGB
    int _playerWidth = 12; // Player width
    int _itemWidth = 8; // Item width
    float _aVel = 180; // Angular velocity for all player and items
    // TODO: Igual es necesario hacer un state para el game over

    /**
     * Generates a new level from a JSONObject provided in the constructor.
     *
     * @param level (JSONObject) Level to be loaded.
     * @param nlev (int) Number of the actual level.
     * @param diff (boolean) Difficulty level
     */
    public PlayGameState(JSONObject level, int nlev, int diff, Logic l){
        // Init GameObject pool
        _go = new ArrayList<GameObject>();
        _diffLevel = diff;
        _l = l;

        _posOrY = (int)_l._cnv.height/2;
        _posOrX = (int)_l._cnv.width/2;

        // Parse all data from the level
        _nLevel = nlev + 1;
        _name = (String) level.get("name"); // Name of the level
        JSONArray p = (JSONArray) level.get("paths"); // Paths of the level
        JSONArray i = (JSONArray) level.get("items"); // Items
        JSONArray e = (JSONArray) level.get("enemies"); // Enemies

        // Create Path
        Path pO = new Path(_posOrX, _posOrY, 0xFFFFFFFF, p); // Path object
        _go.add(pO);

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
            coordX = parse_double(nx);
            coordY = parse_double(ny);

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
                trueAng = parse_double(ang);

                // Then parse the radius to get the magnitude of the vector (distance really)
                magnitude = parse_double(mag);

                // Finally, parse the angular velocity that all items will have.
                totalAngularVelocity = parse_double(angVel);

                // Now, using simple trigonometry, get the position at which the object should be
                // With multiplication of magnitude by cos and sin we get how much will add to
                // each coordinate to get the position. Due to Java Math.cos/sin use the angle
                // value as radians, it is important to convert angle to radians. Then round the
                // result to get real result, because when cos/sin return values too near to 0
                // they give a number like 1.89423678*10^-16, too small.
                coordX += magnitude * Math.round(Math.sin(Math.toRadians(trueAng)));
                coordY += magnitude * Math.round(Math.cos(Math.toRadians(trueAng)));

                nItem = new Item((int)coordX, (int)coordY, _itemC, _itemWidth,
                        _itemWidth, _aVel, (float)totalAngularVelocity, (float)trueAng, (float)magnitude);
            }
            else{
                // Else create a normal Item
                nItem = new Item((int)coordX, (int)coordY, _itemC, _itemWidth, _itemWidth, _aVel);
            }

            nItem.set_coordOrigin(new Vector2(_posOrX, _posOrY));
            _go.add(nItem);
        } // for

        // Create enemies, if there are enemies
        if(e != null){
            JSONObject enemyData;
            double length, angle, angSpeed, waitTime;
            Vector2 direction, linearSpeed;
            Enemy nEnemy;
            for(int en = 0; en < e.size(); en++){
                enemyData = (JSONObject) e.get(en);

                Object nx = enemyData.get("x");
                Object ny = enemyData.get("y");
                Object le = enemyData.get("length");
                Object an = enemyData.get("angle");

                // First parse coordinates
                // Check value
                coordX = parse_double(nx);
                coordY = parse_double(ny);
                // Parse length
                length = parse_double(le);

                // Parse angle
                angle = parse_double(an);

                // Parse angular speed
                if(enemyData.containsKey("speed")){
                    Object sp = enemyData.get("speed");
                    angSpeed = parse_double(sp);
                } // if
                else{
                    angSpeed = 0;
                } // else

                if(enemyData.containsKey("offset")){
                    Object offX = ((JSONObject)enemyData.get("offset")).get("x");
                    Object offY = ((JSONObject)enemyData.get("offset")).get("y");
                    Object time1 = enemyData.get("time1");
                    Object time2 = enemyData.get("time2");

                    // Calculate vector between positions
                    direction = new Vector2(parse_double(offX), parse_double(offY));

                    double timeMove = parse_double(time1);

                    // Calculate linear speed
                    linearSpeed = new Vector2(Math.abs(direction._x / timeMove),
                            Math.abs(direction._y / timeMove));

                    waitTime = parse_double(time2);
                } // if
                else{
                    linearSpeed = null;
                    waitTime = 0;
                    direction = null;
                } // else


                nEnemy = new Enemy((int)coordX, (int)coordY, _enemyC, (int)length, (int)angle,
                        (float)angSpeed, linearSpeed, (float)waitTime, direction);
                nEnemy.set_coordOrigin(new Vector2(_posOrX, _posOrY));
                _go.add(nEnemy);
            }
        } // if

        // Lives

        // Player


    } // PlayGameState

    /**
     * Constructor of the main play state, receives an instance of Logic to notify when the play loop
     * has ended.
     *
     * @param l (Logic) Logic's instance.
     */
    public PlayGameState(Logic l){
        _l = l;
    } // PlayGameState

    @Override
    public void initState() {
        // TODO: Implement
    } // initState

    @Override
    public void update(double t) {
        // Update all objects in scene
        for(int i = 0; i < _go.size(); i++) {
            _go.get(i).update(t);
        } // for
    } // update

    @Override
    public void render(Graphics g) {
        // Render everything
        for(int i = 0; i < _go.size(); i++) {
            _go.get(i).render(g);
        } // for
    } // render

    /**
     * Process all input incoming form the Input class. If mouse clicked or screen touched, throw
     * player to his right until it hits a new line or leaves the play zone.
     *
     * @param eng (Engine) Engine instance to access Input.
     */
    @Override
    public void processInput(Engine eng) {
        // Get list of touch events from processing them
        List<Input.TouchEvent> e = eng.getInput().getTouchEvent();

        int ptr = e.size() - 1; // Pointer to roam the list

        while(!e.isEmpty() && ptr >= -1){ // While list is not empty...
            Input.TouchEvent te = e.get(ptr); // Get touch event at pointers position

            switch(te.getType()){
                case CLICKED:

                    break;
                default:
                    // Ignore the rest
                    break;
            } // switch

            e.remove(ptr);
            ptr--;
        } // while
    } // processInput
} // PlayGameState
