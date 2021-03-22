package es.ucm.vdm.pcengine;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import es.ucm.vdm.engine.AbstractEngine;
import es.ucm.vdm.engine.Logic;
import es.ucm.vdm.engine.Rect;

// TODO: Comentar un poco lo que hace la calse y demás.
public class Engine extends AbstractEngine implements Runnable, ComponentListener {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    // Initializing
    Window _win;

    // Time and Calculations
    int _width;
    int _height;
    int FPS;
    double averageFPS;
    boolean forward = true;

    // Testing
    int _currentColor = 0; //Background sprites iterator
    //GREEN, TURQUOISE, LIGHTBLUE, BLUE, PURPLE, DARKBLUE, YELLOW, ORANGE, BROWN
    int _planeColor[] = {0xff41a85f, 0xff00a885, 0xff3d8eb9, 0xff2969b0, 0xff553982, 0xff28324e, 0xfff37934,
            0xffd14b41, 0xff75706b};

    /**
     * Class constructor. Creates a new Engine instance and sets everything for game to run
     * correctly. Sets all window values needed, creates the new window, creates a new graphics
     * instance, initializes all data related to time controlling and calculations and sets
     * itself as listener of window events.
     */
    public Engine(){
        //Window generation
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Window values
        _width = gd.getDisplayMode().getWidth();
        _height = gd.getDisplayMode().getHeight();
        String name = "Off the Line";

        // Create the Window
        _win = new Window(_width, _height, name, this);

        // Create Graphics instance
        _g = new Graphics(_win);

        // Create input
        _ip = new Input(_win, (Graphics)_g);

        // Initialize some time values
        _lastFrameTime = System.nanoTime(); // System time in ms
        _info = _lastFrameTime; // Information about the fps (debug)
        _frames = 0; // Number of frames passed

        _win.addComponentListener(this);
    } // Engine

    /**
     * Resize canvas to fit the screen. Only called when the window is resized. (Fullscreen, or
     * anything else.
     */
    public void resize(){
        Rect temp;
        Rect temp2;

        // RESIZE
        // Get window size (as a rectangle)
        temp2 = new Rect(_win.getWidth(), 0, 0, _win.getHeight());

        // Get Logic's canvas
        temp = _l.getCanvasSize();

        // Resize the Logic's canvas with that reference
        _g.setCanvasSize(temp, temp2);

        _g.setCanvasPos(((_win.getWidth()/2) - (_g.getCanvas().getWidth() / 2)),
                ((_win.getHeight()/2) - (_g.getCanvas().getHeight() / 2)));
    } // resize

    /**
     * Returns access to the Graphics instance saved here for the Logic to paint it's updates.
     *
     * @return (Graphics) Graphics instance.
     */
    @Override
    public Graphics getGraphics() {
        return (Graphics)_g;
    } // getGraphics

    /**
     * Returns access to the Input(Manager) for input management in Logic.
     *
     * @return (Input) Input instance.
     */
    @Override
    public Input getInput() {
        return (Input)_ip;
    } // getInput

