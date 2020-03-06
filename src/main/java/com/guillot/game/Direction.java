package com.guillot.game;


public enum Direction {
    LEFT(1), RIGHT(3), UP(2), DOWN(0);

    private int frame;

    private Direction(int frame) {
        this.frame = frame;
    }

    public int getFrame() {
        return frame;
    }
}
