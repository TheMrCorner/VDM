package es.ucm.vdm.androidengine;

// Android
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

// Java
import java.io.IOException;
import java.io.InputStream;

// UCM
import es.ucm.vdm.engine.AbstractEngine;
import es.ucm.vdm.engine.Logic;
import es.ucm.vdm.engine.Rect;
import es.ucm.vdm.engine.VDMColor;

/**
 * Android Engine class. Runs the main loop of the app, calling the necessary functions and methods
 * to update and render current game state. Checks the time elapsed between frames and manages all
 * rendering process.
 */
public class Engine extends AbstractEngine implements Runnable {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    AndroidWindow _win;
    AssetManager _aMan;

    Thread _renderThread;

    volatile boolean _running = false;
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------

    /**
     * Constructor. Creates a new Engine instance and initializes all necessary atributes for it to
     * work. Creates a new SurfaceView with the Activity received and creates an AssetManager.
     * Creates the Graphics instance to paint everything correctly on the screen. Creates the Input
     * instance to get access to the Input received from the View. Initialize the time values
     * and frame values for debugging and getting information about the app working process.
     * Prepares the SurfaceView to see it Fullscreen and without the upper banner with the app name.
     * Set the content view and the OnTouchListener.
     *
     * @param cont (Context) Android activity's context.
     */
    public Engine(Context cont){
        _win = new AndroidWindow(cont);

        // Save the assets to load them later
        _aMan = cont.getAssets();

        // Init Graphics with all values needed
        _g = new Graphics(_win, _aMan);

        // Create the Input
        _ip = new Input((Graphics)_g);

        // Initialize some time values
        _lastFrameTime = System.nanoTime(); // System time in ms
        _info = _lastFrameTime; // Information about the fps (debug)
        _frames = 0; // Number of frames passed

        // Set surface in fullscreen and not showing the upper banner
        _win.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                // Inmersion flags and navigation
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide Banner with the name
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        // Set Input as OnTouchListener
        _win.setOnTouchListener((View.OnTouchListener)_ip);
    } // Engine


    //---------------------------------------------------------------
    //----------------------App Life management----------------------
    //---------------------------------------------------------------
    /**
     * Method called when active rendering needs to be activated again. The game recovers focus.
     */
    public void onResume(){
        if(!_running){
            // Reset
            _running = true;

            // Init thread
            _renderThread = new Thread(this);
            _renderThread.start();
        } // if
    } // onResume

    /**
     * Method called when active rendering must be stopped. It can take a moment to perform this
     * action, because waits until last frame is generated.
     *
     * It's made this way to block the UI thread temporarily. This can avoid problems like
     * Android System calling onResume() before the last frame is generated.
     */
    public void onPause(){
        if(_running){ // Check running first
            _running = false;
            while(true){ // Wait for thread to end
                try{
                    // Wait for thread end
                    _renderThread.join();
                    _renderThread = null;
                    break;
                } // try
                catch(InterruptedException ie) {
                    HandleException((Exception) ie);
                } // catch
            } // while
        } // if
    } // onPause
    //---------------------------------------------------------------
    //----------------------App Life management----------------------
    //---------------------------------------------------------------


    //---------------------------------------------------------------
    //----------------------Getters and Setters----------------------
    //---------------------------------------------------------------

    /**
     * Creates an input stream of a file.
     *
     * @param filename (String) Name of the file to open the stream.
     * @return (InputStream) Open file.
     */
    @Override
    public InputStream openInputStream(String filename) {
        InputStream data = null;

        try{
            data = _aMan.open(filename);
        }catch(IOException e){
            e.printStackTrace();
        }

        return data;
    } // openInputStream

    /**
     * Handles an exception received. Print out the message of the exception.
     *
     * @param e (Exception)
     */
    @Override
    public void HandleException(Exception e) {
        System.err.println(e);
    } // HandleException
    //---------------------------------------------------------------
    //----------------------Getters and Setters----------------------
    //---------------------------------------------------------------


    //---------------------------------------------------------------
    //----------------------Surface and Canvas-----------------------
    //---------------------------------------------------------------
    /**
     * Return the SurfaceWidth if needed for calculations
     *
     * @return (int) _win Width
     */
    @Override
    public int getWinWidth() {
        return _win.width();
    } // getWidth

    /**
     * Return the SurfaceWidth if needed for calculations
     *
     * @return (int) _win Height
     */
    @Override
    public int getWinHeight() {
        return _win.height();
    } // getHeight

    /**
     * Returns the View, needed from the Main Activity in setContentView
     *
     * @return (View) _win View
     */
    public View getView() {return _win.getRootView();}
    //---------------------------------------------------------------
    //----------------------Surface and Canvas-----------------------
    //---------------------------------------------------------------


    //---------------------------------------------------------------
    //----------------------Main Loop Management---------------------
    //---------------------------------------------------------------

    /**
     * Renders all new information.
     * Initialize the canvas and lock it to paint in it. Starts the frame and calls for Logic's
     * render. Then Unlock canvas and show it on the screen.
     */
    protected void render(){
        // Get holder canvas
        Canvas c = _win.getSurfaceHolder().lockCanvas();

        // Start the frame
        ((Graphics)_g).startFrame(c);

        _g.clear(new VDMColor(0, 0, 0, 255));

        _l.render();

        // Show the new information
        _win.getSurfaceHolder().unlockCanvasAndPost(c);
    } // render

    /**
     * Game's main loop. This method should not be called directly, because it's called by the
     * renderThread. Calls for Logic's update() and render(). Updates all data related with time
     * and elapsed time between frames.
     */
    @Override
    public void run() {
        if(_renderThread != Thread.currentThread()){
            // Check directly calling run()
            throw new RuntimeException("run() called directly");
        }

        // Wait for surface availability
        while(_running && _win.width() == 0);

        // Loop
        while(_running){
            if(_tempLogic != null) {
                resetLogic();
            } // if

            // Update
            // Calculate time passed between frames and convert it to seconds
            _currentTime = System.nanoTime();
            _nanoElapsedTime = _currentTime - _lastFrameTime;
            _lastFrameTime = _currentTime;
            _elapsedTime = (double) _nanoElapsedTime / 1.0E9;

            //processInput();
            resize();

            // Update
            update();

            // Render

            // Wait till Surface is ready
            while(!_win.surfaceValid());

            // Render result
            render();

        }// while running
    } // run
    //---------------------------------------------------------------
    //----------------------Main Loop Management---------------------
    //---------------------------------------------------------------
} // Engine
