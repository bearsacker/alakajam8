package com.guillot.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.gui.View;

public class InitialView extends View {

    private Map map;

    private Player player;

    @Override
    public void start() throws Exception {
        setBackgroundColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));

        map = new Map("maps/1.map");
    }

    @Override
    public void update() throws Exception {
        super.update();

        map.update();
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        super.paintComponents(g);

        map.draw(g);
    }

    public static void main(String[] args) throws SlickException {
        new Game("Everyone hates depth", new InitialView());
    }
}
