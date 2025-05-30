package io.mainia.model;

public class StaticHealth implements Health {
    private float health;

    public StaticHealth(float health) {
        this.health = health;
    }
    @Override
    public float currentHealth() {
        return health;
    }
    @Override
    public void updateHealth(HitResult hitResult){
        if(hitResult == HitResult.MISS) decreaseHealth();
    }
    public void decreaseHealth() {
        health--;
    }

}
