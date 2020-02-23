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

    private final static Color OVERLAY = new Color(0f, 0f, 0f, .7f);

    private TimedRunView parent;

    private Button buttonRetry;

    private Button buttonMenu;

    private Text text;

    private Image portrait;

    private int frame;

    public DefeatView(TimedRunView parent) throws Exception {
        super(parent);

        this.parent = parent;
        text = new Text("", 144, EngineConfig.HEIGHT - 92);

        portrait = new Image("sprites/portrait.png");
        frame = 0;

        buttonRetry = new Button("Retry", EngineConfig.WIDTH - 160, EngineConfig.HEIGHT - 112, 128, 32);
        buttonRetry.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                parent.retryLevel();
            }
        });

        buttonMenu = new Button("Menu", EngineConfig.WIDTH - 160, EngineConfig.HEIGHT - 64, 128, 32);
        buttonMenu.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GUI.get().switchView(new MenuView());
            }
        });

        add(text, buttonRetry, buttonMenu);
    }

    @Override
    public void start() throws Exception {}

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
        g.fillRect(x, EngineConfig.HEIGHT - 144, EngineConfig.WIDTH, 144);

        g.drawImage(portrait, 24, EngineConfig.HEIGHT - 120, 120, EngineConfig.HEIGHT - 24, frame * 144, 0, (frame + 1) * 144, 144);

        super.paint(g);
    }

    @Override
    public void stop() throws Exception {}


    public void setDeathType(DeathType deathType) {
        switch (deathType) {
        case BLOCKED:
            text.setText("Damn.\nI'm blocked.");
            frame = 0;
            break;
        case DROWNED:
            text.setText("Glug-glug..?,\nGLUG-GLUG!!");
            frame = 1;
            break;
        }
    }

}
