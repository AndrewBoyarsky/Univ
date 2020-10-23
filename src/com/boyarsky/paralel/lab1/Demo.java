package com.boyarsky.paralel.lab1;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
public class Demo {
    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.init();
    }

    private JFrame mainWindow;

    public Demo() {
        mainWindow = new JFrame("Parallel game");
    }

    private void init() {
        mainWindow.setSize(1000, 800);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainWindow.setVisible(true);
        mainWindow.createBufferStrategy(2);
        Game game = new Game(mainWindow);
        mainWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                log.info("Start game by mouse");
                synchronized (game) {
                    game.gameStarted = true;
                    game.notify();
                }
            }
        });
        game.setUncaughtExceptionHandler(new DefaultExceptionHandler());
        game.start();
    }
}
