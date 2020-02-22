package com.guillot.engine.particles;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Particles {

    private static Particles instance = new Particles();

    private ArrayList<Generator> generators;

    private long lastUpdate;

    private Particles() {
        this.generators = new ArrayList<Generator>();
        this.lastUpdate = System.currentTimeMillis();
    }

    public static Particles get() {
        return instance;
    }

    public void add(Generator generator) {
        if (generator != null) {
            generators.add(generator);
        }
    }

    public void update() {
        long time = System.currentTimeMillis();
        float delta = (time - lastUpdate) / 1000.0f;
        lastUpdate = time;

        Iterator<Generator> iterator = generators.iterator();
        while (iterator.hasNext()) {
            Generator generator = iterator.next();
            if (generator.isDead()) {
                iterator.remove();
            } else {
                generator.update(delta);
            }
        }
    }

    public void draw(Graphics g) {
        for (Generator generator : generators) {
            generator.draw(g);
        }

        g.setColor(Color.white);
    }

    public void removeAll() {
        generators.clear();
    }
}
