package io.mainia.model;

public abstract class Note {
    protected final float hitTime;
    Note(float hitTime) {
        this.hitTime = hitTime;
    }

    public float hitTime() {
        return hitTime;
    }
    public HitResult hitCheck(float time) {
        if(time <  hitTime - 1000) {
            return HitResult.NONE;
        }
        if(Math.abs(time - hitTime)<=100) {
            return HitResult.PERFECT;
        }
        if(Math.abs(time - hitTime)<=200) {
            return HitResult.GREAT;
        }
        if(Math.abs(time - hitTime)<=300){
            return HitResult.OK;
        }
        return HitResult.NONE;
    }
}
