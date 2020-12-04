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

public class PlayGameState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    ArrayList<GameObject> _go;
    Logic _l; // For changing gamestate
    String _name; // Name of the actual level
    int _nLevel; // Number of level, for writing
    int _diffLevel;
    int _posOrX;
    int _posOrY;

    // Constant values for colors and etc.
    int _playerC = 0x0000FF;
    int _itemC = 0xFFF200;
    int _enemyC = 0xFF0000;
    int _playerWidth = 12;
    int _itemWidth = 8;
    float _aVel = 180; //  angular velocity for all player and items
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
        Path pO = new Path(_posOrX, _posOrY, 0xFFFFFF, p); // Path object
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

            // Check value
            if(nx instanceof Long){ // if Long, parse it to double
                coordX = ((Long) nx).doubleValue();
            } // if
            else{ // else, assume double
                coordX = (double)nx;
            } // else
            if(ny instanceof Long){
                coordY = ((Long) ny).doubleValue();
            } // if
            else{
                coordY = (double)ny;
            } // else

            nItem = new Item((int)coordX, (int)coordY, _itemC, _itemWidth, _itemWidth, _aVel);
            nItem.set_coordOrigin(new Vector2(_posOrX, _posOrY));
            _go.add(nItem);
        } // for

        // Create enemies, if there are enemies
        if(e != null){

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
