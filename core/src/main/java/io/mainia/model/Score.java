package io.mainia.model;

public class Score {
    private int score;
    private int noOfPerfects;
    private int noOfGreats;
    private int noOfOks;
    private int noOfMisses;

    public int getScore(){return score;}
    public int getNoOfPerfects(){return noOfPerfects;}
    public int getNoOfGreats(){return noOfGreats;}
    public int getNoOfOks(){return noOfOks;}
    public int getNoOfMisses(){return noOfMisses;}

    public void update(HitResult State){
        switch (State) {
            case PERFECT -> {
                noOfPerfects++;
                score += 300;
            } case GREAT -> {
                noOfGreats++;
                score += 100;
            } case OK -> {
                noOfOks++;
                score += 50;
            }
        }
    }
    public void missedUpdate(){
        noOfMisses++;
    }
}
