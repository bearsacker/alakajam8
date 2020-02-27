package com.guillot.game;

import static com.guillot.game.Images.FLOWERS;
import static com.guillot.game.Map.TILE_SIZE;

import org.newdawn.slick.Graphics;

import com.guillot.engine.utils.NumberGenerator;

public class Flower {

    private Point position;

    private int frame;

    public Flower(int x, int y) {
        position = new Point(x, y);
        frame = NumberGenerator.get().randomInt(4);
    }

    public void draw(Graphics g, int depth) {
        int x = position.getX() * TILE_SIZE;
        int y = position.getY() * TILE_SIZE + (5 - depth) * 8;
        g.drawImage(FLOWERS.getImage(), x, y, x + TILE_SIZE, y + TILE_SIZE, frame * TILE_SIZE, 0, (frame + 1) * TILE_SIZE, TILE_SIZE);
    }

    public boolean isAtPosition(int x, int y) {
        return position.is(x, y);
    }
}
