package com.boyarsky.paralel;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;

@Data
@AllArgsConstructor
public abstract class GameObject {
    int x;
    int y;

    public abstract void draw(JPanel jPanel);
}
