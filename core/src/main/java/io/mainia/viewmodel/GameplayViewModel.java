package io.mainia.viewmodel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import io.mainia.model.*;
import io.mainia.services.ResultWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.mainia.Mainia.worldHeight;
import static io.mainia.view.GameplayScreen.columnWidth;

public class GameplayViewModel {
    //level info
    private final Level level;
    private final List<List<Note>> notes;
    private final float speed;
    private final int columnCount;
    //sprites(temporary) TODO: switch location of the sprites array to view
    private ArrayList<Array<Sprite>> noteSprites = new ArrayList<>();
    private Array<Sprite> sliderSprites = new Array<>();
    private Array<Sprite> missedSprites = new Array<>();
    private int[] firstToHit;
    private int[] firstToAdd;
    private final Score score = new Score();
    private final float startingTime;
    private final Health health;
    private final float perfectHitHeight;
    private final NoteSpriteFactory noteSpriteFactory;
    private final Combo combo = new Combo();

    public GameplayViewModel(Level level) {
        this.level = level;
        this.startingTime = level.startTime();
        notes = level.notes();
        speed = level.speed();
        columnCount = level.columnCount();
        noteSpriteFactory = new NoteSpriteFactory(columnWidth, level);
        firstToHit = new int[columnCount];
        firstToAdd = new int[columnCount];
        for(int i=0; i<columnCount; i++) {
            noteSprites.add(new Array<>());
            if(notes.get(i).isEmpty()){
                firstToHit[i] = -1;
                firstToAdd[i] = -1;
            }
        }
        perfectHitHeight = 0.2f;
        if(level.isHealthStatic()) health = new StaticHealth(level.healthAmount());
        else health = new DynamicHealth(level.healthAmount());
    }

    public boolean update(float currentTime, Texture noteTexture, Texture sliderStartTexture, Texture sliderMiddleTexture, Texture sliderEndTexture) {
        boolean missed = false;
        for(int i = 0; i < columnCount; i++){
            //missed notes
            while(firstToHit[i]!=-1 && notes.get(i).get(firstToHit[i]).hitTime() + 1000/speed < currentTime){
                missed = true;
                firstToHit[i]++;
                missedSprites.add(noteSprites.get(i).get(0));
                noteSprites.get(i).removeIndex(0);
                score.missedUpdate();
                health.updateHealth(HitResult.MISS);
                combo.updateCombo(HitResult.MISS);
                if(notes.get(i).size() == firstToHit[i]) firstToHit[i] = -1;
            }
            while(firstToAdd[i]!=-1 && notes.get(i).get(firstToAdd[i]).hitTime() - (1-perfectHitHeight)*worldHeight*1000/speed < currentTime){
                Note note = notes.get(i).get(firstToAdd[i]);
                Sprite sprite = null;

                if(note instanceof HitNote hit) {
                    ArrayList<Float> coords = noteSpriteFactory.buildHit(hit, i, currentTime);
                    sprite = new Sprite(noteTexture);
                    sprite.setPosition(coords.get(0), coords.get(1));
                }
                if(note instanceof SliderNote slider) {
                    ArrayList<Float> coords = noteSpriteFactory.buildSlider(slider, i, currentTime);
                    sprite = new  Sprite(sliderStartTexture);
                    sprite.setPosition(coords.get(0), coords.get(1));

                    Sprite start =  new Sprite(sliderStartTexture);
                    start.setSize(columnWidth, columnWidth/4);
                    start.setPosition(coords.get(0), coords.get(1));

                    Sprite end =  new Sprite(sliderEndTexture);
                    end.setSize(columnWidth, columnWidth/4);
                    end.setPosition(coords.get(0), coords.get(2));

                    Sprite middle = new Sprite(sliderMiddleTexture);
                    middle.setSize(columnWidth, end.getY() - start.getY()+columnWidth/4);
                    middle.setPosition(coords.get(0), coords.get(3));

                    sliderSprites.addAll(start, end, middle);
                }
                sprite.setSize(columnWidth, columnWidth/4);
                noteSprites.get(i).add(sprite);
                firstToAdd[i]++;
                if(notes.get(i).size() == firstToAdd[i]) firstToAdd[i] = -1;
            }
        }
        for(int i=missedSprites.size-1; i>=0; i--){
            if(missedSprites.get(i).getY()<0)missedSprites.removeIndex(i);
        }
        return missed;
    }

    public HitResult onPressUpdate(int column, float time){
        if(firstToHit[column] == -1){return HitResult.NONE;}
        HitResult result = notes.get(column).get(firstToHit[column]).hitCheck(time);
        health.updateHealth(result);
        combo.updateCombo(result);
        score.update(result,combo);
        if(result != HitResult.NONE) {
            if (firstToHit[column] == notes.get(column).size() - 1)
                firstToHit[column] = -1;
            else firstToHit[column]++;
            noteSprites.get(column).removeIndex(0);
        }
        return result;
    }

    public void onHoldUpdate(int column, float time){
        for(Note note : notes.get(column)){
            if(note instanceof SliderNote slider) {
                if(time >= slider.hitTime() && time <= slider.releaseTime()){
                    health.updateHealth(HitResult.HOLD);
                    combo.updateCombo(HitResult.HOLD);
                    score.update(HitResult.HOLD,combo);
                }
            }
        }
    }

    public void addNewResult() throws IOException {
        ResultWriter.addResult(new File(level.resultLocation()),score.createResult());
    }

    public Score getScore() {
        return score;
    }
    public float getHealth(){
        return health.currentHealth();
    }
    public List<List<Note>> getNotes() {
        return notes;
    }
    public Array<Sprite> getMissed() {return missedSprites;}
    public float getSpeed() {
        return speed;
    }
    public int getColumnCount() {
        return columnCount;
    }
    public ArrayList<Array<Sprite>> getNoteSprites() {
        return noteSprites;
    }
    public Level getLevel() {
        return level;
    }

    public Array<Sprite> getSliderSprites() {
        return sliderSprites;
    }

    public int getCombo(){
        return combo.currentCombo();
    }
}
