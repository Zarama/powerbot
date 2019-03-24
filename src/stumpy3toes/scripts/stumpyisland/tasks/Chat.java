package stumpy3toes.scripts.stumpyisland.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ChatOption;
import org.powerbot.script.rt4.Component;
import stumpy3toes.api.script.ClientContext;
import stumpy3toes.api.task.Task;

public class Chat extends Task {
    private static final int OLD_CHAT_CONTINUE_WIDGET_ID = 162;
    private static final int[][] CHAT_CONTINUES = {
            {11, 3},
            {OLD_CHAT_CONTINUE_WIDGET_ID, 33},
            {193, 2},
            {233, 2}
    };
    private static final String[] CHAT_OPTIONS = {"I am brand new! This is my first time here.","I've played in the past, but not recently.","I am an experienced player."};

    public Chat(ClientContext ctx) {
        super(ctx, "Chat");
    }

    @Override
    public boolean checks() {
        return ctx.chat.canContinue() || getContinueComponent() != null || ctx.chat.chatOptions().size() > 0;
    }

    @Override
    public void poll() {
        setStatus("Skipping through chat");
        if (ctx.chat.canContinue()) {
            ctx.chat.clickContinue(true);
        } else if(!ctx.chat.select().text(CHAT_OPTIONS).isEmpty()) {
            ChatOption experienceOption = ctx.chat.select().text(CHAT_OPTIONS[Random.nextInt(1, CHAT_OPTIONS.length - 1)]).poll();
            experienceOption.select();
        } else if(!ctx.chat.select().text("No, I'm not planning to do that.").isEmpty()){
            ChatOption ironManOption = ctx.chat.select().text("No, I'm not planning to do that.").poll();
            ironManOption.select();
        } else if (!ctx.chat.select().text("Yes.").isEmpty()) {
            ChatOption bankOption = ctx.chat.select().text("Yes").poll();
            bankOption.select();
        } else {
            Component continueComponent = getContinueComponent();
            if (continueComponent != null) {
                if (continueComponent.widget().id() == OLD_CHAT_CONTINUE_WIDGET_ID) {
                    continueComponent.click();
                } else {
                    ctx.input.send(" ");
                }
            }
        }
        Condition.sleep(500);
    }

    private Component getContinueComponent() {
        for (int[] chatContinues : CHAT_CONTINUES) {
            Component continueComponent = ctx.widgets.component(chatContinues[0], chatContinues[1]);
            if (continueComponent.visible()) {
                return continueComponent;
            }
        }
        return null;
    }
}
