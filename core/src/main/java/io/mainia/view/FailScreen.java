package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

    public FailScreen(Mainia game, Level level, List<Integer> keymap) {
        this.game = game;
        stage = new Stage(game.getViewport());
        this.level = level;
        this.keymap = keymap;
    }

    @Override
    public void show() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();
        style.fontColor = Color.WHITE;
        TextButton button = new TextButton("Try again", style);
        button.setSize(1,1);
        button.setPosition(5,5);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameplayScreen(game, new GameplayViewModel(level), keymap, level.startTime(), -level.startTime(), 0));
                dispose();
            }
        });
        Gdx.input.setInputProcessor(stage);
        stage.addActor(button);
        TextButton button2 = new TextButton("Select another level", style);
        button2.setSize(1,1);
        button2.setPosition(5,4);
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
        ScreenUtils.clear(Color.RED);
        stage.act(delta);
        stage.draw();
        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), "You lost the game", 5, 9);
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
