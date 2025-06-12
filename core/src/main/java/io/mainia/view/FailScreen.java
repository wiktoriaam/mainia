package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import io.mainia.Mainia;
import io.mainia.model.Level;
import io.mainia.viewmodel.GameplayViewModel;

import java.util.List;

public class FailScreen implements Screen {

    private final Mainia game;
    private final Stage stage;
    private final Level level;
    private final List<Integer> keymap;
    private final float customOffset;
    private final float volume;

    public FailScreen(Mainia game, Level level, List<Integer> keymap, float customOffset, float volume) {
        this.game = game;
        stage = new Stage(game.getViewport());
        this.level = level;
        this.keymap = keymap;
        this.customOffset = customOffset;
        this.volume = volume;
    }

    @Override
    public void show() {
        Texture up = new Texture(Gdx.files.internal("buttons/tryagain_lose.png"));
        //Texture down = new Texture(Gdx.files.internal("button_textures/custom_image_down.png"));

        Drawable upp = new TextureRegionDrawable(new TextureRegion(up));
        Drawable downn = new TextureRegionDrawable(new TextureRegion(up));
        ImageButton button = new ImageButton(upp,downn);
        button.setSize(3,2);
        button.setPosition(8,6, Align.center);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameplayScreen(game, new GameplayViewModel(level), keymap, level.startTime(), customOffset, 0, -level.startTime(), volume));
                dispose();
            }
        });
        Gdx.input.setInputProcessor(stage);
        stage.addActor(button);
        Texture up1 = new Texture(Gdx.files.internal("buttons/select_another.png"));
        //Texture down = new Texture(Gdx.files.internal("button_textures/custom_image_down.png"));

        Drawable upp1 = new TextureRegionDrawable(new TextureRegion(up1));
        Drawable downn1 = new TextureRegionDrawable(new TextureRegion(up1));
        ImageButton button2 = new ImageButton(upp1,downn1);
        button2.setSize(3,2);
        button2.setPosition(8,4, Align.center);
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
                dispose();
            }
        });
        stage.addActor(button2);
    }

    @Override
    public void render(float delta) {
        Color color = Color.valueOf("#a01c1c");
        ScreenUtils.clear(color.r, color.g, color.b, color.a);
        stage.act(delta);
        stage.draw();
        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), "You lost the game!", 6.7f, 9);
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
        stage.dispose();
    }
}
