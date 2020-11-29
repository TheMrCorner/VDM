package es.ucm.vdm.logic;

import java.util.List;

import es.ucm.vdm.engine.Engine;
import es.ucm.vdm.engine.GameObject;
import es.ucm.vdm.engine.GameState;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Input;

public class PlayGameState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    GameObject[] _go;
    Logic _l; // For changing gamestate
    int _pts; // Score
    // TODO: Igual es necesario hacer un state para el game over

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
        for(int i = 0; i < _go.length; i++) {
            _go[i].update(t);
        } // for
    } // update

    @Override
    public void render(Graphics g) {
        // Render everything
        for(int i = 0; i < _go.length; i++) {
            _go[i].render(g);
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
