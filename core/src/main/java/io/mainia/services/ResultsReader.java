package io.mainia.services;

import io.mainia.model.Result;
import io.mainia.model.WrongFileFormatException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ResultsReader {
    private final File resultsFile;

    public ResultsReader(File resultsFile) {
        this.resultsFile = resultsFile;
    }

    public ArrayList<Result> readResults() throws FileNotFoundException, WrongFileFormatException {
        ArrayList<Result> results = new ArrayList<>();
        Scanner scanner = new Scanner(resultsFile);
        String line;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if(line.isBlank()) continue;
            String[] parts = line.split(" ");
            if(parts.length != 5) throw new WrongFileFormatException("Expected different amount of data");
            results.add(new Result(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),Integer.parseInt(parts[3]),Integer.parseInt(parts[4])));
        }

        return results;
    }
}
