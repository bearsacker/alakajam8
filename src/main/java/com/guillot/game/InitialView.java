package com.guillot.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.gui.View;

public class InitialView extends View {

    private Map map;

    private int level;

    @Override
    public void start() throws Exception {
        level = 1;

        map = new Map("maps/" + level + ".map");
    }

    @Override
    public void update() throws Exception {
        super.update();

        map.update();

        if (map.isComplete()) {
            level++;
            map = new Map("maps/" + level + ".map");
        }
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
