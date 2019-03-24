package Dark.scripts.ranged_guild;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;

import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Script.Manifest(description = "Plays the mini-game at the Ranged Guild for Ranged XP. Start with lots of bronze arrows equipped at the minigame.", name = "Ranged Guild", properties = "client=4; topic=1338249; author=Dark;")
public class RangeMain extends PollingScript<ClientContext> implements PaintListener {
    private boolean paid = true;
    private String status = "Starting";
    private int initialXp;
    private int initialLevel;
    EscapeThread escapeThread = new EscapeThread(ctx);

    @Override
    public void poll() {
        handleState(getState());
    }

    @Override
    public void repaint(Graphics graphics) {
        graphics.drawOval(ctx.input.getLocation().x, ctx.input.getLocation().y,5,5);

        graphics.drawString("Runtime: " + msToString(getRuntime()),10,200);
        graphics.drawString("Status: " + status,10,215);
        graphics.drawString("EXP Gain: " + kmb(ctx.skills.experience(4) - initialXp),10,230);
        graphics.drawString("Level: " + ctx.skills.level(4) + " (" + (ctx.skills.level(4) - initialLevel) + ")",10,245);
    }

    @Override
    public void start(){
        initialLevel = ctx.skills.level(4);
        initialXp = ctx.skills.experience(4);

        ctx.game.tab(Game.Tab.EQUIPMENT);
        escapeThread.start();
    }

    @Override
    public void stop(){
        escapeThread.setStop(true);
    }

    public String msToString(long runTime) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(runTime),
                TimeUnit.MILLISECONDS.toMinutes(runTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(runTime)),
                TimeUnit.MILLISECONDS.toSeconds(runTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(runTime)));
    }

    enum State{
        EQUIP_ARROWS("Equipping arrows"),
        CLICK_TARGET("Clicking target"),
        TALK_ARCHER("Talking to archer"),
        HANDLE_DIALOGUE("Handle dialogue");

        private String status;
        State(String status) {
            this.status = status;
        }
    }

    private State getState(){
        if(ctx.equipment.select().name("Bronze arrow").isEmpty()){
            return State.EQUIP_ARROWS;
        }else if((!paid && !ctx.chat.chatting()) || isSorryMessage()){
            return State.TALK_ARCHER;
        }else if(!paid && !isSorryMessage()){
            return State.HANDLE_DIALOGUE;
        }
        return State.CLICK_TARGET;
    }

    private void handleState(State state){
        status = state.status;
        switch (state){
            case EQUIP_ARROWS:
                ctx.game.tab(Game.Tab.INVENTORY);
                Item arrows = ctx.inventory.select().name("Bronze arrow").poll();
                if(arrows != null && arrows.click()){
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !ctx.equipment.select().name("Bronze arrow").isEmpty();
                        }
                    });
                    ctx.game.tab(Game.Tab.EQUIPMENT);
                }
                break;
        case CLICK_TARGET:
                GameObject target = ctx.objects.select().id(11663).poll();
                if(target != null && target.inViewport() && target.click()){
                    Condition.sleep(Random.nextGaussian(20,75,50));
                    if(ctx.chat.chatting() && ctx.chat.canContinue()){
                        paid = false;
                    }
                }else if(target != null){
                    ctx.camera.turnTo(target);
                }
                break;
            case TALK_ARCHER:
                Npc archer = ctx.npcs.select().name("Competition Judge").poll();
                if(archer != null && archer.interact("Talk-to")){
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.chat.canContinue();
                        }
                    });
                }
                break;
            case HANDLE_DIALOGUE:
                long timeout = System.currentTimeMillis() + 30000;
                while(ctx.chat.chatting() && System.currentTimeMillis() < timeout){
                    if(ctx.chat.canContinue()){
                        if(!isSorryMessage()) {
                            if (ctx.chat.clickContinue(true)) {
                                Condition.sleep(Random.nextGaussian(500, 1000, 800));
                            }
                        }else{
                            paid = false;
                            break;
                        }
                    }else{
                        if(ctx.chat.continueChat(true, "Sure, I'll give it a go")){
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return ctx.chat.canContinue();
                                }
                            });
                        }
                    }
                }
                if(!ctx.chat.chatting())
                    paid = true;
                break;
        }
    }

    private boolean isSorryMessage(){
        Component message = ctx.widgets.component(231,3);
        return message != null && message.visible() && message.text().contains("Sorry");
    }

    private String kmb(int input){
        if (input >= 1000 && input < 1000000) {
            return input / 1000 + "k";
        } else if (input >= 1000000) {
            return input / 1000000 + "m";
        }
        return String.valueOf(input);
    }

}
