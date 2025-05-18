package io.mainia.services;

import com.badlogic.gdx.Input;
import io.mainia.model.WrongFileFormatException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KeymapReader {
    public static final String settingsPath = "settings/settings.mainia";

    private final File file;
    public KeymapReader(){
        file = new File(settingsPath);
    }
    public KeymapReader(String filePath){
        file = new File(filePath);
    }
    public List<Integer> readKeymap(int columnCount) throws IOException {
        Scanner scanner = new Scanner(file);
        List<Integer> keymap = new ArrayList<>();
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if(line.startsWith(String.valueOf(columnCount))){
                String[] keys = line.split(" ");
                for(int i = 2; i < keys.length; i++){
                    if(keys[i].trim().equals("SPACE"))keymap.add(62);
                    else keymap.add(Input.Keys.valueOf(keys[i].trim()));
                }
                return keymap;
            }
        }
        throw new WrongFileFormatException("Keymap for columnCount " + columnCount + " not found");
    }
}
