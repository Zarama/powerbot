package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class ParticularBankOpeningAction extends BankOpeningAction
{
    private final Tile tile;
    private final int id;

    public ParticularBankOpeningAction(int id, Tile tile)
    {
        this.tile = tile;
        this.id = id;
    }
    @Override
    public void execute(ClientContext ctx)
    {
        ctx.objects.select().id(id).within(this.tile, 1).poll().interact("Bank");
    }
}
