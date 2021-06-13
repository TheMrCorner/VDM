package es.ucm.vdm.logic;

/**
 * Class that stores some functions that are useful for calculating distances, Vector calculations
 * and segments intersections.
 */
public final class Utils {

    /**
     * Function that calculates the point where two segments intersect. If there is no intersection
     * returns null.
     *
     * @param seg1Or (Vector2) First point of the first segment.
     * @param seg1End (Vector2) Second point of the first segment.
     * @param seg2Or (Vector2) First point of the second segment.
     * @param seg2End (Vector2) Second point of the second segment.
     * @return (Vector2) Point of intersection, null if there is no intersection.
     */
	public static Vector2 segmentsIntersection(Vector2 seg1Or, Vector2 seg1End, Vector2 seg2Or, Vector2 seg2End) {
        double s1_x, s1_y, s2_x, s2_y;
        s1_x = seg1End._x - seg1Or._x;     s1_y = seg1End._y - seg1Or._y;
        s2_x = seg2End._x - seg2Or._x;     s2_y = seg2End._y - seg2Or._y;

        double s, t;
        s = (-s1_y * (seg1Or._x - seg2Or._x) + s1_x * (seg1Or._y - seg2Or._y)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (seg1Or._y - seg2Or._y) - s2_y * (seg1Or._x - seg2Or._x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
        {
            // Collision detected
            double i_x = seg1Or._x + (t * s1_x);
            double i_y = seg1Or._y + (t * s1_y);
            return new Vector2(i_x, i_y);
        } // if

        // No collision
        return null;
    } // segmentsIntersection

    /**
     * Function to calculate the squared distance between a point p and the segment formed by the
     * points v1 and v2.
     *
     * @param v1 (Vector2) Position 1 of the segment.
     * @param v2 (Vector2) Position 2 of the segment.
     * @param p (Vector2) Point.
     * @return (double) Square distance between the point and the segment.
     */
	public static double sqrDistancePointSegment(Vector2 v1, Vector2 v2, Vector2 p) {
	    if(v1 == v2) {
	        return subVect(p, v1).sqMagnitude();
        } // if

	    Vector2 V = subVect(v2, v1);
	    Vector2 VP = subVect(p, v1);

        double t = Math.max(0, Math.min(1, dotProduct(VP, V) / V.sqMagnitude()));
        Vector2 temp = new Vector2(t * V._x, t * V._y);
        Vector2 proj = sumVect(v1, temp);
        return subVect(proj, p).sqMagnitude();
    } // sqrDistancePointSegment

    /**
     * Function to rotate a vector an amount provided.
     *
     * @param v (Vector2) Vector to rotate.
     * @param ang (double) Angle to rotate the vector.
     * @return (Vector2) Vector rotated.
     */
	public static Vector2 rotVect(Vector2 v, double ang){
        return new Vector2(
                Math.round(Math.cos(Math.toRadians(ang)) * (v._x)) - Math.round(Math.sin(Math.toRadians(ang)) * (v._y)),
                Math.round(Math.sin(Math.toRadians(ang)) * (v._x)) + Math.round(Math.cos(Math.toRadians(ang)) * (v._y))
        );
    } // rotVect

    /**
     * Function that adds one vector to another.
     *
     * @param a (Vector2) Vector 1.
     * @param b (Vector2) Vector 2.
     * @return (Vector2) Result of the adding process.
     */
	public static Vector2 sumVect(Vector2 a, Vector2 b){
	    return new Vector2(a._x + b._x, a._y + b._y);
    } // sumVect

    /**
     * Function that subtracts one vector to another.
     *
     * @param a (Vector2) Vector 1.
     * @param b (Vector2) Vector 2.
     * @return (Vector2) Result of the subtraction.
     */
    public static Vector2 subVect(Vector2 a, Vector2 b){
	    return new Vector2(a._x - b._x, a._y - b._y);
    } // subVect

    /**
     * Function that calculates the dot product of two vectors
     *
     * @param v (Vector2) Vector 1.
     * @param w (Vector2) Vector 2.
     * @return (double) Dot product of the vectors.
     */
    public static double dotProduct(Vector2 v, Vector2 w) {
	    return ((v._x * w._x) + (v._y * w._y));
    } // dotProduct

    /**
     * Function to parse data from JSON, checks if it is a Long or a double and converts it to
     * double, then return value.
     *
     * @param o (Object) Object parsed from json.
     * @return (double) Object value converted to double
     */
    public static double parseDouble(Object o){
        if(o instanceof Long){ // if Long, parse it to double
            return ((Long) o).doubleValue();
        } // if
        else{ // else, assume double
            return (double)o;
        } // else
    } // parse_double
} // Utils
