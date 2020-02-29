package com.guillot.game;

import static com.guillot.game.Images.TILESHEET;
import static com.guillot.game.Tile.SIZE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.utils.FileLoader;
import com.guillot.engine.utils.NumberGenerator;

public class Map {

    private Tile[][] tiles;

    private Player player;

    private String sentence;

    private Image image;

    private Graphics graphics;

    private int animation;

    private long lastAnimationTime;

    public Map() throws SlickException {
        player = new Player(this);
        animation = -1;
    }

    public Map(int width, int height) throws SlickException {
        this();

        tiles = new Tile[width][height];
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                tiles[i][j] = new Tile(i, j, 0);
            }
        }

        int numberSummits = NumberGenerator.get().randomInt(10, 30);
        for (int i = 0; i < numberSummits; i++) {
            int x = NumberGenerator.get().randomInt(getWidth());
            int y = NumberGenerator.get().randomInt(getWidth());
            int depth = NumberGenerator.get().randomInt(2, 5);

            getTile(x, y).setHeight(depth);
            double type = NumberGenerator.get().randomDouble();

            if (type < .25f) {
                if (getTile(x + 1, y) != null) {
                    getTile(x + 1, y).setHeight(depth);
                }
                if (getTile(x + 1, y - 1) != null) {
                    getTile(x + 1, y - 1).setHeight(depth);
                }
                if (getTile(x, y - 1) != null) {
                    getTile(x, y - 1).setHeight(depth);
                }
            } else if (type < .4f && getTile(x + 1, y) != null) {
                getTile(x + 1, y).setHeight(depth);
            } else if (type < .55f && getTile(x, y + 1) != null) {
                getTile(x, y + 1).setHeight(depth);
            }
        }

        for (int k = 0; k < 5; k++) {
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    if (getTile(i - 1, j) != null && getTile(i - 1, j).getHeight() < getTile(i, j).getHeight() - 1) {
                        getTile(i - 1, j).setHeight(getTile(i, j).getHeight() - 1);
                    }

                    if (getTile(i + 1, j) != null && getTile(i + 1, j).getHeight() < getTile(i, j).getHeight() - 1) {
                        getTile(i + 1, j).setHeight(getTile(i, j).getHeight() - 1);
                    }

                    if (getTile(i, j - 1) != null && getTile(i, j - 1).getHeight() < getTile(i, j).getHeight() - 1) {
                        getTile(i, j - 1).setHeight(getTile(i, j).getHeight() - 1);
                    }

                    if (getTile(i, j + 1) != null && getTile(i, j + 1).getHeight() < getTile(i, j).getHeight() - 1) {
                        getTile(i, j + 1).setHeight(getTile(i, j).getHeight() - 1);
                    }
                }
            }
        }

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (tiles[i][j].getHeight() == 0) {
                    tiles[i][j].setWaterHeight(1);
                }
            }
        }

        image = new Image(width * SIZE, height * SIZE + (TILESHEET.getImage().getHeight() - SIZE));
        graphics = image.getGraphics();
    }

    public Map(String path) throws Exception {
        this();

        List<String> lines = new BufferedReader(new InputStreamReader(FileLoader.streamFromResource(path), UTF_8)).lines()
                .collect(Collectors.toList());

        for (int i = 0; i < lines.size() - 1; i++) {
            String[] depths = lines.get(i + 1).split(";");

            if (tiles == null) {
                tiles = new Tile[depths.length][];

                for (int j = 0; j < depths.length; j++) {
                    tiles[j] = new Tile[lines.size()];
                }
            }

            for (int j = 0; j < depths.length; j++) {
                if (!depths[j].equals("...")) {
                    int value = Integer.parseInt(depths[j]);

                    int weather = value / 100;
                    int waterHeight = (value % 100) / 10;
                    int height = value % 10;
                    tiles[j][i] = new Tile(j, i, Weather.findByValue(weather), height, waterHeight);
                }
            }
        }

        sentence = lines.get(0);

        image = new Image(getWidth() * SIZE, getHeight() * SIZE + (TILESHEET.getImage().getHeight() - SIZE));
        graphics = image.getGraphics();
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public boolean isComplete() {
        if (player.isHolding()) {
            return false;
        }

        Integer value = getTile(0, 0).getHeight();

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (getTile(i, j) != null && getTile(i, j).getHeight() != value) {
                    return false;
                }
            }
        }

        return true;
    }

    public void update() {
        if (isAnimating()) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastAnimationTime > 75) {
                for (int i = animation; i >= 0; i--) {
                    Tile depth = getTile(i, animation - i);
                    if (depth != null) {
                        depth.increaseHeight(false);
                    }
                }

                for (int i = animation + 1; i >= 0; i--) {
                    Tile depth = getTile(i, animation - 1 - i);
                    if (depth != null) {
                        depth.increaseHeight(false);
                    }
                }

                for (int i = animation + 2; i >= 0; i--) {
                    Tile depth = getTile(i, animation - 2 - i);
                    if (depth != null) {
                        depth.decreaseHeight(false);
                    }
                }

                for (int i = animation + 3; i >= 0; i--) {
                    Tile depth = getTile(i, animation - 3 - i);
                    if (depth != null) {
                        depth.decreaseHeight(false);
                    }
                }

                animation++;
                lastAnimationTime = currentTime;
            }
        } else {
            player.update();

            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    Tile tile = getTile(i, j);
                    if (tile != null && tile.isFlooded()) {
                        if (getTile(i - 1, j) != null && getTile(i - 1, j).getHeight() < tile.getHeight()) {
                            getTile(i - 1, j).increaseWaterHeight();
                        }

                        if (getTile(i + 1, j) != null && getTile(i + 1, j).getHeight() < tile.getHeight()) {
                            getTile(i + 1, j).increaseWaterHeight();
                        }

                        if (getTile(i, j - 1) != null && getTile(i, j - 1).getHeight() < tile.getHeight()) {
                            getTile(i, j - 1).increaseWaterHeight();
                        }

                        if (getTile(i, j + 1) != null && getTile(i, j + 1).getHeight() < tile.getHeight()) {
                            getTile(i, j + 1).increaseWaterHeight();
                        }
                    }
                }
            }
        }
    }

    public void draw(Graphics g, float x, float y) {
        graphics.clear();

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                Tile tile = getTile(i, j);
                if (tile != null) {
                    tile.draw(graphics);

                    if (player.isAtPosition(i, j)) {
                        player.draw(graphics);
                    } else if (player.isLookingAtPosition(i, j)) {
                        player.drawCursor(graphics);
                    }
                }
            }
        }

        g.drawImage(image, x, y);
    }

    public void draw(Graphics g) {
        draw(g, EngineConfig.WIDTH / 2 - image.getCenterOfRotationX(),
                EngineConfig.HEIGHT / 2 - image.getCenterOfRotationY() + SIZE);
    }

    public Tile getTile(Point position) {
        if (position == null) {
            return null;
        }

        return getTile(position.getX(), position.getY());
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return null;
        }

        return tiles[x][y];
    }

    public boolean increaseDepth(HoldingTile holdingTile, int x, int y, int targetX, int targetY) {
        Tile target = getTile(targetX, targetY);
        if (target != null) {
            if (target.getHeight() - tiles[x][y].getHeight() <= 0) {
                switch (holdingTile) {
                case DIRT:
                    target.increaseHeight(true);
                    break;
                case WATER:
                    target.increaseWaterHeight();
                    break;
                }

                return true;
            }
        }

        return false;
    }

    public HoldingTile decreaseDepth(int x, int y, int targetX, int targetY) {
        Tile target = getTile(targetX, targetY);
        if (target != null) {
            if (target.getHeight() - tiles[x][y].getHeight() == 1) {
                return target.decreaseHeight(true);
            }
        }

        return null;
    }

    public boolean isAnimating() {
        return animation >= 0 && !isAnimationEnded();
    }

    public void launchAnimation() {
        animation = 0;
        lastAnimationTime = System.currentTimeMillis();
    }

    public boolean isAnimationEnded() {
        return animation >= getWidth() * 2 + 4;
    }

    public DeathType isDead() {
        if (player.isDrowned()) {
            return DeathType.DROWNED;
        } else if (player.isBlocked()) {
            return DeathType.BLOCKED;
        }

        return null;
    }

    public String getSentence() {
        return sentence;
    }
}
