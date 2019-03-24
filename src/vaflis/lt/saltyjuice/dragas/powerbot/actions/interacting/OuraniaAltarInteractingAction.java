package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import org.powerbot.script.rt4.ClientContext;

public class OuraniaAltarInteractingAction extends ParticularObjectInteractingAction
{

    public OuraniaAltarInteractingAction()
    {
        super(29631, "Craft-rune");
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.inventory.select().id(Constant.Item.PURE_ESSENCE).count() == 0;
    }
}
