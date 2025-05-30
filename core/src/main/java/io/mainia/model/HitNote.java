package io.mainia.model;


public class HitNote implements Note {
    private final float hitTime;

    public HitNote(float hitTime) {
        this.hitTime = hitTime;
    }

    @Override
    public float hitTime() {
        return hitTime;
    }
    @Override
    public HitResult hitCheck(float time){
        if(time <  hitTime - 1000){return HitResult.NONE;}
        if(Math.abs(time -  hitTime)<=100) {
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
