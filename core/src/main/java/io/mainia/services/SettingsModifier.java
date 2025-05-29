package io.mainia.services;

import com.badlogic.gdx.Input;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SettingsModifier {
    public static final String settingsPath = "settings/settings.mainia";
    public File file;
    public SettingsModifier() {
        file = new File(settingsPath);
    }

    //columnCount zaczyna sie od 1
    public void modify(int columnCount, int columnNr, int newKeycode) throws IOException {
        List<String> lines = new ArrayList<String>();
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
        String changedLineKeys[] = lines.get(columnCount).split(" ");
        StringBuilder changedKeys = new StringBuilder();
        for(int i = 0; i < changedLineKeys.length; i++) {
            if(i == columnNr + 2) {
                changedKeys.append(Input.Keys.toString(newKeycode).toUpperCase());
            }
            else changedKeys.append(changedLineKeys[i]);
            changedKeys.append(" ");
        }
        lines.set(columnCount, changedKeys.toString());
        FileWriter fileWriter = new FileWriter(file, false);
        for(String line : lines) {
            fileWriter.write(line + "\n");
        }
        fileWriter.close();






    }
}
