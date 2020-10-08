package com.boyarsky.paralel;

import javax.swing.*;

public abstract class GameObject {
    int x;
    int y;

    public abstract void move();

    public abstract void draw(JPanel jPanel);
}
