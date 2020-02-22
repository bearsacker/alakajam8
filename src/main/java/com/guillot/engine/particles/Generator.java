package com.guillot.engine.particles;

import static org.apache.commons.math3.util.FastMath.max;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Graphics;


public class Generator {

    private float remainingDuration;

    private int x;

    private int y;

    private int angle;

    private int radius;

    private int generationPerSeconds;

    private float timeWithoutGeneration;

    private boolean running;

    private String name;

    private Particle pattern;

    private ArrayList<Particle> particles;

    public Generator(int x, int y, int angle, int radius, int generationPerSeconds, float duration) {
        this.x = x;
        this.y = y;
        this.angle = angle % 360;
        this.radius = radius;
        this.generationPerSeconds = generationPerSeconds;
        this.remainingDuration = duration;
        this.timeWithoutGeneration = 0.0f;
        this.running = true;

        this.particles = new ArrayList<Particle>();
    }

    public Generator(int radius, int generationPerSeconds) {
        this(0, 0, 0, radius, generationPerSeconds, -1);
    }

    public Generator(int x, int y, int angle, int radius, int generationPerSeconds) {
        this(x, y, angle, radius, generationPerSeconds, -1);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setAngle(int angle) {
        this.angle = angle % 360;
    }

    public int getAngle() {
        return angle;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setPattern(Particle pattern) {
        this.pattern = pattern;
    }

    public Particle getPattern() {
        return pattern;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void update(float delta) {
        if (remainingDuration != 0.0f && running) {
            timeWithoutGeneration += delta;

            while (timeWithoutGeneration >= 1.0f / generationPerSeconds) {
                particles.add(pattern.create());
                timeWithoutGeneration -= (1.0f / generationPerSeconds);
            }
        }

        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            if (particle.isDead()) {
                iterator.remove();
            } else {
                particle.update(delta);
            }
        }

        if (remainingDuration > 0.0f && running) {
            remainingDuration = max(0.0f, remainingDuration - delta);
        }
    }

    public void draw(Graphics g) {
        for (Particle particle : particles) {
            particle.draw(g);
        }
    }

    public boolean isDead() {
        return remainingDuration == 0.0f && particles.isEmpty();
    }
}
