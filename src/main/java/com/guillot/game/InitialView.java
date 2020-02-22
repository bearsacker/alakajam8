package com.guillot.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.View;

public class InitialView extends View {

    private Map map;

    private Text levelText;

    private int level;

    @Override
    public void start() throws Exception {
        level = 1;

        map = new Map("maps/" + level + ".map");


        levelText = new Text("Level " + level, 32, 32, GUI.get().getFont(), Color.yellow);

        add(levelText);
    }

    @Override
    public void update() throws Exception {
        super.update();

        map.update();

        if (map.isComplete() && !map.isAnimating() && !map.isAnimationEnded()) {
            map.launchAnimation();
        }

        if (map.isAnimationEnded()) {
            level++;
            levelText.setText("Level " + level);
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
