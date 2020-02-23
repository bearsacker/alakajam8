package com.guillot.game;

import static org.newdawn.slick.Input.KEY_SPACE;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.FastMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.guillot.engine.Game;
import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.gui.Button;
import com.guillot.engine.gui.Controller;
import com.guillot.engine.gui.Event;
import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.View;
import com.guillot.engine.utils.NumberGenerator;

public class MenuView extends View {

    private final static Color YELLOW = new Color(1f, .9f, .2f);

    private final static int TILE_SIZE = 32;

    private Image tileSheet;

    private Image water;

    private Image flowers;

    private Image logo;

    private Button buttonRun;

    private Button buttonEndless;

    private Button buttonQuit;

    private Text author;

    private int[][] tiles;

    private int animation;

    private long lastAnimationTime;

    private View viewToSwitch;

    private List<Point> flowersPositions;

    @Override
    public void start() throws Exception {
        if (!Sounds.MUSIC.getSound().playing()) {
            Sounds.MUSIC.getSound().loop();
        }

        buttonRun = new Button("Timed Run", EngineConfig.WIDTH / 2 - 96, 240, 192, 32);
        buttonRun.setEvent(new Event() {

            @Override
            public void perform() throws Exception {
                launchAnimation(new TimedRunView());
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

        author = new Text("Bearsucker - 2020", 16, EngineConfig.HEIGHT - 24, YELLOW);

        add(buttonRun, buttonEndless, buttonQuit, author);

        logo = new Image("sprites/logo.png");
        tileSheet = new Image("sprites/tilesheet.png");
        water = new Image("sprites/water.png");
        flowers = new Image("sprites/flowers.png");

        tiles = new int[getWidth()][getHeight()];

        int numberSummits = NumberGenerator.get().randomInt(10, 30);
        for (int i = 0; i < numberSummits; i++) {
            int x = NumberGenerator.get().randomInt(getWidth());
            int y = NumberGenerator.get().randomInt(getWidth());
            int depth = NumberGenerator.get().randomInt(2, 5);

            setTile(x, y, depth);
            double type = NumberGenerator.get().randomDouble();

            if (type < .25f) {
                setTile(x + 1, y, depth);
                setTile(x + 1, y - 1, depth);
                setTile(x, y - 1, depth);
            } else if (type < .4f) {
                setTile(x + 1, y, depth);
            } else if (type < .55f) {
                setTile(x, y + 1, depth);
            }
        }

        for (int k = 0; k < 5; k++) {
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    if (getTile(i - 1, j) != null && getTile(i - 1, j) < getTile(i, j) - 1) {
                        setTile(i - 1, j, getTile(i, j) - 1);
                    }

                    if (getTile(i + 1, j) != null && getTile(i + 1, j) < getTile(i, j) - 1) {
                        setTile(i + 1, j, getTile(i, j) - 1);
                    }

                    if (getTile(i, j - 1) != null && getTile(i, j - 1) < getTile(i, j) - 1) {
                        setTile(i, j - 1, getTile(i, j) - 1);
                    }

                    if (getTile(i, j + 1) != null && getTile(i, j + 1) < getTile(i, j) - 1) {
                        setTile(i, j + 1, getTile(i, j) - 1);
                    }
                }
            }
        }

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (tiles[i][j] == 0) {
                    tiles[i][j] = -1;
                }
            }
        }

