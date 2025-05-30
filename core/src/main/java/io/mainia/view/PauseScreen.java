package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.mainia.Mainia;
import io.mainia.viewmodel.GameplayViewModel;

import java.util.List;

public class PauseScreen implements Screen {

    private final Mainia game;
    private final Stage stage;
    private final GameplayViewModel gameplayViewModel;
    private final List<Integer> keymap;

    //stopTime - czas w grze w którym następiło zatrzymanie gry, timeAfterPause - offset po wznowieniu
    private final float stopTime;
    private final float timeAfterPause;

    public PauseScreen(Mainia game, GameplayViewModel gameplayViewModel, List<Integer> keymap, float stopTime) {
        this.game = game;
        stage = new Stage(game.getViewport());
        this.gameplayViewModel = gameplayViewModel;
        this.keymap = keymap;
        this.stopTime = stopTime;
        this.timeAfterPause = 1000;
    }

    @Override
    public void show() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();
        style.fontColor = Color.WHITE;
        TextButton button = new TextButton("Quit and retry this level", style);
        button.setSize(1,1);
        button.setPosition(5,4);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameplayScreen(game, new GameplayViewModel(gameplayViewModel.getLevel()), keymap, gameplayViewModel.getLevel().startTime, -gameplayViewModel.getLevel().startTime, 0));
                dispose();
            }
        });
        Gdx.input.setInputProcessor(stage);
        stage.addActor(button);
        TextButton button2 = new TextButton("Quit and change level", style);
        button2.setSize(1,1);
        button2.setPosition(5,3);
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
        ScreenUtils.clear(Color.valueOf("006400"));
        stage.act(delta);
        stage.draw();
        game.getBatch().begin();
        game.getFont().draw(game.getBatch(),
                "You paused the game! Press Esc to resume.",
                1, 9);
        game.getBatch().end();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            //najpierw trzeba node'y poprzesuwać znwou w góre(tylko te które jeszcze nie były kliknięte!!!) - żeby dalej mogły być poprawnie kliknięte, działa cała logika
            for(int i = 0; i < gameplayViewModel.getColumnCount(); i++){
                for(Sprite s : gameplayViewModel.getNoteSprites().get(i)){
                    float y = s.getY();
                    s.setY(y + gameplayViewModel.getLevel().speed()*timeAfterPause/1000);
                }
            }
            for(Sprite s : gameplayViewModel.getMissed()){
                float y = s.getY();
                s.setY(y + gameplayViewModel.getLevel().speed()*timeAfterPause/1000);
            }
            game.setScreen(new GameplayScreen(game, gameplayViewModel, keymap, stopTime-timeAfterPause, timeAfterPause, stopTime));
        }
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