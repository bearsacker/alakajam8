package com.guillot.game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public enum Images {
    LOGO("sprites/logo.png"), PORTRAIT("sprites/portrait.png"), PLAYER("sprites/player.png"), WATER("sprites/water.png"), ICE(
            "sprites/ice.png"), FLOWERS("sprites/flowers.png"), TILESHEET("sprites/tilesheet.png");

    private Image image;

    private Images(String path) {
        try {
            image = new Image(path);
        } catch (SlickException e) {
        }
    }

    public Image getImage() {
        return image;
    }
}
