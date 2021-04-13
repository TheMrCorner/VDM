package es.ucm.vdm.engine;

import java.util.List;

public interface Input {

    // Represents screen touch inf.
    // Represents keyboard and mouse inf.
    class TouchEvent{
        public enum TouchType{
            PRESSED_DOWN,
            RELEASED,
            MOVED,
            CLICKED,
            KEY_PRESSED,
            KEY_RELEASED,
            KEY_TYPED
        }

        /**
         * Constructor of a TouchEvent. Gives it a position in the screen.
         * @param x Position X (pixels)
         * @param y Position Y (pixels)
         * @param t TouchType of the event
         */
        public TouchEvent(int x, int y, TouchType t, int i){
            _x = x;
            _y = y;
            _t = t;
            _fingerId = i;
        }

        /**
         * Return value of X position.
         * @return X position
         */
        public int getX(){
            return _x;
        }

        /**
         * Return value of Y position.
         * @return Y position
         */
        public int getY(){
            return _y;
        }

        public int getIdf(){
            return _fingerId;
        }

        /**
         * Return the type of the event, to be handled by the Logic
         * @return TouchType
         */
        public TouchType getType(){
            return _t;
        }

        // Private Atributes
        // Position X and Y
        int _x, _y;

        // finger identifier
        int _fingerId;

        /**
         * TouchType of the TouchEvent.
         */
        TouchType _t;
    }

    List<TouchEvent> getTouchEvents();
}
