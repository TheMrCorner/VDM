package es.ucm.vdm.androidengine;

// Android
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

// Java
import java.io.IOException;
import java.io.InputStream;

// UCM
import es.ucm.vdm.engine.Logic;
import es.ucm.vdm.engine.Rect;

/**
 * Android Engine class. Runs the main loop of the app, calling the necessary functions and methods
 * to update and render current game state. Checks the time elapsed between frames and manages all
 * rendering process.
 */
public class Engine implements es.ucm.vdm.engine.Engine, Runnable{
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    SurfaceView _mSurface;
    SurfaceHolder _holder;
    AssetManager _aMan;

    Thread _renderThread;

    volatile boolean _running = false;

    Graphics _g;
    Input _ip;
    Logic _l;

    // Atributes for calculations and time.
    long _lastFrameTime;
    long _currentTime, _nanoElapsedTime;
    double _elapsedTime;
    int _frames;
    long _info;
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
     * @param act (Activity) Android activity for painting and management.
     * @param cont (Context) Android activity's context.
     */
    public Engine(Activity act, Context cont){
        // Create Surface
        _mSurface = new SurfaceView(act);

        // Save the assets to load them later
        _aMan = cont.getAssets();

        // Init Graphics with all values needed
        _g = new Graphics(_mSurface, _aMan);

        // Create the Input
        _ip = new Input(_g);

        // Initialize some time values
        _lastFrameTime = System.nanoTime(); // System time in ms
        _info = _lastFrameTime; // Information about the fps (debug)
        _frames = 0; // Number of frames passed

        // Set surface in fullscreen and not showing the upper banner
        _mSurface.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                // Inmersion flags and navigation
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide Banner with the name
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        // Set Input as OnTouchListener
        _mSurface.setOnTouchListener(_ip);

        // Set ContentView
        act.setContentView(_mSurface);
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
     * Returns the instance of Graphics when needed to draw or making calculations.
     *
     * @return (Graphics) Graphics instance saved here.
     */
    @Override
    public Graphics getGraphics() {
        return _g;
    } // getGraphics

    /**
     * Return Input Instance when needed for processing Input and etc.
     *
     * @return (Input) Input instance saved here.
     */
    @Override
    public Input getInput() {
        return _ip;
    } // getInput

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
     * Save an instance of the logic of the game. Used to call the different necessary methods
     * lates (render and update) and for scaling the canvas correctly.
     *
     * @param l (Logic) Logic's instance created previously.
     */
    @Override
    public void setLogic(Logic l) {
        _l = l;
        _g.setReferenceCanvas(_l.getCanvasSize());

        resize();
    } // setLogic

    @Override
    public void setFPS(int fps) { }

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
     * @return (int) _mSurface Width
     */
    @Override
    public int getWidth() {
        return _mSurface.getWidth();
    } // getWidth

    /**
     * Return the SurfaceWidth if needed for calculations
     *
     * @return (int) _mSurface Height
     */
    @Override
    public int getHeight() {
        return _mSurface.getHeight();
    } // getHeight

    /**
     * Resizes the canvas to make it fit the screen. Called when the screen has changed it's
     * position.
     */
    public void resize(){
        Rect t1;
        Rect t2;

        // Resize
        // Get window size
        t2 = new Rect(_mSurface.getWidth(), 0, 0, _mSurface.getHeight());

        // Get logic canvas
        t1 = _l.getCanvasSize();

        // Resize
        _g.setCanvasSize(t1, t2);

        // Set pos
        _g.setCanvasPos(((_mSurface.getWidth()/2) - (_g.getCanvas().getWidth() / 2)),
                ((_mSurface.getHeight()/2) - (_g.getCanvas().getHeight() / 2)));
    } // resize
    //---------------------------------------------------------------
    //----------------------Surface and Canvas-----------------------
    //---------------------------------------------------------------


    //---------------------------------------------------------------
    //----------------------Main Loop Management---------------------
    //---------------------------------------------------------------
    /**
     * Update method. Is called once per frame and updates the logic with the elapsedTime value.
     */
    protected void update(){
        _l.update(_elapsedTime);
    } // update

    /**
     * Renders all new information.
     * Initialize the canvas and lock it to paint in it. Starts the frame and calls for Logic's
     * render. Then Unlock canvas and show it on the screen.
     */
    protected void render(){
        // Get holder canvas
        Canvas c = _holder.lockCanvas();

        // Start the frame
        _g.startFrame(c);

        _l.render();

        // Show the new information
        _holder.unlockCanvasAndPost(c);
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
        while(_running && _mSurface.getWidth() == 0);

        // Loop
        while(_running){
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

            // Inform about the fps (Debug only)
            if(_currentTime - _info > 1000000000l){
                long fps = _frames * 1000000000l / (_currentTime - _info);
                System.out.println("Info: " + fps + " fps");
                _frames = 0;
                _info = _currentTime;
            }
            ++_frames; // Update frames

            // Render
            // Update holder.
            _holder = _mSurface.getHolder();

            // Wait till Surface is ready
            while(!_holder.getSurface().isValid());

            // Render result
            render();
        }// while running
    } // run
    //---------------------------------------------------------------
    //----------------------Main Loop Management---------------------
    //---------------------------------------------------------------
} // Engine
