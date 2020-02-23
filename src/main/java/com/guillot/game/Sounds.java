package com.guillot.game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public enum Sounds {
    SUCCESS("sounds/success.wav"), TAKE("sounds/take.wav"), DROP("sounds/drop.wav"), BLOCKED("sounds/blocked.wav"), DROWNED(
            "sounds/drowned.wav"), END("sounds/end.wav"), MUSIC("sounds/music.ogg");

    private Sound sound;

    private Sounds(String path) {
        try {
            sound = new Sound(path);
        } catch (SlickException e) {
        }
    }

    public Sound getSound() {
        return sound;
    }
}
