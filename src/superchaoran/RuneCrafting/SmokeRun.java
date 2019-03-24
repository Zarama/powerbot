package superchaoran.RuneCrafting;
import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Path;

/**
 * Created by chaoran on 5/24/16.
 */
@Script.Manifest(
        name = "Smoke Rune Runner", properties = "author=superchaoran; topic=1313029; client=6;",
        description = "Smoke Rune Runner, 1m -3m/hour"
)
public class SmokeRun extends PollingScript<ClientContext> {
    final int ringOfDueling3 = 2562;
    final int ringOfDueling8 = 2552;
    private int ringOfDuelingID;
    private ImprovedBank improvedBank;
    private Locatable bankLocation = new Locatable() {
        @Override
        public Tile tile() {
            return new Tile(2446,3085);
        }
    };

    private Locatable smokingLocation = new Locatable() {
        @Override
        public Tile tile() {
            return new Tile(2582,4842);
        }
    };

    private Path pathToMysteriousRuins0 = ctx.movement.newTilePath(
        new Tile(3314, 3247),
        new Tile(3314, 3253)
    );

    private Path pathToMysteriousRuins1 = ctx.movement.newTilePath(
            new Tile(2583,4840)
    );

    private Path pathToBank = ctx.movement.newTilePath(
        new Tile(2446,3085)
    );
    int mysteriousRuinsId = 66454;

    @Override
    public void start(){
        improvedBank = new ImprovedBank(ctx);
    }

    @Override
    public void poll(){


        switch (state()) {
            case Banking:
                log.info("Banking");
                ctx.camera.turnTo(bankLocation);
                ctx.bank.open();
//                Condition.wait(new Callable<Boolean>() {
//                    @Override
//                    public Boolean call() throws Exception {
//                        if (ctx.bank.opened()) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                }, 200, 5 * 2);

                Condition.sleep(500);

                log.info("Loading preset 1");
                ctx.bank.presetGear1();

                Condition.sleep(500);
                log.info("Close bank.");
                improvedBank.closeBank();


//                Condition.wait(new Callable<Boolean>() {
//                    @Override
//                    public Boolean call() throws Exception {
//                        if(ctx.backpack.select().count() == 27 && !ctx.bank.open()){
//
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                }, 200, 5 * 2);

                Condition.sleep(1000);
                //fill giant and large pouch
                ctx.backpack.select().id(5515).poll().interact("Fill");//degraded giant
                ctx.backpack.select().id(5514).poll().interact("Fill");//ok giant
                ctx.backpack.select().id(5513).poll().interact("Fill");//degraded large
                ctx.backpack.select().id(5512).poll().interact("Fill");//ok large

                Condition.sleep(1000);
                log.info("Open bank.");
                ctx.bank.open();

