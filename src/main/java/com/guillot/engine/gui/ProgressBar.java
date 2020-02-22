package com.guillot.engine.gui;

import static org.apache.commons.math3.util.FastMath.max;
import static org.apache.commons.math3.util.FastMath.min;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class ProgressBar extends Component {

    public final static String DEFAULT_PROGRESSBAR_SPRITE = "gui/default_progressbar.png";

    private Image image;

    private int state;

    public ProgressBar(int x, int y, int width, int height, int state) throws Exception {
        super();

        this.image = new Image(DEFAULT_PROGRESSBAR_SPRITE);
        this.x = x;
        this.y = y;
        this.setState(state);
        this.width = width;
        this.height = height;
    }

    @Override
    public void paint(Graphics g) {
        image.draw(x, y, x + 2, y + height, 0, 0, 2, 32, filter);
        image.draw(x + 2, y, x + width - 2, y + height, 2, 0, 30, 32, filter);
        image.draw(x + width - 2, y, x + width, y + height, 30, 0, 32, 32, filter);

        double ratio = state / 100.0;

        image.draw(x + 2, y, x + 2 + (int) (ratio * (width - 4)), y + height, 4, 32, 4, 64, filter);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = max(0, min(100, state));
    }

    public void setState(float state) {
        this.state = (int) (max(0.0f, min(1.0f, state)) * 100);
    }
}
