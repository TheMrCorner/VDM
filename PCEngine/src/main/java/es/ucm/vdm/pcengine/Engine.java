package es.ucm.vdm.pcengine;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import es.ucm.vdm.engine.Logic;

public class Engine implements es.ucm.vdm.engine.Engine, Runnable, ComponentListener {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    // Initializing
    Window _win;
    Graphics _g;
    Input _ip;
    Logic _logic;

    // Time and Calculations
    long _lastFrameTime;
    long _currentTime, _nanoElapsedTime;
    double _elapsedTime;
    int _width;
    int _height;
    int _frames;
    long _info;
    int FPS = 30;
    double averageFPS;

    // Testing
    int _currentColor = 0; //Background sprites iterator
    //GREEN, TURQUOISE, LIGHTBLUE, BLUE, PURPLE, DARKBLUE, YELLOW, ORANGE, BROWN
    int _planeColor[] = {0xff41a85f, 0xff00a885, 0xff3d8eb9, 0xff2969b0, 0xff553982, 0xff28324e, 0xfff37934,
            0xffd14b41, 0xff75706b};

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

        // Initialize some time values
        _lastFrameTime = System.nanoTime(); // System time in ms
        _info = _lastFrameTime; // Information about the fps (debug)
        _frames = 0; // Number of frames passed

        _win.addComponentListener(this);
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }

    @Override
    public Input getInput() {
        return _ip;
    }

    @Override
    public void setLogic(Logic l) {

    }

    @Override
    public void HandleException(Exception e) {
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {

    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {

    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {

    }

    /**
     * Render function.
     * Clears the buffer to paint again.
     * Calls the render from the logic to paint the updated version of the screen.
     * If it fails, it will still paint the screen.  It has 2 loops because it will try to paint
     * the buffer and then show the buffer. Ideally, this won't fail and it will do only 1 iteration
     * per loop.
     */
    void render(){
        _g.clear(0); // Clear the whole buffer

        // Ideally this will only do 1 iteration per loop
        do {
            do {
                _win.setGraphics();
                try { // Try to paint in the Graphics
                    //_g.testCanvas(_win);
                    //_g.drawLine(200, 200, 800, 1000, _planeColor[_currentColor]);
                    _win.getJGraphics().setColor(new Color(_planeColor[_currentColor]));
                    // TODO: Line stroke
                    _win.getJGraphics().drawRect(200, 200, 400, 400);
                    _win.getJGraphics().setColor(Color.BLACK);
                    _win.getJGraphics().drawString("FPS: " + averageFPS, 10, 10);
                    _currentColor++;
                    if(_currentColor == _planeColor.length){
                        _currentColor = 0;
                    }
                    // Set the position of the canvas
                }
                finally { // If not, still dispose the Swing Graphics
                    _win.getJGraphics().dispose();
                }
            } while(_win.getBufferStrategy().contentsRestored());
            _win.getBufferStrategy().show(); // Show the buffer with the updated information (painted)
        } while(_win.getBufferStrategy().contentsLost());
    }

    @Override
    public void run() {
        // FPS
        long startTime; // Starting loop time
        long URDTimeMilliSec; // Time since start in millisecond
        long waitTime; // Waiting time for next frame
        long totalTime = 0; // Total time

        int fCount = 0; // FrameCount
        int maxFrameRate = 30; // Max FPS (Default 30, can change)

        long targetTime = 1000 / FPS; // Time to run at FPS

        // TODO: Change to while(running)
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

            // TODO: Update();

            // Inform about the fps (Debug only)
            if(_currentTime - _info > 1000000000l){
                long fps = _frames * 1000000000l / (_currentTime - _info);
                System.out.println("Info: " + fps + " fps");
                _frames = 0;
                _info = _currentTime;
            }
            ++_frames; // Update frames

            // Clear and update graphics
            render();

            URDTimeMilliSec = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - URDTimeMilliSec;

            try{
                Thread.sleep(waitTime);
            }catch(Exception e){
                HandleException(e);
            }

            totalTime += System.nanoTime() - startTime;
            fCount++;
            if(fCount == maxFrameRate) {
                averageFPS = 1000.0 / ((totalTime / fCount) / 1000000);
                fCount = 0;
                totalTime = 0;
            }
        }
    }
}
