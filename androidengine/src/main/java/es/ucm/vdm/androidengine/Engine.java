package es.ucm.vdm.androidengine;

import android.content.res.AssetManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.InputStream;

import es.ucm.vdm.engine.Logic;
import es.ucm.vdm.engine.Rect;

public class Engine implements es.ucm.vdm.engine.Engine, Runnable{
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    SurfaceView _mView;
    SurfaceHolder _mHolder;
    AssetManager _mAMan;

    volatile boolean _running; // Thread control
    Thread _renderThread;

    Graphics _g;
    Input _ip;
    Logic _l;

    // Atributes for calculations and time.
    long _lastFrameTime;
    long _currentTime, _nanoElapsedTime;
    double _elapsedTime;
    int _frames;
    long _info;

    public void onResume(){
        if(!_running){
            // Reset
            _running = true;

            // Init thread
            _renderThread = new Thread(this);
            _renderThread.start();
        } // if
    } // onResume

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

    @Override
    public Graphics getGraphics() {
        return null;
    }

    @Override
    public Input getInput() {
        return _ip;
    }

    @Override
    public InputStream openInputStream(String filename) {
        return null;
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

    public void resize(){
        Rect t1;
        Rect t2;

        // Resize
        // Get window size
        t2 = new Rect(_mView.getWidth(), 0, 0, _mView.getHeight());

        // Get logic canvas
        t1 = _l.getCanvasSize();

        // Resize
        _g.setCanvasSize(t1, t2);

        // Set pos
        _g.setCanvasPos(((_mView.getWidth()/2) - (_g.getCanvas().getWidth() / 2)),
                ((_mView.getHeight()/2) - (_g.getCanvas().getHeight() / 2)));
    } // resize

    @Override
    public void run() {
        if(_renderThread != Thread.currentThread()){
            // Check directly calling run()
            throw new RuntimeException("run() called directly");
        }

        while(_running && _mView.getWidth() == 0);

        while(_running){
            // Update
            // Calculate time passed between frames and convert it to seconds
            _currentTime = System.nanoTime();
            _nanoElapsedTime = _currentTime - _lastFrameTime;
            _lastFrameTime = _currentTime;
            _elapsedTime = (double) _nanoElapsedTime / 1.0E9;

            //processInput();
            resize();

            // Update all Logic
            //update();

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
            _mHolder = _mView.getHolder();

            // Wait till Surface is ready
            while(!_mHolder.getSurface().isValid());

            //render();

        }// while running
    } // run
} // Engine
