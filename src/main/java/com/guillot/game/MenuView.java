package com.guillot.game;

import static com.guillot.game.Colors.YELLOW;
import static com.guillot.game.Images.LOGO;
import static org.newdawn.slick.Input.KEY_SPACE;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Controller;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.View;

public class MenuView extends View {

    private Button buttonRun;

    private Button buttonEndless;

    private Button buttonQuit;

    private Text author;

    private Map map;

    private View viewToSwitch;

    @Override
    public void start() throws Exception {
        if (!Sounds.MUSIC.getSound().playing()) {
            Sounds.MUSIC.getSound().loop();
        }

        buttonRun = new Button("Timed Run", EngineConfig.WIDTH / 2 - 96, 240, 192, 32);
        buttonRun.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                launchTimedRun();
            }
        });

        buttonEndless = new Button("Endless run", EngineConfig.WIDTH / 2 - 96, 288, 192, 32);
        buttonEndless.setEnabled(false);

        buttonQuit = new Button("Quit", EngineConfig.WIDTH / 2 - 96, 384, 192, 32);
        buttonQuit.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                System.exit(0);
            }
        });

        author = new Text("Bearsucker - 2020", 16, EngineConfig.HEIGHT - 24, YELLOW.getColor());

        add(buttonRun, buttonEndless, buttonQuit, author);

        map = new Map(getWidth(), getHeight());
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (!map.isAnimating() && GUI.get().isKeyPressed(KEY_SPACE) || Controller.get().isButtonPressed()) {
            launchTimedRun();
        }

        if (map.isAnimationEnded() && viewToSwitch != null) {
            GUI.get().switchView(viewToSwitch);
        }

        map.update();
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        map.draw(g, 0, -Tile.SIZE, null);
        g.drawImage(LOGO.getImage(), EngineConfig.WIDTH / 2 - LOGO.getImage().getWidth() / 2, 80);

        super.paintComponents(g);
    }

    public void launchTimedRun() {
        map.launchAnimation();
        viewToSwitch = new TimedRunView();
        Sounds.SUCCESS.getSound().play();
    }

    public int getWidth() {
        return EngineConfig.WIDTH / Tile.SIZE;
    }

    public int getHeight() {
        return EngineConfig.HEIGHT / Tile.SIZE;
    }

    public static void main(String[] args) throws SlickException {
        new Game("Everyone hates depth", "sprites/icon.png", new MenuView());
    }
}
