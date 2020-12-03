package es.ucm.vdm.engine;

public abstract class GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    protected Vector2 _pos;
    protected Vector2 _initPos;
    protected float _rot; // rotation
    protected int _c; // Color for painting player
    protected boolean _act; // Check if GO is active or not

    public GameObject(int x, int y, int c){
        _pos = _initPos = new Vector2(x, y);
        setColor(c);
        _rot = 0;
    }

    // Functions and methods to access GO position
    /**
     * Gives access to the GO position (if needed for calculations)
     *
     * @return (double) Actual X position
     */
    public double getPosX() {
        return _pos._x;
    } // getPosX

    /**
     * Gives access to the GO position (if needed for calculations)
     *
     * @return (double) Actual Y position
     */
    public double getPosY(){
        return _pos._y;
    } // getPosY

    /**
     * Check if GO is active or not.
     *
     * @return
     */
    public boolean isActive(){
        return _act;
    } // isActive

    // Functions and methods to place the GO
    /**
     * Sets X coordinate that this GO will have.
     */
    public void setPosX(double x) {
        _pos._x = x;
    } // setPosX

    /**
     * Sets the position Y of the GO to the position provided.
     *
     * @param y (int) New Y position.
     */
    public void setPosY(double y) {
        _pos._y = y;
    } // setPosY

    /**
     * Change actual GO state to provided active state (true or false)
     *
     * @param chng (boolean) New active state
     */
    public void setActive(boolean chng){
        _act = chng;
    } // setActive

    /**
     * Set the color to draw the GO.
     *
     * @param c (int) Color to draw.
     */
    public void setColor(int c) {
        _c = c;
    } // setColor

    // Functions to print GO and update its state
    /**
     * Updates the state of the gameobject with the time elapsed between frames. Update is specific
     * from every GO, there is no default update.
     *
     * @param t (double) Time elapsed
     */
    public void update(double t){ }

    /**
     * Renders the GO with the characteristics set on this object. Render is specific from every
     * GO, there is no default render.
     *
     * @param g (Graphics) Graphics instance to paint it
     */
    public void render(Graphics g){ }
} // GameObject
