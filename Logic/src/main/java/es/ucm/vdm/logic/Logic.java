package es.ucm.vdm.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    int _actualLevel; // number to count the actual level
    PlayGameState test;
    Item test2;
    JSONArray _levels;

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
        // Load JSON file with all levels and save them into a JSONArray
        InputStream data = _eng.openInputStream("levels.json");
        JSONParser pars = new JSONParser();
        try {
            _levels = (JSONArray)pars.parse(new InputStreamReader(data, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject l = (JSONObject)_levels.get(2);

        test = new PlayGameState(l, 5, 0, this);

        // Init first state (Create a main menu state)
    } // initLogic

    /**
     * Updates the game variables and data for the next frame render.
     *
     * @param t (double) Time elapsed between frames.
     */
    @Override
    public void update(double t) {
        // Process actual input
        //_currentGameState.processInput(_eng);
        test.update(t);
        // Update GameState
        //_currentGameState.update(t);
    } // update

    /**
     * Renders the actual state of the game.
     */
    @Override
    public void render() {
        // Clear buffer with black
        _eng.getGraphics().clear(_clearColor);

        test.render(_eng.getGraphics());
    } // render

    /**
     * Function to draw the dimensions of the canvas, for debugging only
     */
    public void drawCanvas(){
        Rect tmp = _eng.getGraphics().getCanvas();

        _eng.getGraphics().setColor(0x00FF00);
        _eng.getGraphics().save();
        _eng.getGraphics().translate(tmp.getLeft(), tmp.getTop());

        _eng.getGraphics().fillRect(tmp.getLeft(), tmp.getTop(), tmp.getRight(), tmp.getBottom());

        _eng.getGraphics().restore();
    } // drawCanvas
} // Logic
