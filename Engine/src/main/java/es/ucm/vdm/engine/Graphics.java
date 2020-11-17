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
     * Draws a line between to points specified with some color provided.
     *
     * @param y1 (int) Y position of the beginning point
     * @param x1 (int) X position of the beginning point
     * @param y2 (int) Y position of the ending point
     * @param x2 (int) X position of the ending point
     * @param color (int) Color to paint the line
     */
    void drawLine(int y1, int x1, int y2, int x2, int color);

    // TODO: TTF y texto

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

    /**
     * Reposition X coordinate to fit in canvas.
     *
     * @param x (int) Coordinate
     * @return (int) New coordinate
     */
    int repositionX(int x);

    /**
     * Reposition Y coordinate to fit in canvas.
     *
     * @param y (int) Coordinate
     * @return (int) New coordinate
     */
    int repositionY(int y);

    // TODO: Quizá necesitamos hacer el reverso de los anteriores para el input.

    //------------------------------------------------------------------------

} // Graphics
