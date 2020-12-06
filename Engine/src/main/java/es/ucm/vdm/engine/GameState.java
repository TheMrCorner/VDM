package es.ucm.vdm.engine;

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
     * @param eng (Engine) Engine instance to access Input.
     */
    public void processInput (Engine eng); // processInput
} // GameState
