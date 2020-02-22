package com.guillot.engine.gui;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

public abstract class SubView extends Component {

    private ArrayList<Component> components;

    private View parent;

    public SubView(View parent) {
        this.parent = parent;
        this.components = new ArrayList<Component>();
        this.visible = false;
    }

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

    public void add(Component c) {
        components.add(c);
    }

    @Override
    public void setVisible(boolean visible) {
        try {
            if (!this.visible && visible) {
                start();
                if (parent != null) {
                    parent.setFocused(false);
                }
            } else if (this.visible && !visible) {
                stop();
                if (parent != null) {
                    parent.setFocused(true);
                }
            }

            this.visible = visible;
        } catch (Exception e) {
            GUI.get().switchView(new ViewException(e));
        }
    }

    @Override
    public void update() throws Exception {
        super.update();

        for (Component c : components) {
            c.update();
        }
    }

    @Override
    public void paint(Graphics g) {
        paintCompoments(g);
    }

    protected void paintCompoments(Graphics g) {
        for (Component c : components) {
            if (c.isVisible()) {
                c.paint(g);
            }
        }
    }
}
