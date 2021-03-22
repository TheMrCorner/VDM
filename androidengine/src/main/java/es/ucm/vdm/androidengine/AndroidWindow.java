package es.ucm.vdm.androidengine;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidWindow extends SurfaceView {
    protected SurfaceHolder _holder;

    public AndroidWindow(Context context) {
        super(context);

        _holder = getHolder();
    } // Constructor

    public SurfaceHolder getSurfaceHolder(){
        return _holder;
    } // getSurfaceHolder

    public boolean surfaceValid(){
        return _holder.getSurface().isValid();
    } // surfaceValid

    public int width() {
        return getWidth();
    } // width

    public int height() {
        return getHeight();
    } // height
} // AndroidWindow
