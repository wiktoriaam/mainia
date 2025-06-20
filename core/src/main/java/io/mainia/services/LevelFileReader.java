package io.mainia.services;

import io.mainia.model.*;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class LevelFileReader {
    private final File file;
    private final String resultLocation;
    private final HashSet<Modifier> modifiers;

    public LevelFileReader(String filePath,String resultLocation, HashSet<Modifier> modifiers) {
        file = new File(filePath);
        this.resultLocation = resultLocation;
        this.modifiers = modifiers;
    }

    public Level readLevel() throws IOException {
        Scanner scanner = new Scanner(file);
        List<List<Note>> notes = new ArrayList<>();
        String line;
        short columnCount = -1;
        float speed = -1;
        float length = -1;
        float startTime = -1;
        String musicFilename = null;
        float healthAmount = -1;
        boolean stat=false;
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            if(line.isBlank()) continue;
            if(line.trim().equals("[General]")){
                line = scanner.nextLine();//wczytywanie ilosci kolumn
                String tmp[] = line.split("=");
                columnCount = Short.parseShort(tmp[1]);
                for(int i = 0; i < columnCount; i++) notes.add(new ArrayList<>());

                line = scanner.nextLine(); //wczytywanie dlugosci
                line = line.trim();
                String[] split = line.split("=");
                length = Float.parseFloat(split[1]);

                line = scanner.nextLine();//wczytywanie muzyki-nazwa pliku
                line = line.trim();
                String[] split2 = line.split("=");
                musicFilename = split2[1];

                line=scanner.nextLine();
                line=line.trim();
                String[] split3 = line.split("=");
                startTime = Float.parseFloat(split3[1]);
            }
            else if(line.trim().equals("[Health]")){
                line = scanner.nextLine();
                line = line.trim();
                if(line.equals("Static")) stat = true;
                else if(!line.equals("Dynamic")) throw new WrongFileFormatException("Health type doesn't exist");
                line = scanner.nextLine();
                line = line.trim();
                healthAmount = Float.parseFloat(line);
            }
            else if(line.trim().equals("[Speed]")){
                line = scanner.nextLine();
                line = line.trim();
                speed = Float.valueOf(line);
            }
            else if(line.trim().equals("[HitObjects]")){
                line = scanner.nextLine();
                while(!line.isBlank()){
                    String[] tmp = line.split(", ");
                    switch(tmp[0]) {
                        case "h"-> {
                            notes.get(Integer.parseInt(tmp[1]))
                                    .add(new HitNote(Float.parseFloat(tmp[2])));
                        }
                        //todo: zrobic obsluge sliderow
                        case "s" -> {
                            notes.get(Integer.parseInt(tmp[1]))
                                    .add(new SliderNote(Float.parseFloat(tmp[2]), Float.parseFloat(tmp[3])));
                        }
                    }
                    if(scanner.hasNextLine())line = scanner.nextLine();
                    else break;
                }
            }
        }

        if(columnCount==-1) throw new WrongFileFormatException("ColumnCount not found");
        if(speed==-1) throw new WrongFileFormatException("Speed not found");
        if(length==-1) throw new WrongFileFormatException("Length not found");
        if(startTime==-1) throw new WrongFileFormatException("StartTime not found");

        return new Level(speed, notes, columnCount, length, musicFilename, startTime,healthAmount,stat,resultLocation, modifiers);

    }

}
