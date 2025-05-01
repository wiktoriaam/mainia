package java.mainia;

import com.badlogic.gdx.Game;

import java.mainia.views.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Mainia extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
