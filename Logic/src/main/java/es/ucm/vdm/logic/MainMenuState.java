package es.ucm.vdm.logic;

import java.util.ArrayList;
import java.util.List;

import es.ucm.vdm.engine.Engine;
import es.ucm.vdm.engine.Font;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Input;
import es.ucm.vdm.engine.VDMColor;

public class MainMenuState implements GameState {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    Logic _l; // For changing gamestate
    int _posOrX; // Pos of coord origin X
    int _posOrY; // Pos of coord origin Y
    VDMColor _colorPicker; // Used for picking colors for the text and it also stores a grey color
    Button _easy;
    Button _hard;
    ArrayList<Text> _texts;

    //---------------------------------------------------------------
    //--------------------------Constants----------------------------
    //---------------------------------------------------------------
    final String GAME_TITLE = "OFF THE LINE";
    final String GAME_SUBTITLE = "A game copied from Bryan Perfetto";
    final String EASY_DESCRIPTION = "(Slow speed, 10 lives)";
    final String HARD_DESCRIPTION = "(Fast speed, 5 lives)";

    public MainMenuState(Logic l) {
        _l = l;

        _posOrY = _l._cnv.height/2;
        _posOrX = _l._cnv.width/2;

        Vector2 ors = new Vector2(_posOrX, _posOrY);

        _colorPicker = new VDMColor(60, 60, 60, 255);

        _texts = new ArrayList<Text>();

        Text esText = new Text(-308, -78, _colorPicker.getWhite(),
                35, "EASY MODE", false, Font.FONT_FILE);
        esText.setCoordOrigin(ors);
        _easy = new Button(-200, -68, 216, 29,
                _colorPicker.getEnemyColor(), 10, esText);
        _easy.setCoordOrigin(ors);

        Text hdText =  new Text(-308, -116, _colorPicker.getWhite(),
                35, "HARD MODE", false, Font.FONT_FILE);
        hdText.setCoordOrigin(ors);
        _hard = new Button(-196, -110, 224, 29,
                _colorPicker.getItemColor(), 10, hdText);
        _hard.setCoordOrigin(ors);

        Text title = new Text(-308, 160, _colorPicker.getPlayerColor(),
                50, GAME_TITLE, true, Font.FONT_FILE);
        title.setCoordOrigin(ors);
        _texts.add(title);

        Text stitle = new Text(-308, 129, _colorPicker.getPlayerColor(),
                25, GAME_SUBTITLE, false, Font.FONT_FILE);
        stitle.setCoordOrigin(ors);
        _texts.add(stitle);

        Text easText = new Text(-84, -78, _colorPicker,
                20, EASY_DESCRIPTION, false, Font.FONT_FILE);
        easText.setCoordOrigin(ors);
        _texts.add(easText);

        Text hardText = new Text(-72, -116, _colorPicker,
                20, HARD_DESCRIPTION, false, Font.FONT_FILE);
        hardText.setCoordOrigin(ors);
        _texts.add(hardText);
    } // Constructor

    @Override
    public void update(double t) {}

    @Override
    public void render(Graphics g) {
        g.save();

        // Render game title
        for (int i = 0; i < _texts.size(); i++){
            _texts.get(i).render(g);
        } // for

        _easy.render(g);
        _hard.render(g);

        g.restore();
    }

    @Override
    public void processInput(List<Input.TouchEvent> e) {
        // int ptr = e.size() - 1; // Pointer to roam the list
        int ptr = 0;

        while(ptr < e.size()){ // While list is not empty...
            Input.TouchEvent te = e.get(ptr); // Get touch event at pointers position

            if (te.getType() == Input.TouchEvent.TouchType.CLICKED) {
                if (_easy.isPressed(te.getX(), te.getY())) {
                    _l.setGameState(Logic.GameStates.PLAY, 0);
                } // if
                else if(_hard.isPressed(te.getX(), te.getY())) {
                    _l.setGameState(Logic.GameStates.PLAY, 1);
                } // else if
            } // if

            ptr++;
        } // while
    } // processInput
} // MainMenuState
