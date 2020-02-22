package com.guillot.engine.configs;

import org.apache.commons.configuration2.Configuration;

public final class EngineConfig extends Config {

    private static final EngineConfig INSTANCE = new EngineConfig("engine.properties");

    public final static int WIDTH = get().getInt("engine.width");

    public final static int HEIGHT = get().getInt("engine.height");

    public final static boolean FULLSCREEN = get().getBoolean("engine.fullscreen");

    public final static int FPS = get().getInt("engine.fps");

    private EngineConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }
}
