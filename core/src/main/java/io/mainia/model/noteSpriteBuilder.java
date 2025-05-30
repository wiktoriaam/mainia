package io.mainia.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

public class noteSpriteBuilder {
    private final Texture noteTexture;
    private final Texture sliderStartTexture;
    private final Texture sliderMiddleTexture;
    private final Texture sliderEndTexture;
    
    public noteSpriteBuilder(Texture noteTexture, Texture sliderStartTexture, Texture sliderMiddleTexture, Texture sliderEndTexture) {
        this.noteTexture = noteTexture;
        this.sliderStartTexture = sliderStartTexture;
        this.sliderMiddleTexture = sliderMiddleTexture;
        this.sliderEndTexture = sliderEndTexture;
    }

    public Sprite build(HitNote hitNote, int column) {
        return null;
    }

    public ArrayList<Sprite> build(SliderNote sliderNote, int column) {
        return null;
    }
}
