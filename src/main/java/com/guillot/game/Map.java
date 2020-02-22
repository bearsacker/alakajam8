package com.guillot.game;

import java.nio.file.Files;
import java.util.List;

import org.apache.commons.math3.util.FastMath;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.utils.FileLoader;

public class Map implements Entity {

    private final static int TILE_SIZE = 32;

    private Image tilesSheet;

    private int[][] tiles;

    private Player player;

    private Image image;

    private Graphics graphics;

    public Map(String path) throws Exception {
        List<String> lines = Files.readAllLines(FileLoader.fileFromResource(path).toPath());
        tiles = new int[lines.size()][];

        for (int i = 0; i < lines.size(); i++) {
            String[] depths = lines.get(i).split(";");
            tiles[i] = new int[depths.length];

            for (int j = 0; j < depths.length; j++) {
                tiles[i][j] = Integer.parseInt(depths[j]);
            }
        }

        image = new Image(getWidth() * TILE_SIZE, (getHeight() + 1) * TILE_SIZE);
        graphics = image.getGraphics();

        tilesSheet = new Image("sprites/tiles.png");
        player = new Player(this, 0, 0);
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public boolean isComplete() {
        int value = tiles[0][0];

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (tiles[i][j] != value) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void update() {
        player.update();
    }

    @Override
    public void draw(Graphics g) {
        graphics.clear();

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                int frame = tiles[j][i];
                graphics.drawImage(tilesSheet, i * TILE_SIZE, j * TILE_SIZE, (i + 1) * TILE_SIZE, (j + 2) * TILE_SIZE, frame * TILE_SIZE, 0,
                        (frame + 1) * TILE_SIZE, TILE_SIZE * 2);

                if (player.isAtPosition(j, i)) {
                    player.draw(graphics);
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

        int difference = FastMath.abs(tiles[x][y] - tiles[x - 1][y]);
        return difference <= 1;
    }

    public boolean canWalkRight(int x, int y) {
        if (x == getWidth() - 1) {
            return false;
        }

        int difference = FastMath.abs(tiles[x][y] - tiles[x + 1][y]);
        return difference <= 1;
    }

    public boolean canWalkTop(int x, int y) {
        if (y == 0) {
            return false;
        }

        int difference = FastMath.abs(tiles[x][y] - tiles[x][y - 1]);
        return difference <= 1;
    }

    public boolean canWalkBottom(int x, int y) {
        if (y == getHeight() - 1) {
            return false;
        }

        int difference = FastMath.abs(tiles[x][y] - tiles[x][y + 1]);
        return difference <= 1;
    }

    public int getTile(int x, int y) {
        return tiles[x][y];
    }

    public boolean increaseDepth(int x, int y, int targetX, int targetY) {
        int difference = tiles[targetX][targetY] - tiles[x][y];

        if (tiles[targetX][targetY] < 4 && difference <= 0 && difference >= -1) {
            tiles[targetX][targetY]++;
            return true;
        }

        return false;
    }

    public boolean decreaseDepth(int x, int y, int targetX, int targetY) {
        int difference = tiles[targetX][targetY] - tiles[x][y];

        if (tiles[targetX][targetY] > 0 && difference == 1) {
            tiles[targetX][targetY]--;
            return true;
        }

        return false;
    }
}
