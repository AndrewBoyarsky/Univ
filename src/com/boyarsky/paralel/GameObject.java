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

    public abstract void draw(Graphics graphics);

    abstract boolean isAlive();
}
