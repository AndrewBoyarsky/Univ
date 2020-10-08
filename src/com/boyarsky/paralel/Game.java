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

    @SneakyThrows
    @Override
    public void run() {
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
            @SneakyThrows
            @Override
            public void run() {

                while (true) {

                    new Enemy()
                    log.info("Spawn enemy");
                    Thread.sleep(Math.max(sleepDuration -= timeStep, minSleepDuration));
                }
            }
        }.start();
    }
}
