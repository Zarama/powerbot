package j333.scripts.smithing;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.powerbot.script.rt6.*;
import org.powerbot.script.Script;
import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt6.Component;

@Script.Manifest(name="JPSGeneralSmithing", description="A general smithing script for rs3.", properties="author=J333; client=6; topic=1341312;")

public class JPSGeneralSmithing extends PollingScript<ClientContext> implements JPSFormEventHandler
{
    interface Action { void execute(); }
    interface AutoRetryAction { boolean evaluate(); }

    private enum State { IDLE, WITHDRAW, DEPOSIT, SMELT }

    private static final boolean DEBUG = true;

    public static final int MIN_ANGLE = 20;
    public static final int MAX_ANGLE = 40;
    public static final int MIN_PITCH = 30;
    public static final int MAX_PITCH = 50;
    public static final JPSGeneralSmithing.Action STATE_FAILURE_ACTION = () -> { };

    private static final int SMELT_BUTTON_ID = 40;
    private static final int SMITHING_WIDGET = 1370;
    private static final int TIME_REMAINING_LABEL_ID = 5;
    private static final int SMELT_STATUS_WIDGET_ID = 1251;

    private Random random = new Random();

    private Action idle;
    private Action smelt;
    private Action deposit;
    private Action withdraw;
    private State currentState = State.IDLE;

    private JPSGeneralSmithingForm form;

    public HashMap<Integer, Integer> getOres()
    {
        HashMap<Integer, Integer> ores = new HashMap<>();

        List<Integer> oreIds = this.form.getOreIds();
        List<Integer> oreAmounts = this.form.getOreAmounts();

        for (int i = 0; i < this.form.getOreIds().size(); i++) {
            ores.put(oreIds.get(i), oreAmounts.get(i));
        }

        return ores;
    }

    public void start()
    {
        this.form = new JPSGeneralSmithingForm(this.getStorageDirectory());
        this.form.setEventHandler(this);

        this.idle = () -> { };

        this.smelt = () -> {

            GameObject smelter = this.getClosestObject(this.form.getSmelterNames());

            if (!smelter.valid())
            {
                this.setCurrentState(State.IDLE);
                return;
            }

            if (!smelter.inViewport()) { this.execute(() -> this.goTo(smelter)); }

            this.execute(() -> this.smeltUsing(smelter, this.form.getDefaultWait()));
            this.execute(() -> this.waitUntilFinished(this.form.getDefaultWait()));
            this.setCurrentState(State.IDLE);
        };

        this.deposit = () -> {

            if (this.isValidDeposit(this.form.getBarIds())) { return; }

            this.bank(this.form.getBankIds(), () -> this.deposit(this.form.getBarIds(), this.form.getDefaultWait()), this.form.getDefaultWait());
            this.setCurrentState(State.IDLE);
        };

        this.withdraw = () -> {

            if (this.isValidWithdraw(this.getOres())) { return; }

            this.bank(this.form.getBankIds(), () -> this.withdraw(this.getOres(), this.form.getDefaultWait()), this.form.getDefaultWait());
            this.setCurrentState(State.IDLE);
        };
    }

    public void poll()
    {
        this.adjustCameraIfNeeded();

        if (this.shouldGetNextState())
        {
            State nextState = this.getNextState(this.form.getBarIds(), this.getOres());
            this.setCurrentState(nextState);
        }
    }

    public void formDidPressStart(JPSGeneralSmithingForm form)
    {

    }

    /********* Debug *********/

    private void debugPrint(String message)
    {
        if (!JPSGeneralSmithing.DEBUG) { return; }

        System.out.println(message);
    }

    /********* State *********/

    private void setCurrentState(State state)
    {
        this.debugPrint("Current State: " + state);

        this.currentState = state;
        this.getActionForState(this.currentState).execute();
    }

    private boolean shouldGetNextState() {
        return this.currentState == State.IDLE;
    }

    private State getNextState(List<Integer> barIds, HashMap<Integer, Integer> ores)
    {
        State state = State.IDLE;

        if (!this.isValidDeposit(barIds)) {
            state = State.DEPOSIT;
        }
        else if (!this.isValidWithdraw(ores)) {
            state = State.WITHDRAW;
        }
        else if (this.isValidDeposit(barIds) && this.isValidWithdraw(ores)) {
            state = State.SMELT;
        }

        return state;
    }

