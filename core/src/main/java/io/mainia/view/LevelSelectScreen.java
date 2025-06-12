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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.mainia.Mainia;
import io.mainia.model.Level;
import io.mainia.model.Modifier;
import io.mainia.model.Result;
import io.mainia.services.KeymapReader;
import io.mainia.services.LevelFileReader;
import io.mainia.services.ResultsReader;

import java.io.File;
import java.util.*;


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

    Map<ImageButton, Modifier> button_modifiers = new HashMap<>();
    ArrayList<ImageButton> all_buttons = new ArrayList<>();
    Map<ImageButton, ArrayList<ImageButton>> restrictions = new HashMap<>();

    private HashSet<Modifier> modifiers = new HashSet<Modifier>();

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
        style.fontColor = Color.BLUE;

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
        //selectBoxStyle.scrollStyle.background = new TextureRegionDrawable(new Texture(listBackgroundTexturePath));



        //actual selectbox
        selectBox = new SelectBox<>(selectBoxStyle);
        selectBox.setPosition(4.5f,4);
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
        Texture up = new Texture(Gdx.files.internal("buttons/play_button.png"));

        Drawable upp = new TextureRegionDrawable(new TextureRegion(up));
        ImageButton button = new ImageButton(upp, upp);
        button.setSize(3,1.5f);
        button.setPosition(4.5f,2.5f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String levelPath = levelFilesPath + selectBox.getSelected() + levelExtension;
                String resultPath = resultsPath + selectBox.getSelected() + resultExtension;
                for(ImageButton b : all_buttons) {
                    if(b.isChecked()) {
                        modifiers.add(button_modifiers.get(b));
                    }
                }
                for(Modifier m : modifiers)System.out.println(m);
                levelFileReader = new LevelFileReader(levelPath,resultPath, modifiers);
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

        //BUTTONS FOR MODIFIERS!!
        ImageButton.ImageButtonStyle nofail_style = new ImageButton.ImageButtonStyle();
        nofail_style.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/no_fail_unselected.png"))));
        nofail_style.checked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/no_fail_selected.png"))));
        ImageButton button_nofail = new ImageButton(nofail_style);
        button_nofail.setSize(3,1.5f);
        button_nofail.setPosition(12.5f,8, Align.center);
        button_modifiers.put(button_nofail, Modifier.NOFAIL);
        all_buttons.add(button_nofail);

        ImageButton.ImageButtonStyle nomiss_style = new ImageButton.ImageButtonStyle();
        nomiss_style.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/no_miss_unselected.png"))));
        nomiss_style.checked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/no_miss_selected.png"))));
        ImageButton button_nomiss = new ImageButton(nomiss_style);
        button_nomiss.setSize(3,1.5f);
        button_nomiss.setPosition(12.5f,6, Align.center);
        button_modifiers.put(button_nomiss, Modifier.NOMISS);
        all_buttons.add(button_nomiss);

        ImageButton.ImageButtonStyle perfect_style = new ImageButton.ImageButtonStyle();
        perfect_style.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/perfect_unselected.png"))));
        perfect_style.checked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("buttons/perfect_selected.png"))));
        ImageButton button_perfect = new ImageButton(perfect_style);
        button_perfect.setSize(3,1.5f);
        button_perfect.setPosition(12.5f,4, Align.center);
        button_modifiers.put(button_perfect, Modifier.PERFECT);
        all_buttons.add(button_perfect);

        restrictions.put(button_nofail, new ArrayList<>(Arrays.asList(button_nomiss, button_perfect)));
        restrictions.put(button_nomiss, new ArrayList<>(Arrays.asList(button_nofail)));
        restrictions.put(button_perfect, new ArrayList<>(Arrays.asList(button_nofail)));

        for(ImageButton b : all_buttons){
            stage.addActor(b);
        }

        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ImageButton clicked = (ImageButton) event.getListenerActor();
                boolean new_state = clicked.isChecked();
                System.out.println(new_state);
                if(new_state){
                    for(ImageButton b : restrictions.get(clicked)){
                        b.setChecked(false);
                    }
                }

                clicked.setChecked(new_state);
            }
        };
        for(ImageButton b : all_buttons){
            b.addListener(listener);
        }


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
                4.5f, 9f);
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
