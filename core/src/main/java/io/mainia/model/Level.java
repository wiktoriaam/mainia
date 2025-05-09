package io.mainia.model;

import java.util.List;

public class Level {
    private final List<List<Note>> notes;
    private float speed;
    private final short columnCount;
    private final float length;
    private String musicFilename;

    public Level(float speed, List<List<Note>> notes, short columnCount, float length, String musicFilename) {
        this.speed = speed;
        this.notes = notes;
        this.columnCount = columnCount;
        this.length = length;
        this.musicFilename = musicFilename;
    }

    public List<List<Note>> getNotes() {return notes;}

    public float getSpeed() {return speed;}

    public short getColumnCount() {return columnCount;}

    public float getLength() {return length;}

    public String getMusicFilename() {return musicFilename;}
}
