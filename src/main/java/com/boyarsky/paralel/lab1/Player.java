package com.boyarsky.paralel.lab1;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
@Slf4j
public class Player extends GameObject{
     final int stepSize;
    boolean turnLeft;
    volatile boolean isAlive = true;
    public static final int PLATFORM_SIZE = 100;
    public Player(int stepSize, int x, int y) {
        super(x,  y, PLATFORM_SIZE);
        this.stepSize = stepSize;
    }

    public void moveLeft(int leftCorner) {
        x = Math.max(x - stepSize, leftCorner + PLATFORM_SIZE);
    }

    public void moveRight(int rightCorner) {
        x = Math.min(x + stepSize, rightCorner - PLATFORM_SIZE);
    }

    public Missile fire() {
        int x1, x2;
        if (turnLeft) {
            x1 = x - PLATFORM_SIZE;
            x2 = x - PLATFORM_SIZE + 20;
        } else {
            x1 = x + PLATFORM_SIZE - 20;
            x2 = x + PLATFORM_SIZE;
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
        polygon.addPoint(x - PLATFORM_SIZE, y);
        polygon.addPoint(x - PLATFORM_SIZE, y - 80);
        polygon.addPoint(x - 80, y - 80);
        polygon.addPoint(x - 80, y - 40);
        polygon.addPoint(x - 40, y - 40);
        polygon.addPoint(x, y - 80);
        polygon.addPoint(x + 40, y - 40);
        polygon.addPoint(x + 80, y - 40);
        polygon.addPoint(x + 80, y - 80);
        polygon.addPoint(x + PLATFORM_SIZE, y - 80);
        polygon.addPoint(x + PLATFORM_SIZE, y);
        polygon.addPoint(x - PLATFORM_SIZE, y);
        graphics.fillPolygon(polygon);
    }

    @Override
    boolean isAlive() {
        return isAlive;
    }

    @Override
    void collide(GameObject gameObject) {
        if (gameObject instanceof Enemy) {
            log.info("Collide {} with {}", this, gameObject);
            Enemy enemy = (Enemy) gameObject;
            if (enemy.isAlive()) {
                isAlive = false;
            }
        }
    }
}
