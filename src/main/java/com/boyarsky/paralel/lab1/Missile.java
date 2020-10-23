package com.boyarsky.paralel.lab1;
import java.awt.*;
import java.util.concurrent.Semaphore;

public class Missile extends MovableGameObject implements Runnable {
    static final Semaphore semaphore = new Semaphore(3);
    int width;
    int height;
    volatile boolean isAlive = true;

    private Missile(int width, int height, int x, int y, int speedX, int speedY) {
        super(Math.max(width /2, height /2), x, y, speedX, speedY);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void checkCoordinates() {
        if (y < 0) {
            isAlive = false;
        }
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.gray);
        Polygon polygon = new Polygon();
        int hHeight = height / 2;
        int hWidth = width / 2;
        polygon.addPoint(x - hWidth, y + hHeight);
        polygon.addPoint(x + hWidth, y + hHeight);
        polygon.addPoint(x + hWidth, y - hHeight + 10);
        polygon.addPoint(x, y - hHeight);
        polygon.addPoint(x - hWidth, y - hHeight + 10);
        polygon.addPoint(x - hWidth, y + hHeight);
        graphics.fillPolygon(polygon);
    }

    @Override
    boolean isAlive() {
        return isAlive;
    }

    @Override
    void collide(GameObject gameObject) {
        if (gameObject instanceof Enemy) {
            Enemy enemy = (Enemy) gameObject;
            if (enemy.destroy()) {
                isAlive = false;
            }
        }
    }

    public static Missile tryCreateBullet(int x, int y, int x1, int y1, int speed) {
        int width = Math.abs(x - x1);
        int height = Math.abs(y - y1);
        return new Missile(width, height, x + (width / 2), y - (height / 2), 0, -speed);
    }

    @Override
    public void run() {
        if (semaphore.tryAcquire()) {
            try {
                while (isAlive()) {
                    move();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                semaphore.release();
            }
        } else {
            isAlive = false;
        }
    }
}

