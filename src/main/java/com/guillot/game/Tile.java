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

    private int height;

    private int waterHeight;

    private Weather weather;

    private Flower flower;

    public Tile(int x, int y, Weather weather, int height, int waterHeight) {
        this.position = new Point(x, y);
        this.weather = weather != null ? weather : SUNNY;
        this.height = height;
        this.waterHeight = waterHeight;

        if (NumberGenerator.get().randomDouble() > .8f) {
            this.flower = new Flower(position);
        }
    }

    public Tile(int x, int y, int depth) {
        this(x, y, SUNNY, depth, 0);
    }

    public void draw(Graphics g) {
        int x = position.getX() * TILE_SIZE;
        int y = position.getY() * TILE_SIZE;
        g.drawImage(TILESHEET.getImage(), x, y, x + TILE_SIZE, y + TILESHEET.getImage().getHeight(), height * TILE_SIZE, 0,
                (height + 1) * TILE_SIZE, TILESHEET.getImage().getHeight());

        if (isFlooded()) {
            for (int i = 0; i < waterHeight; i++) {
                g.drawImage(WATER.getImage(), x, y + TILE_SIZE - (height + i) * 8);
            }
        } else if (flower != null) {
            flower.draw(g, height);
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isFlooded() {
        return waterHeight > 0;
    }

    public int getWaterHeight() {
        return waterHeight;
    }

    public int getHeight() {
        return height + getWaterHeight();
    }

    public void setWaterHeight(int waterHeight) {
        this.waterHeight = waterHeight;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public void increaseHeight(boolean replaceWater) {
        if (replaceWater && isFlooded()) {
            waterHeight--;
        }

        if (height < DEPTH_MAX) {
            height++;
        }
    }

    public void decreaseHeight(boolean replaceWater) {
        if (replaceWater && isFlooded()) {
            waterHeight--;
        } else if (height > 0) {
            height--;
        }
    }

}
