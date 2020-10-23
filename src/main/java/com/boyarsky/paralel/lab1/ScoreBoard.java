package com.boyarsky.paralel.lab1;

import lombok.Data;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class ScoreBoard implements Runnable {
    private AtomicInteger miss = new AtomicInteger(0);
    private AtomicInteger hit = new AtomicInteger(0);
    private final int toHit;
    private final JFrame jFrame;

    public void miss() {
        miss.incrementAndGet();
    }

    public void hit() {
        hit.incrementAndGet();
    }

    public boolean isWin() {
        return (hit.get() >= toHit);
    }

    public boolean isLose() {
        return miss.get() >= toHit;
    }


    @Override
    public void run() {
        while (!isWin() && !isLose()) {
            jFrame.setTitle("Hit: " + hit.get() + ", miss: " + miss.get());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isWin()) {
            jFrame.setTitle("Victory!!!");
            JOptionPane.showMessageDialog(jFrame, "Victory!");
        } else {
            jFrame.setTitle("Game over!");
            JOptionPane.showMessageDialog(jFrame, "Game Over!");
//            jFrame.setBackground(Color.RED);
        }
    }

    public void lose() {
        miss.addAndGet(toHit);
    }
}
