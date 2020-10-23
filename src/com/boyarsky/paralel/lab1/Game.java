package com.boyarsky.paralel.lab1;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
public class Game extends Thread {
    final JFrame field;
    private final List<GameObject> gameObjects = Collections.synchronizedList(new ArrayList<>());
    private Player player;
    private ScoreBoard scoreBoard;
    public static final int TOTAL_WAIT_TIME = 15000;

    public Game(JFrame field) {
        this.field = field;
    }
    boolean gameStarted = false;

    @Override
    public void run() {
        if (!gameStarted()) return;
        log.info("GameStarted");

        createInitialObjects();
        createEnemySpawningThread();
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player.moveLeft(0);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.moveRight(field.getSize().width);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    Missile missle = player.fire();
                    if (missle != null) {
                        gameObjects.add(missle);
                        Thread thread = new Thread(missle);
                        thread.setUncaughtExceptionHandler(new DefaultExceptionHandler());
                        thread.start();
                    }
                }
            }
        });

        while (!Thread.currentThread().isInterrupted()) {
            if (scoreBoard.isWin() || scoreBoard.isLose()) {
                break;
            }
            BufferStrategy bufferStrategy = field.getBufferStrategy();
            Graphics drawGraphics = bufferStrategy.getDrawGraphics();
            drawGraphics.setColor(Color.BLACK);
            drawGraphics.fillRect(0, 0, field.getWidth(), field.getHeight());
            try {
                for (int i = 0; i < gameObjects.size(); i++) {
                    GameObject gameObject = gameObjects.get(i);
                    if (gameObject.isAlive()) {
                        for (int j = i + 1; j < gameObjects.size(); j++) {

                            GameObject anotherObj = gameObjects.get(j);
                            if (anotherObj.isAlive()) {
                                int sumR = gameObject.getSize() + anotherObj.getSize();
                                double actualDistance = Math.sqrt(Math.pow(gameObject.getX() - anotherObj.getX(), 2) + Math.pow(gameObject.getY() - anotherObj.getY(), 2));
                                if (sumR >= actualDistance) {
                                    gameObject.collide(anotherObj);
                                    anotherObj.collide(gameObject);
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < gameObjects.size(); i++) {
                    GameObject gameObject = gameObjects.get(i);
                    if (gameObject.isAlive()) {
                        gameObject.draw(drawGraphics);
                    } else {
                        gameObjects.remove(i);
                    }
                }
            }
            finally {
                drawGraphics.dispose();
            }
            bufferStrategy.show();
            Toolkit.getDefaultToolkit().sync();

            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                log.error("Main thread for rendering was interrupted");
                Thread.currentThread().interrupt();
            }
        }
        log.info("End of Game");
    }

    private boolean gameStarted() {
        synchronized (this) {
            int currentWaitTime = 0;
            while (!gameStarted && currentWaitTime < TOTAL_WAIT_TIME) {
                long startWaitTime = System.currentTimeMillis();
                try {
                    wait(100);
                } catch (InterruptedException ignored) {
                    log.info("Game was interrupted");
                    return false;
                }
                currentWaitTime += (System.currentTimeMillis() - startWaitTime);
            }
        }
        return true;
    }

    public void createInitialObjects() {
        Dimension size = field.getSize();
        player = new Player((int) (size.getWidth()/ 32), (int) size.getWidth() / 2, (int) size.getHeight());
        gameObjects.add(player);
        scoreBoard = new ScoreBoard(30, field);
        Thread scoreBoardThread = new Thread(scoreBoard);
        scoreBoardThread.setUncaughtExceptionHandler(new DefaultExceptionHandler());
        scoreBoardThread.start();
    }

    public void createEnemySpawningThread() {
        Game game = this;
        new Thread("Enemy Spawning") {
            int sleepDuration = 1000;
            int probability = 50;
            int maxProbability = 100;
            List<Enemy> enemies = Collections.synchronizedList(new ArrayList<>());
            @Override
            public void run() {
                Dimension size = field.getSize();
                double widthDrift = 0.15 * size.getWidth();
                double heightDrift = 0.2 * size.getHeight();

                double enemyHeight = size.getHeight() - 2 * heightDrift;
                double enemyWidth = size.getWidth() - 2 * widthDrift;
                double scale = enemyWidth / (enemyHeight);
                int cols = 5;
                int rows = (int) (scale * cols);
                double rowsHeight = enemyHeight / rows;
                double colWidth = (enemyWidth) / cols;
                double slotSize = Math.min(colWidth, rowsHeight);


                while (!Thread.currentThread().isInterrupted()) {
                    probability = Math.min(probability + 1, maxProbability);
                    Random random = new Random();
                    int hit = random.nextInt(maxProbability);
                    if (hit < probability) {
                        log.info("Spawn enemy");
                        int i = random.nextInt(cols);
                        int j = random.nextInt(rows);
                        Enemy newEnemy = new Enemy(game, (int) slotSize, (int) (i * colWidth + widthDrift), (int) (j * rowsHeight + widthDrift), random.nextInt(10) - 5, random.nextInt(10) - 5, scoreBoard);
                        gameObjects.add(newEnemy);
                        Thread enemyThread = new Thread(newEnemy);
                        enemyThread.setUncaughtExceptionHandler(new DefaultExceptionHandler());
                        enemyThread.start();
                    }
                    try {
                        Thread.sleep(sleepDuration);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }.start();
    }
}
