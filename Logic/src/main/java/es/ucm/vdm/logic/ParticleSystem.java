package es.ucm.vdm.logic;

// JAVA
import java.util.ArrayList;
import java.util.Random;

// UCM
import es.ucm.vdm.engine.Graphics;
import es.ucm.vdm.engine.VDMColor;

/**
 * ParticleSystem GameObject. This Object manages all the particles that can be created, calling for
 * the different functions that particles need to work properly.
 */
public class ParticleSystem extends GameObject {

    // Variables
    ArrayList<Particle> _particles;
    Random _rnd;

    /**
     * ParticleSystem constructor. Creates the new system with a random number of particles within
     * a range.
     *
     * @param x (double) X position of the System.
     * @param y (double) Y position of the System.
     * @param c (VDMColor) Color of the particles.
     * @param thickness (int) Thickness of the lines of the particles.
     * @param coordOr (Vector2) Coordinates origin for giving it to the particles.
     */
    public ParticleSystem(double x, double y, VDMColor c, int thickness, Vector2 coordOr) {
        super(x, y, c, thickness);

        // Set the Coordinates origin
        setCoordOrigin(coordOr);

        // Initialize all important variables
        _rnd = new Random();
        _particles = new ArrayList<Particle>();

        // Establish the number of particles within a range
        int low = 5;
        int high = 10;
        int particles = _rnd.nextInt(high - low) + low;

        // Create all particles
        for(int i = 0; i < particles; i++){
            CreateNewParticle();
        } // for
    } // Constructor

    /**
     * Function to create a new particle and add it to the ArrayList that contains all Particles.
     * it gives them random values for linear speed, angular speed, direction and initial rotation.
     */
    private void CreateNewParticle() {
        float max = 0.6f;
        float min = 0.1f;

        // Velocities
        float lVel = min + _rnd.nextFloat() * (max - min);
        float aVel = min + _rnd.nextFloat() * (max - min);

        // Direction
        double dirX = (-1) +_rnd.nextDouble() * (1 - (-1));
        double dirY = (-1) +_rnd.nextDouble() * (1 - (-1));

        // Initial angle rotation
        int rot = _rnd.nextInt(180);

        // Normalize direction
        Vector2 v = new Vector2(dirX, dirY);
        v.normalize();

        // Create particle
        Particle p = new Particle(_pos._x, _pos._y, _c, _lineThickness,
                20, lVel, aVel, v._unit, rot);
        p.setCoordOrigin(_coordOrigin);

        // Add it to the list
        _particles.add(p);
    } // CreateNewParticle

    /**
     * Call for the update of all particles created.
     *
     * @param t (double) Time elapsed.
     */
    @Override
    public void update(double t){
        for(Particle p : _particles){
            p.update(t);
        } // for
    } // update

    /**
     * Call for the rendering process of all particles created.
     *
     * @param g (Graphics) Graphics instance to paint it
     */
    @Override
    public void render(Graphics g){
        for(Particle p : _particles){
            p.render(g);
        } // for
    } // render
} // ParticleSystem
