package com.boyarsky.paralel;

import java.awt.*;

public class Player extends GameObject{
    int stepSize = 25;
    boolean turnLeft;
    public Player(int x, int y) {
        super(x, y);
    }

    public void moveLeft() {
        x = Math.max(x - stepSize, 100);
    }

    public void moveRight() {
        x = Math.min(x + stepSize, 900);
    }

    public Missile fire() {
        int x1, x2;
        if (turnLeft) {
            x1 = x - 100;
            x2 = x - 80;
        } else {
            x1 = x + 80;
            x2 = x + 100;
        }
        Missile missle = Missile.tryCreateBullet(x1, y - 90, x2, y - 160, 5);
        if (missle != null) {
            turnLeft = !turnLeft;
        }
        return missle;

    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.CYAN);
        Polygon polygon = new Polygon();
        polygon.addPoint(x - 100, y);
        polygon.addPoint(x - 100, y - 80);
        polygon.addPoint(x - 80, y - 80);
        polygon.addPoint(x - 80, y - 40);
        polygon.addPoint(x - 40, y - 40);
        polygon.addPoint(x, y - 80);
        polygon.addPoint(x + 40, y - 40);
        polygon.addPoint(x + 80, y - 40);
        polygon.addPoint(x + 80, y - 80);
        polygon.addPoint(x + 100, y - 80);
        polygon.addPoint(x + 100, y);
        polygon.addPoint(x - 100, y);
        graphics.fillPolygon(polygon);
    }

    @Override
    boolean isAlive() {
        return true;
    }
}
