package io.mainia.viewmodel;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import io.mainia.model.Level;
import io.mainia.model.Note;
import io.mainia.view.GameplayScreen;

import java.util.ArrayList;
import java.util.List;

public class GameplayViewModel {
    GameplayScreen gameplayScreen;
    private final Level level;
    private final List<List<Note>> notes;
    private final float speed;
    private final int columnCount;
    private ArrayList<Array<Sprite>> noteSprites;
    private int[] firstToHit;
    private int[] firstToShow;
    private int[] firstToAdd;

    public GameplayViewModel(Level level, GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.level = level;
        notes = level.getNotes();
        speed = level.getSpeed();
        columnCount = level.getColumnCount();
        noteSprites = new ArrayList<>();
        firstToHit = new int[columnCount];
        firstToShow = new int[columnCount];
        firstToAdd = new int[columnCount];
        for(int i=0; i<columnCount; i++) {
            noteSprites.add(new Array<>());
            if(notes.get(i).isEmpty()){
                firstToHit[i] = -1;
                firstToShow[i] = -1;
                firstToAdd[i] = -1;
            }
        }

    }

    public void update(float currentTime){
        for(int i = 0; i < columnCount; i++){
            while(firstToHit[i]!=-1 && notes.get(i).get(firstToHit[i]).getHitTime() + 1000/speed < currentTime){
                firstToHit[i]++;
                if(notes.get(i).size() == firstToHit[i]) firstToHit[i] = -1;
            }
            while(firstToShow[i]!=-1 && notes.get(i).get(firstToShow[i]).getHitTime() + 2000/speed < currentTime){
                firstToShow[i]++;
                if(notes.get(i).size() == firstToShow[i]) firstToShow[i] = -1;
                noteSprites.get(i).removeIndex(0);
            }
            while(firstToAdd[i]!=-1 && notes.get(i).get(firstToAdd[i]).getHitTime() - 8000/speed < currentTime){
                firstToAdd[i]++;
                if(notes.get(i).size() == firstToAdd[i]) firstToAdd[i] = -1;
                Sprite sprite = new Sprite(gameplayScreen.noteTexture);
                sprite.setSize(gameplayScreen.columnWidth, gameplayScreen.columnWidth/4);
                if(columnCount%2 == 1){
                    sprite.setCenterX(gameplayScreen.getViewPort().getWorldWidth()/2 + gameplayScreen.columnWidth*(i-columnCount/2));
                }
                else{
                    sprite.setX(gameplayScreen.getViewPort().getWorldWidth()/2 + gameplayScreen.columnWidth*(i-columnCount/2));
                }
                sprite.setY(gameplayScreen.getViewPort().getWorldHeight());
                noteSprites.get(i).add(sprite);
            }
        }
    }

    public List<List<Note>> getNotes() {
        return notes;
    }
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
