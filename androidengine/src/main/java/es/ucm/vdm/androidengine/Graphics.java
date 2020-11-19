package es.ucm.vdm.androidengine;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

import es.ucm.vdm.engine.AbstractGraphics;
import es.ucm.vdm.engine.Rect;

public class Graphics extends AbstractGraphics {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    SurfaceView _sView;
    AssetManager _aMan;
    Paint _pnt;
    Canvas _cnv;

    public Graphics(SurfaceView sv, AssetManager am){
        _sView = sv;
        _aMan = am;
        _pnt = new Paint();
    } // Graphics

    public void startFrame(Canvas c) {
        _cnv = c;
    } // startFrame

    @Override
    public void clear(int color) {
        // Set color
        _pnt.setColor(color);

        // Paint screen
        _cnv.drawRect((float)_can.getX(), (float)_can.getY(),
                (float)(_can.getX() + _can.getWidth()), (float)(_can.getY() + _can.getHeight()),
                _pnt);

        // Reset paint
        _pnt.reset();
    } // clear

    @Override
    public void drawLine(int y1, int x1, int y2, int x2, int color) {
        _pnt.setColor(color);
        _cnv.drawLine(x1, y1, x2, y2, _pnt);
    } // drawLine

    @Override
    public int getWidth() {
        return _sView.getWidth();
    } // getWidth

    @Override
    public int getHeight() {
        return _sView.getHeight();
    } // getHeight
} // Graphics
