/**
 * Logic
 *
 * Implementation of Logic's Interface
 */

package es.ucm.vdm.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import es.ucm.vdm.engine.Engine;
import es.ucm.vdm.engine.Rect;
import es.ucm.vdm.engine.VDMColor;

public class Logic implements es.ucm.vdm.engine.Logic {
    // Public - Protected - Private
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    Engine _eng; // Instance of Engine for loading levels and resources
    Rect _cnv; // Surface to paint current GameState
    VDMColor _clearColor; // Black
    GameState _currentGameState; // Current GameState instance
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
        _clearColor = new VDMColor(0, 0, 0, 255);
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

        JSONObject l = (JSONObject)_levels.get(11);

        test = new PlayGameState(l, 11, 0, this);

        // Init first state (Create a main menu state)
    } // initLogic

    public boolean checkWindowBoundaries(int x, int y) {
        x = _eng.getGraphics().repositionX(x);
        y = _eng.getGraphics().repositionY(y);

        return x < 0 || x > _eng.getWinWidth() || y < 0 || y > _eng.getWinHeight();
    } // checkWIndowBoundaries

    /**
     * Updates the game variables and data for the next frame render.
     *
     * @param t (double) Time elapsed between frames.
     */
    @Override
    public void update(double t) {
        // Process actual input
        test.processInput(_eng.getInput().getTouchEvents());
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
        VDMColor c = new VDMColor(0, 255, 0, 255);

        Rect tmp = _eng.getGraphics().getCanvas();

        _eng.getGraphics().setColor(c);
        _eng.getGraphics().save();
        _eng.getGraphics().translate(tmp.getLeft(), tmp.getTop());

        _eng.getGraphics().fillRect(tmp.getLeft(), tmp.getTop(), tmp.getRight(), tmp.getBottom());

        _eng.getGraphics().restore();
    } // drawCanvas
} // Logic
