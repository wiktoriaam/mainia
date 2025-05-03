package io.mainia.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import io.mainia.Mainia;
import io.mainia.viewmodel.GameViewModel;

public class LevelSelectScreen implements Screen {

    final Mainia game;
    private GameViewModel gameViewModel;

    public LevelSelectScreen(final Mainia game) {
        this.game = game;
        gameViewModel = game.getGameViewModel();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BLACK);
    }

    @Override
    public void resize(int i, int i1) {

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
}
