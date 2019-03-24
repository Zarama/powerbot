package furze;

import org.powerbot.script.*;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Widget;

import java.awt.*;

@Script.Manifest(name = "F**king Magic m8",properties = "author=Furze; client=4; topic=1319542;",  description = "Gets those $$$")
public class MagicCutter extends PollingScript<ClientContext> implements PaintListener {

    private static Tile BANK_TILE_NOR_WEST = new Tile(1589, 3480);
    private static  Tile BANK_TILE_SOUTH_EAST = new Tile(1593,3476);
    private static  Tile BANK_TILE = new Tile(1592,3476);
    private static  Tile TREE_TILE = new Tile(1580,3491);

    private boolean hasInitialized = false;
    private int magicsCut = 0;
    private int inventoryCount = 0;

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;

        int seconds = (int) (getRuntime() / 1000) % 60 ;
        int minutes = (int) ((getRuntime() / (1000*60)) % 60);
        int hours   = (int) ((getRuntime() / (1000*60*60)) % 24);
        final int bHr = (int) ((magicsCut * 3600000D) / getRuntime());

        g.setColor(Color.BLACK);
        g.fillRect(5, 5, 145, 45);

        g.setColor(Color.GREEN);
        g.drawString(String.format("magics cut: %d", magicsCut), 10, 20);
        g.drawString(String.format("magics/hr: %,d", bHr), 10, 30);
        g.drawString(String.format("Running for: %d hour, %d min, %d sec", hours, minutes, seconds), 10, 40);
    }

    @Override
    public void poll() {
        final State state = getState();
        switch (state){
            case INIT:
                initialize();
            case CUTTING:
                handleCutting();
                return;
            case BANKING:
                handleBanking();
        }
    }

    private void initialize(){
        updateInventory();
        magicsCut = 0;
        hasInitialized = true;
    }
    private void handleCutting() {
        if(isCutting()){
            updateInventory();
            specialATK();
        } else {
            ctx.objects.select();
            BasicQuery<GameObject> magicTrees = ctx.objects.name("Magic tree");
            if(magicTrees.size() > 0){
                GameObject magicTree = magicTrees.nearest().peek();
                if(magicTree.inViewport()) {
                    magicTree.interact("Chop down");
                } else {
                    ctx.movement.findPath(TREE_TILE).traverse();
                }
            } else {
                walkToTrees();
            }
        }
    }
    private void specialATK(){
        int specialPercentage = ctx.combat.specialPercentage();
        if(specialPercentage == 100){
            ctx.widgets.select(); //clear
            Widget bottomBar = ctx.widgets.id(164).peek();
            bottomBar.component(41).click(); //COMBAT TAB YO
            ctx.widgets.select(); //clear
            Widget combatTab = ctx.widgets.id(593).peek();
            combatTab.component(34).click();//use special
            sleep(3000);
        }
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleBanking(){
        if(isInBankRoom()){
            if(!ctx.bank.opened()) {
                openBank();
            } else {
                depositItems();
            }
        } else {
            walkToBank();
        }
    }

    private void depositItems() {
        ctx.bank.depositInventory();
        updateInventory();
        ctx.bank.close();
    }

    private void openBank(){
        //Clear filters
        ctx.objects.select();
        BasicQuery<GameObject> banks = ctx.objects.id(28861);
        if(banks.size() > 0){
            GameObject bank = banks.nearest().peek();
            bank.interact("Use");
        }
    }
    private void walkToTrees(){
        if(!isWalking()) {
            walkToTile(TREE_TILE);
        }
    }

    private void walkToBank(){
        ctx.movement.findPath(BANK_TILE).traverse();
    }

    private void walkToTile(Tile tile){
        ctx.movement.newTilePath(tile).traverse();
    }

    private void updateInventory(){
        int newInvCount = ctx.inventory.select().count();
        if(newInvCount > inventoryCount){
            int difference = newInvCount - inventoryCount;
            magicsCut += difference;
        }
        inventoryCount = newInvCount;
    }

    private boolean isCutting(){
        return ctx.players.local().animation() == 2846;
    }

    private boolean isWalking(){
        return ctx.players.local().inMotion();
    }

    private boolean isInBankRoom(){
        Area area = new Area(BANK_TILE_NOR_WEST, BANK_TILE_SOUTH_EAST);
        return  area.contains(ctx.players.local());
    }

    private State getState() {
        if(!hasInitialized){
            return State.INIT;
        } else if (ctx.inventory.select().count() == 28) {
            return State.BANKING;
        } else {
            return State.CUTTING;
        }
    }

    private enum State {
        CUTTING,BANKING,INIT
    }
}
