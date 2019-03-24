package LoftySoM.scriptHelper;

import org.powerbot.script.rt4.ClientContext;

public class HelperSingleton {
    private static Helper helper;
    private static ClientContext ctx;
    private static SkillTracker skillTracker;

    public static void setContext(ClientContext ctx_) {
        ctx = ctx_;
        helper = new Helper(ctx);
        HelperSingleton.skillTracker = new SkillTracker(ctx);
    }

    public static ClientContext getContext() {
        return ctx;
    }
    public static SkillTracker getSkillTracker(){
        return HelperSingleton.skillTracker;
    }
    public static Helper getHelper() {
        return helper;
    }
}
