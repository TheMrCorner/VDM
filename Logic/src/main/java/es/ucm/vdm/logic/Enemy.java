package es.ucm.vdm.logic;

import es.ucm.vdm.engine.GameObject;
import es.ucm.vdm.engine.Graphics;

public class Enemy extends GameObject {
    public Enemy(int x, int y, int c) {
        super(x, y, c);
    }

    @Override
    public double getPosX() {
        return 0;
    }

    @Override
    public double getPosY() {
        return 0;
    }

    @Override
    public void setColor(int c) {

    }

    @Override
    public void update(double t) {

    }

    @Override
    public void render(Graphics g) {

    }
} // Enemy
