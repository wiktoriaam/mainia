package io.mainia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.mainia.Mainia;
import io.mainia.model.Level;
import io.mainia.viewmodel.GameplayViewModel;

import java.util.List;

public class GameplayScreen implements Screen {

    public final static String noteTexturePath = "hit_note.png";
    public final static String perfectWindowTexturePath = "perfect_window.png";
    public final static String columnTexturePath = "column.png";
    public final static String musicPath = "music/";
    public final static float columnWidth = 0.75f;
    public static float worldHeight;
    public static float worldWidth;

    //czas w ms poczatkowy ustawiany w konstruktorze(zwykle ujemny, znajduje sie teraz w pliku z levelem)
    public final float startTime;
    //czas w ms po ktorym muzyka się zaczyna(dodatni!!)
    public final float musicStartTime;
    //moment pliku dzwiekowaego w ktorym zaczyna grac muzyka, ms
    public final float musicTimeStamp;
    private final Mainia game;
    private final GameplayViewModel gameplayViewModel;
    public final Texture noteTexture;
    private final Texture perfectWindowTexture;
    private final Texture columnTexture;
    private float currentTime;
    private final List<Integer> keymap;
    private final Music music;

    public GameplayScreen(final Mainia game, final GameplayViewModel gameplayViewModel, final List<Integer> keymap, float startTime, float musicStartTime, float musicTimeStamp) {
        //gra
        this.game = game;
        this.gameplayViewModel = gameplayViewModel;
        this.keymap = keymap;
        this.startTime = startTime;
        currentTime = startTime;
        for(int i : keymap) {System.out.println(i);}
        System.out.println(Input.Keys.SPACE);

        //grafika i audio
        this.musicStartTime = musicStartTime;
        this.musicTimeStamp = musicTimeStamp;
        worldHeight = game.getViewport().getWorldHeight();
        worldWidth = game.getViewport().getWorldWidth();
        noteTexture  = new Texture(noteTexturePath);
        perfectWindowTexture = new Texture(perfectWindowTexturePath);
        columnTexture = new Texture(columnTexturePath);
        music = Gdx.audio.newMusic(Gdx.files.internal(musicPath+gameplayViewModel.getLevel().getMusicFilename()));
        music.setLooping(false);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.ROYAL);
        currentTime+=1000*delta;
        //uogólniony moment rozpoczecia muzyki - zeby po wznowieniu gry w odpowiednim momencie pliku dzwiekowego startowała
        if(currentTime >= startTime+musicStartTime && currentTime < startTime + musicStartTime + 1000*delta) {
            music.play();
            music.setPosition(musicTimeStamp/1000);//ustawia muzyke na konkretny moment ściezki dzwiekowej
        }
        int columnCount = gameplayViewModel.getColumnCount();
        //update aktualnie wyswietlanych node'ow
        gameplayViewModel.update(currentTime, noteTexture, worldWidth, worldHeight, columnWidth);

        if(gameplayViewModel.getHealth() <= 0) fail();
        if(currentTime>=gameplayViewModel.getLevel().getLength()*1000) win();


        //CAŁE WYŚWIETLANIE PONIZEJ:
        game.getViewport().apply();
        game.getBatch().setProjectionMatrix(game.getViewport().getCamera().combined);

        //wyswietlanie node'ow
        game.getBatch().begin();
        for(int i=0; i<columnCount; i++) {
            game.getBatch().draw(columnTexture, worldWidth/2 - columnWidth*columnCount/2 + i*columnWidth, 0, columnWidth, worldHeight);
        }
        for(Array<Sprite> arr : gameplayViewModel.getNoteSprites()) {
            for (Sprite sprite : arr) {
                sprite.translateY(-gameplayViewModel.getSpeed()*delta);
                sprite.draw(game.getBatch());
            }
        }
        for(Sprite sprite : gameplayViewModel.getMissed()) {
            sprite.translateY(-gameplayViewModel.getSpeed()*delta);
            sprite.draw(game.getBatch());
        }
        game.getBatch().draw(perfectWindowTexture, worldWidth/2 - columnWidth*columnCount/2, 2, columnWidth*columnCount, 0.25f);
        game.getFont().draw(game.getBatch(), "Score:"+gameplayViewModel.getScore().getScore(), 0,9);
        game.getFont().draw(game.getBatch(), "Health remaining:"+gameplayViewModel.getHealth(), 0, 8);
        game.getBatch().end();
        //KONIEC WYŚWIETLANIA

        //onPressUpdate w momencie kliknięcia(tylko jesli juz mozna klikac - czyli jak juz minie mucisStartTime
        if(currentTime-startTime>=musicStartTime) {
            for (int i = 0; i < keymap.size(); i++) {
                if (Gdx.input.isKeyJustPressed(keymap.get(i))) {
                    gameplayViewModel.onPressUpdate(i, currentTime);
                }
            }
        }

        //sprawdzenie czy wciśnięto Esc - jesli tak to PauseScreen
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && currentTime-startTime>=musicStartTime) {
            music.pause();
            game.setScreen(new PauseScreen(game, gameplayViewModel, keymap, currentTime));
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
        noteTexture.dispose();
        perfectWindowTexture.dispose();
        columnTexture.dispose();
        music.dispose();
    }

    public Viewport getViewPort(){
        return game.getViewport();
    }

    private void fail() {
        game.setScreen(new FailScreen(game, gameplayViewModel.getLevel(), keymap));
        dispose();
    }

    private void win(){
        game.setScreen(new WinScreen(game, gameplayViewModel.getLevel(), keymap, gameplayViewModel.getScore()));
        dispose();
    }
}
