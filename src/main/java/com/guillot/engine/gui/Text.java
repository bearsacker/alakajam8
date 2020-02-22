package com.guillot.engine.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class Text extends Component {

    private String text;

    private TrueTypeFont font;

    public Text(String text) {
        this(text, 0, 0);
    }

    public Text(String text, int x, int y) {
        super();

        this.text = text;
        this.x = x;
        this.y = y;
        this.font = GUI.get().getFont();
        this.height = font.getHeight();
        this.width = font.getWidth(text);
    }

    public Text(String text, int x, int y, Color color) {
        this(text, x, y);
        this.filter = new Color(color);
    }

    public Text(String text, int x, int y, TrueTypeFont font, Color color) {
        this(text, x, y, color);
        this.font = font;
        this.height = font.getHeight();
        this.width = font.getWidth(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.width = font.getWidth(text);
    }

    public Color getColor() {
        return filter;
    }

    public void setColor(Color color) {
        this.filter = new Color(color);
    }

    public TrueTypeFont getFont() {
        return font;
    }

    public void setFont(TrueTypeFont font) {
        this.font = font;
    }

    @Override
    public void paint(Graphics g) {
        if (font != null) {
            int offset = 0;
            for (String line : text.split("\n")) {
                font.drawString(x, y + offset, line, filter);
                offset += font.getHeight();
            }
        }
    }
}