                log.info("Withdraw ring of dueling.");

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if (ctx.bank.opened()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }, 200, 5 * 2);

                ringOfDuelingID = 0;
                for(int i = ringOfDueling3; i>=ringOfDueling8; i-=2 ){
                    if(ctx.bank.select().id(i).isEmpty()){
                        log.info("Withdraw ring of dueling." + i + "is empty");
                        continue;
                    } else {
                        ringOfDuelingID = i;
                        improvedBank.withdrawCustomized(ctx.bank.select().id(i).poll(), 1, false);
                        improvedBank.withdrawCustomized(ctx.bank.select().id(7936).poll(), 28, false);
                        improvedBank.closeBank();
                        break;
                    }
                }

                if (ringOfDuelingID == 0) {
                    this.log.info("Required item(ring of dueling 3-8) not found in the bank, stopping.");
                    this.stop();
                    ctx.controller.stop();
                    return;
                }
                Condition.sleep(2000);
                ctx.backpack.select().id(ringOfDuelingID).poll().interact("Rub");
                Condition.sleep(1500);
                ctx.input.send("{VK_1}");
                break;

            case GotoBank:

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        double distance = new Tile(2446,3085).tile().distanceTo(ctx.players.local());
                        if (distance < 0 || distance > 1) {
                            log.info("Still teleporting to bank");
                            ctx.camera.turnTo(pathToBank.next());
                            pathToBank.traverse();
                            return false;
                        } else {
                            log.info("Bank reached.");
                            return true;
                        }
                    }
                }, 500, 2 * 4);

                log.info("GotoBank");
                break;

            case GotoSmoke:
                log.info("Goto smoking");

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        double distance = new Tile(3314,3235).tile().distanceTo(ctx.players.local());
                        if (distance < 0 || distance > 7) {
                            log.info("Teleport not complete yet");
                            return false;
                        } else {
                            log.info("Teleport completed");
                            return true;
                        }
                    }
                }, 500, 2 * 3);

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        double distance = new Tile(3314, 3253).distanceTo(ctx.players.local());
                        if (distance < 0 || distance > 1) {
                            log.info("Traversing to mysterious ruins0");
                            ctx.camera.turnTo(pathToMysteriousRuins0.next());
                            pathToMysteriousRuins0.traverse();
                            return false;
                        } else {
                            log.info("Traversing to mysterious ruins0:finished");
                            return true;
                        }
                    }
                }, 500, 2 * 5);


                ctx.input.send("{VK_UP}");
                Condition.sleep(200);
                ctx.input.send("{VK_UP}");
                Condition.sleep(200);
                ctx.input.send("{VK_UP}");
                Condition.sleep(200);
                ctx.input.send("{VK_UP}");
                Condition.sleep(200);

                //Click to go inside alter
                ctx.objects.select(ctx.players.local(), 3).id(66454).poll().click();


                //Wait teleportation completion
                Condition.wait(new Callable<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {

                        double distance = new Tile(2577,4846).tile().distanceTo(ctx.players.local());
                        if (distance < 0 || distance > 6) {
                            log.info("Wait teleportation completion");
                            return false;
                        } else {
                            log.info("teleportation completed");
                            return true;
                        }
                    }
                }, 500, 2 * 3);

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        double distance = new Tile(2583,4840).tile().distanceTo(ctx.players.local());
                        if (distance < 0 || distance > 1) {
                            log.info("Traversing to mysterious ruins1");
                            ctx.camera.turnTo(pathToMysteriousRuins1.next());
                            pathToMysteriousRuins1.traverse();
                            return false;
                        } else {
                            return true;
                        }
                    }
                }, 500, 2 * 3);

                ctx.backpack.select().id(556).poll().click();

                ctx.input.send("{VK_UP}");
                Condition.sleep(200);
                ctx.input.send("{VK_UP}");
                Condition.sleep(200);
                ctx.input.send("{VK_UP}");
                Condition.sleep(200);
                ctx.input.send("{VK_UP}");
                Condition.sleep(200);

                ctx.objects.select(ctx.players.local(), 6).id(2482).poll().click();
                Condition.sleep(500);
                ctx.backpack.select().id(2552, 2554, 2556, 2558, 2560, 2562, 2562).poll().interact("rub");
                Condition.sleep(1000);
                ctx.input.send("{VK_2}");
                break;
        }
    }

    private State state() {
    log.info("Checking state");
    if(!ctx.backpack.select().id(4697).isEmpty()){
        log.info("Checking gotoBank && banking");
        double distance = bankLocation.tile().distanceTo(ctx.players.local());
        if( distance<0 || distance > 4) {
            log.info("Checking gotoBank;");
            return State.GotoBank;
        } else {
            log.info("Checking banking;");
            return State.Banking;
        }
    } else {
        log.info("Checking gotoSmoking && Smoking");
        double distance = smokingLocation.tile().distanceTo(ctx.players.local());
        if(distance<0 || distance > 4) {
            if(ctx.backpack.select().count() == 28) {
                log.info("Checking gotoSmoking");
                return State.GotoSmoke;
            }else{
                log.info("Checking banking;");
                return State.Banking;
            }
        } else {
            log.info("Checking Smoking");
            return State.Smoking;
        }
    }
}
    private enum State {
        Smoking, GotoBank, GotoSmoke, Banking
    }
}
