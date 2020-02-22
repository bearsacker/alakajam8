package com.guillot.engine.configs;

import org.apache.commons.configuration2.Configuration;
import org.newdawn.slick.Color;

public final class ParticlesConfig extends Config {

    private static final ParticlesConfig INSTANCE = new ParticlesConfig();

    private ParticlesConfig() {
        super("particles.properties");
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    public static Color getColor(String key) {
        float red = get().getFloat(key + ".red", 1f);
        float green = get().getFloat(key + ".green", 1f);
        float blue = get().getFloat(key + ".blue", 1f);

        return new Color(red, green, blue);
    }
}
