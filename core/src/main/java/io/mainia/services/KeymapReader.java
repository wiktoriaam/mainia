package io.mainia.services;

import com.badlogic.gdx.Input;
import io.mainia.model.WrongFileFormatException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KeymapReader {
    public static final String settingsPath = "settings/settings.mainia";
    public static Map<String, Integer> map = new HashMap<>();
    static{//potrzebne zeby potem ładnie w pliku zapisywać samą nazwę klawisza i nie robić if ani switch
        map.put("ANY_KEY", Input.Keys.ANY_KEY);
        map.put("NUM_0", Input.Keys.NUM_0);
        map.put("NUM_1", Input.Keys.NUM_1);
        map.put("NUM_2", Input.Keys.NUM_2);
        map.put("NUM_3", Input.Keys.NUM_3);
        map.put("NUM_4", Input.Keys.NUM_4);
        map.put("NUM_5", Input.Keys.NUM_5);
        map.put("NUM_6", Input.Keys.NUM_6);
        map.put("NUM_7", Input.Keys.NUM_7);
        map.put("NUM_8", Input.Keys.NUM_8);
        map.put("NUM_9", Input.Keys.NUM_9);
        map.put("A", Input.Keys.A);
        map.put("B", Input.Keys.B);
        map.put("C", Input.Keys.C);
        map.put("D", Input.Keys.D);
        map.put("E", Input.Keys.E);
        map.put("F", Input.Keys.F);
        map.put("G", Input.Keys.G);
        map.put("H", Input.Keys.H);
        map.put("I", Input.Keys.I);
        map.put("J", Input.Keys.J);
        map.put("K", Input.Keys.K);
        map.put("L", Input.Keys.L);
        map.put("M", Input.Keys.M);
        map.put("N", Input.Keys.N);
        map.put("O", Input.Keys.O);
        map.put("P", Input.Keys.P);
        map.put("Q", Input.Keys.Q);
        map.put("R", Input.Keys.R);
        map.put("S", Input.Keys.S);
        map.put("T", Input.Keys.T);
        map.put("U", Input.Keys.U);
        map.put("V", Input.Keys.V);
        map.put("W", Input.Keys.W);
        map.put("X", Input.Keys.X);
        map.put("Y", Input.Keys.Y);
        map.put("Z", Input.Keys.Z);
        map.put("LEFT", Input.Keys.LEFT);
        map.put("RIGHT", Input.Keys.RIGHT);
        map.put("UP", Input.Keys.UP);
        map.put("DOWN", Input.Keys.DOWN);
        map.put("TAB", Input.Keys.TAB);
        map.put("SPACE", Input.Keys.SPACE);
        map.put("BACKSPACE", Input.Keys.BACKSPACE);
        map.put("-", Input.Keys.MINUS);
        map.put("=", Input.Keys.EQUALS);
        map.put("ENTER", Input.Keys.ENTER);
        map.put("ESCAPE", Input.Keys.ESCAPE);
        map.put("DEL", Input.Keys.DEL);
        map.put("FORWARD_DEL", Input.Keys.FORWARD_DEL);
        map.put("INSERT", Input.Keys.INSERT);
        map.put("HOME", Input.Keys.HOME);
        map.put("END", Input.Keys.END);
        map.put("PAGE_UP", Input.Keys.PAGE_UP);
        map.put("PAGE_DOWN", Input.Keys.PAGE_DOWN);
        map.put("SHIFT_LEFT", Input.Keys.SHIFT_LEFT);
        map.put("SHIFT_RIGHT", Input.Keys.SHIFT_RIGHT);
        map.put("CTRL_LEFT", Input.Keys.CONTROL_LEFT);
        map.put("CTRL_RIGHT", Input.Keys.CONTROL_RIGHT);
        map.put("ALT_LEFT", Input.Keys.ALT_LEFT);
        map.put("ALT_RIGHT", Input.Keys.ALT_RIGHT);
        map.put(";", Input.Keys.SEMICOLON);
        map.put(",", Input.Keys.COMMA);
        map.put(".", Input.Keys.PERIOD);
        map.put("/", Input.Keys.SLASH);
        map.put("'", Input.Keys.APOSTROPHE);
        map.put("\\", Input.Keys.BACKSLASH);
        map.put("(", Input.Keys.LEFT_BRACKET);
        map.put(")", Input.Keys.RIGHT_BRACKET);
        map.put("F1", Input.Keys.F1);
        map.put("F2", Input.Keys.F2);
        map.put("F3", Input.Keys.F3);
        map.put("F4", Input.Keys.F4);
        map.put("F5", Input.Keys.F5);
        map.put("F6", Input.Keys.F6);
        map.put("F7", Input.Keys.F7);
        map.put("F8", Input.Keys.F8);
        map.put("F9", Input.Keys.F9);
        map.put("F10", Input.Keys.F10);
        map.put("F11", Input.Keys.F11);
        map.put("F12", Input.Keys.F12);
    }



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
                String keys[] = line.split(" ");
                for(int i = 2; i < keys.length; i++){
                    keymap.add(map.get(keys[i]));
                }
                return keymap;
            }
        }
        throw new WrongFileFormatException("Keymap for columnCount " + columnCount + " not found");
    }
}
