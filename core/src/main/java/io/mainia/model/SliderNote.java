package io.mainia.model;

public class SliderNote implements Note {
    private float hitTime;
    private float releaseTime;

    public SliderNote(float hitTime, float releaseTime) {
        this.hitTime = hitTime;
        this.releaseTime = releaseTime;
    }

    @Override
    public float getHitTime() {
        return hitTime;
    }

    @Override
    public HitResult hitCheck(float time) {
        return null;
    }

    public float getReleaseTime() {
        return releaseTime;
    }

}
