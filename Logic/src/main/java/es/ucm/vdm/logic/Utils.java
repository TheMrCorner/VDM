package es.ucm.vdm.logic;

public final class Utils {

	//public static int segmentsIntersection(...) {}

	//public static int sqrDistancePointSegment(...) {}

    /**
     * Function to parse data from JSON, checks if it is a Long or a double and converts it to
     * double, then return value.
     *
     * @param o (Object) Object parsed from json.
     * @return (double) Object value converted to double
     */
    public static double parse_double(Object o){
        if(o instanceof Long){ // if Long, parse it to double
            return ((Long) o).doubleValue();
        } // if
        else{ // else, assume double
            return (double)o;
        } // else
    } // parse_double
}
