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

import org.apache.commons.math3.util.FastMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.guillot.engine.gui.Controller;
import com.guillot.engine.gui.GUI;


public class Player implements Entity {

    private final static Color YELLOW = new Color(1f, .9f, .2f);

    private static final int TILE_SIZE = 32;

    private int x;

    private int y;

    private Image image;

    private Map map;

    private Direction direction;

    private boolean holding;

    private long lastAnimation;

    private int animation;

    public Player(Map map, int x, int y) throws SlickException {
        this.map = map;
        this.x = x;
        this.y = y;
        this.holding = false;
        this.direction = BOTTOM;

        this.image = new Image("sprites/player.png");
        this.animation = 0;
        this.lastAnimation = System.currentTimeMillis();
    }

    @Override
    public void update() {
        long time = System.currentTimeMillis();
        if (time - lastAnimation > 250) {
            animation++;
            animation %= 2;
            lastAnimation = time;
        }

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
                Sounds.DROP.getSound().play();
            } else if (!holding && map.decreaseDepth(x, y, directionX, directionY)) {
                holding = !holding;
                Sounds.TAKE.getSound().play();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        int frame = direction.getFrame() * 2 + animation;
        if (holding) {
            frame += 8;
        }

        Integer depth = FastMath.max(0, map.getTile(x, y));

        g.drawImage(image, x * TILE_SIZE, y * TILE_SIZE + (5 - depth) * 8 - 8, x * TILE_SIZE + TILE_SIZE,
                y * TILE_SIZE + (5 - depth) * 8 - 8 + TILE_SIZE, frame * TILE_SIZE, 0, (frame + 1) * TILE_SIZE, TILE_SIZE);
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
            depth = FastMath.abs(depth);

            g.setColor(YELLOW);
            g.drawRect(directionX * TILE_SIZE, directionY * TILE_SIZE + (5 - depth) * 8,
                    TILE_SIZE - 1, TILE_SIZE - 1);
        }
    }

    public boolean isAtPosition(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean isLookingAtPosition(int x, int y) {
        int directionX = this.x;
        int directionY = this.y;

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

        return directionX == x && directionY == y;
    }

    public boolean isBlocked() {
        return !map.canWalkTop(x, y) && !map.canWalkBottom(x, y) && !map.canWalkLeft(x, y) && !map.canWalkRight(x, y);
    }

    public boolean isDrowned() {
        return getDepth() < 0;
    }

    public Integer getDepth() {
        return map.getTile(x, y);
    }

    public boolean isHolding() {
        return holding;
    }
}
