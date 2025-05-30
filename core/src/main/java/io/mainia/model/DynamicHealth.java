package io.mainia.model;

public class DynamicHealth implements Health {
    private final float maxHealth;
    private float health;

    public DynamicHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    @Override
    public float currentHealth(){
        return health;
    }

    @Override
    public void updateHealth(HitResult hitResult){
        switch(hitResult){
            case MISS: health-=1; break;
            case OK: health-=0.2f; break;
            case GREAT: health+=0.3f; break;
            case PERFECT: health+=0.5f; break;
            case HOLD: health+=0.01f; break;
        }
        if(health > maxHealth) health = maxHealth;
    }
}
