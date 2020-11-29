package es.ucm.vdm.logic;

import es.ucm.vdm.engine.Engine;
import es.ucm.vdm.engine.GameState;
import es.ucm.vdm.engine.Rect;

public class Logic implements es.ucm.vdm.engine.Logic {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    Engine _eng; // Instance of Engine for loading levels and resources
    Rect _cnv; // Canvas of the game (surface for painting graphics)
    int _clearColor; // Black
    GameState _currentGameState; // Current GameState instance to call it's methods
    int _fps = 60;

    /**
     * Logic constructor, creates a new instance of Logic with a new Engine.
     *
     * @param e (Engine) New engine instance.
     */
    public Logic(Engine e){
        // Save instance of engine for updates and rendering
        _eng = e;

        // Init everything
        _cnv = new Rect (640, 0, 0, 480);
        _clearColor = 0x000000;
        _eng.setFPS(_fps);
    } // Logic

    /**
     * Returns the actual canvas of the logic established here.
     *
     * @return (Rect) Logic canvas
     */
    @Override
    public Rect getCanvasSize() {
        return _cnv;
    } // getCanvasSize

    @Override
    public void initLogic() {

    } // initLogic

    /**
     * Updates the game variables and data for the next frame render.
     *
     * @param t (double) Time elapsed between frames.
     */
    @Override
    public void update(double t) {
        // Process actual input
        _currentGameState.processInput(_eng);

        // Update GameState
        _currentGameState.update(t);
    } // update

    /**
     * Renders the actual state of the game.
     */
    @Override
    public void render() {
        // Clear buffer with black
        _eng.getGraphics().clear(_clearColor);

        // Render GameState
        _currentGameState.render(_eng.getGraphics());
    } // render
}
