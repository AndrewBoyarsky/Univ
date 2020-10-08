package com.boyarsky.paralel;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
@Slf4j
public class Demo {
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
        playField.setSize(1000, 700);
        playField.setBackground(Color.BLACK);
        mainWindow.getContentPane().setLayout(new BoxLayout(mainWindow.getContentPane(), BoxLayout.Y_AXIS));
        mainWindow.add(playField);
        JButton startButton = new JButton("Start game");
        startButton.addActionListener((ev)-> startGame());
        mainWindow.getContentPane().add(startButton);
        mainWindow.setVisible(true);
    }

    public void startGame() {
        new Game(playField).start();
        log.info("Game was started");
    }

}
