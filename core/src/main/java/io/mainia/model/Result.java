package io.mainia.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public record Result(int score,
                     int noOfPerfects,
                     int noOfGreats,
                     int noOfOk,
                     int noOfMisses) {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void addResult(File f,Result r) throws IOException {
        if (!f.exists()) {f.createNewFile();}
        try (FileWriter fw = new FileWriter(f,true)) {
            fw.write( r.score + " " + r.noOfPerfects + " " + r.noOfGreats + " " + r.noOfOk + " " + r.noOfMisses + "\n");
        }
    }

}