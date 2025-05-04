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
import io.mainia.services.LevelFileReader;

public class LevelSelectScreen implements Screen {

    final Mainia game;
    private Stage stage;
    private LevelFileReader levelFileReader;

    public LevelSelectScreen(final Mainia game) {
        this.game = game;
        stage = new Stage(game.getViewport());
    }

    @Override
    public void show() {

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();
        style.fontColor = Color.WHITE;
        TextButton button = new TextButton("Default level", style);
        button.setSize(1,1);
        button.setPosition(5,5);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelFileReader = new LevelFileReader("levelfiles/poziom.mainiabm");
                try {
                    game.setScreen(new GameplayScreen(game, levelFileReader.readLevel()));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(Gdx.files.internal("levelfiles/poziom.mainiabm").file().getAbsolutePath());
                }
            }
        });
        Gdx.input.setInputProcessor(stage);
        stage.addActor(button);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(v);
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
