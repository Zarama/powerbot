package LoftySoM.scriptHelper.utilities;

import LoftySoM.scriptHelper.HelperSingleton;
import org.powerbot.script.rt4.Component;

public class CustomConstants {
    private static final int LEVEL_UP_WIDGET_ID = 233, LEVEL_UP_CONTINUE_TEXT_ID = 3, LEVEL_UP_EXTRAS_WIDGET_ID = 193,
            LEVEL_UP_EXTRAS_CONTINUE_ID = 3, ANTI_BAN_RANDOMIZE_INT = 13000, SKILLS_WIDGET = 320,
            MAGIC_SKILL_COMPONENT = 6, MAX_SLEEP_TIME = (1000 * 60) * 15;
    public static int CHAT_WIDGET = 162;
    public static int GAME_CHAT_STATUS_COMPONENT = 12;
    public static enum SkillComponent {
        ATTACK(1),
        STRENGTH(2),
        DEFENCE(3),
        RANGE(4),
        PRAYER(5),
        MAGIC(6),
        RUNECRAFTING(7),
        CONSTRUCTION(8),
        WOODCUTTING(22);
        public final int componentId;

        private SkillComponent(int componentId) {
            this.componentId = componentId;
        }
    }

    public static Component getStatHover(int skill) {
        return HelperSingleton.getContext().widgets.widget(SKILLS_WIDGET).component(skill);
    }
}
