package com.boyarsky.paralel;

import javax.swing.*;
import java.awt.*;

public class Enemy extends MovableGameObject {


    public Enemy(int x, int y, int speedX, int speedY) {
        super(x, y, speedX, speedY);
    }

    @Override
    protected void checkCoordinates() {

    }

    @Override
    public void draw(Graphics graphics) {

    }

    @Override
    boolean isAlive() {
        return true;
    }
}
