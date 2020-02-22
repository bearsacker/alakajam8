package com.guillot.engine;

import static com.guillot.engine.configs.EngineConfig.FPS;
import static com.guillot.engine.configs.EngineConfig.FULLSCREEN;
import static com.guillot.engine.configs.EngineConfig.HEIGHT;
import static com.guillot.engine.configs.EngineConfig.WIDTH;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.View;

public class Game extends BasicGame {

    private View initialView;

    public Game(String title, View initialView) throws SlickException {
        this(title, null, initialView);
    }

    public Game(String title, String icon, View initialView) throws SlickException {
        super(title);

        this.initialView = initialView;

        AppGameContainer app = new AppGameContainer(this);
        app.setDisplayMode(WIDTH, HEIGHT, FULLSCREEN);
        app.setShowFPS(false);
        if (icon != null) {
            app.setIcon(icon);
        }
        if (FPS > 0) {
            app.setTargetFrameRate(FPS);
        }
        app.start();
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        GUI.get().switchView(initialView);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        GUI.get().paint(g);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        GUI.get().update(container);
    }

}
