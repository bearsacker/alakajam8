package com.guillot.game;

import static com.guillot.game.Images.TILESHEET;
import static com.guillot.game.Tile.SIZE;
import static com.guillot.game.Weather.SNOW;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.utils.FileLoader;
import com.guillot.engine.utils.NumberGenerator;

public class Map {

    private Tile[][] tiles;

    private String sentence;

    private DepthBufferedImage image;

    private Point position;

    private int animation;

    private long lastAnimationTime;

    private long lastWaterAnimationTime;

    public Map() throws SlickException {
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

        for (int i = 0; i < 32; i++) {
            boolean snowed = NumberGenerator.get().randomDouble() > .8f;
            int x = NumberGenerator.get().randomInt(getWidth());
            int y = NumberGenerator.get().randomInt(getWidth());
            int depth = NumberGenerator.get().randomInt(2, 5);

            getTile(x, y).setHeight(depth);
            if (snowed) {
                getTile(x, y).setWeather(SNOW);
            }
            double type = NumberGenerator.get().randomDouble();

            if (type < .25f) {
                if (getTile(x + 1, y) != null) {
                    getTile(x + 1, y).setHeight(depth);
                    if (snowed) {
                        getTile(x + 1, y).setWeather(SNOW);
                    }
                }
                if (getTile(x + 1, y - 1) != null) {
                    getTile(x + 1, y - 1).setHeight(depth);
                    if (snowed) {
                        getTile(x + 1, y - 1).setWeather(SNOW);
                    }
                }
                if (getTile(x, y - 1) != null) {
                    getTile(x, y - 1).setHeight(depth);
                    if (snowed) {
                        getTile(x, y - 1).setWeather(SNOW);
                    }
                }
            } else if (type < .4f && getTile(x + 1, y) != null) {
                getTile(x + 1, y).setHeight(depth);
                if (snowed) {
                    getTile(x + 1, y).setWeather(SNOW);
                }
            } else if (type < .55f && getTile(x, y + 1) != null) {
                getTile(x, y + 1).setHeight(depth);
                if (snowed) {
                    getTile(x, y + 1).setWeather(SNOW);
                }
            }
        }

        for (int k = 0; k < 5; k++) {
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    boolean snowed = SNOW.equals(getTile(i, j).getWeather());

                    if (getTile(i - 1, j) != null && getTile(i - 1, j).getHeight() <= getTile(i, j).getHeight() - 1) {
                        getTile(i - 1, j).setHeight(getTile(i, j).getHeight() - 1);

                        if (snowed) {
                            getTile(i - 1, j).setWeather(SNOW);
                        }
                    }

                    if (getTile(i + 1, j) != null && getTile(i + 1, j).getHeight() <= getTile(i, j).getHeight() - 1) {
                        getTile(i + 1, j).setHeight(getTile(i, j).getHeight() - 1);

                        if (snowed) {
                            getTile(i + 1, j).setWeather(SNOW);
                        }
                    }

                    if (getTile(i, j - 1) != null && getTile(i, j - 1).getHeight() <= getTile(i, j).getHeight() - 1) {
                        getTile(i, j - 1).setHeight(getTile(i, j).getHeight() - 1);

                        if (snowed) {
                            getTile(i, j - 1).setWeather(SNOW);
                        }
                    }

                    if (getTile(i, j + 1) != null && getTile(i, j + 1).getHeight() <= getTile(i, j).getHeight() - 1) {
                        getTile(i, j + 1).setHeight(getTile(i, j).getHeight() - 1);

                        if (snowed) {
                            getTile(i, j + 1).setWeather(SNOW);
                        }
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

        int imageWidth = (getWidth() + getHeight()) * SIZE / 2;
        int imageHeight = (getWidth() + getHeight()) * TILESHEET.getImage().getHeight() / 2;
        image = new DepthBufferedImage(imageWidth, imageHeight);
        position = new Point((int) (EngineConfig.WIDTH / 2 - image.getCenterOfRotationX()),
                (int) (EngineConfig.HEIGHT / 2 - image.getCenterOfRotationY()));
    }

    public Map(String path) throws SlickException {
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

        int imageWidth = (getWidth() + getHeight()) * SIZE / 2;
        int imageHeight = (getWidth() + getHeight()) * TILESHEET.getImage().getHeight() / 2;
        image = new DepthBufferedImage(imageWidth, imageHeight);
        position = new Point((int) (EngineConfig.WIDTH / 2 - image.getCenterOfRotationX()),
                (int) (EngineConfig.HEIGHT / 2 - image.getCenterOfRotationY()));
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public boolean isComplete() {
        if (isAnimating() || isAnimationEnded()) {
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
                for (int i = 0; i < getWidth(); i++) {
                    Tile depth = getTile(i, animation);
                    if (depth != null) {
                        depth.increaseHeight(false);
                    }
                }

                for (int i = 0; i < getWidth(); i++) {
                    Tile depth = getTile(i, animation - 1);
                    if (depth != null) {
                        depth.increaseHeight(false);
                    }
                }

                for (int i = 0; i < getWidth(); i++) {
                    Tile depth = getTile(i, animation - 4);
                    if (depth != null) {
                        depth.decreaseHeight(false);
                    }
                }

                for (int i = 0; i < getWidth(); i++) {
                    Tile depth = getTile(i, animation - 5);
                    if (depth != null) {
                        depth.decreaseHeight(false);
                    }
                }

                animation++;
                lastAnimationTime = currentTime;
            }
        } else {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastWaterAnimationTime > 75) {
                Set<Point> tileToIncrease = new HashSet<>();

                for (int i = 0; i < getWidth(); i++) {
                    for (int j = 0; j < getHeight(); j++) {
                        Tile tile = getTile(i, j);
                        if (tile != null && tile.isFlooded()) {
                            if (getTile(i - 1, j) != null && getTile(i - 1, j).getHeight() < tile.getHeight()) {
                                tileToIncrease.add(new Point(i - 1, j));
                            }

                            if (getTile(i + 1, j) != null && getTile(i + 1, j).getHeight() < tile.getHeight()) {
                                tileToIncrease.add(new Point(i + 1, j));
                            }

                            if (getTile(i, j - 1) != null && getTile(i, j - 1).getHeight() < tile.getHeight()) {
                                tileToIncrease.add(new Point(i, j - 1));
                            }

                            if (getTile(i, j + 1) != null && getTile(i, j + 1).getHeight() < tile.getHeight()) {
                                tileToIncrease.add(new Point(i, j + 1));
                            }
                        }
                    }
                }

                for (Point point : tileToIncrease) {
                    getTile(point).increaseWaterHeight();
                    if (!Sounds.WATER.getSound().playing()) {
                        Sounds.WATER.getSound().play();
                    }
                }

                lastWaterAnimationTime = currentTime;
            }
        }
    }

    public void draw(Graphics g, Player player) {
        image.clear();

        int offsetY = image.getHeight() / 2 - TILESHEET.getImage().getHeight() / 2;

        for (int i = getWidth(); i >= 0; i--) {
            for (int j = 0; j < getHeight(); j++) {
                Tile tile = getTile(i, j);
                if (tile != null) {
                    tile.draw(image, offsetY);

                    if (player != null) {
                        if (player.isAtPosition(i, j)) {
                            player.draw(image.getGraphics(), offsetY);
                        }

                        if (player.isCursorAtPosition(i, j)) {
                            player.drawCursor(image.getGraphics(), offsetY);
                        }
                    }
                }
            }
        }

        g.drawImage(image.getImage(), position.getX(), position.getY());
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

    public boolean increaseDepth(HoldingTile holdingTile, int x, int y, Point targetPosition) {
        Tile target = getTile(targetPosition);
        if (target != null) {
            if (target.getHeight() - tiles[x][y].getHeight() <= 0) {
                switch (holdingTile) {
                case DIRT:
                    target.increaseHeight(true);
                    break;
                case WATER:
                    if (target.getWaterHeight() == 0 || SNOW.equals(target.getWeather())) {
                        target.increaseWaterHeight();
                    }
                    break;
                }

                return true;
            }
        }

        return false;
    }

    public HoldingTile decreaseDepth(int x, int y, Point targetPosition) {
        Tile target = getTile(targetPosition);
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

    public String getSentence() {
        return sentence;
    }

    public void setPosition(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    public Point getPosition() {
        return position;
    }

    public int getCenterOfRotationX() {
        return (int) image.getCenterOfRotationX();
    }

    public int getCenterOfRotationY() {
        return (int) image.getCenterOfRotationY();
    }

    public Point getPositionIntoImage(int x, int y) {
        return image.getDepth(x, y);
    }
}
