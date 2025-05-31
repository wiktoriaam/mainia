package io.mainia.model;

public class SliderNote implements Note {
    private final float hitTime;
    private final float releaseTime;

    public SliderNote(float hitTime, float releaseTime) {
        this.hitTime = hitTime;
        this.releaseTime = releaseTime;
    }

    @Override
    public float hitTime() {
        return hitTime;
    }

    @Override
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

    public boolean hold(float time) {
        return time > hitTime && time < releaseTime;
    }

    public float releaseTime() {
        return releaseTime;
    }

}
