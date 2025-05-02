package io.mainia.model;

public class SliderNote implements Note {
    private float hitTime;
    private float releaseTime;
    private byte column;

    public SliderNote(float hitTime, float releaseTime,byte column) {
        this.hitTime = hitTime;
        this.releaseTime = releaseTime;
        this.column = column;
    }

    @Override
    public float getHitTime() {
        return hitTime;
    }

    public float getReleaseTime() {
        return releaseTime;
    }

    @Override
    public byte getColumn() {
        return column;
    }
}
