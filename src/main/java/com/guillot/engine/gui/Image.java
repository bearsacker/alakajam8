package com.guillot.engine.gui;

import static org.newdawn.slick.Input.MOUSE_LEFT_BUTTON;

import org.newdawn.slick.Graphics;

public class Image extends Component {

    private org.newdawn.slick.Image image;

    private int frameHeight;

    private int frame;

    private float zoom;

    private int zoneWidth;

    private int zoneHeight;

    private boolean repeated;

    public Image(String path) throws Exception {
        super();

        this.image = new org.newdawn.slick.Image(path);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.frameHeight = this.height;
        this.frame = 1;
        this.zoom = 1.0f;
        this.x = 0;
        this.y = 0;

        this.zoneWidth = this.width;
        this.zoneHeight = this.height;
        this.repeated = false;
    }

    public Image(String path, int x, int y) throws Exception {
        super();

        this.image = new org.newdawn.slick.Image(path);
        this.x = x;
        this.y = y;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.frameHeight = this.height;
        this.frame = 1;
        this.zoom = 1.0f;

        this.zoneWidth = this.width;
        this.zoneHeight = this.height;
        this.repeated = false;
    }

    public org.newdawn.slick.Image getImage() {
        return this.image;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public void setFrame(int frame) {
        if (frame * this.frameHeight <= this.height && frame > 0) {
            this.frame = frame;
        }
    }

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    @Override
    public void update() throws Exception {
        int mX = GUI.get().getMouseX();
        int mY = GUI.get().getMouseY();

        if (mX >= x && mX <= (x + width * zoom) && mY >= y && mY <= (y + height * zoom)) {
            this.mouseOn = true;
        } else {
            this.mouseOn = false;
        }
    }

    @Override
    public void paint(Graphics g) {
        if (repeated) {
            image.draw(x, y, zoneWidth, zoneHeight, filter);
        } else {
            image.draw(x, y, (int) (x + this.width * zoom), (int) (y + this.frameHeight * zoom), 0, (this.frame - 1) * this.frameHeight,
                    this.width, this.frame * this.frameHeight, filter);
        }
    }

    public void coverZone(int zoneWidth, int zoneHeight, boolean repeated) {
        if (!repeated) {
            float zoomw = zoneWidth / (float) width;
            float zoomh = zoneHeight / (float) height;

            if (height * zoomw < zoneHeight) {
                zoom = zoomh;
            } else {
                zoom = zoomw;
            }
        }

        this.zoneWidth = zoneWidth;
        this.zoneHeight = zoneHeight;
        this.repeated = repeated;
    }

    public boolean isHover(int x, int y) {
        return x >= this.x && x <= this.x + width * zoom && y >= this.y && y <= this.y + height * zoom;
    }

    public boolean isClicked() {
        return mouseOn && GUI.get().getInput().isMousePressed(MOUSE_LEFT_BUTTON);
    }
}
