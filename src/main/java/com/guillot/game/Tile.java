package com.guillot.game;


public class Tile {

    private final static int DEPTH_MAX = 5;

    private int depth;

    private Weather weather;

    private boolean flooded;

    public Tile(Weather weather, int depth, boolean flooded) {
        this.weather = weather != null ? weather : Weather.NORMAL;
        this.depth = depth;
        this.flooded = flooded;
    }

    public Tile(int depth) {
        this(Weather.NORMAL, depth, false);
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
