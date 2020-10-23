package com.boyarsky.paralel.lab1;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;

@Slf4j
public class Enemy extends MovableGameObject implements Runnable {
    boolean isAlive = true;
    private final ScoreBoard scoreBoard;
    private final Game game;

    public Enemy(Game game, int enemySize, int x, int y, int speedX, int speedY, ScoreBoard scoreBoard) {
        super(enemySize, x, y, speedX, speedY);
        this.scoreBoard = scoreBoard;
        this.game = game;
    }

    @Override
    protected void checkCoordinates() {
        if (x < 0 || y < 0 || x >= game.field.getSize().getWidth() || y >= game.field.getSize().getHeight()) {
            scoreBoard.miss();
            isAlive = false;
        }
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.yellow);
        Polygon polygon = new Polygon();
        int hSize = size / 2;
        System.out.println(size);
        polygon.addPoint(x, y + hSize);
        polygon.addPoint(x - hSize / 3, y + (int)(hSize / 1.5));
        polygon.addPoint(x - hSize / 3, y - (int)(hSize / 1.4));
        polygon.addPoint(x - hSize / 2, y - (int)(hSize / 1.8));
        polygon.addPoint(x - (int)(hSize / 1.7), y - (int)(hSize / 1.8));
        polygon.addPoint(x - (int)(hSize / 1.7), y - (int)(hSize));


        polygon.addPoint(x + (int)(hSize / 1.7), y - (int)(hSize));

        polygon.addPoint(x + (int)(hSize / 1.7), y - (int)(hSize / 1.8));
        polygon.addPoint(x + hSize / 2, y - (int)(hSize / 1.8));
        polygon.addPoint(x + hSize / 3, y - (int)(hSize / 1.4));
        polygon.addPoint(x + hSize / 3, y + (int)(hSize / 1.5));
        polygon.addPoint(x, y + hSize);
        graphics.drawPolygon(polygon);
    }

    @Override
    boolean isAlive() {
        return isAlive;
    }

    @Override
    void collide(GameObject gameObject) {
        if (gameObject instanceof Player) {
            isAlive = false;
            scoreBoard.lose();
        }
    }

    public boolean destroy() {
        if (isAlive()) {
            isAlive = false;
            scoreBoard.hit();
            return true;
        }
        return false;

    }

    @Override
    public void run() {
        while (isAlive()) {
            move();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
