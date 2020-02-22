package com.guillot.engine.utils;

import java.util.Random;

public class NumberGenerator {

    private static final NumberGenerator INSTANCE = new NumberGenerator();

    private Random random;

    private long seed;

    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    public double randomDouble() {
        return random.nextDouble();
    }

    public double randomDouble(double max) {
        return random.nextDouble() * max;
    }

    public double randomDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    public int randomInt(int max) {
        return (int) randomDouble(max);
    }

    public int randomInt(int min, int max) {
        return (int) randomDouble(min, max);
    }

    private NumberGenerator() {
        seed = System.currentTimeMillis();
        System.out.println(seed);

        random = new Random();
        random.setSeed(seed);
    }

    public static NumberGenerator get() {
        return INSTANCE;
    }
}
