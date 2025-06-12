package io.mainia.model;

public class Combo {
    private int combo=0;
    private int holdcounter=0;
    private int maxCombo=0;

    public Combo() {}

    public void updateCombo(HitResult hit){
        switch(hit){
            case MISS -> combo=0;
            case HOLD -> {
                holdcounter++;
                if(holdcounter>=30){
                    holdcounter=0;
                    combo++;
                }
            }
            case NONE -> {}
            default -> combo+=1;
        }
        if(combo>maxCombo) maxCombo=combo;
    }

    public int currentCombo(){
        return combo;
    }

    public int highestCombo(){
        return maxCombo;
    }
}
