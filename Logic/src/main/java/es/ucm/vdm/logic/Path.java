package es.ucm.vdm.logic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import es.ucm.vdm.engine.Graphics;

import static es.ucm.vdm.logic.Utils.parseDouble;

/**
 * This class stores all important information relative to the path of that the player must
 * follow. Extends GameObject to be rendered in scene.
 */
public class Path extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    ArrayList<ArrayList<Vector2>> _paths;
    ArrayList<ArrayList<Vector2>> _pathsDirs;
    int _activePath; // Variable that stores the actual path that the player is following

    /**
     * Constructor of a new path. Parses all the information relative to the paths and
     * stores it in an arraylist for using them later.
     *
     * @param x (int) X position (central) of the paths
     * @param y (int) Y position (central) of the paths
     * @param c (int) Color to paint the paths
     * @param paths (JSONArray) Array with all the paths
     */
    public Path(int x, int y, int c, JSONArray paths) {
        super(x, y, c);

        _paths = new ArrayList<>();
        _pathsDirs = new ArrayList<>();

        JSONObject temp;
        JSONArray vert;
        JSONArray dirs = null;
        Vector2 v;
        ArrayList<Vector2> t;
        double coordX, coordY;

        // Parse level data
        for(int i = 0; i < paths.size(); i++) {
            temp = (JSONObject) paths.get(i);
            vert = (JSONArray) temp.get("vertices");

            if (temp.containsKey("directions")) {
                dirs = (JSONArray) temp.get("directions");
            }

            t = new ArrayList<Vector2>();
            JSONObject coord;
            for (int j = 0; j < vert.size(); j++) {
                // Get object with coordinates
                coord = (JSONObject) vert.get(j);

                // Get each coordinate value
                Object nx = coord.get("x");
                Object ny = coord.get("y");

                // Coords
                coordX = parseDouble(nx);
                coordY = parseDouble(ny);

                v = new Vector2(coordX, coordY);
                t.add(v);
            } // vertices for
            _paths.add(t); // Save new vertices group

            if(temp.containsKey("directions")){
                dirs = (JSONArray) temp.get("directions");
                ArrayList<Vector2> d = new ArrayList<Vector2>();
                for(int j = 0; j < dirs.size(); j++){
                    // Get object with coordinates
                    coord = (JSONObject)dirs.get(j);

                    // Get each coordinate value
                    Object nx = coord.get("x");
                    Object ny = coord.get("y");

                    // Coords
                    coordX = parseDouble(nx);
                    coordY = parseDouble(ny);

                    v = new Vector2(coordX, coordY);
                    d.add(v);
                } // vertices for
                _pathsDirs.add(d);
            } // if
        } // paths for
    } // Path

    /**
     * Function to set which path is the player following now.
     *
     * @param a (int) New path to set active.
     */
    public void setActivePath(int a){
        _activePath = a;
    } // set_activePath

    /**
     * Function to access the actual active path and get it's information.
     *
     * @return (int) actual active path
     */
    public int getActivePath(){
        return _activePath;
    } // get_activePath

    /**
     * Get access to the paths array stored, for calculations and etc.
     *
     * @return (ArrayList) ArrayList of all vertices for this level.
     */
    public ArrayList<ArrayList<Vector2>> getPaths(){
        return _paths;
    } // get_paths

    /**
     * Return the initial position for placing the player.
     *
     * @return (Vector2) Initial position.
     */
    public Vector2 getInitPos(){
        return _paths.get(0).get(0);
    } // get_init_pos()

    /**
     * Returns the jumping direction of a path if it is specified.
     *
     * @param p (int) Path
     * @param v (int) Vertex
     * @return (Vector2) Direction jump (null if not exists)
     */
    public Vector2 getJumpDir(int p, int v) {
        // If list is not empty, that means that there is directions set for every path
        if(!_pathsDirs.isEmpty()){
            return _pathsDirs.get(p).get(v); // Return the jump direction of this path and vertex
        } // if
        else {
            return null;
        } // else
        //return null;
    } // get_jump_dir

    /**
     * Renders the GO with the characteristics set on this object. Render is specific from every
     * GO, there is no default render.
     *
     * @param g (Graphics) Graphics instance to paint it
     */
    public void render(Graphics g){
        // Set color to paint the player
        g.setColor(_c);

        g.save();

        g.translate(g.getCanvas().getLeft() + (int)_pos._x,
                g.getCanvas().getTop() + (int)_pos._y);

        int next;

        // Change transformation matrix
        for(int i = 0; i < _paths.size(); i++){
            for(int j = _paths.get(i).size() - 1; j >= 0; j--){
                next = j - 1;

                if(next < 0){
                    next = _paths.get(i).size() - 1;
                }

                g.drawLine(g.repositionX((int)_paths.get(i).get(j)._x),
                        g.repositionY((int)_paths.get(i).get(j)._y) * (-1),
                        g.repositionX((int)_paths.get(i).get(next)._x),
                        g.repositionY((int)_paths.get(i).get(next)._y) * (-1));
            } // vertices for
        } // paths for

        g.restore();
    } // render
} // Path
