package es.ucm.vdm.androidengine;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

/**
 * Class that implements the Font class for its use in Android platform
 */
public class Font implements es.ucm.vdm.engine.Font {
    /**
     * Font object
     */
    private Typeface _font = null;


    /**
     * Attributes for rendering purposes
     */
    private View _view = null;
    private Paint _paint = null;
    private Canvas _canvas = null;

    /**
     * Attributes that control the appearance of the text
     */
    String _contents = "";
    int _fontSize = 1;
    int _fontColor = 0xFFFFFFFF;
    int _x = 0, _y = 0;

    @Override
    public boolean initializeFont(String filename, int fontSize, int fontColor, boolean isBold) {
        if (_view == null && _canvas == null && _paint == null) {
            System.err.println("Tried to load font before setting View, Canvas and Paint");
            return false;
        }

        _font = Typeface.createFromAsset(_view.getContext().getAssets(), filename);
        if(_font == null) {
            System.err.println("Error loading font");
            return false;
        }

        _fontSize = fontSize;
        _fontColor= fontColor;

        _paint.setTypeface(_font);
        _paint.setFakeBoldText(isBold);
        _paint.setColor(fontColor);
        _paint.setTextSize(fontSize);

        return true;
    }

    @Override
    public void setContents(String contents) {
        _contents = contents;
    }

    @Override
    public void render() {
        _canvas.drawText(_contents, _x, _y, _paint);
    }

    @Override
    public void setPosition(int x, int y) {
        _x = x;
        _y = y;
    }

    /**
     * Sets the View component used for retrieving the font asset
     * @param view (View) View component from Graphics
     */
    public void setView(View view) {
        _view = view;
    }

    /**
     * Sets the Paint component used for rendering/typesetting purposes
     * @param paint (Paint) Paint component from Graphics
     */
    public void setPaint(Paint paint) {
        _paint = paint;
    }

    /**
     * Sets the Canvas for rendering purposes
     * @param canvas (Canvas) Canvas component from Graphics
     */
    public void setCanvas(Canvas canvas) {
        _canvas = canvas;
    }
}
