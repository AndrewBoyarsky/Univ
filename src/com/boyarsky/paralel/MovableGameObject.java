package com.boyarsky.paralel;

import lombok.AllArgsConstructor;

public abstract class MovableGameObject extends GameObject implements Movable{
    int speedX;
    int speedY;

    public MovableGameObject(int x, int y, int speedX, int speedY) {
        super(x, y);
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
