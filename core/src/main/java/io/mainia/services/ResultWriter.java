package io.mainia.services;

import io.mainia.model.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void addResult(File f, Result r) throws IOException {
        if (!f.exists()) {f.createNewFile();}
        try (FileWriter fw = new FileWriter(f,true)) {
            fw.write( r.score() + " " + r.noOfPerfects() + " " + r.noOfGreats() + " " + r.noOfOk() + " " + r.noOfMisses() + "\n");
        }
    }
}
