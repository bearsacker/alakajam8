package com.guillot.game;

import static com.guillot.game.Direction.DOWN;
import static com.guillot.game.Direction.LEFT;
import static com.guillot.game.Direction.RIGHT;
import static com.guillot.game.Direction.UP;
import static com.guillot.game.Images.CURSOR;
import static com.guillot.game.Images.PLAYER;
import static com.guillot.game.Tile.HEIGHT_MAX;
import static com.guillot.game.Tile.SIZE;
import static com.guillot.game.Tile.STEP_HEIGHT;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.guillot.engine.gui.Controller;
import com.guillot.engine.gui.GUI;


public class Player {

    private Point position;

    private Point cursorPosition;

    private Direction direction;

    private Map map;

    private HoldingTile holdingTile;

    private long lastAnimation;

    private int animation;

    public Player(Map map) throws SlickException {
        this.map = map;
        this.position = new Point();
        this.cursorPosition = new Point();
        this.direction = DOWN;
        this.holdingTile = null;
        this.animation = 0;
        this.lastAnimation = System.currentTimeMillis();
    }

    public void update() {
        long time = System.currentTimeMillis();
        if (time - lastAnimation > 250) {
            animation++;
            animation %= 2;
            lastAnimation = time;
        }

        cursorPosition = new Point(position);
        switch (direction) {
        case DOWN:
            cursorPosition.incrementY();
            break;
        case LEFT:
            cursorPosition.decrementX();
            break;
        case RIGHT:
            cursorPosition.incrementX();
            break;
        case UP:
            cursorPosition.decrementY();
            break;
        }

        if (GUI.get().isKeyPressed(Input.KEY_LEFT) || GUI.get().isKeyPressed(Input.KEY_A) || Controller.get().isLeftPressed()) {
            if (direction != LEFT) {
                direction = LEFT;
            } else if (canWalkLeft()) {
                position.decrementX();
            }
        }

        if (GUI.get().isKeyPressed(Input.KEY_RIGHT) || GUI.get().isKeyPressed(Input.KEY_D) || Controller.get().isRightPressed()) {
            if (direction != RIGHT) {
                direction = RIGHT;
            } else if (canWalkRight()) {
                position.incrementX();
            }
        }

        if (GUI.get().isKeyPressed(Input.KEY_UP) || GUI.get().isKeyPressed(Input.KEY_W) || Controller.get().isUpPressed()) {
            if (direction != UP) {
                direction = UP;
            } else if (canWalkTop()) {
                position.decrementY();
            }
        }

        if (GUI.get().isKeyPressed(Input.KEY_DOWN) || GUI.get().isKeyPressed(Input.KEY_S) || Controller.get().isDownPressed()) {
            if (direction != DOWN) {
                direction = DOWN;
            } else if (canWalkBottom()) {
                position.incrementY();
            }
        }

        if (GUI.get().isKeyPressed(Input.KEY_E) || GUI.get().isKeyPressed(Input.KEY_SPACE) || Controller.get().isButtonPressed()) {
            if (isHolding()) {
                if (map.increaseDepth(holdingTile, position.getX(), position.getY(), cursorPosition)) {
                    holdingTile = null;
                    Sounds.DROP.getSound().play();
                }
            } else {
                holdingTile = map.decreaseDepth(position.getX(), position.getY(), cursorPosition);
            }
        }
    }

    public void draw(Graphics g, int offsetY) {
        int frame = direction.getFrame() * 2 + animation;
        if (isHolding()) {
            frame += (holdingTile.getValue() + 1) * 8;
        }

        int x = position.getX() * 16 + position.getY() * 16;
        int y = position.getY() * 13 - position.getX() * 13 + (HEIGHT_MAX - getHeight()) * STEP_HEIGHT - STEP_HEIGHT + offsetY;

        g.drawImage(PLAYER.getImage(), x, y, x + SIZE, y + SIZE, frame * SIZE, 0, (frame + 1) * SIZE, SIZE);
    }

    public void drawCursor(Graphics g, int offsetY) {
        Tile tile = map.getTile(cursorPosition);
        if (tile != null) {
            int x = cursorPosition.getX() * 16 + cursorPosition.getY() * 16;
            int y = cursorPosition.getY() * 13 - cursorPosition.getX() * 13
                    + (HEIGHT_MAX - tile.getHeight() + tile.getWaterHeight()) * STEP_HEIGHT + offsetY;

            int w = 1;
            if ((!isHolding() && tile.getHeight() - getHeight() != 1) || (isHolding() && tile.getHeight() - getHeight() > 0)) {
                w = 0;
            }

            if (isHolding() && w == 1) {
                y -= STEP_HEIGHT;
            }
            g.drawImage(CURSOR.getImage(), x, y, x + 32, y + 32, w * 32, 0, (w + 1) * 32, 32);
        }
    }

    public boolean isAtPosition(int x, int y) {
        return position.is(x, y);
    }

    public boolean isCursorAtPosition(int x, int y) {
        if (cursorPosition == null) {
            return false;
        }

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
        return holdingTile != null;
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

    public DeathType isDead() {
        if (isDrowned()) {
            return DeathType.DROWNED;
        } else if (isBlocked()) {
            return DeathType.BLOCKED;
        }

        return null;
    }
}
