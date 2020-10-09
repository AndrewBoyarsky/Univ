package com.boyarsky.paralel;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
public class Game extends Thread {
    private final JPanel field;
    private final List<GameObject> gameObjects = Collections.synchronizedList(new ArrayList<>());

    public Game(JPanel field) {
        this.field = field;
    }
    boolean gameStarted = false;

    @SneakyThrows
    @Override
    public void run() {
        int totalWaitTime = 15000;
        synchronized (this) {
            int currentWaitTime = 0;
            while (!gameStarted && currentWaitTime < totalWaitTime) {
                long startWaitTime = System.currentTimeMillis();
                wait(100);
                currentWaitTime += (System.currentTimeMillis() - startWaitTime);
            }
        }
        log.info("GameStarted");

        createInitialObjects();
        createEnemySpawningThread();
        while (true) {

            Thread.sleep(10);
        }
    }

    public void createInitialObjects() {
        Dimension size = field.getSize();
        Player player = new Player((int) size.getWidth() / 2, (int) size.getHeight() - 50);
        gameObjects.add(player);
    }

    public void createEnemySpawningThread() {
        new Thread("Enemy Spawning") {
            int timeStep = 10;
            int sleepDuration = 1000;
            int minSleepDuration = 250;
            int enemyRadius = 100;
            List<Enemy> enemies = Collections.synchronizedList(new ArrayList<>());
            @SneakyThrows
            @Override
            public void run() {

                while (true) {
                    Dimension size = field.getSize();
//                    new Enemy()
                    log.info("Spawn enemy");
                    Thread.sleep(Math.max(sleepDuration -= timeStep, minSleepDuration));
                }
            }
        }.start();
    }
}
