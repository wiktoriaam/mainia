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
    public void updateHealth(HitResult hitResult, boolean nomiss, boolean perfect) {
        if(hitResult == HitResult.MISS) {
            if(nomiss){health = 0;}
            else health--;
        }
        if(hitResult != HitResult.PERFECT && hitResult != HitResult.NONE && perfect){
            health = 0;
        }
    }

}
