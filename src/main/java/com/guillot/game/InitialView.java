package com.guillot.game;

import static org.newdawn.slick.Input.KEY_ENTER;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.View;

public class InitialView extends View {

    private final static SimpleDateFormat TIMER_FORMAT = new SimpleDateFormat("mm:ss");

    private Map map;

    private Text levelText;

    private Text timerText;

    private long time;

    private int level;

    private WinView winView;

    private DefeatView defeatView;

    @Override
    public void start() throws Exception {
        level = 1;
        time = System.currentTimeMillis();
        map = new Map("maps/" + level + ".map");

        levelText = new Text("Level " + level, 32, 32, Color.yellow);
        timerText = new Text("", 0, 32, Color.yellow);
        winView = new WinView(this);
        defeatView = new DefeatView(this);

        add(levelText, timerText, winView, defeatView);
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (isFocused()) {
            map.update();

            timerText.setText(TIMER_FORMAT.format(new Date(System.currentTimeMillis() - time)));
            timerText.setX(EngineConfig.WIDTH - timerText.getWidth() - 32);

            if (map.isComplete() && !map.isAnimating() && !map.isAnimationEnded()) {
                map.launchAnimation();
            }

            if (map.isAnimationEnded()) {
                level++;
                levelText.setText("Level " + level);
                map = new Map("maps/" + level + ".map");

                // winView.setTimer(timerText.getText());
                // winView.setVisible(true);
                defeatView.setVisible(true);
            }

            if (GUI.get().isKeyPressed(KEY_ENTER)) {
                retryLevel();
            }
        }
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        map.draw(g);

        super.paintComponents(g);
    }

    public void retryLevel() throws Exception {
        map = new Map("maps/" + level + ".map");
        defeatView.setVisible(false);
    }

    public static void main(String[] args) throws SlickException {
        new Game("Everyone hates depth", new InitialView());
    }
}
