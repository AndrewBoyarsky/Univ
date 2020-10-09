package com.boyarsky.paralel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

public class Bullet extends MovableGameObject implements Runnable {
    static final Semaphore semaphore = new Semaphore(3);
    int width;
    int height;
    private Bullet(int width, int height, int x, int y, int speedX, int speedY) {
        super(x, y, speedX, speedY);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void checkCoordinates() {

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
        return true;
    }

    public static Bullet tryCreateBullet(int x, int y, int x1, int y1, int speed) {
        if (semaphore.tryAcquire()) {
            try {
                int width = Math.abs(x - x1);
                int height = Math.abs(y - y1);

                return new Bullet(width, height, x + (width /2), y - (height / 2), 0, -speed);
            } finally {
                semaphore.release();
            }
        }
        return null;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            move();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
