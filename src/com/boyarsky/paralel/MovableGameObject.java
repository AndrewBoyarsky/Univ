package com.boyarsky.paralel;

public abstract class MovableGameObject extends GameObject implements Movable{
    int speedX;
    int speedY;

    public MovableGameObject(int x, int y) {
        super(x, y);
    }


    @Override
    public void move() {
        x += speedX;
        y += speedY;
        checkCoordinates();
    }

    protected abstract void checkCoordinates();
}
