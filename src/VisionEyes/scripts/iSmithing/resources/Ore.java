package VisionEyes.scripts.iSmithing.resources;


import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Ore {
    /**
     * Passing Bar Name (key) will return Ores Required and its quantity to do that bar.
     */
    private LinkedHashMap<String, int[][]> ores = new LinkedHashMap<>();
    private Consts consts;

    public Ore() {
        this.consts = new Consts();
        ores.put("Bronze", new int[][]{{438, 1}, {436, 1}}); // 438 Tin Ore 436 Copper Ore
        ores.put("Iron", new int[][]{{440, 1}}); // 440 Iron Ore
        ores.put("Silver", new int[][]{{442, 1}}); // 442 Silver Ore
        ores.put("Steel", new int[][]{{440, 1}, {453, 2}}); // 453 Coal
        ores.put("Gold", new int[][]{{444, 1}}); // 444 Gold Ore
        ores.put("Mithril", new int[][]{{447, 1}, {453, 4}}); // 447 Mithril Ore
        ores.put("Adamantite", new int[][]{{449, 1}, {453, 6}}); // 449 Adamantite Ore
        ores.put("Runite", new int[][]{{451, 1}, {453, 8}}); // 451 Runite Ore
    }

    public LinkedHashMap<Integer, Integer> getQuantity(Bar bar) {
        int[][] OreRequired = ores.get(bar.getSelectedBar());
        int n = this.totalBars(OreRequired);
        LinkedHashMap<Integer, Integer> hashMap = new LinkedHashMap<>();
        for (int[] i : OreRequired) hashMap.put(i[0], this.computeQuantity(n, i[1]));
        return hashMap;
    }

    private int computeQuantity(int n, int q) {
        /*
          Receives Quantity of Specific Ore to do a Bar and Total bars it can do with "full" inventory.
          This way is more readable
         */
        return n * q;

    }

    private int totalBars(int[][] ore) {
        int n = 0;
        for (int[] i : ore) n += i[1];
        return consts.MAX_ITEMS / n;
    }

    public int getMaxItemsByBar(Bar bar){
        AtomicInteger n = new AtomicInteger();
        this.getQuantity(bar).forEach((key, value) -> n.addAndGet(value));
        return Integer.valueOf(n.toString());
    }

}
