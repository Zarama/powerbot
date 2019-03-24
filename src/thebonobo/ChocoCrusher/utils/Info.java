package thebonobo.ChocoCrusher.utils;

import org.powerbot.script.rt4.GeItem;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 16/09/17
 */

public class Info {

    private static Info currentInfo = new Info();

    private int chocolateBarPrice;
    private int chocolateDustPrice;
    private int chocolateDone;
    private long startTime;

    private String currentTask;


    public Info() {
        this.chocolateDone = 0;
        this.currentTask = "";
        this.chocolateBarPrice = new GeItem(Items.CHOCOLATE_BAR).price;
        this.chocolateDustPrice = new GeItem(Items.CHOCOLATE_DUST).price;
    }

    public static Info getInstance() {
        return currentInfo;
    }

    public long getStartTime() {return startTime;
    }
    public int getChocolateBarPrice() {
        return chocolateBarPrice;
    }

    public int getChocolateDustPrice() {
        return chocolateDustPrice;
    }

    public int chocolateDone() {
        return chocolateDone;
    }

    public String getCurrentTask() {
        return currentTask;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public void setCurrentTask(String task) {
        currentTask = task;
    }

    public void incrementChocolateDust(int dust) {
        chocolateDone += dust;
    }
}