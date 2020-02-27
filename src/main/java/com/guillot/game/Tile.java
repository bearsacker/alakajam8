package com.guillot.game;

import static com.guillot.game.Images.TILESHEET;
import static com.guillot.game.Images.WATER;
import static com.guillot.game.Map.TILE_SIZE;
import static com.guillot.game.Weather.SUNNY;

import org.newdawn.slick.Graphics;

import com.guillot.engine.utils.NumberGenerator;

public class Tile {

    private final static int DEPTH_MAX = 5;

    private Point position;

    private int depth;

    private Weather weather;

    private boolean flooded;

    private Flower flower;

    public Tile(int x, int y, Weather weather, int depth, boolean flooded) {
        this.position = new Point(x, y);
        this.weather = weather != null ? weather : SUNNY;
        this.depth = depth;
        this.flooded = flooded;

        if (NumberGenerator.get().randomDouble() > .8f) {
            this.flower = new Flower(position);
        }
    }

    public Tile(int x, int y, int depth) {
        this(x, y, SUNNY, depth, false);
    }

    public void draw(Graphics g) {
        int frame = depth;
        if (flooded) {
            frame -= 1;
        }

        int x = position.getX() * TILE_SIZE;
        int y = position.getY() * TILE_SIZE;
        g.drawImage(TILESHEET.getImage(), x, y, x + TILE_SIZE, y + TILESHEET.getImage().getHeight(), frame * TILE_SIZE, 0,
                (frame + 1) * TILE_SIZE, TILESHEET.getImage().getHeight());

        if (flooded) {
            g.drawImage(WATER.getImage(), x, y + TILE_SIZE - frame * 8);
        } else if (flower != null) {
            flower.draw(g, frame);
        }
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isFlooded() {
        return flooded;
    }

    public void setFlooded(boolean flooded) {
        this.flooded = flooded;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public void increaseDepth() {
        if (depth < DEPTH_MAX) {
            depth++;
        }
    }

    public void decreaseDepth() {
        if (depth > 0) {
            depth--;
        }
    }

}
