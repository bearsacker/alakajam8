package com.guillot.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.View;
import com.guillot.engine.utils.NumberGenerator;

public class MenuView extends View {

    private final static int TILE_SIZE = 32;

    private Image tileSheet;

    private Button buttonRun;

    private Button buttonEndless;

    private Button buttonCommands;

    private Button buttonQuit;

    private int[][] tiles;

    private int animation;

    private long lastAnimationTime;

    private View viewToSwitch;

    @Override
    public void start() throws Exception {
        buttonRun = new Button("Timed Run", EngineConfig.WIDTH / 2 - 96, 192, 192, 32);
        buttonRun.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                launchAnimation(new InitialView());
            }
        });

        buttonEndless = new Button("Endless run", EngineConfig.WIDTH / 2 - 96, 240, 192, 32);
        buttonEndless.setEnabled(false);

        buttonCommands = new Button("Commands", EngineConfig.WIDTH / 2 - 96, 320, 192, 32);
        buttonCommands.setEnabled(false);

        buttonQuit = new Button("Quit", EngineConfig.WIDTH / 2 - 96, 368, 192, 32);
        buttonQuit.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                System.exit(0);
            }
        });

        add(buttonRun, buttonEndless, buttonCommands, buttonQuit);

        tileSheet = new Image("sprites/tilesheet.png");
        tiles = new int[getWidth()][getHeight()];
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                tiles[i][j] = NumberGenerator.get().randomInt(0, 4);
            }
        }

        animation = Integer.MAX_VALUE;
    }

    @Override
    public void update() throws Exception {
        super.update();

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastAnimationTime > 50) {
            for (int i = animation; i >= 0; i--) {
                Integer value = getTile(i, animation - i);
                if (value != null && value < 2) {
                    setTile(i, animation - i, 4);
                }
            }

            for (int i = animation + 1; i >= 0; i--) {
                Integer value = getTile(i, animation - 1 - i);
                if (value != null && value < 2) {
                    setTile(i, animation - 1 - i, 2);
                }
            }

            for (int i = animation + 2; i >= 0; i--) {
                Integer value = getTile(i, animation - 2 - i);
                if (value != null && value < 2) {
                    setTile(i, animation - 2 - i, 3);
                }
            }

            for (int i = animation + 3; i >= 0; i--) {
                Integer value = getTile(i, animation - 3 - i);
                if (value != null && value < 2) {
                    setTile(i, animation - 3 - i, 4);
                }
            }

            for (int i = animation + 4; i >= 0; i--) {
                setTile(i, animation - 4 - i, 3);
            }

            for (int i = animation + 5; i >= 0; i--) {
                setTile(i, animation - 5 - i, 2);
            }

            for (int i = animation + 6; i >= 0; i--) {
                setTile(i, animation - 6 - i, 2);
            }

            animation++;
            lastAnimationTime = currentTime;
        }

        if (isAnimationEnded() && viewToSwitch != null) {
            GUI.get().switchView(viewToSwitch);
        }
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                int frame = tiles[i][j];

                g.drawImage(tileSheet, i * TILE_SIZE, (j - 2) * TILE_SIZE, (i + 1) * TILE_SIZE,
                        (j - 2) * TILE_SIZE + tileSheet.getHeight(),
                        frame * TILE_SIZE, 0, (frame + 1) * TILE_SIZE, tileSheet.getHeight());
            }
        }

        super.paintComponents(g);
    }

    public void setTile(int x, int y, int depth) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return;
        }

        tiles[x][y] = depth;
    }

    public Integer getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return null;
        }

        return tiles[x][y];
    }

    public int getWidth() {
        return EngineConfig.WIDTH / TILE_SIZE + 2;
    }

    public int getHeight() {
        return EngineConfig.HEIGHT / TILE_SIZE + 2;
    }

    private void launchAnimation(View view) {
        viewToSwitch = view;
        animation = 0;
        buttonCommands.setEnabled(false);
        buttonEndless.setEnabled(false);
        buttonQuit.setEnabled(false);
        buttonRun.setEnabled(false);
    }

    public boolean isAnimationEnded() {
        return animation >= getWidth() * 2 + 6;
    }

    public static void main(String[] args) throws SlickException {
        new Game("Everyone hates depth", "sprites/icon.png", new MenuView());
    }
}
