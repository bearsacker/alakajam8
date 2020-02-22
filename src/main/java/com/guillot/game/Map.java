package com.guillot.game;

import java.nio.file.Files;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.utils.FileLoader;

public class Map implements Entity {

    private final static int TILE_SIZE = 32;

    private Image tileSheet;

    private Integer[][] tiles;

    private Player player;

    private Image image;

    private Graphics graphics;

    private int animation;

    private long lastAnimationTime;

    private int animationDepth;

    public Map(String path) throws Exception {
        List<String> lines = Files.readAllLines(FileLoader.fileFromResource(path).toPath());

        for (int i = 0; i < lines.size(); i++) {
            String[] depths = lines.get(i).split(";");

            if (tiles == null) {
                tiles = new Integer[depths.length][];

                for (int j = 0; j < depths.length; j++) {
                    tiles[j] = new Integer[lines.size()];
                }
            }

            for (int j = 0; j < depths.length; j++) {
                if (!depths[j].equals(".")) {
                    tiles[j][i] = Integer.parseInt(depths[j]);
                }
            }
        }

        tileSheet = new Image("sprites/tilesheet.png");
        player = new Player(this, 0, 0);
        animation = -1;

        image = new Image(getWidth() * TILE_SIZE, getHeight() * TILE_SIZE + (tileSheet.getHeight() - TILE_SIZE));
        graphics = image.getGraphics();
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public boolean isComplete() {
        Integer value = getTile(0, 0);

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (getTile(i, j) != null && getTile(i, j) != value) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void update() {
        if (isAnimating()) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastAnimationTime > 50) {
                for (int i = 0; i < getWidth(); i++) {
                    for (int j = 0; j < getHeight(); j++) {
                        setTile(i, j, animationDepth);
                    }
                }

                for (int i = animation; i >= 0; i--) {
                    setTile(i, animation - i, animationDepth + 1);
                }

                for (int i = animation + 1; i >= 0; i--) {
                    setTile(i, animation - 1 - i, animationDepth + 2);
                }

                for (int i = animation + 2; i >= 0; i--) {
                    setTile(i, animation - 2 - i, animationDepth + 1);
                }

                animation++;
                lastAnimationTime = currentTime;
            }
        } else {
            player.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        graphics.clear();

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                Integer frame = tiles[i][j];
                if (frame != null) {
                    graphics.drawImage(tileSheet, i * TILE_SIZE, j * TILE_SIZE, (i + 1) * TILE_SIZE, j * TILE_SIZE + tileSheet.getHeight(),
                            frame * TILE_SIZE, 0, (frame + 1) * TILE_SIZE, tileSheet.getHeight());

                    if (player.isAtPosition(i, j)) {
                        player.draw(graphics);
                    }
                }
            }
        }

        player.drawCursor(graphics);

        g.drawImage(image, EngineConfig.WIDTH / 2 - image.getCenterOfRotationX(),
                EngineConfig.HEIGHT / 2 - image.getCenterOfRotationY());
    }

    public boolean canWalkLeft(int x, int y) {
        if (x == 0) {
            return false;
        }

        Integer destination = getTile(x - 1, y);
        if (destination == null) {
            return false;
        }

        int difference = destination - tiles[x][y];
        return difference <= 1;
    }

    public boolean canWalkRight(int x, int y) {
        if (x == getWidth() - 1) {
            return false;
        }

        Integer destination = getTile(x + 1, y);
        if (destination == null) {
            return false;
        }

        int difference = destination - tiles[x][y];
        return difference <= 1;
    }

    public boolean canWalkTop(int x, int y) {
        if (y == 0) {
            return false;
        }

        Integer destination = getTile(x, y - 1);
        if (destination == null) {
            return false;
        }

        int difference = destination - tiles[x][y];
        return difference <= 1;
    }

    public boolean canWalkBottom(int x, int y) {
        if (y == getHeight() - 1) {
            return false;
        }

        Integer destination = getTile(x, y + 1);
        if (destination == null) {
            return false;
        }

        int difference = destination - tiles[x][y];
        return difference <= 1;
    }

    public Integer setTile(int x, int y, int depth) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return null;
        }

        return tiles[x][y] = depth;
    }

    public Integer getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return null;
        }

        return tiles[x][y];
    }

    public boolean increaseDepth(int x, int y, int targetX, int targetY) {
        Integer target = getTile(targetX, targetY);
        if (target != null) {
            int difference = target - tiles[x][y];

            if (tiles[targetX][targetY] < 5 && difference <= 0) {
                tiles[targetX][targetY]++;
                return true;
            }
        }

        return false;
    }

    public boolean decreaseDepth(int x, int y, int targetX, int targetY) {
        Integer target = getTile(targetX, targetY);
        if (target != null) {
            int difference = target - tiles[x][y];

            if (tiles[targetX][targetY] > 0 && difference == 1) {
                tiles[targetX][targetY]--;
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
        animationDepth = getTile(0, 0);
    }

    public boolean isAnimationEnded() {
        return animation >= getWidth() * 3;
    }

}
