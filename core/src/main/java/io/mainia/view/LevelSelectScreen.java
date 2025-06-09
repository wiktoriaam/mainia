package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.mainia.Mainia;
import io.mainia.model.Level;
import io.mainia.model.Result;
import io.mainia.services.KeymapReader;
import io.mainia.services.LevelFileReader;
import io.mainia.services.ResultsReader;

import java.io.File;
import java.util.ArrayList;


public class LevelSelectScreen implements Screen {
    private static final String selectBoxTexturePath = "hit_note.png";
    private static final String listBackgroundTexturePath = "hit_note.png";
    private static final String levelFilesPath = "levelfiles/";
    private static final String resultsPath = "results/";
    private static final String resultExtension = ".mainiasc";
    private static final String levelExtension = ".mainiabm";

    final Mainia game;
    private final Stage stage;
    private LevelFileReader levelFileReader;
    private KeymapReader keymapReader;
    private SelectBox<String> selectBox;

    private ArrayList<Result> results = new ArrayList<>();

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
        listStyle.selection = new TextureRegionDrawable(new Texture(selectBoxTexturePath));
        selectBoxStyle.listStyle = listStyle;
        selectBoxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle();
        selectBoxStyle.scrollStyle.background = new TextureRegionDrawable(new Texture(listBackgroundTexturePath));



        //actual selectbox
        selectBox = new SelectBox<>(selectBoxStyle);
        selectBox.setPosition(6.5f,4);
        selectBox.setSize(3,2);
        FileHandle levels = Gdx.files.internal(levelFilesPath);
        Array<String> levelNames = new Array<>();
        for(FileHandle level : levels.list()) {levelNames.add(level.nameWithoutExtension());}
        selectBox.getList().setStyle(listStyle);
        selectBox.getList().setWidth(4);
        selectBox.setItems(levelNames);
        selectBox.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor){
               try{
                   File plik = new File(resultsPath + selectBox.getSelected() + resultExtension);
                   if (plik.exists()) {
                       ResultsReader rr = new ResultsReader(plik);
                       results = rr.readResults();
                   }
                   else{
                       results = new ArrayList<>();
                   }
               }
               catch(Exception e) {

               }
           }
        });

        //actual button
        Texture up = new Texture(Gdx.files.internal("backgrounds/custom_image_up.png"));
        //Texture down = new Texture(Gdx.files.internal("button_textures/custom_image_down.png"));

        Drawable upp = new TextureRegionDrawable(new TextureRegion(up));
        Drawable downn = new TextureRegionDrawable(new TextureRegion(up));
        ImageButton button = new ImageButton(upp, downn);
        button.setSize(3,1.5f);
        button.setPosition(8f,3, Align.center);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String levelPath = levelFilesPath + selectBox.getSelected() + levelExtension;
                String resultPath = resultsPath + selectBox.getSelected() + resultExtension;
                levelFileReader = new LevelFileReader(levelPath,resultPath);
                try {
                    Level level = levelFileReader.readLevel();
                    game.setScreen(new SettingsScreen(game, level.columnCount(), level));
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

        try{
            File resPath = new File(resultsPath);
            resPath.mkdirs();

            File plik = new File(resultsPath + selectBox.getSelected() + resultExtension);
            if (plik.exists()) {
                ResultsReader rr = new ResultsReader(plik);
                results = rr.readResults();
            }
            else{
                results = new ArrayList<>();
            }
        }
        catch(Exception e) {

        }
    }

    @Override
    public void render(float v) {
        Color color = Color.valueOf("#6e74b2");
        ScreenUtils.clear(color.r, color.g, color.b, color.a);
        stage.act(v);
        stage.draw();
        game.getViewport().apply();
        game.getBatch().begin();
        StringBuilder scoreboard = new StringBuilder("Scoreboard:\n");
        int i=0;
        ArrayList<Integer> scores = new ArrayList<>();
        for(Result result : results) scores.add(result.score());
        scores.sort(Integer::compareTo);
        for(int j=scores.size()-1,m=1;j>= scores.size()-9 && j>=0;j--,m++) {
            scoreboard.append("#").append(m).append("    ").append(scores.get(j)).append("\n");
        }
        game.getFont().draw(game.getBatch(),
                scoreboard,
                7, 9.5f);
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
