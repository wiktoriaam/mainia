package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private final float customOffset;

    public PauseScreen(Mainia game, GameplayViewModel gameplayViewModel, List<Integer> keymap, float stopTime, float customOffset) {
        this.game = game;
        stage = new Stage(game.getViewport());
        this.gameplayViewModel = gameplayViewModel;
        this.keymap = keymap;
        this.stopTime = stopTime;
        this.timeAfterPause = 1100;
        this.customOffset = customOffset;
    }

    @Override
    public void show() {
        Texture up = new Texture(Gdx.files.internal("tryagain_neutral.png"));
        //Texture down = new Texture(Gdx.files.internal("button_textures/custom_image_down.png"));

        Drawable upp = new TextureRegionDrawable(new TextureRegion(up));
        Drawable downn = new TextureRegionDrawable(new TextureRegion(up));
        ImageButton button = new ImageButton(upp,downn);
        button.setSize(3,2);
        button.setPosition(8,4, Align.center);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameplayScreen(game, new GameplayViewModel(gameplayViewModel.getLevel()), keymap, gameplayViewModel.getLevel().startTime(), customOffset, 0, -gameplayViewModel.getLevel().startTime()));
                dispose();
            }
        });
        Gdx.input.setInputProcessor(stage);
        stage.addActor(button);
        Texture up1 = new Texture(Gdx.files.internal("select_another.png"));
        //Texture down = new Texture(Gdx.files.internal("button_textures/custom_image_down.png"));

        Drawable upp1 = new TextureRegionDrawable(new TextureRegion(up1));
        Drawable downn1 = new TextureRegionDrawable(new TextureRegion(up1));
        ImageButton button2 = new ImageButton(upp1,downn1);
        button2.setSize(3,2);
        button2.setPosition(8,6, Align.center);
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
        Color color = Color.valueOf("#6e74b2");
        ScreenUtils.clear(color.r, color.g, color.b, color.a);
        stage.act(delta);
        stage.draw();
        game.getBatch().begin();
        game.getFont().draw(game.getBatch(),
                "You paused the game! Press Esc to resume.",
                5f, 9);
        game.getBatch().end();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            //najpierw trzeba node'y poprzesuwać znwou w góre(tylko te które jeszcze nie były kliknięte!!!) - żeby dalej mogły być poprawnie kliknięte, działa cała logika
            for(int i = 0; i < gameplayViewModel.getColumnCount(); i++){
                for(Sprite s : gameplayViewModel.getNoteSprites().get(i)){
                    s.translateY(gameplayViewModel.getLevel().speed()*timeAfterPause/1000);
                }
            }
            for(Sprite s : gameplayViewModel.getSliderSprites()) {
                s.translateY(gameplayViewModel.getLevel().speed()*timeAfterPause/1000);
            }
            for(Sprite s : gameplayViewModel.getMissed()){
                s.translateY( gameplayViewModel.getLevel().speed()*timeAfterPause/1000);
            }
            game.setScreen(new GameplayScreen(game, gameplayViewModel, keymap, stopTime-timeAfterPause, customOffset, stopTime, timeAfterPause));
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