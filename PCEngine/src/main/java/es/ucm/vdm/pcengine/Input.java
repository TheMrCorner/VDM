package es.ucm.vdm.pcengine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class Input implements es.ucm.vdm.engine.Input, MouseListener, KeyListener, MouseMotionListener {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    // Event list (Or even Queue)
    List<TouchEvent> _touchEvn;


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
    }

    /**
     * Returns an event of touching something in the window (mouse click etc.) Saves the list in a
     * temporary Variable and then clears it.
     * @return TouchEvent List
     */
    @Override
    public synchronized List<TouchEvent> getTouchEvent() {
        List<TouchEvent> tmp;

        synchronized (this) {
            tmp = new ArrayList<TouchEvent>(_touchEvn);
            _touchEvn.clear();
        }

        return tmp;
    }

    //-----------------------KeyboardEvent---------------------------

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        TouchEvent aux = new TouchEvent(0, 0, TouchEvent.TouchType.KEY_TYPED, (int)keyEvent.getKeyChar());

        synchronized (this){
            _touchEvn.add(aux);
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        TouchEvent aux = new TouchEvent(0, 0, TouchEvent.TouchType.KEY_PRESSED, keyEvent.getKeyCode());

        synchronized (this){
            _touchEvn.add(aux);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        TouchEvent aux = new TouchEvent(0, 0, TouchEvent.TouchType.KEY_RELEASED, keyEvent.getID());

        synchronized (this){
            _touchEvn.add(aux);
        }
    }

    //-----------------------KeyboardEvent---------------------------

    //------------------------MouseEvent-----------------------------

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            int x, y;

            if(_g.isInCanvas(mouseEvent.getX(), mouseEvent.getY())){
                x = _g.reverseRepositionX(mouseEvent.getX() - _g.getCanvas().getX());
                y = _g.reverseRepositionY(mouseEvent.getY() - _g.getCanvas().getY());
            }
            else{
                x = mouseEvent.getX();
                y = mouseEvent.getY();
            }

            TouchEvent aux = new TouchEvent(x, y, TouchEvent.TouchType.CLICKED, 0);
            synchronized (this){
                _touchEvn.add(aux);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        //Left click
        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            TouchEvent aux = new TouchEvent(mouseEvent.getX(), mouseEvent.getY(), TouchEvent.TouchType.PRESSED_DOWN, 0);
            synchronized (this){
                _touchEvn.add(aux);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            TouchEvent aux = new TouchEvent(mouseEvent.getX(), mouseEvent.getY(), TouchEvent.TouchType.RELEASED, 0);
            synchronized (this){
                _touchEvn.add(aux);
            }
        }
    }

    //------------------------MouseEvent-----------------------------

    //------------------------MouseMotionEvent-----------------------

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        TouchEvent aux = new TouchEvent(mouseEvent.getX(), mouseEvent.getY(), TouchEvent.TouchType.MOVED, 0);
        synchronized (this){
            _touchEvn.add(aux);
        }
    }

    //------------------------MouseMotionEvent-----------------------
}
