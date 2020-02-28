package com.guillot.game;

import static com.guillot.game.Colors.YELLOW;
import static com.guillot.game.Direction.BOTTOM;
import static com.guillot.game.Direction.LEFT;
import static com.guillot.game.Direction.RIGHT;
import static com.guillot.game.Direction.TOP;
import static com.guillot.game.Images.PLAYER;
import static com.guillot.game.Tile.HEIGHT_MAX;
import static com.guillot.game.Tile.SIZE;
import static com.guillot.game.Tile.STEP_HEIGHT;
import static org.newdawn.slick.Input.KEY_A;
import static org.newdawn.slick.Input.KEY_D;
import static org.newdawn.slick.Input.KEY_DOWN;
import static org.newdawn.slick.Input.KEY_E;
import static org.newdawn.slick.Input.KEY_LEFT;
import static org.newdawn.slick.Input.KEY_RIGHT;
import static org.newdawn.slick.Input.KEY_S;
import static org.newdawn.slick.Input.KEY_SPACE;
import static org.newdawn.slick.Input.KEY_UP;
import static org.newdawn.slick.Input.KEY_W;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.gui.Controller;
import com.guillot.engine.gui.GUI;


public class Player {

    private Point position;

    private Point cursorPosition;

    private Map map;

    private Direction direction;

    private boolean holding;

    private long lastAnimation;

    private int animation;

    public Player(Map map) throws SlickException {
        this.map = map;
        this.position = new Point();
        this.cursorPosition = new Point();
        this.holding = false;
        this.direction = BOTTOM;
        this.animation = 0;
        this.lastAnimation = System.currentTimeMillis();
    }

    public void update() {
        cursorPosition = new Point(position);
        switch (direction) {
        case BOTTOM:
            cursorPosition.incrementY();
            break;
        case LEFT:
            cursorPosition.decrementX();
            break;
        case RIGHT:
            cursorPosition.incrementX();
            break;
        case TOP:
            cursorPosition.decrementY();
            break;
        }

        long time = System.currentTimeMillis();
        if (time - lastAnimation > 250) {
            animation++;
            animation %= 2;
            lastAnimation = time;
        }

        if (GUI.get().isKeyPressed(KEY_LEFT) || GUI.get().isKeyPressed(KEY_A) || Controller.get().isLeftPressed()) {
            if (direction != LEFT) {
                direction = LEFT;
            } else if (canWalkLeft()) {
                position.decrementX();
            }
        }

        if (GUI.get().isKeyPressed(KEY_RIGHT) || GUI.get().isKeyPressed(KEY_D) || Controller.get().isRightPressed()) {
            if (direction != RIGHT) {
                direction = RIGHT;
            } else if (canWalkRight()) {
                position.incrementX();
            }
        }

        if (GUI.get().isKeyPressed(KEY_UP) || GUI.get().isKeyPressed(KEY_W) || Controller.get().isUpPressed()) {
            if (direction != TOP) {
                direction = TOP;
            } else if (canWalkTop()) {
                position.decrementY();
            }
        }

        if (GUI.get().isKeyPressed(KEY_DOWN) || GUI.get().isKeyPressed(KEY_S) || Controller.get().isDownPressed()) {
            if (direction != BOTTOM) {
                direction = BOTTOM;
            } else if (canWalkBottom()) {
                position.incrementY();
            }
        }

        if (GUI.get().isKeyPressed(KEY_E) || GUI.get().isKeyPressed(KEY_SPACE) || Controller.get().isButtonPressed()) {
            if (holding && map.increaseDepth(position.getX(), position.getY(), cursorPosition.getX(), cursorPosition.getY())) {
                holding = !holding;
                Sounds.DROP.getSound().play();
            } else if (!holding && map.decreaseDepth(position.getX(), position.getY(), cursorPosition.getX(), cursorPosition.getY())) {
                holding = !holding;
                Sounds.TAKE.getSound().play();
            }
        }
    }

    public void draw(Graphics g) {
        int frame = direction.getFrame() * 2 + animation;
        if (holding) {
            frame += 8;
        }

        int x = position.getX() * SIZE;
        int y = position.getY() * SIZE + (HEIGHT_MAX - getHeight()) * STEP_HEIGHT - STEP_HEIGHT;

        g.drawImage(PLAYER.getImage(), x, y, x + SIZE, y + SIZE, frame * SIZE, 0, (frame + 1) * SIZE, SIZE);
    }

    public void drawCursor(Graphics g) {
        Tile tile = map.getTile(cursorPosition);
        if (tile != null) {
            g.setColor(YELLOW.getColor());
            g.drawRect(cursorPosition.getX() * SIZE, cursorPosition.getY() * SIZE + (HEIGHT_MAX - tile.getHeight()) * STEP_HEIGHT,
                    SIZE - 1, SIZE - 1);
        }
    }

    public boolean isAtPosition(int x, int y) {
        return position.is(x, y);
    }

    public boolean isLookingAtPosition(int x, int y) {
        return cursorPosition.is(x, y);
    }

    public boolean isBlocked() {
        return !canWalkTop() && !canWalkBottom() && !canWalkLeft() && !canWalkRight();
    }

    public boolean isDrowned() {
        return map.getTile(position).isFlooded();
    }

    public int getHeight() {
        return map.getTile(position).getHeight();
    }

    public boolean isHolding() {
        return holding;
    }

    public int getX() {
        return position.getX();
    }

    public void setX(int x) {
        position.setX(x);
    }

    public int getY() {
        return position.getY();
    }

    public void setY(int y) {
        position.setY(y);
    }

    public boolean canWalkLeft() {
        Tile destinationTile = map.getTile(position.getX() - 1, position.getY());
        if (destinationTile == null) {
            return false;
        }

        return (destinationTile.getHeight() - getHeight()) <= 1;
    }

    public boolean canWalkRight() {
        Tile destinationTile = map.getTile(position.getX() + 1, position.getY());
        if (destinationTile == null) {
            return false;
        }

        return (destinationTile.getHeight() - getHeight()) <= 1;
    }

    public boolean canWalkTop() {
        Tile destinationTile = map.getTile(position.getX(), position.getY() - 1);
        if (destinationTile == null) {
            return false;
        }

        return (destinationTile.getHeight() - getHeight()) <= 1;
    }

    public boolean canWalkBottom() {
        Tile destinationTile = map.getTile(position.getX(), position.getY() + 1);
        if (destinationTile == null) {
            return false;
        }

        return (destinationTile.getHeight() - getHeight()) <= 1;
    }
}
