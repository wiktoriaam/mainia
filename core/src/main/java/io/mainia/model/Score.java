package io.mainia.model;

public class Score {
    private int score;
    private int noOfPerfects;
    private int noOfGreats;
    private int noOfOks;
    private int noOfMisses;

    public int currentScore(){return score;}
    public int NoOfPerfects(){return noOfPerfects;}
    public int NoOfGreats(){return noOfGreats;}
    public int tNoOfOks(){return noOfOks;}
    public int NoOfMisses(){return noOfMisses;}

    public void update(HitResult State,Combo combo){
        switch (State) {
            case PERFECT -> {
                noOfPerfects++;
                score += (int) ( 300 * (1+((float)combo.currentCombo())/100));
            } case GREAT -> {
                noOfGreats++;
                score += (int) (100 * (1+((float)combo.currentCombo())/100));
            } case OK -> {
                noOfOks++;
                score += (int) (50 * (1+((float)combo.currentCombo())/100));
            } case HOLD -> {
                score += 1;
            }
        }
    }

    public void missedUpdate(){
        noOfMisses++;
    }

    public Result createResult(){
        return new Result(score,noOfPerfects,noOfGreats,noOfOks,noOfMisses);
    }
}
