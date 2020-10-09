package com.boyarsky.paralel;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
public class Demo {
    private volatile boolean gameStarted = false;

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.init();
    }

    private JFrame mainWindow;
    private JPanel playField;

    public Demo() {
        mainWindow = new JFrame("Parallel game");
        playField = new JPanel();
    }

    private void init() {
        mainWindow.setSize(1000, 800);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        playField = new JPanel();
        playField.setSize(1000, 800);
        playField.setBackground(Color.BLACK);
        mainWindow.getContentPane().setLayout(new BoxLayout(mainWindow.getContentPane(), BoxLayout.Y_AXIS));
        mainWindow.add(playField);
        mainWindow.setVisible(true);
        Game game = new Game(playField);
        playField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                log.info("Start game by mouse");
                synchronized (game) {
                    game.gameStarted = true;
                    game.notify();
                }
            }
        });
        game.start();
    }
}
