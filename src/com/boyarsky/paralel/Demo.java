package com.boyarsky.paralel;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
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
//        playField = new JPanel();
    }

    private void init() {
        mainWindow.setSize(1000, 800);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

//        playField = new JPanel();
//        playField.setSize(1000, 800);
//        playField.setBackground(Color.BLACK);
//        mainWindow.getContentPane().setLayout(new BoxLayout(mainWindow.getContentPane(), BoxLayout.Y_AXIS));
//        mainWindow.add(playField);
//        mainWindow.createBufferStrategy(2);
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
