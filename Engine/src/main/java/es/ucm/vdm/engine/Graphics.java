package es.ucm.vdm.engine;

public interface Graphics {

    //-----------------------------Canvas-------------------------------------

    /**
     * Sets size and dimensions of the canvas.
     *
     * @param c (Rect) Size of canvas
     * @param dim (Rect) Dimensions to scale it
     */
    void setCanvasSize(Rect c, Rect dim);

    /**
     * Gets reference to the canvas.
     *
     * @return (Rect) Canvas of the game
     */
    Rect getCanvas();

    /**
     * Sets a canvas to use as reference for scaling graphics and images.
     *
     * @param c (Rect) Size of the canvas to be used as reference.
     */
    void setReferenceCanvas(Rect c);

    /**
     * Set canvas position in screen.
     *
     * @param x (int) X position
     * @param y (int) Y position
     */
    void setCanvasPos(int x, int y);

    /**
     * Check if some position is inside of canvas.
     *
     * @param x (int) X coordinate
     * @param y (int) Y coordinate
     * @return (boolean) Is in canvas
     */
    boolean isInCanvas(int x, int y);

    //------------------------------------------------------------------------


    //---------------------------Drawing-------------------------------------

    /**
     * Clears the screen filling it with a specific color.
     *
     * @param color (int) Color to fill screen
     */
    void clear(int color);

    /**
     * Sets a color to draw things in screen.
     *
     * @param color (int) Color to set for drawing
     */
    void setColor(int color);

    /**
     * Draws a line between to points specified with some color provided.
     *
     * @param y1 (int) Y position of the beginning point
     * @param x1 (int) X position of the beginning point
     * @param y2 (int) Y position of the ending point
     * @param x2 (int) X position of the ending point
     */
    void drawLine(int x1, int y1, int x2, int y2, int thickness);

    void fillRect(int x1, int y1, int x2, int y2);

    void drawText(String text, int x, int y);

    Font newFont(String filename, int size, boolean isBold);

    //------------------------------------------------------------------------

    //-----------------------------Getters------------------------------------

    /**
     * Get window Width
     *
     * @return (int) Window width
     */
    int getWidth();

    /**
     * Get window Height
     *
     * @return (int) Window height
     */
    int getHeight();

    //------------------------------------------------------------------------

    //-----------------------------Scaling------------------------------------

    /**
     * Scale a rectangle to fit another dimensions provided.
     *
     * @param src (Rect) Rectangle to scale.
     * @param dim (Rect) Rectangle to fit source Rect.
     * @return (Rect) New scalated rectangle.
     */
    Rect scale(Rect src, Rect dim);

    void save();

    void restore();

    /**
     * Rotates an object using angles given.
     *
     * @param angle (float) Angle to rotate object
     */
    void rotate(float angle);

    /**
     * Translates the origin coordinates to the specified position.
     *
     * @param x (int) X position to set as origin
     * @param y (int) Y position to set as origin
     */
    void translate(int x, int y);

    int repositionX(int x);
    int repositionY(int y);

    int reverseRepositionX(int x);
    int reverseRepositionY(int y);

    //------------------------------------------------------------------------

} // Graphics
