package io.mainia.model;

import java.util.List;

public class Level {
    private final List<List<Note>> notes;
    private final float speed;
    private final short columnCount;
    private final float length;
    private final String musicFilename;
    public final float startTime;

    public Level(float speed, List<List<Note>> notes, short columnCount, float length, String musicFilename, float startTime) {
        this.speed = speed;
        this.notes = notes;
        this.columnCount = columnCount;
        this.length = length;
        this.musicFilename = musicFilename;
        this.startTime = startTime;
    }

    public List<List<Note>> notesList() {return notes;}

    public float speed() {return speed;}

    public short columnCount() {return columnCount;}

    public float length() {return length;}

    public String NameOfMusicFile() {return musicFilename;}
}
