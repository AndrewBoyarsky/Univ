package com.boyarsky.paralel;

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
    private final JFrame field;
    private final List<GameObject> gameObjects = Collections.synchronizedList(new ArrayList<>());
    private Player player;
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
                    player.moveLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.moveRight();
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

            BufferStrategy bufferStrategy = field.getBufferStrategy();
            Graphics drawGraphics = bufferStrategy.getDrawGraphics();
            drawGraphics.setColor(Color.BLACK);
            drawGraphics.fillRect(0, 0, field.getWidth(), field.getHeight());
            try {
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
        player = new Player((int) size.getWidth() / 2, (int) size.getHeight() - 50);
        gameObjects.add(player);
    }

    public void createEnemySpawningThread() {
        new Thread("Enemy Spawning") {
            int sleepDuration = 1000;
            int enemyRadius = 100;
            int probability = 50;
            int maxProbability = 100;
            List<Enemy> enemies = Collections.synchronizedList(new ArrayList<>());
            @Override
            public void run() {

                while (!Thread.currentThread().isInterrupted()) {
                    probability = Math.min(probability + 1, maxProbability);
                    int hit = new Random().nextInt(maxProbability);
                    if (hit < probability) {
                        log.info("Spawn enemy");
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
