package com.guillot.game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public enum Images {
    LOGO("sprites/logo.png"), PORTRAIT("sprites/portrait.png"), PLAYER("sprites/player.png"), WATER("sprites/iso-water.png"), FLOWERS(
            "sprites/iso-flowers.png"), TILESHEET("sprites/iso-tilesheet.png"), CURSOR("sprites/cursor.png"), MOUSE("sprites/mouse.png");

    private Image image;

    private Images(String path) {
        try {
            image = new Image(path);
            image.setFilter(Image.FILTER_NEAREST);
        } catch (SlickException e) {
        }
    }

    public Image getImage() {
        return image;
    }
}
