package io.mainia.viewmodel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import io.mainia.model.*;
import io.mainia.view.GameplayScreen;

import java.util.ArrayList;
import java.util.List;

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
    private float worldHeight;
    private float worldWidth;
    private float columnWidth;
    private Texture noteTexture;
    private final Health health = new StaticHealth(4) {
    };

    public GameplayViewModel(Level level, float startingTime, float worldHeight, float worldWidth, float columnWidth, Texture noteTexture) {
        this.level = level;
        this.startingTime = startingTime;
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
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;
        this.columnWidth = columnWidth;
        this.noteTexture = noteTexture;
    }

    public void update(float currentTime){
        for(int i = 0; i < columnCount; i++){
            //missed notes
            while(firstToHit[i]!=-1 && notes.get(i).get(firstToHit[i]).getHitTime() + 1000/speed < currentTime){
                firstToHit[i]++;
                missedSprites.add(noteSprites.get(i).get(0));
                noteSprites.get(i).removeIndex(0);
                score.missedUpdate();
                health.decreaseHealth();
                if(notes.get(i).size() == firstToHit[i]) firstToHit[i] = -1;
            }
            while(firstToAdd[i]!=-1 && notes.get(i).get(firstToAdd[i]).getHitTime() - 8000/speed < currentTime){
                Sprite sprite = new Sprite(noteTexture);
                sprite.setSize(columnWidth, columnWidth/4);
                sprite.setX(worldWidth/2 + columnWidth*(i-(float)columnCount/2));
                if((notes.get(i).get(firstToAdd[i]).getHitTime() - startingTime) - 8000/speed <= 0)
                    sprite.setY(0.2f*worldHeight+(notes.get(i).get(firstToAdd[i]).getHitTime() - startingTime)*speed/1000);
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

    public void onPressUpdate(int column, float time){
        if(firstToHit[column] == -1){return;}
        HitResult result = notes.get(column).get(firstToHit[column]).hitCheck(time);
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
    public Level getLevel() {
        return level;
    }
}
