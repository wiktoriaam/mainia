package io.mainia.view;

import com.badlogic.gdx.Screen;
import io.mainia.Mainia;
import io.mainia.viewmodel.GameViewModel;

public class GameplayScreen implements Screen {

    final Mainia game;
    private GameViewModel gameViewModel;

    public GameplayScreen(final Mainia game) {
        this.game = game;
        gameViewModel = game.getGameViewModel();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

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
