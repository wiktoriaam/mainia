package io.mainia.viewmodel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import io.mainia.model.*;

import java.util.ArrayList;
import java.util.List;

import static io.mainia.Mainia.worldHeight;
import static io.mainia.Mainia.worldWidth;
import static io.mainia.view.GameplayScreen.columnWidth;

public class GameplayViewModel {
    //level info
    private final Level level;
    private final List<List<Note>> notes;
    private final float speed;
    private final int columnCount;
    //sprites(temporary) TODO: switch location of the sprites array to view
    private ArrayList<Array<Sprite>> noteSprites = new ArrayList<>();
    private Array<Sprite> missedSprites = new Array<>();
    private int[] firstToHit;
    private int[] firstToAdd;
    private final Score score = new Score();
    private final float startingTime;
    private final Health health;
    private final float perfectHitHeight;

    public GameplayViewModel(Level level) {
        this.level = level;
        this.startingTime = level.startTime();
        notes = level.notes();
        speed = level.speed();
        columnCount = level.columnCount();
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

    public void update(float currentTime, Texture noteTexture, Texture sliderStartTexture, Texture sliderMiddleTexture, Texture sliderEndTexture) {
        for(int i = 0; i < columnCount; i++){
            //missed notes
            while(firstToHit[i]!=-1 && notes.get(i).get(firstToHit[i]).hitTime() + 1000/speed < currentTime){
                firstToHit[i]++;
                missedSprites.add(noteSprites.get(i).get(0));
                noteSprites.get(i).removeIndex(0);
                score.missedUpdate();
                health.updateHealth(HitResult.MISS);
                if(notes.get(i).size() == firstToHit[i]) firstToHit[i] = -1;
            }
            NoteSpriteBuilder noteSpriteBuilder = new NoteSpriteBuilder(noteTexture, sliderStartTexture, sliderMiddleTexture, sliderEndTexture, columnWidth, level);
            while(firstToAdd[i]!=-1 && notes.get(i).get(firstToAdd[i]).hitTime() - (1-perfectHitHeight)*worldHeight*1000/speed < currentTime){
                Note note = notes.get(i).get(firstToAdd[i]);
//                Sprite sprite = new Sprite(noteTexture);
//                sprite.setSize(columnWidth, columnWidth/4);
//                sprite.setX(worldWidth/2 + columnWidth*(i-(float)columnCount/2));
//                if((notes.get(i).get(firstToAdd[i]).hitTime() - startingTime) - (1-perfectHitHeight)*worldHeight*1000/speed <= 0)
//                    sprite.setY(perfectHitHeight*worldHeight+(notes.get(i).get(firstToAdd[i]).hitTime() - startingTime)*speed/1000);
//                else sprite.setY(worldHeight);
                Sprite sprite = null;
                if(note instanceof HitNote hit) {
                    sprite = noteSpriteBuilder.buildHit(hit, i, currentTime);
                }
                noteSprites.get(i).add(sprite);
                firstToAdd[i]++;
                if(notes.get(i).size() == firstToAdd[i]) firstToAdd[i] = -1;
            }
        }
        for(int i=missedSprites.size-1; i>=0; i--){
            if(missedSprites.get(i).getY()<0)missedSprites.removeIndex(i);
        }
    }

    public void onPressUpdate(int column, float time){
        if(firstToHit[column] == -1){return;}
        HitResult result = notes.get(column).get(firstToHit[column]).hitCheck(time);
        health.updateHealth(result);
        score.update(result);
        if(result != HitResult.NONE) {
            if (firstToHit[column] == notes.get(column).size() - 1)
                firstToHit[column] = -1;
            else firstToHit[column]++;
            noteSprites.get(column).removeIndex(0);
        }
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
}
