package io.mainia.model;

import java.util.Observable;
import java.util.Observer;

public class Score implements Observer {
    private int score;
    private int noOfPerfects;
    private int noOfGreats;
    private int noOfOks;
    private int noOfMisses;

    int getScore(){
        return score;
    }
    int getNoOfPerfects(){
        return noOfPerfects;
    }
    int getNoOfGreats(){
        return noOfGreats;
    }
    int getNoOfOks(){
        return noOfOks;
    }
    int getNoOfMisses(){
        return noOfMisses;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
