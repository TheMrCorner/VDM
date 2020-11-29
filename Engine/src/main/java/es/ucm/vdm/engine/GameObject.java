package es.ucm.vdm.engine;

public interface GameObject {
    // Functions and methods to access GO position
    /**
     * Gives access to the GO position (if needed for calculations)
     *
     * @return (double) Actual X position
     */
    public double getPosX();

    /**
     * Gives access to the GO position (if needed for calculations)
     *
     * @return (double) Actual Y position
     */
    public double getPosY();

    // Functions and methods to place the GO
    /**
     * Sets X coordinate that this GO will have.
     */
    public void setPosX();
    /**
     * Sets the Y coordinate that this GO will hace.
     */
    public void setPosY();

    // Color to paint this GO (if it has one)
    /**
     * Sets the color that the GO will use to paint it self. It can be set to null if the GO
     * uses any kind of image.
     *
     * @param c (int) Color to draw.
     */
    public void setColor(int c);

    // Functions to print GO and update its state
    /**
     * Updates the state of the gameobject with the time elapsed between frames.
     *
     * @param t (double) Time elapsed
     */
    public void update(double t);

    /**
     * Renders the GO with the characteristics set on this object.
     *
     * @param g (Graphics) Graphics instance to paint it
     */
    public void render(Graphics g);
} // GameObject
