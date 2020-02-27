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

    public Point(Point point) {
        this();

        if (point != null) {
            this.x = point.x;
            this.y = point.y;
        }
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

    public void incrementX() {
        x++;
    }

    public void decrementX() {
        x--;
    }

    public void incrementY() {
        y++;
    }

    public void decrementY() {
        y--;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
