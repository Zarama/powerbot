package Noodleking;

import org.powerbot.script.*;
import org.powerbot.script.GeItem;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Interactive;
 
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;
 
@Script.Manifest(name = "Fishing Lobsters/Swordfish",properties = "author=Noodleking, Terminator1; topic=1325074; client=4;",  description = "Fishes Lobsters/Swordfish at Musa Point and banks them at the Monks of Entrana")
public class FishingLobsters extends PollingScript<ClientContext> implements PaintListener,MessageListener{
 
    private final int coins = 995,fishbounds[] = {-48, 48, 0, 0, -48, 48},lxp = 90,sxp = 100,txp = 80, KaramjaSailor = 3648,SarimSailor[] = {3644,3645,3646}, KaramjaGP = 2082, SarimGP = 2084, spotID = 1522,lobsters = 377, swordfish = 371,tuna = 359, cage = 301,harpoon = 311;
    private GeItem ge;
    private DecimalFormat formatted = new DecimalFormat("#,###,###");
    private int LobsterProfit, SwordfishProfit, TunaProfit, LobsterCount = 0, SwordfishCount = 0,TunaCount = 0,
            xpgained = 0;
    private long Stopwatch = 0;
    private String interacting = "wtf";
    private Area Karamjafishing = new Area(new Tile(2914,3164), new Tile(2934,3184));
    private Area EntranaDeposit = new Area(new Tile(3041,3238), new Tile(3051,3233));
    private Area KaramjaDock = new Area(new Tile(2941,3141), new Tile(2957,3151));
    private Area PortSarimDock = new Area(new Tile(3022,3216), new Tile(3032, 3226));
    private boolean lobster = true,fishing = false, Karamja, Draynor,tunaFished=false,powerfish;
 
    private final Tile[] path_to_sailor_Karamja = {
            new Tile(2924, 3178),
            new Tile(2924, 3173), new Tile(2923, 3169),
            new Tile(2923, 3164), new Tile(2920, 3162),
            new Tile(2919, 3158), new Tile(2916, 3156),
            new Tile(2917, 3152), new Tile(2922, 3152),
            new Tile(2926, 3151), new Tile(2931, 3151),
            new Tile(2933, 3148), new Tile(2937, 3147),
            new Tile(2941, 3146), new Tile(2946, 3146),
            new Tile(2951, 3146), new Tile(2956, 3146)
    };
    private final Tile[] path_to_entrana_monks = {
            new Tile(3027, 3218),
            new Tile(3028, 3222), new Tile(3028, 3227),
            new Tile(3028, 3232), new Tile(3029, 3236),
            new Tile(3034, 3236), new Tile(3039, 3236),
            new Tile(3044, 3235)
    };
 
    private TilePath to_sailor_Karamja, to_sailor_Port_Sarim, to_fishing_spot, to_entrana_monks;
 
