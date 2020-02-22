package com.guillot.engine.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class Component {

    public final static Color DEFAULT_FILTER_COLOR = new Color(Color.white);

    protected Color filter;

    protected int x;

    protected int y;

    protected int width;

    protected int height;

    protected boolean mouseOn;

    protected boolean visible;

    public Component() {
        this.filter = new Color(DEFAULT_FILTER_COLOR);
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.mouseOn = false;
        this.visible = true;
    }

    public abstract void paint(Graphics g);

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean mouseOn() {
        return this.mouseOn;
    }

    public void update() throws Exception {
        int mX = GUI.get().getMouseX();
        int mY = GUI.get().getMouseY();

        if (mX >= x && mX <= (x + width) && mY >= y && mY <= (y + height)) {
            this.mouseOn = true;
        } else {
            this.mouseOn = false;
        }
    }

    public float getOpacity() {
        return filter.a;
    }

    public void setOpacity(float opacity) {
        filter.a = opacity;
    }
}
