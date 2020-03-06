package com.guillot.game;

import static com.guillot.game.Images.TILESHEET;
import static com.guillot.game.Images.WATER;
import static com.guillot.game.Weather.SNOW;
import static com.guillot.game.Weather.SUNNY;

import com.guillot.engine.utils.NumberGenerator;

public class Tile {

    public final static int SIZE = 32;

    public final static int STEP_HEIGHT = 6;

    public final static int HEIGHT_MAX = 5;

    private Point position;

    private int height;

    private int waterHeight;

    private Weather weather;

    private Flower flower;

    private long lastAnimation;

    private int frame;

    public Tile(int x, int y, Weather weather, int height, int waterHeight) {
        this.position = new Point(x, y);
        this.weather = weather != null ? weather : SUNNY;
        this.height = height;
        this.waterHeight = waterHeight;
        this.lastAnimation = System.currentTimeMillis();

        if (NumberGenerator.get().randomDouble() > .8f) {
            this.flower = new Flower(position);
        }
    }

    public Tile(int x, int y, int depth) {
        this(x, y, SUNNY, depth, 0);
    }

    public void draw(DepthBufferedImage image, int offsetY) {
        long time = System.currentTimeMillis();
        if (time - lastAnimation > 500) {
            frame++;
            frame %= 2;

            lastAnimation = time;
        }

        int x = position.getX() * 16 + position.getY() * 16;
        int y = position.getY() * 13 - position.getX() * 13 + offsetY;
        int w = weather.getValue() * (HEIGHT_MAX + 1) * SIZE;

        image.drawImage(position, TILESHEET.getImage(), x, y, x + SIZE, y + TILESHEET.getImage().getHeight(), height * SIZE + w, 0,
                (height + 1) * SIZE + w, TILESHEET.getImage().getHeight());

        if (SUNNY.equals(weather) && flower != null) {
            flower.draw(image.getGraphics(), height, offsetY);
        }

        w = ((SNOW.equals(weather) && waterHeight == 1 ? 2 : 0) + frame) * SIZE;
        for (int i = 0; i < waterHeight; i++) {
            int y2 = y + SIZE - (height + i + 1) * STEP_HEIGHT;
            image.drawImage(position, WATER.getImage(), x, y2, x + SIZE, y2 + SIZE, w, 0, w + SIZE, SIZE);
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isFlooded() {
        return (SUNNY.equals(weather) && waterHeight > 0) || waterHeight > 1;
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

    public Point getPosition() {
        return position;
    }

    public boolean isTraversable() {
        return !isFlooded();
    }

    public boolean isTraversable(Tile from) {
        return !isFlooded() && getHeight() - from.getHeight() <= 1;
    }

    public int distanceFrom(Tile tile) {
        return tile != null ? (int) position.distanceFrom(tile.getPosition()) : 0;
    }

}
