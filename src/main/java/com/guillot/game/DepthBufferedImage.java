package com.guillot.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class DepthBufferedImage {

    private Image image;

    private Graphics graphics;

    private Point[][] depthBuffer;

    public DepthBufferedImage(int width, int height) throws SlickException {
        image = new Image(width, height);
        graphics = image.getGraphics();
        depthBuffer = new Point[width][height];
    }

    public void clear() {
        graphics.clear();
        depthBuffer = new Point[getWidth()][getHeight()];
    }

    public void drawImage(Point point, Image image, float x, float y, float x2, float y2, float srcx, float srcy, float srcx2,
            float srcy2) {
        graphics.drawImage(image, x, y, x2, y2, srcx, srcy, srcx2, srcy2);

        for (int i = 0; srcx + i < srcx2; i++) {
            for (int j = 0; srcy + j < srcy2; j++) {
                if (image.getColor((int) (srcx + i), (int) (srcy + j)).a > 0f) {
                    int bufferX = (int) (x + i);
                    int bufferY = (int) (y + j);

                    if (bufferX >= 0 && bufferY >= 0 && bufferX < getWidth() && bufferY < getHeight()) {
                        depthBuffer[bufferX][bufferY] = point;
                    }
                }
            }
        }
    }

    public Image getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public Point getDepth(int x, int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            return depthBuffer[x][y];
        }

        return null;
    }

    public float getCenterOfRotationX() {
        return image.getCenterOfRotationX();
    }

    public float getCenterOfRotationY() {
        return image.getCenterOfRotationY();
    }

    public Graphics getGraphics() {
        return graphics;
    }
}
