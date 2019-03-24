package nagi.sorceress;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import nagi.sorceress.misc.Statistics;
import nagi.sorceress.script.Season;
import nagi.sorceress.script.SeasonScript;
import nagi.sorceress.script.bank.BankScript;
import nagi.sorceress.script.summer.Summer;
import nagi.sorceress.script.summer.SummerHerbs;
import nagi.sorceress.type.ClientPhase;

@Script.Manifest(name = "Sorceress's Garden", description = "Picks herbs from Summer in the Sorceress's Garden. Banks at Shanty Pass.", properties = "client=4;topic=1338773;author=stormneo7")
public class SorceressGarden extends PollingScript<ClientContext> {

    private ClientPhase clientPhase;
    private Statistics stats;

    private Season season = null;
    private SeasonScript seasonScript = null;
    private BankScript bankScript;

    public void start() {
        this.ctx.input.speed(5);
        this.clientPhase = ClientPhase.SET_UP;
        this.bankScript = new BankScript(this, this.ctx);

        this.stats = new Statistics(this, this.ctx);
        this.ctx.dispatcher.add(this.stats);

        // new SettingsGUI(this);

        /**
         * TODO: This is just a hard-coded version of the summer herbs script.
         * Reflections doesn't appear to be usable on the SDN, although it works fine on local.
         * For better future-proofed code, check out src/stormneo7/sorceress/misc/SettingsGUI.java
         */

        this.season = new Summer(this);
        this.seasonScript = new SummerHerbs(this.season);

        this.getStatistics().resetStartTime();

        Tile tile = this.ctx().players.local().tile();
        if (this.ctx().inventory.select().count() < 28) {
            if (this.getSeason().inGarden(tile) || this.getSeason().inLobby(tile)) {
                this.setClientPhase(ClientPhase.COLLECTING);
            } else {
                this.getBankScript().generateReturnPath();
                this.setClientPhase(ClientPhase.RETURNING);
            }
        } else {
            this.getBankScript().generateBankPath();
            this.setClientPhase(ClientPhase.BANKING);
        }
    }

    public void poll() {
        if (this.season == null || this.seasonScript == null)
            return;

        switch (this.clientPhase) {
            case BANKING:
                this.bankScript.poll();
                break;
            case COLLECTING:
                this.seasonScript.poll();
                break;
            case RETURNING:
                this.bankScript.poll();
                break;
            case SET_UP:
                break;
        }
    }

    public void stopScript(String reason) {
        System.out.println(reason);
        this.ctx.controller.stop();
    }

    public ClientContext ctx() {
        return this.ctx;
    }

    public ClientPhase getClientPhase() {
        return this.clientPhase;
    }

    public void setClientPhase(ClientPhase clientPhase) {
        this.clientPhase = clientPhase;
    }

    public Season getSeason() {
        return this.season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public SeasonScript getSeasonScript() {
        return this.seasonScript;
    }

    public void setSeasonScript(SeasonScript seasonScript) {
        this.seasonScript = seasonScript;
    }

    public Statistics getStatistics() {
        return this.stats;
    }

    public BankScript getBankScript() {
        return this.bankScript;
    }
}
