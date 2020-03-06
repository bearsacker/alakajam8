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
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;
import static org.newdawn.slick.Input.MOUSE_RIGHT_BUTTON;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.guillot.engine.gui.GUI;
import com.guillot.game.ai.AStar;
import com.guillot.game.ai.Path;


public class Player {

    private Point position;

    private Point cursorPosition;

    private Direction direction;

    private Map map;

    private HoldingTile holdingTile;

    private long lastStep;

    private long lastAnimation;

    private int animation;

    private AStar astar;

    private Path path;

    private int currentStep;

    public Player(Map map) throws SlickException {
        this.map = map;
        this.astar = new AStar(map, 100);
        this.position = new Point();
        this.cursorPosition = new Point();
        this.direction = DOWN;
        this.holdingTile = null;
        this.animation = 0;
        this.lastStep = System.currentTimeMillis();
        this.lastAnimation = System.currentTimeMillis();
    }

    public void update() {
        long time = System.currentTimeMillis();
        if (time - lastStep > 100) {
            if (path != null) {
                Point newPosition = path.getStep(currentStep);
                currentStep++;

                if (position.getX() - newPosition.getX() == 1) {
                    direction = LEFT;
                } else if (position.getX() - newPosition.getX() == -1) {
                    direction = RIGHT;
                } else if (position.getY() - newPosition.getY() == 1) {
                    direction = UP;
                } else if (position.getY() - newPosition.getY() == -1) {
                    direction = DOWN;
                }
                position = newPosition;

                if (currentStep >= path.getLength()) {
                    path = null;
                }
            }

            lastStep = time;
        }

        if (time - lastAnimation > 250) {
            animation++;
            animation %= 2;
            lastAnimation = time;
        }

        int mouseX = GUI.get().getMouseX() - map.getPosition().getX();
        int mouseY = GUI.get().getMouseY() - map.getPosition().getY();

        cursorPosition = map.getPositionIntoImage(mouseX, mouseY);
        if (cursorPosition != null) {
            if (GUI.get().isMouseButtonReleased(MOUSE_LEFT_BUTTON)) {
                path = astar.findPath(position, cursorPosition, false);
                currentStep = 0;
            }

            if (GUI.get().isMouseButtonReleased(MOUSE_RIGHT_BUTTON) && position.distanceFrom(cursorPosition) == 1f) {
                if (isHolding()) {
                    if (map.increaseDepth(holdingTile, position.getX(), position.getY(), cursorPosition)) {
                        holdingTile = null;
                        Sounds.DROP.getSound().play();
                    }
                } else {
                    holdingTile = map.decreaseDepth(position.getX(), position.getY(), cursorPosition);
                    if (isHolding()) {
                        Sounds.TAKE.getSound().play();
                    }
                }
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
            int y = cursorPosition.getY() * 13 - cursorPosition.getX() * 13 + (HEIGHT_MAX - tile.getHeight()) * STEP_HEIGHT + offsetY;

            int w = 0;
            if (position.distanceFrom(cursorPosition) > 0 && position.distanceFrom(cursorPosition) <= 1) {
                w = 1;

                if ((!isHolding() && tile.getHeight() - getHeight() != 1) || (isHolding() && tile.getHeight() - getHeight() > 0)) {
                    w = 0;
                }
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
