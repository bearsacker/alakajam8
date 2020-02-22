package com.guillot.engine.gui;

import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class CheckBox extends Component {

    public final static String DEFAULT_CHECKBOX_SPRITE = "gui/default_checkbox.png";

    public final static Color DEFAULT_CHECKBOX_DISABLED_COLOR = new Color(0.5f, 0.5f, 0.5f);

    private Image image;

    private boolean enabled;

    private boolean value;

    private int offset;

    public CheckBox(int x, int y, int width, int height, boolean value) throws Exception {
        super();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new Image(DEFAULT_CHECKBOX_SPRITE);
        this.enabled = true;
        this.value = value;
    }

    @Override
    public void paint(Graphics g) {
        offset = mouseOn ? 32 : 0;

        if (!value) {
            image.draw(x, y, x + 2, y + 2, 0 + offset, 0, 2 + offset, 2, filter);
            image.draw(x, y + 2, x + 2, y + height, 0 + offset, 2, 2 + offset, 30, filter);
            image.draw(x, y + height - 2, x + 2, y + height, 0 + offset, 30, 2 + offset, 32, filter);

            image.draw(x + 2, y, x + width - 2, y + 2, 2 + offset, 0, 30 + offset, 2, filter);
            image.draw(x + 2, y + 2, x + width - 2, y + height - 2, 2 + offset, 2, 30 + offset, 30, filter);
            image.draw(x + 2, y + height - 2, x + width - 2, y + height, 2 + offset, 30, 30 + offset, 32, filter);

            image.draw(x + width - 2, y, x + width, y + 2, 30 + offset, 0, 32 + offset, 2, filter);
            image.draw(x + width - 2, y + 2, x + width, y + height - 2, 30 + offset, 2, 32 + offset, 30, filter);
            image.draw(x + width - 2, y + height - 2, x + width, y + height, 30 + offset, 30, 32 + offset, 32, filter);
        } else {
            image.draw(x, y, x + 2, y + 2, 0 + offset, 32, 2 + offset, 34, filter);
            image.draw(x, y + 2, x + 2, y + height, 0 + offset, 34, 2 + offset, 62, filter);
            image.draw(x, y + height - 2, x + 2, y + height, 0 + offset, 62, 2 + offset, 64, filter);

            image.draw(x + 2, y, x + width - 2, y + 2, 2 + offset, 32, 30 + offset, 34, filter);
            image.draw(x + 2, y + 2, x + width - 2, y + height - 2, 2 + offset, 34, 30 + offset, 62, filter);
            image.draw(x + 2, y + height - 2, x + width - 2, y + height, 2 + offset, 62, 30 + offset, 64, filter);

            image.draw(x + width - 2, y, x + width, y + 2, 30 + offset, 32, 32 + offset, 34, filter);
            image.draw(x + width - 2, y + 2, x + width, y + height - 2, 30 + offset, 34, 32 + offset, 62, filter);
            image.draw(x + width - 2, y + height - 2, x + width, y + height, 30 + offset, 62, 32 + offset, 64, filter);
        }
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (enabled && mouseOn && GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
            value = !value;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        float opacity = this.filter.a;
        this.filter = enabled ? new Color(DEFAULT_FILTER_COLOR) : new Color(DEFAULT_CHECKBOX_DISABLED_COLOR);
        this.filter.a = opacity;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
