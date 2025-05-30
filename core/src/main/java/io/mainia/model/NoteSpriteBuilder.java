package io.mainia.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.mainia.Mainia;
import io.mainia.view.GameplayScreen;

import java.util.ArrayList;

import static io.mainia.Mainia.worldHeight;
import static io.mainia.Mainia.worldWidth;
import static io.mainia.view.GameplayScreen.perfectHitHeight;

public class NoteSpriteBuilder {
    private final Texture noteTexture;
    private final Texture sliderStartTexture;
    private final Texture sliderMiddleTexture;
    private final Texture sliderEndTexture;
    private final float columnWidth;
    private final Level level;

    public NoteSpriteBuilder(Texture noteTexture, Texture sliderStartTexture, Texture sliderMiddleTexture, Texture sliderEndTexture,
                             float columnWidth, Level level) {
        this.noteTexture = noteTexture;
        this.sliderStartTexture = sliderStartTexture;
        this.sliderMiddleTexture = sliderMiddleTexture;
        this.sliderEndTexture = sliderEndTexture;
        this.level = level;
        this.columnWidth = columnWidth;
    }

    public float calculateX(int column) {
        return worldWidth/2 + columnWidth*(column-(float)level.columnCount()/2);
    }

    public float calculateY(float currentTime, float hitTime) {
        if ((hitTime - level.startTime()) - (1 - perfectHitHeight) * worldHeight * 1000 / level.speed() <= 0)
            return perfectHitHeight * worldHeight + (hitTime - level.startTime()) * level.speed() / 1000;
        else return worldHeight;
    }

    public Sprite buildHit(HitNote hitNote, int column, float currentTime) {
        Sprite sprite = new Sprite(noteTexture);
        sprite.setSize(columnWidth, columnWidth/4);
        sprite.setX(calculateX(column));
        sprite.setY(calculateY(currentTime, hitNote.hitTime()));
        return sprite;
    }

    public ArrayList<Sprite> buildSlider(SliderNote sliderNote, int column, float currentTime) {
        ArrayList<Sprite> sprites = new ArrayList<>();
        Sprite start = new Sprite(sliderStartTexture);
        start.setSize(columnWidth, (float)columnWidth/4);
        start.setX(calculateX(column));
        start.setY(calculateY(currentTime, sliderNote.hitTime()));
        sprites.add(start);

        Sprite end = new Sprite(sliderEndTexture);
        end.setSize(columnWidth, (float)columnWidth/4);
        end.setX(calculateX(column));
        end.setY(calculateY(currentTime, sliderNote.releaseTime()));
        sprites.add(end);

        Sprite middle = new Sprite(sliderMiddleTexture);
        middle.setSize(columnWidth, end.getY()-start.getY() + columnWidth/4);
        middle.setX(calculateX(column));
        middle.setY(calculateY(currentTime, sliderNote.hitTime()+columnWidth/4));
        sprites.add(middle);

        return sprites;
    }
}
