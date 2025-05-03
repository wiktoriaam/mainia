package io.mainia.model;


public class HitNote implements Note {
    private final float hitTime;

    public HitNote(float hitTime) {
        this.hitTime = hitTime;
    }

    @Override
    public float getHitTime() {
        return hitTime;
    }

}
