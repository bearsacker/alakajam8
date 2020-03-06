package com.guillot.game.views;

import static com.guillot.game.Colors.BACKGROUND;
import static com.guillot.game.Colors.YELLOW;
import static com.guillot.game.Images.LOGO;
import static org.newdawn.slick.Input.KEY_ENTER;
import static org.newdawn.slick.Input.KEY_SPACE;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.View;
import com.guillot.game.Map;
import com.guillot.game.Sounds;
import com.guillot.game.Tile;

public class MenuView extends View {

    private Button buttonRun;

    private Button buttonQuit;

    private Text author;

    private Map map;

    private View viewToSwitch;

    @Override
    public void start() throws Exception {
        setBackgroundColor(BACKGROUND.getColor());

        if (!Sounds.MUSIC.getSound().playing()) {
            // Sounds.MUSIC.getSound().loop();
        }

        buttonRun = new Button("Timed Run", EngineConfig.WIDTH / 2 - 96, 336, 192, 32);
        buttonRun.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                if (!map.isAnimating()) {
                    launchTimedRun();
                }
            }
        });

        buttonQuit = new Button("Quit", EngineConfig.WIDTH / 2 - 96, 384, 192, 32);
        buttonQuit.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                System.exit(0);
            }
        });

        author = new Text("Bearsucker - 2020", 16, EngineConfig.HEIGHT - 24, YELLOW.getColor());

        add(buttonRun, buttonQuit, author);

        map = new Map(getWidth(), getHeight());
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (!map.isAnimating() && (GUI.get().isKeyPressed(KEY_ENTER) || GUI.get().isKeyPressed(KEY_SPACE))) {
            launchTimedRun();
        }

        if (map.isAnimationEnded() && viewToSwitch != null) {
            GUI.get().switchView(viewToSwitch);
        }

        map.update();
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        map.draw(g, null);
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
