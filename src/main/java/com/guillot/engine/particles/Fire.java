package com.guillot.engine.particles;

import static org.apache.commons.math3.util.FastMath.cos;
import static org.apache.commons.math3.util.FastMath.random;
import static org.apache.commons.math3.util.FastMath.sin;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.ParticlesConfig;
import com.guillot.engine.utils.NumberGenerator;


public class Fire implements Particle {

    private final static int SIZE = ParticlesConfig.get().getInt("fire.size");

    private final static Color COLOR = ParticlesConfig.getColor("fire.color");

    private final static double RATIODEGRAD = 0.0174533;

    private Generator generator;

    private float x;

    private float y;

    private int angle;

    private float remainingDuration;

    private float duration;

    private int speed;

    public Fire(Generator generator, float duration, int speed) {
        this.generator = generator;
        x = generator.getX() + NumberGenerator.get().randomInt(-2, 2);
        y = generator.getY() + NumberGenerator.get().randomInt(-2, 2);

        this.duration = duration;
        this.speed = speed;

        angle = generator.getAngle();
        if (random() < 0.5) {
            angle += (int) (random() * generator.getRadius() / 2);
        } else {
            angle -= (int) (random() * generator.getRadius() / 2);
        }
        angle %= 360;

        remainingDuration = (int) (random() * duration) + duration / 2;
    }

    @Override
    public Particle create() {
        return new Fire(generator, duration, speed);
    }

    @Override
    public boolean isDead() {
        return remainingDuration <= 0.0f;
    }

    @Override
    public void update(float delta) {
        remainingDuration -= delta;
        x += delta * speed * cos(RATIODEGRAD * angle);
        y += delta * speed * sin(RATIODEGRAD * angle);
    }

    @Override
    public void draw(Graphics g) {
        float alpha = remainingDuration / duration;
        g.setColor(new Color(COLOR.r, COLOR.g * alpha, COLOR.b, alpha));
        g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }
}
