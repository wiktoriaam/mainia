package io.mainia.model;

public class Combo {
    private int combo=0;
    private int holdcounter=0;

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
    }

    public int currentCombo(){
        return combo;
    }
}
