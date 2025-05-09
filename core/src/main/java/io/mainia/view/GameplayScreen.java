package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.mainia.Mainia;
import io.mainia.model.HitNote;
import io.mainia.model.Level;
import io.mainia.model.Note;
import io.mainia.viewmodel.GameplayViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GameplayScreen implements Screen {

    private final Mainia game;
    private final GameplayViewModel gameplayViewModel;
    public final Texture noteTexture;
    private final Texture perfectWindowTexture;
    private final Texture columnTexture;
    private float currentTime;
    private float startTime = -3000;
    public float columnWidth;
    private final List<Integer> keymap;
    private final Music music;

    public GameplayScreen(final Mainia game, final Level level, final List<Integer> keymap) {
        this.game = game;
        gameplayViewModel = new GameplayViewModel(level, this, startTime);
        noteTexture  = new Texture("hit_note.png");
        perfectWindowTexture = new Texture("perfect_window.png");
        columnTexture = new Texture("column.png");
        columnWidth = 0.75f;
        this.keymap = keymap;
        music = Gdx.audio.newMusic(Gdx.files.internal("music/"+gameplayViewModel.getLevel().getMusicFilename()));
        music.setLooping(false);
        currentTime = startTime;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.ROYAL);
        currentTime+=1000*delta;
        if(currentTime >= 0 && currentTime < 1000*delta) music.play();
        int columnCount = gameplayViewModel.getColumnCount();
        gameplayViewModel.update(currentTime);

        if(gameplayViewModel.getHealth() <= 0) fail();
        if(currentTime>=gameplayViewModel.getLevel().getLength()*1000) win();

        float worldHeight = game.getViewport().getWorldHeight();
        float worldWidth = game.getViewport().getWorldWidth();
        game.getViewport().apply();
        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);

        game.getBatch().begin();
        for(int i=0; i<columnCount; i++) {
            game.getBatch().draw(columnTexture, worldWidth/2 - columnWidth*columnCount/2 + i*columnWidth, 0, columnWidth, worldHeight);
        }
        for(Array<Sprite> arr : gameplayViewModel.getNoteSprites()) {
            for (Sprite sprite : arr) {
                sprite.translateY(-gameplayViewModel.getSpeed()*delta);
                sprite.draw(game.getBatch());
            }
        }
        for(Sprite sprite : gameplayViewModel.getMissed()) {
            sprite.translateY(-gameplayViewModel.getSpeed()*delta);
            sprite.draw(game.getBatch());
        }
        game.getBatch().draw(perfectWindowTexture, worldWidth/2 - columnWidth*columnCount/2, 2, columnWidth*columnCount, 0.25f);
        game.getFont().draw(game.getBatch(), "Score:"+gameplayViewModel.getScore().getScore(), 1,9);
        game.getFont().draw(game.getBatch(), "Health remaining:"+gameplayViewModel.getHealth(), 1, 8);
        game.getBatch().end();
        for(int i = 0; i < keymap.size(); i++) {
            if (Gdx.input.isKeyJustPressed(keymap.get(i))) {gameplayViewModel.onPressUpdate(i,currentTime);}
        }
    }

    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        noteTexture.dispose();
        perfectWindowTexture.dispose();
        columnTexture.dispose();
        music.dispose();
    }

    public Viewport getViewPort(){
        return game.getViewport();
    }

    private void fail() {
        game.setScreen(new FailScreen(game, gameplayViewModel.getLevel(), keymap));
        dispose();
    }

    private void win(){
        game.setScreen(new WinScreen(game, gameplayViewModel.getLevel(), keymap, gameplayViewModel.getScore()));
        dispose();
    }
}
