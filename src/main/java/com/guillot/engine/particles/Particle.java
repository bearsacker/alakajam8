package com.guillot.engine.particles;

import org.newdawn.slick.Graphics;


public interface Particle {

    public Particle create();

    public boolean isDead();

    public void update(float delta);

    public void draw(Graphics g);

}