    private Action getActionForState(State state)
    {
        Action action = this.idle;

        switch (state)
        {
            case IDLE: break;

            case SMELT:
                action = this.smelt;
                break;

            case DEPOSIT:
                action = this.deposit;
                break;

            case WITHDRAW:
                action = this.withdraw;
                break;
        }

        return action;
    }

    /********* Helpers *********/

    private List<Integer> backpackItemIds() {
        return Arrays.stream(this.ctx.backpack.items()).map(Item::id).collect(Collectors.toList());
    }

    private int occurrencesOfItem(int id, List<Integer> itemIds)
    {
        int count = 0;

        for (int itemId : itemIds) {
            if (itemId == id) { count++; }
        }

        return count;
    }

    private boolean isValidWithdraw(HashMap<Integer, Integer> items)
    {
        boolean success = true;

        List<Integer> ids = this.backpackItemIds();

        for (int id : items.keySet())
        {
            int desiredAmount = items.get(id);
            int withdrawnAmount = this.occurrencesOfItem(id, ids);

            if (withdrawnAmount != desiredAmount)
            {
                success = false;
                break;
            }
        }

        return success;
    }

    private boolean isValidDeposit(List<Integer> itemIds)
    {
        boolean success = true;

        List<Integer> backpackItemIds = this.backpackItemIds();

        for (int id : itemIds)
        {
            if (this.occurrencesOfItem(id, backpackItemIds) != 0)
            {
                success = false;
                break;
            }
        }

        return success;
    }

    private double distanceFrom(GameObject object)
    {
        Point destination = object.centerPoint().getLocation();
        return this.ctx.players.local().centerPoint().getLocation().distance(destination.x, destination.y);
    }

    private <T> GameObject gameObjectForIdentifier(T identifier)
    {
        GameObject object = null;

        if (identifier instanceof String)  {
            object = this.ctx.objects.select().name((String)identifier).nearest().peek();
        }
        else if (identifier instanceof Integer) {
            object = this.ctx.objects.select().id((Integer)identifier).nearest().peek();
        }
        else { this.ctx.game.logout(true); }

        return object;
    }

    private <T> HashMap<GameObject, Double> calculateDistances(List<T> identifiers)
    {
        HashMap<GameObject, Double> distances = new HashMap<>();

        for (T identifier : identifiers)
        {
            GameObject object = this.gameObjectForIdentifier(identifier);
            if (object.valid()) { distances.put(object, this.distanceFrom(object)); }
        }

        return distances;
    }

