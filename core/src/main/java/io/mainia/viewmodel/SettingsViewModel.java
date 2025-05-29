package io.mainia.viewmodel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputProcessor;
import io.mainia.services.SettingsModifier;

import java.io.IOException;

public class SettingsViewModel{
    SettingsModifier settingsModifier = new SettingsModifier();
    public void change_key(int columnCount, int columnNr, int keycode) throws IOException {
        settingsModifier.modify(columnCount, columnNr, keycode);
    }
}
