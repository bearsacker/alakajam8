package com.guillot.game;

import static org.newdawn.slick.Input.KEY_ENTER;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.View;

public class TimedRunView extends View {

    private final static Color OVERLAY = new Color(0f, 0f, 0f, .7f);

    private final static Color BACKGROUND_COLOR = new Color(.4f, .6f, 1f);

    private final static SimpleDateFormat TIMER_FORMAT = new SimpleDateFormat("mm:ss");

    private Map map;

    private Text levelText;

    private Text timerText;

    private Text sentence;

    private long time;

    private int level;

    private WinView winView;

    private DefeatView defeatView;

    @Override
    public void start() throws Exception {
        setBackgroundColor(BACKGROUND_COLOR);

        level = 1;
        time = System.currentTimeMillis();
        map = new Map("maps/" + level + ".map");
        sentence = new Text(map.getSentence(), 64, 80, Color.white);
        sentence.setX(EngineConfig.WIDTH / 2 - sentence.getWidth() / 2);

        levelText = new Text("Level " + level, 24, 14, Color.yellow);
        timerText = new Text("", 0, 14, Color.yellow);

        winView = new WinView(this);
        defeatView = new DefeatView(this);

        add(levelText, timerText, sentence, winView, defeatView);
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (isFocused()) {
            map.update();

            timerText.setText(TIMER_FORMAT.format(new Date(System.currentTimeMillis() - time)));
            timerText.setX(EngineConfig.WIDTH - timerText.getWidth() - 24);

            if (map.isComplete() && !map.isAnimating() && !map.isAnimationEnded()) {
                map.launchAnimation();
            }

            if (map.isAnimationEnded()) {
                level++;
                levelText.setText("Level " + level);

                try {
                    map = new Map("maps/" + level + ".map");
                    sentence.setText(map.getSentence());
                    sentence.setX(EngineConfig.WIDTH / 2 - sentence.getWidth() / 2);
                } catch (NullPointerException e) {
                    winView.setTimer(timerText.getText());
                    winView.setVisible(true);
                }
            }

            if (!map.isComplete() && !map.isAnimating()) {
                DeathType death = map.isDead();
                if (death != null) {
                    defeatView.setVisible(true);
                    defeatView.setDeathType(death);
                }
            }

            if (GUI.get().isKeyPressed(KEY_ENTER)) {
                retryLevel();
            }
        }
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        g.setColor(OVERLAY);
        g.fillRect(0, 0, EngineConfig.WIDTH, 48);

        map.draw(g);

        super.paintComponents(g);
    }

    public void retryLevel() throws Exception {
        map = new Map("maps/" + level + ".map");
        defeatView.setVisible(false);
    }

}
