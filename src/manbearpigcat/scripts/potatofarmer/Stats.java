package manbearpigcat.scripts.potatofarmer;

/**
 * Created by Shan on 2016-08-17.
 */
public class Stats {
    private int potFarmed = 0;
    private String state = "Waiting...";

    public Stats(){
        this.potFarmed = 0;
        this.state = "Waiting...";
    }

    public Stats(int pot){
        this.potFarmed = pot;
        this.state = "Waiting...";
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public int getPotFarmed(){
        return potFarmed;
    }

    public void setPotFarmed(int pot){
        this.potFarmed = pot;
    }

    public void addPot(){
        this.potFarmed = potFarmed + 1;
    }
}
