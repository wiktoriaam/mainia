package io.mainia.model;


public class HitNote implements Note {
    private final float hitTime;
    private final byte column;

    public HitNote(float hitTime, byte column) {
        this.hitTime = hitTime;
        this.column = column;
    }

    @Override
    public float getHitTime() {
        return hitTime;
    }

    @Override
    public byte getColumn() {
        return column;
    }
}
