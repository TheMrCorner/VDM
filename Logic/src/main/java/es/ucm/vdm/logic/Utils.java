package es.ucm.vdm.logic;

public final class Utils {

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

	public static Vector2 rotVect(Vector2 v, double ang){
        return new Vector2(
                Math.round(Math.cos(Math.toRadians(ang)) * (v._x)) - Math.round(Math.sin(Math.toRadians(ang)) * (v._y)),
                Math.round(Math.sin(Math.toRadians(ang)) * (v._x)) + Math.round(Math.cos(Math.toRadians(ang)) * (v._y))
        );
    } // rotVect

	public static Vector2 sumVect(Vector2 a, Vector2 b){
	    return new Vector2(a._x + b._x, a._y + b._y);
    } // sumVect

    public static Vector2 subVect(Vector2 a, Vector2 b){
	    return new Vector2(a._x - b._x, a._y - b._y);
    } // subVect

	public static Vector2 crossProduct(Vector2 v, Vector2 w){
	    Vector2 res = new Vector2(0, 0);
	    res._x = v._x*w._y - v._y*w._x;
	    res._y = -(v._y*w._x);

	    return res;
    } // scalarProduct

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
}
