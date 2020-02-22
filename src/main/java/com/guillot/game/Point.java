package com.guillot.game;


public class Point {

    private int x;

    private int y;

    public Point() {
        this(0, 0);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean is(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean is(Point point) {
        return point != null && is(point.x, point.y);
    }

    public int distanceFrom(Point point) {
        if (point != null) {
            int dy = point.getY() - getY();
            int dx = point.getX() - getX();
            return (int) Math.sqrt(dy * dy + dx * dx);
        }

        return 0;
    }
}
