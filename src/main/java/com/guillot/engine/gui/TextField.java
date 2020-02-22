package com.guillot.engine.gui;

import static org.newdawn.slick.Input.KEY_BACK;
import static org.newdawn.slick.Input.KEY_DELETE;
import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;


public class TextField extends Component {

    public final static String DEFAULT_TEXTFIELD_SPRITE = "gui/default_textfield.png";

    public final static Color DEFAULT_TEXT_COLOR = Color.white;

    private Image image;

    private boolean focus;

    private String value;

    private Color textColor;

    private String invalidChars;

    public TextField(int x, int y, int width, int height) throws Exception {
        super();

        this.image = new Image(DEFAULT_TEXTFIELD_SPRITE);
        this.x = x;
        this.y = y;
        this.value = "";
        this.width = width;
        this.height = height;
        this.focus = false;
        this.textColor = new Color(DEFAULT_TEXT_COLOR);
        this.invalidChars = "";
    }

    public TextField(int x, int y, int width, int height, String value) throws Exception {
        this(x, y, width, height);

        this.value = value;
    }

    public void setSprite(String sprite) throws Exception {
        this.image = new Image(sprite);
    }

    @Override
    public void setOpacity(float opacity) {
        super.setOpacity(opacity);
        textColor.a = opacity;
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON)) {
            focus = mouseOn;
        }

        if (focus) {
            if (GUI.get().isKeyPressed(KEY_BACK) && value.length() > 0) {
                value = value.substring(0, value.length() - 1);
            }

            if (GUI.get().isKeyPressed(KEY_DELETE) && value.length() > 0) {
                value = "";
            }

            // char c = GUI.get().getInput().;
            char c = '\0';
            if (c != '\0' && GUI.get().getFont().getWidth(value + "      ") < width && !invalidChars.contains("" + c)) {
                value += c;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (!focus && !mouseOn) {
            image.draw(x, y, x + 2, y + 2, 0, 0, 2, 2, filter);
            image.draw(x, y + 2, x + 2, y + height, 0, 2, 2, 30, filter);
            image.draw(x, y + height - 2, x + 2, y + height, 0, 30, 2, 32, filter);

            image.draw(x + 2, y, x + width - 2, y + 2, 2, 0, 30, 2, filter);
            image.draw(x + 2, y + 2, x + width - 2, y + height - 2, 3, 3, 29, 29, filter);
            image.draw(x + 2, y + height - 2, x + width - 2, y + height, 2, 30, 30, 32, filter);

            image.draw(x + width - 2, y, x + width, y + 2, 30, 0, 32, 2, filter);
            image.draw(x + width - 2, y + 2, x + width, y + height - 2, 30, 2, 32, 30, filter);
            image.draw(x + width - 2, y + height - 2, x + width, y + height, 30, 30, 32, 32, filter);
        } else {
            image.draw(x, y, x + 2, y + 2, 0, 32, 2, 34, filter);
            image.draw(x, y + 2, x + 2, y + height, 0, 34, 2, 62, filter);
            image.draw(x, y + height - 2, x + 2, y + height, 0, 62, 2, 64, filter);

            image.draw(x + 2, y, x + width - 2, y + 2, 2, 32, 30, 34, filter);
            image.draw(x + 2, y + 2, x + width - 2, y + height - 2, 3, 35, 29, 61, filter);
            image.draw(x + 2, y + height - 2, x + width - 2, y + height, 2, 62, 30, 64, filter);

            image.draw(x + width - 2, y, x + width, y + 2, 30, 32, 32, 34, filter);
            image.draw(x + width - 2, y + 2, x + width, y + height - 2, 30, 34, 32, 62, filter);
            image.draw(x + width - 2, y + height - 2, x + width, y + height, 30, 62, 32, 64, filter);
        }

        int lineHeight = GUI.get().getFont().getLineHeight();
        int lineWidth = GUI.get().getFont().getWidth(value);
        GUI.get().getFont().drawString(x + (width / 2) - (lineWidth / 2), y + (height / 2) - (lineHeight / 2), value, textColor);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = new Color(textColor);
    }

    public void clear() {
        this.value = "";
    }

    public String getInvalidChars() {
        return invalidChars;
    }

    public void setInvalidChars(String invalidChars) {
        this.invalidChars = invalidChars;
    }
}
