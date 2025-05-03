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

    public float getReleaseTime() {
        return releaseTime;
    }

}
