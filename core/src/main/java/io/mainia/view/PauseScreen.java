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
    private float volume;

    public PauseScreen(Mainia game, GameplayViewModel gameplayViewModel, List<Integer> keymap, float stopTime, float customOffset, float volume) {
        this.game = game;
        stage = new Stage(game.getViewport());
        this.gameplayViewModel = gameplayViewModel;
        this.keymap = keymap;
        this.stopTime = stopTime;
        this.timeAfterPause = 1100;
        this.customOffset = customOffset;
        this.volume = volume;
    }

    @Override
    public void show() {
        Texture up = new Texture(Gdx.files.internal("buttons/tryagain_neutral.png"));
        //Texture down = new Texture(Gdx.files.internal("button_textures/custom_image_down.png"));

        Drawable upp = new TextureRegionDrawable(new TextureRegion(up));
        Drawable downn = new TextureRegionDrawable(new TextureRegion(up));
        ImageButton button = new ImageButton(upp,downn);
        button.setSize(3,2);
        button.setPosition(8,5, Align.center);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameplayScreen(game, new GameplayViewModel(gameplayViewModel.getLevel()), keymap, gameplayViewModel.getLevel().startTime(), customOffset, 0, -gameplayViewModel.getLevel().startTime(), volume));
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
        button2.setPosition(8,7, Align.center);
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game));
                dispose();
            }
        });

        Texture down_volume = new Texture(Gdx.files.internal("buttons/minus.png"));
        Texture up_volume = new Texture(Gdx.files.internal("buttons/plus.png"));
        Drawable upp1_volume = new TextureRegionDrawable(new TextureRegion(up_volume));
        Drawable downn1_volume = new TextureRegionDrawable(new TextureRegion(down_volume));

        ImageButton button_add_volume = new ImageButton(downn1_volume, downn1_volume);
        button_add_volume.setSize(0.7f,0.7f);
        button_add_volume.setPosition(7,3, Align.center);
        button_add_volume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(volume>=0.099f)volume-=0.1f;
            }
        });
        ImageButton button_decrease_volume = new ImageButton(upp1_volume, upp1_volume);
        button_decrease_volume.setSize(0.7f,0.7f);
        button_decrease_volume.setPosition(10,3, Align.center);
        button_decrease_volume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(volume<=0.901f)volume+=0.1f;
            }
        });

        stage.addActor(button_add_volume);
        stage.addActor(button_decrease_volume);
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
        game.getFont().draw(game.getBatch(), "Volume :", 5, 3.4f);
        game.getFont().draw(game.getBatch(), String.format("%.0f", (int) 100*volume) + "%", 8.3f, 3.4f);
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
            game.setScreen(new GameplayScreen(game, gameplayViewModel, keymap, stopTime-timeAfterPause, customOffset, stopTime, timeAfterPause, volume));
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