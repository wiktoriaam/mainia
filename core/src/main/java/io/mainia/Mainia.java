package io.mainia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import io.mainia.view.MainMenuScreen;

import static com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Mainia extends Game {
    public final static float worldWidth = 16;
    public final static float worldHeight = 10;

    private FitViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        viewport = new FitViewport(worldWidth,worldHeight);
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Linear, Linear);
        batch = new SpriteBatch();
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
}