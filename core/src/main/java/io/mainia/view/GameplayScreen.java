package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
    public Texture noteTexture;
    private float currentTime = -10000;
    public float columnWidth;


    public GameplayScreen(final Mainia game, final Level level) {
        this.game = game;
        gameplayViewModel = new GameplayViewModel(level, this);
        noteTexture  = new Texture("hit_note.png");
        columnWidth = 0.75f;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.FIREBRICK);
        currentTime+=1000*delta;
        gameplayViewModel.update(currentTime);
        game.getViewport().apply();
        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);

        game.getBatch().begin();
        for(Array<Sprite> arr : gameplayViewModel.getNoteSprites()) {
            for (Sprite sprite : arr) {
                sprite.translateY(-gameplayViewModel.getSpeed()*delta);
                sprite.draw(game.getBatch());
            }
        }
        game.getBatch().end();

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

    }
    public Viewport getViewPort(){
        return game.getViewport();
    }
}
