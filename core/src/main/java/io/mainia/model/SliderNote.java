package io.mainia.model;

public class SliderNote extends Note {
    private final float releaseTime;

    public SliderNote(float hitTime, float releaseTime) {
        super(hitTime);
        this.releaseTime = releaseTime;
    }

    public boolean hold(float time) {
        return time > hitTime && time < releaseTime;
    }

    public float releaseTime() {
        return releaseTime;
    }

}
