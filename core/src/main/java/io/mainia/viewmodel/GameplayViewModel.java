package io.mainia.viewmodel;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import io.mainia.model.*;
import io.mainia.view.GameplayScreen;

import java.util.ArrayList;
import java.util.List;

public class GameplayViewModel {
    GameplayScreen gameplayScreen;
    private final Level level;
    private final List<List<Note>> notes;
    private final float speed;
    private final int columnCount;
    private ArrayList<Array<Sprite>> noteSprites = new ArrayList<>();
    private Array<Sprite> missedSprites = new Array<>();
    private int[] firstToHit;
    private int[] firstToAdd;
    private final Score score = new Score();
    private final Health health = new StaticHealth(10) {
    };

    public GameplayViewModel(Level level, GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.level = level;
        notes = level.getNotes();
        speed = level.getSpeed();
        columnCount = level.getColumnCount();
        firstToHit = new int[columnCount];
        firstToAdd = new int[columnCount];
        for(int i=0; i<columnCount; i++) {
            noteSprites.add(new Array<>());
            if(notes.get(i).isEmpty()){
                firstToHit[i] = -1;
                firstToAdd[i] = -1;
            }
        }

    }

    public void update(float currentTime){
        float worldHeight = gameplayScreen.getViewPort().getWorldHeight();
        float worldWidth = gameplayScreen.getViewPort().getWorldWidth();
        for(int i = 0; i < columnCount; i++){
            //missed notes
            while(firstToHit[i]!=-1 && notes.get(i).get(firstToHit[i]).getHitTime() + 1000/speed < currentTime){
                firstToHit[i]++;
                missedSprites.add(noteSprites.get(i).get(0));
                noteSprites.get(i).removeIndex(0);
                score.missed_update();
                health.decreaseHealth();
                if(notes.get(i).size() == firstToHit[i]) firstToHit[i] = -1;
            }
            while(firstToAdd[i]!=-1 && notes.get(i).get(firstToAdd[i]).getHitTime() - 8000/speed < currentTime){
                Sprite sprite = new Sprite(gameplayScreen.noteTexture);
                sprite.setSize(gameplayScreen.columnWidth, gameplayScreen.columnWidth/4);
                if(columnCount%2 == 1){
                    sprite.setCenterX(worldWidth/2 + gameplayScreen.columnWidth*(i- (float) columnCount /2));
                }
                else{
                    sprite.setX(worldWidth/2 + gameplayScreen.columnWidth*(i- (float) columnCount /2));
                }
                if(notes.get(i).get(firstToAdd[i]).getHitTime() - 8000/speed <= 0)
                    sprite.setY(0.2f*worldHeight+notes.get(i).get(firstToAdd[i]).getHitTime()*speed/1000);
                else sprite.setY(worldHeight);
                noteSprites.get(i).add(sprite);
                firstToAdd[i]++;
                if(notes.get(i).size() == firstToAdd[i]) firstToAdd[i] = -1;
            }
        }
        for(int i=missedSprites.size-1; i>=0; i--){
            if(missedSprites.get(i).getY()<0)missedSprites.removeIndex(i);
        }
    }

    public HitResult onPressUpdate(int column, float time){
        if(firstToHit[column] == -1){return HitResult.NONE;}
        float hitTime = notes.get(column).get(firstToHit[column]).getHitTime();
        if(time <  hitTime - 1000){return HitResult.NONE;}
        if(firstToHit[column] == notes.get(column).size() - 1)
            firstToHit[column] = -1;
        else firstToHit[column]++;
        noteSprites.get(column).removeIndex(0);
        if(Math.abs(time -  hitTime)<=200) {
            score.update(HitResult.PERFECT);
            return HitResult.PERFECT;
        }
        if(Math.abs(time - hitTime)<=500) {
            score.update(HitResult.GREAT);
            return HitResult.GREAT;
        }
        if(Math.abs(time - hitTime)<=1000){
            score.update(HitResult.OK);
            return HitResult.OK;
        }
        return HitResult.NONE;
    }

    public int getScore() {
        return score.getScore();
    }
    public int getHealth(){
        return health.getHealth();
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
}
