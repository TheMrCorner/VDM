package es.ucm.vdm.logic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import es.ucm.vdm.engine.GameObject;
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.Vector2;

/**
 * This class stores all important information relative to the path of that the player must
 * follow. Extends GameObject to be rendered in scene.
 */
public class Path extends GameObject {
    //---------------------------------------------------------------
    //----------------------Private Atributes------------------------
    //---------------------------------------------------------------
    ArrayList<ArrayList<Vector2>> _paths;


    public Path(int x, int y, int c, JSONArray paths) {
        super(x, y, c);

        _paths = new ArrayList<>();

        JSONObject temp;
        JSONArray vert;
        Vector2 v;
        ArrayList<Vector2> t;
        double coordX, coordY;

        // Parse level data
        for(int i = 0; i < paths.size(); i++){
            temp = (JSONObject)paths.get(i);
            vert = (JSONArray)temp.get("vertices");
            t = new ArrayList<Vector2>();
            JSONObject coord;
            for(int j = 0; j < vert.size(); j++){
                // Get object with coordinates
                coord = (JSONObject)vert.get(j);

                // Get each coordinate value
                Object nx = coord.get("x");
                Object ny = coord.get("y");

                // Check value
                if(nx instanceof Long){ // if Long, parse it to double
                    coordX = ((Long) nx).doubleValue();
                } // if
                else{ // else, assume double
                    coordX = (double)nx;
                } // else
                if(ny instanceof Long){
                    coordY = ((Long) ny).doubleValue();
                } // if
                else{
                    coordY = (double)ny;
                } // else

                v = new Vector2(coordX, coordY);
                t.add(v);
            } // vertices for
            _paths.add(t); // Save new vertices group
        } // paths for
    }

    /**
     * Get access to the paths array stored, for calculations and etc.
     *
     * @return (ArrayList) ArrayList of all vertices for this level.
     */
    public ArrayList<ArrayList<Vector2>> get_paths(){
        return _paths;
    } // get_paths

    /**
     * Updates the state of the gameobject with the time elapsed between frames. Update is specific
     * from every GO, there is no default update.
     *
     * @param t (double) Time elapsed
     */
    public void update(double t){ }

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
