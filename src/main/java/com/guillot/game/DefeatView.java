package com.guillot.game;

import static org.newdawn.slick.Input.KEY_SPACE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Controller;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.SubView;
import com.guillot.engine.gui.Text;

public class DefeatView extends SubView {

    private final static Color OVERLAY = new Color(0f, 0f, 0f, .75f);

    private TimedRunView parent;

    private Button buttonRetry;

    private Button buttonMenu;

    private Text text;

    private Image portrait;

    public DefeatView(TimedRunView parent) {
        super(parent);

        this.parent = parent;
    }

    @Override
    public void start() throws Exception {
        text = new Text("", 32, EngineConfig.HEIGHT - 128);

        portrait = new Image("sprites/portrait.png");

        buttonRetry = new Button("Retry", EngineConfig.WIDTH / 4 - 64, EngineConfig.HEIGHT - 64, 128, 32);
        buttonRetry.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                parent.retryLevel();
            }
        });

        buttonMenu = new Button("Menu", 3 * EngineConfig.WIDTH / 4 - 64, EngineConfig.HEIGHT - 64, 128, 32);
        buttonMenu.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GUI.get().switchView(new MenuView());
            }
        });

        add(text, buttonRetry, buttonMenu);
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (GUI.get().isKeyPressed(KEY_SPACE) || Controller.get().isButtonPressed()) {
            parent.retryLevel();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(OVERLAY);
        g.fillRect(x, EngineConfig.HEIGHT - 160, EngineConfig.WIDTH, 160);

        g.drawImage(portrait, EngineConfig.WIDTH - portrait.getWidth() - 32, EngineConfig.HEIGHT - 192);

        super.paint(g);
    }

    @Override
    public void stop() throws Exception {}


    public void setDeathType(DeathType deathType) {
        switch (deathType) {
        case BLOCKED:
            text.setText("Damn.\nI'm blocked.");
            break;
        case DROWNED:
            text.setText("Glug-glug.., GLUG-GLUG!");
            break;
        }
    }

}
