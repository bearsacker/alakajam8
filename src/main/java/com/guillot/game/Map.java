package com.guillot.game;

import static com.guillot.game.Images.TILESHEET;
import static com.guillot.game.Images.WATER;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.FastMath;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.engine.utils.FileLoader;
import com.guillot.engine.utils.NumberGenerator;

public class Map {

    public final static int TILE_SIZE = 32;

    private Integer[][] tiles;

    private Player player;

    private String sentence;

    private Image image;

    private Graphics graphics;

    private int animation;

    private long lastAnimationTime;

    private int animationDepth;

    private ArrayList<Flower> flowers;

    private boolean playerCanMove;

    public Map() throws SlickException {
        player = new Player(this);
        animation = -1;
        playerCanMove = true;
    }

    public Map(int width, int height) throws SlickException {
        this();

        tiles = new Integer[width][height];
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                tiles[i][j] = 0;
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

        image = new Image(width * TILE_SIZE, height * TILE_SIZE + (TILESHEET.getImage().getHeight() - TILE_SIZE));
        graphics = image.getGraphics();

        flowers = new ArrayList<>();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (NumberGenerator.get().randomDouble() > .8f) {
                    flowers.add(new Flower(i, j));
                }
            }
        }
    }

    public Map(String path) throws Exception {
        this();

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

        image = new Image(getWidth() * TILE_SIZE, getHeight() * TILE_SIZE + (TILESHEET.getImage().getHeight() - TILE_SIZE));
        graphics = image.getGraphics();

        flowers = new ArrayList<>();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (NumberGenerator.get().randomDouble() > .8f) {
                    flowers.add(new Flower(i, j));
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
            if (playerCanMove) {
                player.update();
            }

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

    public void draw(Graphics g, float x, float y) {
        graphics.clear();

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                Integer frame = getTile(i, j);
                if (frame != null) {
                    boolean isWater = frame < 0;
                    if (isWater) {
                        frame = FastMath.abs(frame) - 1;
                    }

                    graphics.drawImage(TILESHEET.getImage(), i * TILE_SIZE, j * TILE_SIZE, (i + 1) * TILE_SIZE,
                            j * TILE_SIZE + TILESHEET.getImage().getHeight(), frame * TILE_SIZE, 0, (frame + 1) * TILE_SIZE,
                            TILESHEET.getImage().getHeight());

                    int tempI = i;
                    int tempJ = j;
                    Optional<Flower> flower = flowers.stream().filter(f -> f.isAtPosition(tempI, tempJ)).findAny();

                    if (isWater) {
                        graphics.drawImage(WATER.getImage(), i * TILE_SIZE, (j + 1) * TILE_SIZE - frame * 8);
                    } else if (flower.isPresent()) {
                        flower.get().draw(graphics, frame);
                    }

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

        tiles[x][y] = depth;
    }

    public Integer getTile(Point position) {
        if (position == null) {
            return null;
        }

        return getTile(position.getX(), position.getY());
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
