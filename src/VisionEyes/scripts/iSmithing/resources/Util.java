package VisionEyes.scripts.iSmithing.resources;


import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Util {
    private ClientContext ctx;

    public Util(ClientContext ctx) {
        this.ctx = ctx;
    }

    public int inventoryCount() {
        return ctx.inventory.select().count();
    }

    private boolean hasOre(int oreId) {
        return ctx.inventory.select().id(oreId).count() > 0;
    }

    public boolean hasOres(HashMap<Integer, Integer> hashMap){
        List<Boolean> n = new ArrayList<>();
        hashMap.forEach((key, value) -> n.add(this.hasOre(key)));
        if(n.size() > 1) return n.get(0) && n.get(1);
        return n.get(0);
    }

    public boolean hasBar(Bar bar) {
        return ctx.inventory.select().id(bar.getBarId()).count() > 0;
    }

    private int getItemCount(int itemID) {
        return ctx.inventory.select().id(itemID).count();
    }

    public int getItemsCount(LinkedHashMap<Integer, Integer> hashMap){
        AtomicInteger n = new AtomicInteger();
        hashMap.forEach((key, value) -> n.addAndGet(this.getItemCount(key)));
        return Integer.valueOf(n.toString());
    }

    public boolean checkTime(Date d) {
        Date now = new Date();
        long MAX_DURATION = MILLISECONDS.convert(5, SECONDS);
        long duration = now.getTime() - d.getTime();
        return duration >= MAX_DURATION;
    }

    public void run() {
        if (!ctx.movement.running() && ctx.movement.energyLevel() > Random.nextInt(35, 55)) {
            ctx.movement.running(true);
        }
    }

    public String[] toOptions(LinkedHashMap<String, Integer> map) {
        Set<String> keys = map.keySet();
        String options[] = new String[keys.size()];
        int index = 0;
        for (String element : keys) options[index++] = element;
        return options;
    }
}
