package io.mainia.model;

public class StaticHealth implements Health {
    private int health;

    public StaticHealth(int health) {
        this.health = health;
    }
    @Override
    public int getHealth() {
        return health;
    }
    @Override
    public void decreaseHealth() {
        health--;
    }
}
