package com.guillot.game;


public enum Direction {
    LEFT(1), RIGHT(3), TOP(2), BOTTOM(0);

    private int frame;

    private Direction(int frame) {
        this.frame = frame;
    }

    public int getFrame() {
        return frame;
    }
}
