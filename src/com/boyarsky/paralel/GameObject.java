package com.boyarsky.paralel;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

@Data
@AllArgsConstructor
public abstract class GameObject {
    int x;
    int y;
    int size;

    public abstract void draw(Graphics graphics);

    abstract boolean isAlive();

    abstract void collide(GameObject gameObject);
}
