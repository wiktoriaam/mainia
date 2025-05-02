package io.mainia.services;

import io.mainia.model.Health;
import io.mainia.model.Note;
import io.mainia.model.Score;

public class OnPressService {
    private final Health health;
    private final Score score;

    public OnPressService(Health health, Score score) {
        this.health = health;
        this.score = score;
    }
    public void press(Note note, float hitTime) {

    }
}
