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
    public void updateHealth(HitResult hitResult, boolean nomiss, boolean perfect){
        switch(hitResult){
            case MISS -> {if(nomiss || perfect) health=0; else health-=1;}
            case OK -> {if(perfect) health=0; else health-=0.2f;}
            case GREAT -> {if(perfect) health=0; else health+=0.3f;}
            case PERFECT -> health+=0.5f;
            case HOLD -> health+=0.01f;
        }
        if(health > maxHealth) health = maxHealth;
    }
}
