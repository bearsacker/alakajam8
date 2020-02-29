package com.guillot.game;

import static com.guillot.game.Images.TILESHEET;
import static com.guillot.game.Images.WATER;
import static com.guillot.game.Weather.SUNNY;

import org.newdawn.slick.Graphics;

import com.guillot.engine.utils.NumberGenerator;

public class Tile {

    public final static int SIZE = 32;

    public final static int STEP_HEIGHT = 8;

    public final static int HEIGHT_MAX = 5;

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
        int x = position.getX() * SIZE;
        int y = position.getY() * SIZE;
        int w = weather.getValue() * (HEIGHT_MAX + 1) * SIZE;

        g.drawImage(TILESHEET.getImage(), x, y, x + SIZE, y + TILESHEET.getImage().getHeight(), height * SIZE + w, 0,
                (height + 1) * SIZE + w, TILESHEET.getImage().getHeight());

        if (SUNNY.equals(weather) && flower != null) {
            flower.draw(g, height);
        }

        w = weather.getValue() * SIZE;
        for (int i = 0; i < waterHeight; i++) {
            int y2 = y + SIZE - (height + i) * STEP_HEIGHT;
            g.drawImage(WATER.getImage(), x, y2, x + SIZE, y2 + SIZE + STEP_HEIGHT, w, 0, w + SIZE, SIZE + STEP_HEIGHT);
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isFlooded() {
        return SUNNY.equals(weather) && waterHeight > 0;
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
        if (replaceWater && waterHeight > 0) {
            waterHeight--;
        }

        if (height < HEIGHT_MAX) {
            height++;
        }
    }

    public void increaseWaterHeight() {
        waterHeight++;
    }

    public HoldingTile decreaseHeight(boolean replaceWater) {
        if (replaceWater && waterHeight > 0) {
            waterHeight--;
            return HoldingTile.WATER;
        } else if (height > 0) {
            height--;
            return HoldingTile.DIRT;
        }

        return null;
    }

}
