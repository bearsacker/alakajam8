package com.guillot.game;

import org.newdawn.slick.Color;

public enum Colors {
    YELLOW(new Color(1f, .9f, .2f)), OVERLAY(new Color(0f, 0f, 0f, .7f)), BACKGROUND(new Color(.4f, .6f, 1f)), SENTENCE(
            new Color(1f, 1f, 1f));

    private Color color;

    private Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
