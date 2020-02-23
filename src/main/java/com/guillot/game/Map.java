package com.guillot.game;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.FastMath;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.utils.FileLoader;
import com.guillot.engine.utils.NumberGenerator;

public class Map implements Entity {

    private final static int TILE_SIZE = 32;

    private Image tileSheet;

    private Image water;

    private Image flowers;

    private Integer[][] tiles;

    private Player player;

    private String sentence;

    private Image image;

    private Graphics graphics;

    private int animation;

    private long lastAnimationTime;

    private int animationDepth;

    private List<Point> flowersPositions;

    public Map(String path) throws Exception {
        List<String> lines = new BufferedReader(new InputStreamReader(FileLoader.streamFromResource(path), UTF_8)).lines()
                .collect(Collectors.toList());

        for (int i = 0; i < lines.size() - 1; i++) {
            String[] depths = lines.get(i + 1).split(";");

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

        sentence = lines.get(0);

        tileSheet = new Image("sprites/tilesheet.png");
        water = new Image("sprites/water.png");
        flowers = new Image("sprites/flowers.png");
        player = new Player(this, 0, 0);
        animation = -1;

        image = new Image(getWidth() * TILE_SIZE, getHeight() * TILE_SIZE + (tileSheet.getHeight() - TILE_SIZE));
        graphics = image.getGraphics();

        flowersPositions = new ArrayList<>();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (NumberGenerator.get().randomDouble() > .8f) {
                    flowersPositions.add(new Point(i, j));
                }
            }
        }
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

        Integer value = FastMath.abs(getTile(0, 0));

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (getTile(i, j) != null && FastMath.abs(getTile(i, j)) != value) {
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

            if (currentTime - lastAnimationTime > 75) {
                for (int i = 0; i < getWidth(); i++) {
                    for (int j = 0; j < getHeight(); j++) {
                        if (getTile(i, j) != null) {
                            if (getTile(i, j) < 0) {
                                setTile(i, j, -animationDepth);
                            } else {
                                setTile(i, j, animationDepth);
                            }
                        }
                    }
                }

                for (int i = animation; i >= 0; i--) {
                    Integer depth = getTile(i, animation - i);
                    if (depth != null) {
                        if (depth < 0) {
                            setTile(i, animation - i, -(animationDepth + 1));
                        } else {
                            setTile(i, animation - i, animationDepth + 1);
                        }
                    }
                }

                for (int i = animation + 1; i >= 0; i--) {
                    Integer depth = getTile(i, animation - 1 - i);
                    if (depth != null) {
                        if (depth < 0) {
                            setTile(i, animation - 1 - i, -(animationDepth + 2));
                        } else {
                            setTile(i, animation - 1 - i, animationDepth + 2);
                        }
                    }
                }

                for (int i = animation + 2; i >= 0; i--) {
                    Integer depth = getTile(i, animation - 2 - i);
                    if (depth != null) {
                        if (depth < 0) {
                            setTile(i, animation - 2 - i, -(animationDepth + 1));
                        } else {
                            setTile(i, animation - 2 - i, animationDepth + 1);
                        }
                    }
                }

                animation++;
                lastAnimationTime = currentTime;
            }
        } else {
            player.update();

            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    Integer depth = getTile(i, j);
                    if (depth != null && depth < 0) {
                        if (getTile(i - 1, j) != null && getTile(i - 1, j) < FastMath.abs(depth)) {
                            setTile(i - 1, j, depth);
                        }

                        if (getTile(i + 1, j) != null && getTile(i + 1, j) < FastMath.abs(depth)) {
                            setTile(i + 1, j, depth);
                        }

                        if (getTile(i, j - 1) != null && getTile(i, j - 1) < FastMath.abs(depth)) {
                            setTile(i, j - 1, depth);
                        }

                        if (getTile(i, j + 1) != null && getTile(i, j + 1) < FastMath.abs(depth)) {
                            setTile(i, j + 1, depth);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        graphics.clear();

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                Integer frame = getTile(i, j);
                if (frame != null) {
                    boolean isWater = frame < 0;
                    if (isWater) {
                        frame = FastMath.abs(frame) - 1;
                    }

                    graphics.drawImage(tileSheet, i * TILE_SIZE, j * TILE_SIZE, (i + 1) * TILE_SIZE,
                            j * TILE_SIZE + tileSheet.getHeight(), frame * TILE_SIZE, 0, (frame + 1) * TILE_SIZE,
                            tileSheet.getHeight());

                    if (isWater) {
                        graphics.drawImage(water, i * TILE_SIZE, (j + 1) * TILE_SIZE - frame * 8);
                    } else if (flowersPositions.contains(new Point(i, j))) {
                        int flowerFrame = (i + j) % 3;

                        graphics.drawImage(flowers, i * TILE_SIZE, j * TILE_SIZE + (5 - frame) * 8, (i + 1) * TILE_SIZE,
                                (j + 1) * TILE_SIZE + (5 - frame) * 8, flowerFrame * TILE_SIZE, 0, (flowerFrame + 1) * TILE_SIZE,
                                TILE_SIZE);
                    }

                    if (player.isAtPosition(i, j)) {
                        player.draw(graphics);
                    } else if (player.isLookingAtPosition(i, j)) {
                        player.drawCursor(graphics);
                    }
                }
            }
        }

        g.drawImage(image, EngineConfig.WIDTH / 2 - image.getCenterOfRotationX(),
                EngineConfig.HEIGHT / 2 - image.getCenterOfRotationY() + 32);
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

    public boolean increaseDepth(int x, int y, int targetX, int targetY) {
        Integer target = getTile(targetX, targetY);
        if (target != null) {
            boolean isWater = target < 0;
            int difference = FastMath.abs(target) - tiles[x][y];

            if (tiles[targetX][targetY] < 5 && difference <= 0) {
                if (isWater) {
                    tiles[targetX][targetY] = FastMath.abs(target);
                } else {
                    tiles[targetX][targetY]++;
                }

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
