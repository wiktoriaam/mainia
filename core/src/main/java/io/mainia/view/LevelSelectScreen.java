package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.mainia.Mainia;
import io.mainia.model.Level;
import io.mainia.services.KeymapReader;
import io.mainia.services.LevelFileReader;


public class LevelSelectScreen implements Screen {
    private static final String selectBoxTexturePath = "hit_note.png";
    private static final String listBackgroundTexturePath = "hit_note.png";
    private static final String levelFilesPath = "levelfiles/";
    private static final String levelExtension = ".mainiabm";

    final Mainia game;
    private final Stage stage;
    private LevelFileReader levelFileReader;
    private KeymapReader keymapReader;
    private SelectBox<String> selectBox;

    public LevelSelectScreen(final Mainia game) {
        this.game = game;
        stage = new Stage(game.getViewport());
    }

    @Override
    public void show() {
        //styles for selectbox and button
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();
        style.fontColor = Color.WHITE;

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = game.getFont();
        selectBoxStyle.fontColor = Color.WHITE;
        //selectBoxStyle.background = new TextureRegionDrawable(new Texture("hit_note.png"));
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = game.getFont();
        listStyle.fontColorSelected = Color.BLUE;
        listStyle.fontColorUnselected = Color.RED;
        listStyle.selection = new TextureRegionDrawable(new Texture(selectBoxTexturePath));
        selectBoxStyle.listStyle = listStyle;
        selectBoxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle();
        selectBoxStyle.scrollStyle.background = new TextureRegionDrawable(new Texture(listBackgroundTexturePath));



        //actual selectbox
        selectBox = new SelectBox<>(selectBoxStyle);
        selectBox.setAlignment(Align.center);
        selectBox.setPosition(3.5f,7);
        selectBox.setSize(3,2);
        FileHandle levels = Gdx.files.internal(levelFilesPath);
        Array<String> levelNames = new Array<>();
        for(FileHandle level : levels.list()) {levelNames.add(level.nameWithoutExtension());}
        selectBox.setItems(levelNames);

        //actual button
        TextButton button = new TextButton("Play", style);
        button.setSize(1,1);
        button.setPosition(5,5, Align.center);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String levelPath = levelFilesPath + selectBox.getSelected() + levelExtension;
                levelFileReader = new LevelFileReader(levelPath);
                keymapReader = new KeymapReader();
                try {
                    Level level = levelFileReader.readLevel();
                    game.setScreen(new GameplayScreen(game, level, keymapReader.readKeymap(level.getColumnCount())));
                    dispose();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(Gdx.files.internal("levelfiles/poziom.mainiabm").file().getAbsolutePath());
                }
            }
        });

        Gdx.input.setInputProcessor(stage);
        stage.addActor(selectBox);
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
