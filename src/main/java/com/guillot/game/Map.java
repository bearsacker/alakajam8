package com.guillot.game;

import static com.guillot.game.Images.TILESHEET;
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

    public final static int TILE_SIZE = 32;

    private Tile[][] tiles;

    private Player player;

    private String sentence;

    private Image image;

    private Graphics graphics;

    private int animation;

    private long lastAnimationTime;

    private int animationDepth;

    private boolean playerCanMove;

    public Map() throws SlickException {
        player = new Player(this);
        animation = -1;
        playerCanMove = true;
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
                    if (getTile(i - 1, j) != null && getTile(i - 1, j).getDepth() < getTile(i, j).getDepth() - 1) {
                        setTile(i - 1, j, getTile(i, j).getDepth() - 1);
                    }

                    if (getTile(i + 1, j) != null && getTile(i + 1, j).getDepth() < getTile(i, j).getDepth() - 1) {
                        setTile(i + 1, j, getTile(i, j).getDepth() - 1);
                    }

                    if (getTile(i, j - 1) != null && getTile(i, j - 1).getDepth() < getTile(i, j).getDepth() - 1) {
                        setTile(i, j - 1, getTile(i, j).getDepth() - 1);
                    }

                    if (getTile(i, j + 1) != null && getTile(i, j + 1).getDepth() < getTile(i, j).getDepth() - 1) {
                        setTile(i, j + 1, getTile(i, j).getDepth() - 1);
                    }
                }
            }
        }

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (tiles[i][j].getDepth() == 0) {
                    tiles[i][j].setDepth(1);
                    tiles[i][j].setFlooded(true);
                }
            }
        }

        image = new Image(width * TILE_SIZE, height * TILE_SIZE + (TILESHEET.getImage().getHeight() - TILE_SIZE));
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
                    boolean flooded = ((value % 100) / 10) == 1;
                    int depth = value % 10;
                    tiles[j][i] = new Tile(j, i, Weather.findByValue(weather), depth, flooded);
                }
            }
        }

        sentence = lines.get(0);

        image = new Image(getWidth() * TILE_SIZE, getHeight() * TILE_SIZE + (TILESHEET.getImage().getHeight() - TILE_SIZE));
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

        Integer value = getTile(0, 0).getDepth();

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (getTile(i, j) != null && getTile(i, j).getDepth() != value) {
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
                    for (int j = 0; j < getHeight(); j++) {
                        if (getTile(i, j) != null) {
                            setTile(i, j, animationDepth);
                        }
                    }
                }

                for (int i = animation; i >= 0; i--) {
                    Tile depth = getTile(i, animation - i);
                    if (depth != null) {
                        setTile(i, animation - i, animationDepth + 1);
                    }
                }

                for (int i = animation + 1; i >= 0; i--) {
                    Tile depth = getTile(i, animation - 1 - i);
                    if (depth != null) {
                        setTile(i, animation - 1 - i, animationDepth + 2);
                    }
                }

                for (int i = animation + 2; i >= 0; i--) {
                    Tile depth = getTile(i, animation - 2 - i);
                    if (depth != null) {
                        setTile(i, animation - 2 - i, animationDepth + 1);
                    }
                }

                animation++;
                lastAnimationTime = currentTime;
            }
        } else {
            if (playerCanMove) {
                player.update();
            }

            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    Tile tile = getTile(i, j);
                    if (tile != null && tile.isFlooded()) {
                        if (getTile(i - 1, j) != null && getTile(i - 1, j).getDepth() < tile.getDepth()) {
                            getTile(i - 1, j).setDepth(tile.getDepth());
                            getTile(i - 1, j).setFlooded(true);
                        }

                        if (getTile(i + 1, j) != null && getTile(i + 1, j).getDepth() < tile.getDepth()) {
                            getTile(i + 1, j).setDepth(tile.getDepth());
                            getTile(i + 1, j).setFlooded(true);
                        }

                        if (getTile(i, j - 1) != null && getTile(i, j - 1).getDepth() < tile.getDepth()) {
                            getTile(i, j - 1).setDepth(tile.getDepth());
                            getTile(i, j - 1).setFlooded(true);
                        }

                        if (getTile(i, j + 1) != null && getTile(i, j + 1).getDepth() < tile.getDepth()) {
                            getTile(i, j + 1).setDepth(tile.getDepth());
                            getTile(i, j + 1).setFlooded(true);
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
                    } else if (player.isLookingAtPosition(i, j) && playerCanMove) {
                        player.drawCursor(graphics);
                    }
                }
            }
        }

        g.drawImage(image, x, y);
    }

    public void draw(Graphics g) {
        draw(g, EngineConfig.WIDTH / 2 - image.getCenterOfRotationX(),
                EngineConfig.HEIGHT / 2 - image.getCenterOfRotationY() + TILE_SIZE);
    }

    public void setTile(int x, int y, int depth) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return;
        }

        tiles[x][y].setDepth(depth);
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

    public boolean increaseDepth(int x, int y, int targetX, int targetY) {
        Tile target = getTile(targetX, targetY);
        if (target != null) {
            int difference = target.getDepth() - tiles[x][y].getDepth();

            if (difference <= 0) {
                if (tiles[targetX][targetY].isFlooded()) {
                    tiles[targetX][targetY].setFlooded(false);
                } else {
                    tiles[targetX][targetY].increaseDepth();
                }

                return true;
            }
        }

        return false;
    }

    public boolean decreaseDepth(int x, int y, int targetX, int targetY) {
        Tile target = getTile(targetX, targetY);
        if (target != null) {
            int difference = target.getDepth() - tiles[x][y].getDepth();

            if (difference == 1) {
                tiles[targetX][targetY].decreaseDepth();
                return true;
            }
        }

        return false;
    }

    public boolean isAnimating() {
        return animation >= 0 && !isAnimationEnded();
    }

    public void launchAnimation() {
        animation = 0;
        lastAnimationTime = System.currentTimeMillis();
        animationDepth = getTile(0, 0).getDepth();
    }

    public boolean isAnimationEnded() {
        return animation >= getWidth() * 3;
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

    public void setPlayerPosition(int x, int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            player.setX(x);
            player.setY(y);
        }
    }

    public void setPlayerCanMove(boolean playerCanMove) {
        this.playerCanMove = playerCanMove;
    }
}