    public void start(){
        ge = new org.powerbot.script.rt4.GeItem(lobsters);
        LobsterProfit = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(swordfish);
        SwordfishProfit = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(tuna);
        TunaProfit = ge.price;
        //incase we ever want to go AIO instead
        powerfish = JOptionPane.showConfirmDialog(null, "Do you want to powerfish?", "Powerfish Check", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        if(JOptionPane.showConfirmDialog(null, "Did you finish the quest 'Pirate's Treasure?", "Completion Check", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            ctx.controller.stop();
        }
        for(Item item:ctx.inventory.items()){
            if(item.id() == cage) {
                interacting = "Cage";
                if(ctx.skills.level(Constants.SKILLS_FISHING) < 40) {
                    performLogout("Your level is too low to fish lobsters!");
                }
                break;
            } else if(item.id() == harpoon){
                interacting = "Harpoon";
                //A way to powerfish for lowerlevels till they reach swordfish
                if(ctx.skills.level(Constants.SKILLS_FISHING) < 35) {
                    performLogout("Your level is too low to fish tuna/swordfishes!");
                }
                lobster = false;
                if(JOptionPane.showConfirmDialog(null, "Do you want to drop tuna?", "Completion Check", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    tunaFished = true;
                }
                break;
            }
        }
        if(interacting.equals("wtf")) {
            performLogout("You neither have a lobster pot,nor a harpoon!");
        }
        Condition.sleep(Random.nextInt(0,250));
        if(Karamjafishing.contains(ctx.players.local().tile())){
            Karamja = true;
            Draynor = false;
        }
        else if(EntranaDeposit.contains(ctx.players.local().tile())){
            Karamja = false;
            Draynor = true;
        }
        else
            ctx.controller.stop();
 
        to_entrana_monks = ctx.movement.newTilePath(path_to_entrana_monks);
        to_sailor_Port_Sarim = ctx.movement.newTilePath(path_to_entrana_monks).reverse();
        to_sailor_Karamja = ctx.movement.newTilePath(path_to_sailor_Karamja);
        to_fishing_spot = ctx.movement.newTilePath(path_to_sailor_Karamja).reverse();
    }
 
    @Override
    public void poll(){
        final State state = getState();
        if(state!=null) {
            switch (state) {
                case BANKING:
                    Banking();
                    break;
                case WALKING_TO_MUSA_POINT:
                    checkIfOuttaCash();
                    Walking_to_Musa_Point();
                    break;
                case FISHING:
                    Fishing();
                    break;
                case DROP:
                    Drop();
                    break;
                case WALKING_TO_DRAYNOR:
                    checkIfOuttaCash();
                    Walking_to_Draynor();
                    break;
            }
        }
    }
 
    private State getState(){
        int inventory_amount = ctx.inventory.select().count();
        boolean inBank = EntranaDeposit.contains(ctx.players.local().tile()),
                atFishingspot = Karamjafishing.contains(ctx.players.local().tile());
        if(atFishingspot) {
            if(inventory_amount == 28)
                if(((ctx.inventory.select().id(tuna).count()==0)||tunaFished)&&!powerfish)
                    return State.WALKING_TO_DRAYNOR;
                else
                    return State.DROP;
            else
                return State.FISHING;
        }
        else if(inBank) {
            if (inventory_amount == 28)
                return State.BANKING;
            else
                return State.WALKING_TO_MUSA_POINT;
        }
        else{
            if(inventory_amount == 28)
                return State.WALKING_TO_DRAYNOR;
            else
                return State.WALKING_TO_MUSA_POINT;
        }
    }
 
    private void performLogout(String reason) {
        ctx.bank.close();
        if(!ctx.game.tab().name().equals(Game.Tab.LOGOUT.name()))
            ctx.game.tab(Game.Tab.LOGOUT);
        if(ctx.widgets.widget(69).component(3).valid())
            ctx.widgets.widget(69).component(3).click();
        if(!ctx.widgets.widget(182).component(10).valid())
            Condition.sleep(Random.nextInt(500,750));
        ctx.widgets.widget(182).component(10).click();
        ctx.controller.suspend();
        alert(reason);
        ctx.controller.stop();
    }
 
    private void checkIfOuttaCash() {
        //Could implement an inventory check but it will need to scan the whole invent
        if(ctx.widgets.widget(217).component(0).valid() || ctx.widgets.widget(231).component(0).valid())
            performLogout("You are out of cash...");
    }
 
    private void alert(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
 
    private void Walking_to_Draynor() {
        if (PortSarimDock.contains(ctx.players.local().tile()) && !Draynor) {
            Karamja = false;
            Draynor = true;
        }
        if (ctx.players.local().tile().floor() == 0) {
            if (Karamja) {
                if(!KaramjaDock.contains(ctx.players.local().tile())){
                    Condition.sleep(Random.nextInt(1000, 1500));
                    to_sailor_Karamja.traverse();
                }
                else if(ctx.npcs.select().id(KaramjaSailor).poll().inViewport()){
                    ctx.npcs.select().id(KaramjaSailor).poll().interact("Pay-fare");
                    Condition.sleep(Random.nextInt(1000, 1500));
                }
 
            } else if (Draynor) {
                Condition.sleep(Random.nextInt(1000, 1500));
                to_entrana_monks.traverse();
            }
        } else if (ctx.objects.select().id(SarimGP).poll().inViewport()){
            ctx.objects.select().id(SarimGP).poll().click();
            Condition.sleep(Random.nextInt(500, 1000));
        }
        else
            Condition.sleep(Random.nextInt(500, 1000));
    }
 
    private void Walking_to_Musa_Point(){
        if (KaramjaDock.contains(ctx.players.local().tile()) && !Karamja) {
            Karamja = true;
            Draynor = false;
        }
        if (ctx.players.local().tile().floor() == 0) {
            if (Karamja) {
                Condition.sleep(Random.nextInt(1000, 1500));
                to_fishing_spot.traverse();
            } else if (Draynor) {
                if(!PortSarimDock.contains(ctx.players.local().tile())){
                    Condition.sleep(Random.nextInt(1000, 1500));
                    to_sailor_Port_Sarim.traverse();
                }
                else if(ctx.npcs.select().id(SarimSailor).poll().inViewport()){
                    ctx.npcs.select().id(SarimSailor).poll().interact("Pay-fare");
                    Condition.sleep(Random.nextInt(1000, 1500));
                }
            }
        } else if (ctx.objects.select().id(KaramjaGP).poll().inViewport()){
            ctx.objects.select().id(KaramjaGP).poll().click();
            Condition.sleep(Random.nextInt(500, 1000));
        }
        else
            Condition.sleep(Random.nextInt(500, 1000));
    }
 
    private boolean ManhattenDistance(){
        return ctx.players.local().tile().distanceTo(ctx.npcs.select().id(spotID).nearest().poll().tile()) == 1;
    }
 
    private void Fishing(){
        if(((!ManhattenDistance() || (ctx.players.local().animation() == -1)) && !ctx.players.local().inMotion()) ||
                (System.currentTimeMillis()-Stopwatch)>Random.nextInt(120000,180000)) {
            fishing = false;
            Npc spot = ctx.npcs.select().id(spotID).each(Interactive.doSetBounds(fishbounds)).nearest().poll();
            spot.interact(interacting);
            Stopwatch = System.currentTimeMillis();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return fishing;
                }
            },75,10);
        }
    }
 
    public void Drop() {
        for(Item item: ctx.inventory.select()) {
            if(ctx.inventory.selectedItemIndex()!=-1)
                ctx.inventory.select().shuffle().poll().click();
            //This case shall do for now...
            if (item.id() != cage && item.id() != harpoon && item.id() != coins)
                if(powerfish||item.id()==tuna)
                    item.interact("Drop");
        }
    }
 
    private void Banking(){
        int fish = -1,fish1 = -1;
        if(!ctx.widgets.widget(192).component(2).inViewport() && ctx.objects.select().id(26254).nearest().poll().inViewport()) {
            ctx.objects.select().id(26254).within(EntranaDeposit).nearest().poll().click();
            Condition.sleep(Random.nextInt(1000, 1500));
        } else {
            for (int i = 0; i < 28; i++) {
                if (lobster) {
                    if (ctx.widgets.widget(192).component(2).component(i).itemId() == lobsters) {
                        fish = i;
                        break;
                    }
                } else {
                    if (ctx.widgets.widget(192).component(2).component(i).itemId() == swordfish) {
                        fish = i;
                    } else if(ctx.widgets.widget(192).component(2).component(i).itemId() == tuna) {
                        fish1 = i;
                    }
                    if((fish!=-1)&&(fish1!=-1)) {
                        break;
                    }
                }
            }
        }
        if(tunaFished) {
            if(fish!=-1) {
                ctx.widgets.widget(192).component(2).component(fish).interact("Deposit-all");
            }
            if(fish1!=-1) {
                ctx.widgets.widget(192).component(2).component(fish1).interact("Deposit-all");
            }
        } else
            ctx.widgets.widget(192).component(2).component(fish).interact("Deposit-all");
    }
 
    @Override
    public void repaint(Graphics gr) {
        final Color color = new Color(0, 255, 0, 150), color1 = new Color(0, 0, 255, 100),
                color2 = new Color(0, 200, 255);
        BasicStroke stroke1 = new BasicStroke(1);
        Font font1 = new Font("Perpetua", 1, 15);
 
        Graphics2D g = (Graphics2D) gr;
        if(getState().toString().equals("FISHING")) {
            g.setColor(color);
            g.drawPolygon(ctx.npcs.select().id(spotID).nearest().poll().tile().matrix(ctx).bounds());
        }
        g.setColor(color1);
        g.fillRect(0, 55,  250, 104);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(0, 55, 250, 104);
        g.setFont(font1);
        g.drawString("Time: " + formated(this.getRuntime()), 5, 70);
        if(lobster) {
            g.drawString("Lobsters:" + formatted.format(LobsterCount) + "(" + formatted.format(
                    getPerHour(LobsterCount, getRuntime())) + " per/Hour)", 5, 84);
            g.drawString("Total Profit: " + formatted.format((LobsterCount*LobsterProfit)) + " (" + formatted.format
                    ((getPerHour((LobsterCount*LobsterProfit), getRuntime()))) + " GP/Hour)", 5, 98);
        } else {
            g.drawString("Swordfish:" + formatted.format(SwordfishCount) + "(" + formatted.format(
                    getPerHour(SwordfishCount, getRuntime())) + " )", 5, 84);
            g.drawString("Total Profit: " + formatted.format((SwordfishCount*SwordfishProfit)+(TunaCount*TunaProfit)) + " (" + formatted.format
                    ((getPerHour((SwordfishCount*SwordfishProfit)+(TunaCount*TunaProfit), getRuntime()))) + " GP/Hour)", 5, 98);
        }
        g.drawString("Total XP: " + formatted.format(xpgained) + " (" + formatted.format((getPerHour(xpgained, getRuntime()))) + " XP/Hour)", 5, 112);
        g.drawString("Status: "+ getState().toString(),5,126);
        g.drawString("Created by Noodleking",5,140);
        g.drawString("Co-author:Terminator1",5,155);
    }
 
    private String formated(long time) {
        final int sec = (int) (time / 1000),
                h = sec / 3600,
                m = sec / 60 % 60,
                s = sec % 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
                + (s < 10 ? "0" + s : s);
    }
 
    private static int getPerHour(int in, long time) {
        return (int) ((in) * 3600000D / time);
    }
 
    @Override
    public void messaged(MessageEvent messageEvent) {
        String txt = messageEvent.text().toLowerCase();
        if(lobster) {
            if (txt.contains("you attempt")) {
                fishing = true;
            } else if(txt.contains("you catch")) {
                LobsterCount++;
                xpgained+=lxp;
            }
        } else {
            if (txt.contains("you start")) {
                fishing = true;
            } else if(txt.contains("you catch a s")){
                SwordfishCount++;
                xpgained+=sxp;
            } else if(txt.contains("you catch a s")){
                xpgained+=txp;
            }
        }
    }
 
    private enum State{
        FISHING, WALKING_TO_DRAYNOR, BANKING, WALKING_TO_MUSA_POINT, DROP
    }
 
}
