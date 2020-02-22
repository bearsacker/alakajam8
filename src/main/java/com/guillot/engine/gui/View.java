package com.guillot.engine.gui;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class View {

    public final static Color DEFAULT_BACKGROUND_COLOR = Color.black;

    protected Color backgroundColor;

    protected boolean focused;

    private LinkedList<Component> components;

    public View() {
        this.components = new LinkedList<Component>();
        this.backgroundColor = DEFAULT_BACKGROUND_COLOR;
        this.focused = true;
    }

    public abstract void start() throws Exception;

    public void stop() throws Exception {}

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = new Color(backgroundColor);
    }

    public void add(Component... components) {
        for (Component component : components) {
            this.components.add(component);
        }
    }

    public void update() throws Exception {
        for (Component c : components) {
            if ((c.isVisible() && focused) || (!focused && c instanceof SubView && c.isVisible())) {
                c.update();
            }
        }
    }

    public void paintComponents(Graphics g) throws Exception {
        g.setBackground(backgroundColor);

        for (Component c : components) {
            if (c.isVisible()) {
                c.paint(g);
            }
        }
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}
