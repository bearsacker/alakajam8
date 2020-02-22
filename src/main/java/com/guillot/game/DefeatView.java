package com.guillot.game;

import static org.newdawn.slick.Input.KEY_SPACE;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Controller;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.SubView;
import com.guillot.engine.gui.Text;

public class DefeatView extends SubView {

    private final static Color OVERLAY = new Color(0f, 0f, 0f, .95f);

    private TimedRunView parent;

    private Button buttonRetry;

    private Button buttonMenu;

    private Text text;

    public DefeatView(TimedRunView parent) {
        super(parent);

        this.parent = parent;
    }

    @Override
    public void start() throws Exception {
        text = new Text("Sorry!\nYou are blocked", 32, EngineConfig.HEIGHT - 128);

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

        super.paint(g);
    }

    @Override
    public void stop() throws Exception {}

}
