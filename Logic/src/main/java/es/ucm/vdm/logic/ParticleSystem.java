package es.ucm.vdm.logic;

import java.util.ArrayList;
import java.util.Random;

import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.VDMColor;

public class ParticleSystem extends GameObject {

    // Variables
    ArrayList<Particle> _particles;
    Random _rnd;

    /**
     * Constructor of the GameObject. Creates a new GameObject and assigns the position, the color
     * and the rotation that the object will have. To make this generic, initializes _rot to 0.
     *
     * @param x         (double) X coordinate.
     * @param y         (double) Y coordinate
     * @param c         (int) Color
     * @param thickness
     */
    public ParticleSystem(double x, double y, VDMColor c, int thickness, Vector2 coordOr) {
        super(x, y, c, thickness);

        setCoordOrigin(coordOr);

        _rnd = new Random();
        _particles = new ArrayList<Particle>();

        int low = 5;
        int high = 10;
        int particles = _rnd.nextInt(high - low) + low;

        for(int i = 0; i < particles; i++){
            CreateNewParticle();
        } // for
    } // Constructor

    private void CreateNewParticle() {
        float max = 0.6f;
        float min = 0.1f;

        float lVel = min + _rnd.nextFloat() * (max - min);
        float aVel = min + _rnd.nextFloat() * (max - min);

        double dirX = (-1) +_rnd.nextDouble() * (1 - (-1));
        double dirY = (-1) +_rnd.nextDouble() * (1 - (-1));

        int rot = _rnd.nextInt(180);

        Vector2 v = new Vector2(dirX, dirY);
        v.normalize();

        Particle p = new Particle(_pos._x, _pos._y, _c, _lineThickness,
                20, lVel, aVel, v._unit, rot);

        p.setCoordOrigin(_coordOrigin);

        _particles.add(p);
    } // CreateNewParticle

    @Override
    public void update(double t){
        for(Particle p : _particles){
            p.update(t);
        } // for
    } // update

    @Override
    public void render(Graphics g){
        for(Particle p : _particles){
            p.render(g);
        } // for
    } // render
} // ParticleSystem
