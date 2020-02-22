package com.guillot.game;

import static com.guillot.game.Direction.BOTTOM;
import static com.guillot.game.Direction.LEFT;
import static com.guillot.game.Direction.RIGHT;
import static com.guillot.game.Direction.TOP;
import static org.newdawn.slick.Input.KEY_D;
import static org.newdawn.slick.Input.KEY_E;
import static org.newdawn.slick.Input.KEY_Q;
import static org.newdawn.slick.Input.KEY_S;
import static org.newdawn.slick.Input.KEY_Z;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.guillot.engine.gui.Controller;
import com.guillot.engine.gui.GUI;


public class Player implements Entity {

    private int x;

    private int y;

    private Image image;

    private Map map;

    private Direction direction;

    private boolean holding;

    public Player(Map map, int x, int y) throws SlickException {
        this.map = map;
        this.x = x;
        this.y = y;
        this.holding = false;
        this.direction = BOTTOM;

        this.image = new Image("sprites/player.png");
    }

    @Override
    public void update() {
        if (GUI.get().isKeyPressed(KEY_Q) || Controller.get().isLeftPressed()) {
            if (direction != LEFT) {
                direction = LEFT;
            } else if (map.canWalkLeft(x, y)) {
                x--;
            }
        }

        if (GUI.get().isKeyPressed(KEY_D) || Controller.get().isRightPressed()) {
            if (direction != RIGHT) {
                direction = RIGHT;
            } else if (map.canWalkRight(x, y)) {
                x++;
            }
        }

        if (GUI.get().isKeyPressed(KEY_Z) || Controller.get().isUpPressed()) {
            if (direction != TOP) {
                direction = TOP;
            } else if (map.canWalkTop(x, y)) {
                y--;
            }
        }

        if (GUI.get().isKeyPressed(KEY_S) || Controller.get().isDownPressed()) {
            if (direction != BOTTOM) {
                direction = BOTTOM;
            } else if (map.canWalkBottom(x, y)) {
                y++;
            }
        }

        if (GUI.get().isKeyPressed(KEY_E) || Controller.get().isButtonPressed()) {
            int directionX = x;
            int directionY = y;

            switch (direction) {
            case BOTTOM:
                directionY++;
                break;
            case LEFT:
                directionX--;
                break;
            case RIGHT:
                directionX++;
                break;
            case TOP:
                directionY--;
                break;
            }

            if (holding && map.increaseDepth(x, y, directionX, directionY)) {
                holding = !holding;
            } else if (!holding && map.decreaseDepth(x, y, directionX, directionY)) {
                holding = !holding;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        Integer depth = map.getTile(x, y);
        g.drawImage(image, x * image.getWidth(), y * image.getHeight() + (5 - depth) * 8);
    }

    public void drawCursor(Graphics g) {
        int directionX = x;
        int directionY = y;

        switch (direction) {
        case BOTTOM:
            directionY++;
            break;
        case LEFT:
            directionX--;
            break;
        case RIGHT:
            directionX++;
            break;
        case TOP:
            directionY--;
            break;
        }

        Integer depth = map.getTile(directionX, directionY);
        if (depth != null) {

            g.setColor(Color.yellow);
            g.drawRect(directionX * image.getWidth(), directionY * image.getHeight() + (5 - depth) * 8,
                    image.getWidth(), image.getHeight() - 1);
        }
    }

    public boolean isAtPosition(int x, int y) {
        return this.x == x && this.y == y;
    }
}
