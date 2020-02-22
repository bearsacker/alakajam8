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
import com.guillot.engine.gui.View;

public class WinView extends SubView {

    private final static Color OVERLAY = new Color(0f, 0f, 0f, .95f);

    private Button buttonMenu;

    private Text text;

    private String timer;

    public WinView(View parent) {
        super(parent);
    }

    @Override
    public void start() throws Exception {
        text = new Text("Congratulations!\nYou finish the run into " + timer, 32, EngineConfig.HEIGHT - 128);
        buttonMenu = new Button("Menu", EngineConfig.WIDTH / 2 - 64, EngineConfig.HEIGHT - 64, 128, 32);
        buttonMenu.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                GUI.get().switchView(new MenuView());
            }
        });

        add(text, buttonMenu);
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (GUI.get().isKeyPressed(KEY_SPACE) || Controller.get().isButtonPressed()) {
            GUI.get().switchView(new MenuView());
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

    public void setTimer(String timer) {
        this.timer = timer;
    }

}
