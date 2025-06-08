package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import io.mainia.Mainia;

public class  MainMenuScreen implements Screen {

    final Mainia game;
    private Stage stage;

    public MainMenuScreen(final Mainia game) {
        this.game = game;
        stage = new Stage(game.getViewport());
    }

    @Override
    public void show() {
        Texture title = new Texture(Gdx.files.internal("backgrounds/mainscreen_title.png"));
        Image titleImage = new Image(title);
        titleImage.setSize(10,7);
        titleImage.setPosition(3,2);
        stage.addActor(titleImage);

        Texture up = new Texture(Gdx.files.internal("backgrounds/custom_image_up.png"));
        //Texture down = new Texture(Gdx.files.internal("button_textures/custom_image_down.png"));

        Drawable upp = new TextureRegionDrawable(new TextureRegion(up));
        Drawable downn = new TextureRegionDrawable(new TextureRegion(up));
        ImageButton button = new ImageButton(upp, downn);
        button.setSize(3,2);
        button.setPosition(6.5f,4f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
                dispose();
            }
        });
        Gdx.input.setInputProcessor(stage);
        stage.addActor(button);
    }

    @Override
    public void render(float delta) {
        Color color = Color.valueOf("#6e74b2");
        ScreenUtils.clear(color.r, color.g, color.b, color.a);
        game.getViewport().apply();
        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);
        //startButton.setPosition(3,3);
        //startButton.draw(game.getBatch(), 1);
        stage.act(delta);
        stage.draw();
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
