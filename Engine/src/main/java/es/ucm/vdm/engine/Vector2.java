package es.ucm.vdm.engine;

/**
 * 2 dimensions vector. Used for calculations and positions. Uses double as type of the positions
 * for avoiding loss of information.
 */
public class Vector2 {
    //---------------------------------------------------------------
    //---------------------------Atributes---------------------------
    //---------------------------------------------------------------
    public double _x;
    public double _y;
    public double _magnitude;
    public double _squareMagnitude;
    public Vector2 _unit;
    //---------------------------------------------------------------
    //---------------------------Atributes---------------------------
    //---------------------------------------------------------------

    /**
     * Constructor.
     *
     * @param x (double) X value
     * @param y (double) Y value
     */
    public Vector2(double x, double y){
        _x = x;
        _y = y;
    } // Constructor

    /**
     * Calculates the magnitude (distance) of the vector
     */
    public void magnitude(){
        _magnitude = Math.sqrt(Math.pow(_x, 2) + Math.pow(_y, 2));
    } // magnitude

    /**
     * Calculates
     */
    public void sqMagnitude(){
        _squareMagnitude = (Math.pow(_x, 2) + Math.pow(_y, 2));
    } // sqMagnitude

    /**
     * Function used to convert the values of the Vector2 into values between 0 and 1
     */
    public void normalize(){
        _unit = new Vector2(_x / _magnitude, _y / _magnitude);
    } // normalize
} // Vector2