        flowersPositions = new ArrayList<>();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (NumberGenerator.get().randomDouble() > .8f) {
                    flowersPositions.add(new Point(i, j));
                }
            }
        }

        animation = Integer.MAX_VALUE;
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (GUI.get().isKeyPressed(KEY_SPACE) || Controller.get().isButtonPressed()) {
            launchAnimation(new TimedRunView());
            Sounds.SUCCESS.getSound().play();
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAnimationTime > 40) {
            for (int i = animation; i >= 0; i--) {
                Integer value = getTile(i, animation - i);
                if (value != null && ((value >= 0 && value < 2) || (value < 0 && value > -2))) {
                    if (value < 0) {
                        setTile(i, animation - i, -2);
                    } else {
                        setTile(i, animation - i, 2);
                    }
                }
            }

            for (int i = animation + 1; i >= 0; i--) {
                Integer value = getTile(i, animation - 1 - i);
                if (value != null && ((value >= 0 && value < 3) || (value < 0 && value > -3))) {
                    if (value < 0) {
                        setTile(i, animation - 1 - i, -3);
                    } else {
                        setTile(i, animation - 1 - i, 3);
                    }
                }
            }

            for (int i = animation + 2; i >= 0; i--) {
                Integer value = getTile(i, animation - 2 - i);
                if (value != null && ((value >= 0 && value < 4) || (value < 0 && value > -4))) {
                    if (value < 0) {
                        setTile(i, animation - 2 - i, -4);
                    } else {
                        setTile(i, animation - 2 - i, 4);
                    }
                }
            }

            for (int i = animation + 3; i >= 0; i--) {
                Integer value = getTile(i, animation - 3 - i);
                if (value != null && ((value >= 0 && value < 5) || (value < 0 && value > -5))) {
                    if (value < 0) {
                        setTile(i, animation - 3 - i, -5);
                    } else {
                        setTile(i, animation - 3 - i, 5);
                    }
                }
            }

            for (int i = animation + 4; i >= 0; i--) {
                Integer value = getTile(i, animation - 4 - i);
                if (value != null) {
                    if (value < 0) {
                        setTile(i, animation - 4 - i, -4);
                    } else {
                        setTile(i, animation - 4 - i, 4);
                    }
                }
            }

            for (int i = animation + 5; i >= 0; i--) {
                Integer value = getTile(i, animation - 5 - i);
                if (value != null) {
                    if (value < 0) {
                        setTile(i, animation - 5 - i, -3);
                    } else {
                        setTile(i, animation - 5 - i, 3);
                    }
                }
            }

            for (int i = animation + 6; i >= 0; i--) {
                Integer value = getTile(i, animation - 6 - i);
                if (value != null) {
                    if (value < 0) {
                        setTile(i, animation - 6 - i, -2);
                    } else {
                        setTile(i, animation - 6 - i, 2);
                    }
                }
            }

            for (int i = animation + 7; i >= 0; i--) {
                Integer value = getTile(i, animation - 7 - i);
                if (value != null) {
                    if (value < 0) {
                        setTile(i, animation - 7 - i, -1);
                    } else {
                        setTile(i, animation - 7 - i, 1);
                    }
                }
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

                boolean isWater = frame < 0;
                if (isWater) {
                    frame = FastMath.abs(frame) - 1;
                }

                g.drawImage(tileSheet, i * TILE_SIZE, (j - 2) * TILE_SIZE, (i + 1) * TILE_SIZE,
                        (j - 2) * TILE_SIZE + tileSheet.getHeight(),
                        frame * TILE_SIZE, 0, (frame + 1) * TILE_SIZE, tileSheet.getHeight());

                if (isWater) {
                    g.drawImage(water, i * TILE_SIZE, (j - 1) * TILE_SIZE - frame * 8);
                } else if (flowersPositions.contains(new Point(i, j))) {
                    int flowerFrame = (i + j) % 3;

                    g.drawImage(flowers, i * TILE_SIZE, (j - 2) * TILE_SIZE + (5 - frame) * 8, (i + 1) * TILE_SIZE,
                            (j - 1) * TILE_SIZE + (5 - frame) * 8, flowerFrame * TILE_SIZE, 0, (flowerFrame + 1) * TILE_SIZE,
                            TILE_SIZE);
                }
            }
        }

        g.drawImage(logo, EngineConfig.WIDTH / 2 - logo.getWidth() / 2, 80);

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
