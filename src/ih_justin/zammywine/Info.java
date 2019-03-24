package ih_justin.zammywine;

import org.powerbot.script.rt4.GeItem;

public class Info {

    private static Info currentInfo = new Info();

    private int winePrice;
    private int wineCollected;
    private int lawRunesUsed;
    private String currentTask;

    public boolean tryWorldHop = false;

    public Info() {
        this.wineCollected = 0;
        this.lawRunesUsed = 0;
        this.currentTask = "";

        this.winePrice = new GeItem(245).price;
    }

    public int getWinePrice() {
        return winePrice;
    }

    public int getWineCollected() {
        return wineCollected;
    }

    public int getLawRunesUsed() {
        return lawRunesUsed;
    }

    public String getCurrentTask() {
        return currentTask;
    }

    public void incrementWine() {
        wineCollected++;
    }

    public void incrementLawRunes() {
        lawRunesUsed++;
    }

    public void setCurrentTask(String task) {
        currentTask = task;
    }

    public static Info getInstance() {
        return currentInfo;
    }
}
