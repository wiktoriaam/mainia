package io.mainia.model;

import java.util.List;

public class Level {
    private final List<List<Note>> notes;
    private float speed;
    private final short columnCount;

    public Level(float speed, List<List<Note>> notes, short columnCount) {
        this.speed = speed;
        this.notes = notes;
        this.columnCount = columnCount;
    }

    public void addNote(int column, Note note){

    }

}
