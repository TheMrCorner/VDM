package es.ucm.vdm.logic;

import java.util.List;

import es.ucm.vdm.engine.Engine;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Input;

public interface GameState {
    /**
     * Updates all GameObjects in this State with the time passed since the las update.
     *
     * @param t (double) Time elapsed since the last frame.
     */
    public void update(double t);// update

    /**
     * Renders all GameObjects in their specific locations. Receives an instance of Graphics
     * to call the drawing methods.
     *
     * @param g (Graphics) Instance of Graphics
     */
    public void render(Graphics g); // render

    /**
     * Method that processes the Input received from the Logic.
     *
     * @param e (List<Input.TouchEvent>) Event list taken from the Input class
     */
    public void processInput (List<Input.TouchEvent> e); // processInput
} // GameState
