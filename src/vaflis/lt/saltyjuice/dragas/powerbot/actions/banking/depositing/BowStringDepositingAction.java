package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.depositing;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;

public class BowStringDepositingAction extends DepositingAction
{
    @Override
    protected int getItemId()
    {
        return Constant.Item.BOW_STRING;
    }

    @Override
    protected int getItemCount()
    {
        return 28;
    }
}
