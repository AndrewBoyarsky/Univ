package com.boyarsky.paralel.lab1;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class MovableGameObject extends GameObject implements Movable{
    int speedX;
    int speedY;

    public MovableGameObject(int size, int x, int y, int speedX, int speedY) {
        super(x, y, size);
        this.speedX = speedX;
        this.speedY = speedY;
    }
    @Override
    public void move() {
        x += speedX;
        y += speedY;
        checkCoordinates();
    }
    protected abstract void checkCoordinates();
}
