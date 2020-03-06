package com.guillot.engine.gui;

import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;


public class Button extends Component {

    public final static String DEFAULT_BUTTON_SPRITE = "gui/default_button.png";

    public final static Color DEFAULT_BUTTON_TEXT_COLOR = new Color(128, 128, 128);

    public final static Color DEFAULT_BUTTON_TEXT_HOVER_COLOR = Color.black;

    public final static Color DEFAULT_BUTTON_DISABLED_COLOR = new Color(0.8f, 0.8f, 0.8f);

    private Image image;

    private String text;

    private Event event;

    private Color color;

    private Color colorHover;

    private boolean drawSprite;

    private TrueTypeFont font;

    private boolean enabled;

    private boolean sizeAuto;

    public Button(String text, int x, int y, int width, int height) throws Exception {
        super();

        this.image = new Image(DEFAULT_BUTTON_SPRITE);
        this.image.setFilter(Image.FILTER_NEAREST);
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = new Color(DEFAULT_BUTTON_TEXT_COLOR);
        this.colorHover = new Color(DEFAULT_BUTTON_TEXT_HOVER_COLOR);
        this.enabled = true;
        this.drawSprite = true;
        this.font = GUI.get().getFont();
        this.width = width;
        this.height = height;
        this.sizeAuto = false;
    }

    public Button(String text, int x, int y) throws Exception {
        super();

        this.image = new Image(DEFAULT_BUTTON_SPRITE);
        this.image.setFilter(Image.FILTER_NEAREST);
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = new Color(DEFAULT_BUTTON_TEXT_COLOR);
        this.colorHover = new Color(DEFAULT_BUTTON_TEXT_HOVER_COLOR);
        this.enabled = true;
        this.drawSprite = true;
        this.font = GUI.get().getFont();
        this.width = this.font.getWidth(this.text);
        this.height = this.font.getHeight();
        this.sizeAuto = true;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
        this.sizeAuto = false;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
        this.sizeAuto = false;
    }

    public boolean isSizeAuto() {
        return sizeAuto;
    }

    public void setSizeAuto(boolean sizeAuto) {
        this.sizeAuto = sizeAuto;
        if (this.sizeAuto) {
            this.width = this.font.getWidth(this.text);
            this.height = this.font.getHeight();
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = new Color(color);
    }

    public Color getColorHover() {
        return colorHover;
    }

    public void setColorHover(Color colorHover) {
        this.colorHover = new Color(colorHover);
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setOpacity(float opacity) {
        super.setOpacity(opacity);
        this.color.a = opacity;
        this.colorHover.a = opacity;
    }

    public boolean isDrawSprite() {
        return drawSprite;
    }

    public void setDrawSprite(boolean drawSprite) {
        this.drawSprite = drawSprite;
    }

    public TrueTypeFont getFont() {
        return font;
    }

    public void setFont(TrueTypeFont font) {
        this.font = font;
        if (this.sizeAuto) {
            this.width = this.font.getWidth(this.text);
            this.height = this.font.getHeight();
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        float opacity = this.filter.a;
        this.filter = enabled ? new Color(DEFAULT_FILTER_COLOR) : new Color(DEFAULT_BUTTON_DISABLED_COLOR);
        this.filter.a = opacity;
    }

    @Override
    public void paint(Graphics g) {
        if (drawSprite) {
            if (!mouseOn || !enabled) {
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
        }

        int lineHeight = font.getLineHeight();
        int lineWidth = font.getWidth(text);
        font.drawString(x + (width / 2) - (lineWidth / 2), y + (height / 2) - (lineHeight / 2), text,
                (mouseOn && enabled ? colorHover : color));
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (enabled && mouseOn && GUI.get().isMouseButtonReleased(MOUSE_LEFT_BUTTON) && event != null) {
            event.perform();
        }
    }

    public void setEvent(Event e) {
        event = e;
    }
}