    private <T> GameObject getClosestObject(List<T> identifiers)
    {
        HashMap<GameObject, Double> distances = this.calculateDistances(identifiers);
        return Collections.min(distances.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
    }

    /********* Actions *********/

    private void execute(AutoRetryAction action)
    {
        int totalRetries = 0;

        do
        {
            if (action.evaluate())
            {
                int randomA = this.random.nextInt(30) + 1;
                int randomB = this.random.nextInt(30) + 1;
                int randomC = this.random.nextInt(30) + 1;
                if (randomA % randomB == randomC) { this.randomSleep(0, 30); }

                break;
            }

            totalRetries++;

        } while (totalRetries < this.form.getRetryAttempts());

        JPSGeneralSmithing.STATE_FAILURE_ACTION.execute();
    }

    private void bank(List<Integer> bankIds, AutoRetryAction action, int defaultWait)
    {
        GameObject bank = this.getClosestObject(bankIds);

        if (!bank.valid())
        {
            this.setCurrentState(State.IDLE);
            return;
        }

        if (!bank.inViewport()) { this.execute(() -> this.goTo(bank)); }

        this.execute(() -> this.openBank(bank, defaultWait));
        this.execute(action);
        this.execute(() -> this.closeBank(defaultWait));
    }

    private void adjustPitch()
    {
        this.debugPrint("adjustPitch()");

        this.ctx.camera.pitch(this.random.nextInt(JPSGeneralSmithing.MAX_PITCH + 1) + JPSGeneralSmithing.MIN_PITCH);
    }

    private void adjustAngle()
    {
        this.debugPrint("adjustAngle()");

        this.ctx.camera.angle(this.random.nextInt(JPSGeneralSmithing.MAX_ANGLE + 1) + JPSGeneralSmithing.MIN_ANGLE);
    }

    private void adjustCameraIfNeeded()
    {
        this.debugPrint("adjustCameraIfNeeded()");

        if (this.ctx.camera.pitch() < JPSGeneralSmithing.MIN_PITCH) { this.adjustPitch(); }
    }

    private boolean goTo(GameObject object)
    {
        this.debugPrint("Go to: " + object.name());

        if (object.inViewport()) { return true; }

        this.ctx.camera.turnTo(object);

        return this.ctx.movement.step(object) && this.waitFor(object);
    }

    private boolean waitFor(GameObject object)
    {
        this.debugPrint("Wait for: " + object.name());

        return Condition.wait(object::inViewport);
    }

    private void randomSleep(int lowerBound, int upperBound)
    {
        int seconds = (this.random.nextInt(upperBound) + lowerBound) * 1000;

        this.debugPrint("Random sleep: " + (seconds / 1000) + "s");

        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    private boolean smeltUsing(GameObject object, int defaultWait)
    {
        this.debugPrint("Smelt using: " + object.name());

        boolean success;

        Widget furnaceWidget = this.ctx.widgets.select().id(JPSGeneralSmithing.SMITHING_WIDGET).peek();
        Component smeltButton = furnaceWidget.component(JPSGeneralSmithing.SMELT_BUTTON_ID);

        if (smeltButton.visible()) {
            success = smeltButton.click();
        }
        else {
            success = object.interact("Smelt");
            success = success && this.waitForSmeltButton(defaultWait);
            success = success && smeltButton.click();
        }

        return success;
    }

    private boolean waitForSmeltButton(int defaultWait)
    {
        this.debugPrint("waitForSmeltButton()");

        return Condition.wait(() ->
        {
            Widget furnaceWidget = this.ctx.widgets.select().id(JPSGeneralSmithing.SMITHING_WIDGET).peek();
            Component smeltButton = furnaceWidget.component(JPSGeneralSmithing.SMELT_BUTTON_ID);
            return smeltButton.valid() && smeltButton.visible();

        }, defaultWait);
    }

    private boolean waitUntilFinished(int defaultWait)
    {
        this.debugPrint("waitUntilFinished()");

        return Condition.wait(() ->
        {
            Widget widget = this.ctx.widgets.select().id(JPSGeneralSmithing.SMELT_STATUS_WIDGET_ID).peek();
            Component label = widget.component(JPSGeneralSmithing.TIME_REMAINING_LABEL_ID);
            return label.visible() && label.text().toLowerCase().contains("done");

        }, defaultWait);
    }

    private boolean openBank(GameObject bank, int defaultWait)
    {
        if (this.ctx.bank.opened()) { return true; }

        this.debugPrint("Open Bank: " + bank.name());

        boolean success = bank.interact("Use");
        if (!success) { this.adjustAngle(); }

        return success && this.waitUntilBank(true, defaultWait);
    }

    private boolean closeBank(int defaultWait)
    {
        this.debugPrint("closeBank()");

        return this.ctx.bank.close() && this.waitUntilBank(false, defaultWait);
    }

    private boolean waitUntilBank(boolean isOpen, int defaultWait)
    {
        this.debugPrint("Wait until bank is " + (isOpen ? "open" : "closed"));

        return Condition.wait(() -> isOpen == this.ctx.bank.opened(), defaultWait);
    }

    private boolean withdraw(HashMap<Integer, Integer> items, int defaultWait)
    {
        this.debugPrint("withdraw()");

        List<Integer> ids = this.backpackItemIds();

        for (int id : items.keySet())
        {
            int desiredAmount = items.get(id);
            int withdrawnAmount = this.occurrencesOfItem(id, ids);
            int delta = Math.abs(withdrawnAmount - desiredAmount);

            if (withdrawnAmount > desiredAmount) {
                this.ctx.bank.deposit(id, delta);
            }
            else if (withdrawnAmount < desiredAmount) {
                this.ctx.bank.withdraw(id, delta);
            }
        }

        return Condition.wait(() -> this.isValidWithdraw(items), defaultWait);
    }

    private boolean deposit(List<Integer> itemIds, int defaultWait)
    {
        this.debugPrint("deposit()");

        List<Integer> ids = this.backpackItemIds();

        for (int id : itemIds)
        {
            int remainingAmount = this.occurrencesOfItem(id, ids);
            if (remainingAmount > 0) { this.ctx.bank.deposit(id, remainingAmount); }
        }

        return Condition.wait(() -> this.isValidDeposit(itemIds), defaultWait);
    }
}