    @Override
    public InputStream openInputStream(String filename) {
        InputStream data = null;
        try {
            data = new FileInputStream("./Data/" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    } // openInputStream

    /**
     * Function to save an instance of the logic and call all it's functions (update, render, handle
     * Input, etc.)
     *
     * @param l (Logic) Instance of Logic
     */
    @Override
    public void setLogic(Logic l) {
        _tempLogic = l;
    } // setLogic

    /**
     * Function called when a change in Logic has happened. Resets everything to meet the Logic's
     * conditions.
     */
    @Override
    public void resetLogic(){
        // Checking that _tempLogic is truly null to avoid callings from other objects.
        if(_tempLogic != null) {
            _l = _tempLogic;

            _g.setReferenceCanvas(_l.getCanvasSize());

            resize();

            _l.initLogic();
            _tempLogic = null;
        } // if
    } // resetLogic

    /**
     * Sets the max frame rate to keep all running at the same velocity
     *
     * @param fps (int) Max frame rate.
     */
    @Override
    public void setFPS(int fps) {
        FPS = fps;
    } // setFPS

    @Override
    public void HandleException(Exception e) {
        // TODO: Implementar
    } // HandleException

    /**
     * Function to get the width of the screen or the window.
     *
     * @return (int) Window/Screen width
     */
    @Override
    public int getWinWidth() {
        return _width;
    } // getWidth

    /**
     * Function to get the height of the screen or the window
     *
     * @return (int) Window/Screen height
     */
    @Override
    public int getWinHeight() {
        return _height;
    } // getHeight

    /**
     * Resize canvas to fit screen after window is resized.
     *
     * @param componentEvent (ComponentEvent) ComponentEvent
     */
    @Override
    public void componentResized(ComponentEvent componentEvent) {
        resize();
    } // componentResized

    /**
     * Render function.
     * Clears the buffer to paint again.
     * Calls the render from the logic to paint the updated version of the screen.
     * If it fails, it will still paint the screen.  It has 2 loops because it will try to paint
     * the buffer and then show the buffer. Ideally, this won't fail and it will do only 1 iteration
     * per loop.
     */
    void render(){
        _g.clear(0x000000); // Clear the whole buffer

        // Ideally this will only do 1 iteration per loop
        do {
            do {
                _win.setGraphics();
                try { // Try to paint in the Graphics
                    _l.render();
                }
                finally { // If not, still dispose the Swing Graphics
                    _win.getJGraphics().dispose();
                }
            } while(_win.getBufferStrategy().contentsRestored());
            _win.getBufferStrategy().show(); // Show the buffer with the updated information (painted)
        } while(_win.getBufferStrategy().contentsLost());
    }

    /**
     * run function, called by the main thread for running the main loop of the game. Controls the
     * frame rate and calls for update and render functions, checks if new Input happened.
     */
    @Override
    public void run() {
        // FPS
        long startTime; // Starting loop time
        long URDTimeMilliSec; // Time since start in millisecond
        long waitTime; // Waiting time for next frame
        long totalTime = 0; // Total time

        int fCount = 0; // FrameCount
        int maxFrameRate = 30; // Max FPS (Default 30, can change)

        long targetTime = 1000000000l / FPS; // Time to run at FPS

        //Main Loop
        while(true){
            startTime = System.nanoTime();

            // Update width and height
            _width = _win.getWidth();
            _height = _win.getHeight();
            // Calculate time passed between frames and convert it to seconds
            _currentTime = System.nanoTime();
            _nanoElapsedTime = _currentTime - _lastFrameTime;
            _lastFrameTime = _currentTime;
            _elapsedTime = (double) _nanoElapsedTime / 1.0E9;

            _l.update(_elapsedTime);

            // Inform about the fps (Debug only)
            if(_currentTime - _info > 1000000000l){
                long fps = _frames * 1000000000l / (_currentTime - _info);
                System.out.println("Info: " + fps + " fps");
                _frames = 0;
                _info = _currentTime;
            } // if
            ++_frames; // Update frames

            // Clear and update graphics
            render();

            // Frame cap
            totalTime = System.nanoTime() - startTime;

            if(totalTime < targetTime){
                try {
                    Thread.sleep((targetTime - totalTime) / 1000000);
                } // try
                catch (InterruptedException e) {
                    e.printStackTrace();
                } // catch
            } // if

            if(_tempLogic != null) {
                resetLogic();
            } // if
        } // while
    } // run


    //------------------Implementation Methods---------------------

    @Override
    public void componentMoved(ComponentEvent componentEvent) {}

    @Override
    public void componentShown(ComponentEvent componentEvent) {}

    @Override
    public void componentHidden(ComponentEvent componentEvent) {}
} // Engine
