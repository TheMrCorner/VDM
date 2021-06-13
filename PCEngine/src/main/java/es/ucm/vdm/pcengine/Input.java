package es.ucm.vdm.pcengine;

// JAVA
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

// UCM
import es.ucm.vdm.engine.AbstractInput;

/**
 * Class that manages the input. Get Input events generated from the various input listeners and
 * manage them with the usage we want to give them. Implements AbstractInput class and MouseListener,
 *  KeyListener and MouseMotionListener interface. Transforms these events into Engine's touch events.
 */
public class Input extends AbstractInput implements es.ucm.vdm.engine.Input, MouseListener, KeyListener, MouseMotionListener {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------

    // Instance of Graphics for checking position
    Graphics _g;

    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------

    /**
     * Constructor of the Input System. Singleton
     */
    Input (Window w, Graphics g){
        // Create the TouchEventList
        _touchEvn = new ArrayList<TouchEvent>();
        w.addMouseListener(this);
        w.addKeyListener(this);
        w.addMouseMotionListener(this);

        _g = g;
    } // Constructor

    //-----------------------KeyboardEvent---------------------------

    /**
     * Called when a key is typed. Gets the listener event and processes it to create our own
     * TouchEvent and add it to the TouchEvent list. Gives it a TouchType value.
     *
     * @param keyEvent (KeyEvent) KE received from the listener
     */
    @Override
    public void keyTyped(KeyEvent keyEvent) {
        TouchEvent aux;

        if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            aux = new TouchEvent(0, 0, TouchEvent.TouchType.KEY_EXIT);
        } // if
        else {
            aux = new TouchEvent(0, 0, TouchEvent.TouchType.KEY_TYPED);
        } // else

        addEvent(aux);
    } // keyTyped

    /**
     * Called when a key is pressed. Gets the listener event and processes it to create our own
     * TouchEvent and add it to the TouchEvent list. Gives it a TouchType value.
     *
     * @param keyEvent (KeyEvent) KE received from the listener
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        TouchEvent aux;

        if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            aux = new TouchEvent(0, 0, TouchEvent.TouchType.KEY_EXIT);
        } // if
        else {
            aux = new TouchEvent(0, 0, TouchEvent.TouchType.KEY_PRESSED);
        } // else

        addEvent(aux);
    } // keyPressed

    /**
     * Called when a key is released. Gets the listener event and processes it to create our own
     * TouchEvent and add it to the TouchEvent list. Gives it a TouchType value.
     *
     * @param keyEvent (KeyEvent) KE received from the listener
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        TouchEvent aux;

        if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            aux = new TouchEvent(0, 0, TouchEvent.TouchType.KEY_EXIT);
        } // if
        else {
            aux = new TouchEvent(0, 0, TouchEvent.TouchType.KEY_RELEASED);
        } // else

        addEvent(aux);
    } // keyReleased

    //-----------------------KeyboardEvent---------------------------

    //------------------------MouseEvent-----------------------------

    /**
     * Called when the mouse is clicked. Gets the listener event and processes it to create our own
     * TouchEvent and add it to the TouchEvent list. Repositions coordinates of the event and gives
     * it a TouchType value.
     *
     * @param mouseEvent (MouseEvent) ME received from the listener
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            int x, y;

            if(_g.isInCanvas(mouseEvent.getX(), mouseEvent.getY())){
                x = _g.reverseRepositionX(mouseEvent.getX() - _g.getCanvas().getX());
                y = _g.reverseRepositionY(mouseEvent.getY() - _g.getCanvas().getY());
            } // if
            else{
                x = mouseEvent.getX();
                y = mouseEvent.getY();
            } // else

            TouchEvent aux = new TouchEvent(x, y, TouchEvent.TouchType.CLICKED);

            addEvent(aux);
        } // if
    } // mouseClicked

    /**
     * Called when the mouse is pressed. Gets the listener event and processes it to create our own
     * TouchEvent and add it to the TouchEvent list. Picks the coordinates of the event and gives
     * it a TouchType value.
     *
     * @param mouseEvent (MouseEvent) ME received from the listener
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        //Left click
        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            TouchEvent aux = new TouchEvent(mouseEvent.getX(), mouseEvent.getY(), TouchEvent.TouchType.PRESSED_DOWN);

            addEvent(aux);
        } // if
    } // mousePressed

    /**
     * Called when the mouse is released. Gets the listener event and processes it to create our own
     * TouchEvent and add it to the TouchEvent list. Picks the coordinates of the event and gives
     * it a TouchType value.
     *
     * @param mouseEvent (MouseEvent) ME received from the listener
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            TouchEvent aux = new TouchEvent(mouseEvent.getX(), mouseEvent.getY(), TouchEvent.TouchType.RELEASED);

            addEvent(aux);
        } // if
    } // mouseReleased

    //------------------------MouseEvent-----------------------------

    //------------------------MouseMotionEvent-----------------------

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {}

    /**
     * Called when the mouse is moved. Gets the listener event and processes it to create our own
     * TouchEvent and add it to the TouchEvent list. Gets the coords from the event and gives it
     * a TouchType value.
     *
     * @param mouseEvent (MouseEvent) ME received from the listener
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        TouchEvent aux = new TouchEvent(mouseEvent.getX(), mouseEvent.getY(), TouchEvent.TouchType.MOVED);

        addEvent(aux);
    } // mouseMoved

    //------------------------MouseMotionEvent-----------------------
} // Input
