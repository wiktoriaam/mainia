package io.mainia.services;

import io.mainia.model.HitNote;
import io.mainia.model.Level;
import io.mainia.model.Note;
import io.mainia.model.WrongFileFormatException;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelFileReader {
    private final File file;
    public LevelFileReader(String filePath){file = new File(filePath);}

    public Level readLevel() throws IOException {
        Scanner scanner = new Scanner(file);
        List<List<Note>> notes = new ArrayList<>();
        String line;
        short columnCount = -1;
        float speed = -1;
        float length = -1;
        String musicFilename = null;
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            if(line.isBlank()) continue;
            if(line.trim().equals("[General]")){
                line = scanner.nextLine();//wczytywanie ilosci kolumn
                line = line.trim();
                columnCount = (short) (line.charAt(12) - '0');
                for(int i = 0; i < columnCount; i++) notes.add(new ArrayList<>());

                line = scanner.nextLine(); //wczytywanie dlugosci
                line = line.trim();
                String[] split = line.split("=");
                length = Float.parseFloat(split[1]);

                line = scanner.nextLine();//wczytywanie muzyki-nazwa pliku
                line = line.trim();
                String[] split2 = line.split("=");
                musicFilename = split2[1];
            }
            else if(line.trim().equals("[Speed]")){
                line = scanner.nextLine();
                line = line.trim();
                speed = Integer.parseInt(line);
            }
            else if(line.trim().equals("[HitObjects]")){
                line = scanner.nextLine();
                while(!line.isBlank()){
                    String[] tmp = line.split(", ");
                    if(tmp[0].equals("h")) {
                        notes.get(Integer.parseInt(tmp[1])).add(new HitNote(Float.parseFloat(tmp[2])));
                    }
                    /*else{ //todo: zrobic obsluge sliderow

                    }*/
                    if(scanner.hasNextLine())line = scanner.nextLine();
                    else break;
                }
            }
        }

        if(columnCount==-1) throw new WrongFileFormatException("ColumnCount not found");
        if(speed==-1) throw new WrongFileFormatException("Speed not found");
        if(length==-1) throw new WrongFileFormatException("Length not found");

        return new Level(speed, notes, columnCount, length, musicFilename);

    }

}
