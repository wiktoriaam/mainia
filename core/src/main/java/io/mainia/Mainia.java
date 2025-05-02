package io.mainia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.mainia.view.MainMenuScreen;
import io.mainia.viewmodel.GameViewModel;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Mainia extends Game {

    private FitViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private GameViewModel gameViewModel;

    @Override
    public void create() {
        viewport = new FitViewport(10,10);
        font = new BitmapFont();
        batch = new SpriteBatch();
        gameViewModel = new GameViewModel();
        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public FitViewport getViewport() {
        return viewport;
    }
    public SpriteBatch getBatch() {
        return batch;
    }
    public BitmapFont getFont() {
        return font;
    }
    public GameViewModel getGameViewModel() {
        return gameViewModel;
    }
}