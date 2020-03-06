package com.guillot.game;

import static com.guillot.game.Images.FLOWERS;
import static com.guillot.game.Tile.HEIGHT_MAX;
import static com.guillot.game.Tile.SIZE;
import static com.guillot.game.Tile.STEP_HEIGHT;

import org.newdawn.slick.Graphics;

import com.guillot.engine.utils.NumberGenerator;

public class Flower {

    private Point position;

    private int frame;

    public Flower(Point position) {
        this.position = position;
        this.frame = NumberGenerator.get().randomInt(FLOWERS.getImage().getWidth() / SIZE);
    }

    public void draw(Graphics g, int height, int offsetY) {
        int x = position.getX() * 16 + position.getY() * 16;
        int y = position.getY() * 13 - position.getX() * 13 + (HEIGHT_MAX - height) * STEP_HEIGHT + offsetY;

        g.drawImage(FLOWERS.getImage(), x, y, x + SIZE, y + SIZE, frame * SIZE, 0, (frame + 1) * SIZE, SIZE);
    }

}
