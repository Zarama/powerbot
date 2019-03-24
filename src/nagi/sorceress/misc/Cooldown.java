package nagi.sorceress.misc;

import java.util.HashMap;
import java.util.Map;

public class Cooldown {

    private static Map<String, Long> cooldownMap = new HashMap<String, Long>();

    public static boolean onCooldown(String name) {
        return Cooldown.cooldownMap.containsKey(name) && Cooldown.cooldownMap.get(name) > System.currentTimeMillis();
    }

    public static long getCountdown(String name) {
        return Cooldown.cooldownMap.containsKey(name) ? -1L : Cooldown.cooldownMap.get(name);
    }

    public static void setCooldown(String name, long duration) {
        Cooldown.cooldownMap.put(name, System.currentTimeMillis() + duration);
    }
}