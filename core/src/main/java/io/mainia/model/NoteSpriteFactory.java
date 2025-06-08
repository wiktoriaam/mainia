package io.mainia.model;

import java.util.ArrayList;

import static io.mainia.Mainia.worldHeight;
import static io.mainia.Mainia.worldWidth;
import static io.mainia.view.GameplayScreen.perfectHitHeight;

public class NoteSpriteFactory {
    private final float columnWidth;
    private final Level level;

    public NoteSpriteFactory(float columnWidth, Level level) {
        this.level = level;
        this.columnWidth = columnWidth;
    }

    public float calculateX(int column) {
        return worldWidth/2 + columnWidth*(column-(float)level.columnCount()/2);
    }

    public float calculateY(float currentTime, float hitTime) {
        return perfectHitHeight*worldHeight + (hitTime - currentTime) * level.speed()/1000;
    }

    public ArrayList<Float> buildHit(Note hitNote, int column, float currentTime) {
        ArrayList<Float> coords = new ArrayList<>();
        coords.add(calculateX(column));
        coords.add(calculateY(currentTime, hitNote.hitTime()));
        return coords;
    }

    public ArrayList<Float> buildSlider(SliderNote sliderNote, int column, float currentTime) {
        ArrayList<Float> coords = buildHit(sliderNote,  column, currentTime);
        coords.add(calculateY(currentTime, sliderNote.releaseTime()));
        coords.add(calculateY(currentTime, sliderNote.hitTime()+columnWidth/4));
        return coords;
    }
}
