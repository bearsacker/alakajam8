package com.guillot.game;


public enum HoldingTile {
    DIRT(0), WATER(1);

    private int value;

    private HoldingTile(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
