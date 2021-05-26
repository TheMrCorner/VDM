package es.ucm.vdm.logic;

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
    Graphics _g = null; // For repositioning of coordinates in processInput
    int _posOrX; // Pos of coord origin X
    int _posOrY; // Pos of coord origin Y
    VDMColor _colorPicker; // Used for picking colors for the text and it also stores a grey color
    Button _easy;
    Button _hard;

    //---------------------------------------------------------------
    //--------------------------Constants----------------------------
    //---------------------------------------------------------------
    final String GAME_TITLE = "OFF THE LINE";
    final String GAME_SUBTITLE = "A game copied from Bryan Perfetto";
    final String EASY_DESCRIPTION = "(Slow speed, 10 lives)";
    final String HARD_DESCRIPTION = "(Fast speed, 5 lives)";

    public MainMenuState(Logic l, Graphics g) {
        _l = l;
        _g = g;

        _posOrY = (int)_l._cnv.height/2;
        _posOrX = (int)_l._cnv.width/2;

        _colorPicker = new VDMColor(60, 60, 60, 255);

        _easy = new Button(0, 0, 100, 100, new VDMColor(255, 255, 255, 255), 1);
    }

    @Override
    public void update(double t) {

    }

    @Override
    public void render(Graphics g) {
        g.save();

        // Render game title
        g.newFont(Font.FONT_FILE, g.repositionX(40), true);
        g.setColor(_colorPicker.getPlayerColor());
        g.translate(_posOrX, _posOrY);
        g.drawText(GAME_TITLE, (-g.getWidth()/3), (-g.getHeight() / 3));

        // Render game subtitle
        g.newFont(Font.FONT_FILE, g.repositionX(20), false);
        g.drawText(GAME_SUBTITLE, (-g.getWidth()/3), (-g.getHeight() / 4));

        // Render difficulty texts
        g.newFont(Font.FONT_FILE, g.repositionX(30), false);
        g.setColor(_colorPicker.getWhite());
        g.drawText("EASY MODE", (-g.getWidth()/3), (g.getHeight() / 6));
        g.drawText("HARD MODE", (-g.getWidth()/3), (g.getHeight() / 4));

        g.newFont(Font.FONT_FILE, g.repositionX(15), false);
        g.setColor(_colorPicker);
        g.drawText(EASY_DESCRIPTION, 0, (g.getHeight() / 6));
        g.drawText(HARD_DESCRIPTION, 0, (g.getHeight() / 4));

        // debug, render button
        _easy.setPos(new Vector2((-g.getWidth()/3), (g.getHeight() / 6)));
        _easy.setWidthHeight(g.getWidth()/4, g.getHeight()/10);
        _easy.render(g);

        g.restore();
    }

    @Override
    public void processInput(List<Input.TouchEvent> e) {
        //TODO: process clicks

        // int ptr = e.size() - 1; // Pointer to roam the list
        int ptr = 0;

        while(ptr < e.size()){ // While list is not empty...
            Input.TouchEvent te = e.get(ptr); // Get touch event at pointers position

            if (te.getType() == Input.TouchEvent.TouchType.PRESSED_DOWN) {
                if (_g != null) {
                    int logicX = _g.reverseRepositionX(te.getX());
                    int logicY = _g.reverseRepositionY(te.getY());

                    if (_easy.isPressed(logicX, logicY, _g))
                        _l.setGameState(Logic.GameStates.PLAY);
                }
            } // if

            ptr++;
        } // while
    }
} // MainMenuState
