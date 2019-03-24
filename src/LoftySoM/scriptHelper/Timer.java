package LoftySoM.scriptHelper;

import LoftySoM.scriptHelper.Helper;

import java.util.Date;
import java.util.HashMap;

public class Timer {
    private Helper helper;
    private HashMap<String, Date> timerMap = new HashMap<>();

    Timer(Helper helper) {
        this.helper = helper;
    }

    /**
     * @param timerName timerName of the timer
     * @param seconds   How long to set the timer for in seconds
     */
    public void addTimer(String timerName, int seconds) {
        if (timerMap.containsKey(timerName)) {
            helper.log(String.format("Timer %s already exists", timerName));
            return;
        }
        helper.log(String.format("Adding %s timer for %s", timerName, new Date(System.currentTimeMillis() + seconds * 1000)));
        timerMap.put(timerName, new Date(System.currentTimeMillis() + seconds * 1000));
    }

    public boolean hasTimePassed(String timerName) {
        if (!timerMap.containsKey(timerName)) {
            helper.log(String.format("Timer %s has not been set", timerName));
            return false;
        }
        return new Date().getTime() >= timerMap.get(timerName).getTime();
    }

    public void removeTimer(String timerName) {
        if (!timerMap.containsKey(timerName)) {
            helper.log(String.format("Timer %s has not been set", timerName));
            return;
        }
        timerMap.remove(timerName);
        helper.log("Removed timer " + timerName);
    }

    public boolean doesTimerExist(String timerName) {
        return timerMap.containsKey(timerName);
    }
}
