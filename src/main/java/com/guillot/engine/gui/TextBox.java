package com.guillot.engine.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;


public class TextBox extends Component {

    public final static String DEFAULT_TEXTBOX_SPRITE = "gui/default_textbox.png";

    public final static Color DEFAULT_TEXT_COLOR = Color.black;

    private Image image;

    private String text;

    private ArrayList<String> lines;

    private Color textColor;

    private boolean drawBox;

    public TextBox(int x, int y, int width, String text) throws Exception {
        super();

        this.image = new Image(DEFAULT_TEXTBOX_SPRITE);
        this.x = x;
        this.y = y;
        this.width = width;
        this.text = text;
        this.textColor = new Color(DEFAULT_TEXT_COLOR);
        this.resizeHeight();
        this.drawBox = true;
    }

    @Override
    public void paint(Graphics g) {
        if (drawBox) {
            image.draw(x, y, x + 2, y + 2, 0, 0, 2, 2, filter);
            image.draw(x, y + 2, x + 2, y + height, 0, 2, 2, 30, filter);
            image.draw(x, y + height - 2, x + 2, y + height, 0, 30, 2, 32, filter);

            image.draw(x + 2, y, x + width - 2, y + 2, 2, 0, 30, 2, filter);
            image.draw(x + 2, y + 2, x + width - 2, y + height - 2, 3, 3, 29, 29, filter);
            image.draw(x + 2, y + height - 2, x + width - 2, y + height, 2, 30, 30, 32, filter);

            image.draw(x + width - 2, y, x + width, y + 2, 30, 0, 32, 2, filter);
            image.draw(x + width - 2, y + 2, x + width, y + height - 2, 30, 2, 32, 30, filter);
            image.draw(x + width - 2, y + height - 2, x + width, y + height, 30, 30, 32, 32, filter);
        }

        for (String line : lines) {
            GUI.get().getFont().drawString(x + 10, y + lines.indexOf(line) * GUI.get().getFont().getHeight() + 10, line,
                    textColor);
        }
    }

    private void resizeHeight() {
        lines = new ArrayList<>();
        String line = "";

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                lines.add(new String(line));
                line = "";
            } else if (GUI.get().getFont().getWidth(line + text.charAt(i) + "    ") > width) {
                lines.add(new String(line));
                line = "" + text.charAt(i);
            } else {
                line += text.charAt(i);
            }
        }

        lines.add(new String(line));
        height = lines.size() * GUI.get().getFont().getHeight() + 20;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.resizeHeight();
    }

    public void clear() {
        this.setText("");
    }

    @Override
    public void setOpacity(float opacity) {
        super.setOpacity(opacity);
        textColor.a = opacity;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = new Color(textColor);
    }

    public boolean isDrawBox() {
        return drawBox;
    }

    public void setDrawBox(boolean drawBox) {
        this.drawBox = drawBox;
    }
}
