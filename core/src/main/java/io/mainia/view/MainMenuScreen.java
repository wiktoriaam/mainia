package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();
        style.fontColor = Color.WHITE;
        TextButton button = new TextButton("Start game", style);
        button.setSize(1,1);
        button.setPosition(5,5);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
            }
        });
        Gdx.input.setInputProcessor(stage);
        stage.addActor(button);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLUE);
        game.getViewport().apply();
        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);
        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), "Mainia menu", 1, 1);
        game.getBatch().end();
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

    }
}
