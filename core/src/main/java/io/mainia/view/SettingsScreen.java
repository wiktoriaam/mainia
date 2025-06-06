package io.mainia.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import io.mainia.Mainia;
import io.mainia.model.Level;
import io.mainia.services.KeymapReader;
import io.mainia.viewmodel.GameplayViewModel;
import io.mainia.viewmodel.SettingsViewModel;

import java.io.IOException;
import java.util.List;

public class SettingsScreen implements Screen {
    //aby zmienic klawisz w danej kolumnie nalezy kliknąc przycisk change obok aktualnego klawisza na tą kolumne i wtedy wcisnąć dopiero
    Mainia game;
    SettingsViewModel settingsViewModel;
    Level level;
    int columnCount;//bo ekran ustawien jest po wyborze levelu
    private String[] keys;

    private final InputAdapter inputAdapter;
    InputMultiplexer inputMultiplexer = new InputMultiplexer();//zeby wczytywac input zarowno od przyciskow jak i z klawiatury
    private final Stage buttonsStage;//guziki zmienajace klawisze
    private final Stage gameButtonStage;//guzik do przejścia do gry

    private boolean canBeClicked = false;//zapobiega zeby dwa razy kliknac i zmienic
    private int columnToBeClicked = 0;

    public SettingsScreen(Mainia game, int columnCount, Level level) {
        this.game = game;
        settingsViewModel = new SettingsViewModel();
        this.columnCount = columnCount;
        this.level = level;
        buttonsStage = new Stage(game.getViewport());
        gameButtonStage = new Stage(game.getViewport());

        inputAdapter = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if(!canBeClicked) {
                    return false;
                }
                else{
                    canBeClicked = false;
                    try {
                        settingsViewModel.change_key(columnCount,columnToBeClicked,keycode);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    refreshButtons();
                    return true;
                }
            }
        };
    }

    @Override
    public void show() {
        refreshButtons();
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();
        style.fontColor = Color.WHITE;
        TextButton button = new TextButton("Play the game", style);
        button.setSize(1,1);
        button.setPosition(7,5, Align.center);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    KeymapReader keymapReader = new KeymapReader();
                    game.setScreen(new GameplayScreen(game, new GameplayViewModel(level), keymapReader.readKeymap(level.columnCount()), level.startTime(), -level.startTime(), 0));
                    dispose();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        gameButtonStage.addActor(button);


        inputMultiplexer.addProcessor(buttonsStage);
        inputMultiplexer.addProcessor(gameButtonStage);
        inputMultiplexer.addProcessor(inputAdapter);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BLACK);
        buttonsStage.act(v);
        buttonsStage.draw();
        gameButtonStage.act(v);
        gameButtonStage.draw();
    }

    @Override
    public void resize(int i, int i1) {

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
        buttonsStage.dispose();
        gameButtonStage.dispose();
    }

    //pomocnicza metoda wywoływana po każdym odświeżeniu przycisków
    public void refreshButtons(){
        buttonsStage.clear();
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();
        style.fontColor = Color.WHITE;

        //najpierw odczytanie klawiszy
        KeymapReader keymapReader = new KeymapReader();
        List<Integer> keycodes;
        try {
            keycodes = keymapReader.readKeymap(columnCount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //petla do dodawania przycisków!!
        for(int i = 0; i < columnCount; i++) {
            TextButton button = new TextButton("Column " + String.valueOf(i+1) + " : " + Input.Keys.toString(keycodes.get(i)), style);
            button.setSize(1,1);
            button.setPosition(5,9-i*0.7f, Align.right);
            int finalI = i;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    try {
                        columnToBeClicked = finalI;
                        canBeClicked = true;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            buttonsStage.addActor(button);
        }

        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
