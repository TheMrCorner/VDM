package es.ucm.vdm.logic;

import java.util.List;

import es.ucm.vdm.engine.Engine;
import es.ucm.vdm.engine.Font;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Input;
import es.ucm.vdm.engine.VDMColor;

public class MainMenuState implements GameState {

    Logic _l; // For changing gamestate
    int _posOrX; // Pos of coord origin X
    int _posOrY; // Pos of coord origin Y
    VDMColor _colorPicker; // Used for picking colors for the text and it also stores a grey color

    final String GAME_TITLE = "OFF THE LINE";
    final String GAME_SUBTITLE = "A game copied from Bryan Perfetto";
    final String EASY_DESCRIPTION = "(Slow speed, 10 lives)";
    final String HARD_DESCRIPTION = "(Fast speed, 5 lives)";

    public MainMenuState(Logic l) {
        _l = l;

        _posOrY = (int)_l._cnv.height/2;
        _posOrX = (int)_l._cnv.width/2;

        _colorPicker = new VDMColor(60, 60, 60, 255);
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

        g.restore();
    }

    @Override
    public void processInput(List<Input.TouchEvent> e) {
        //TODO: process clicks
    }
} // MainMenuState
