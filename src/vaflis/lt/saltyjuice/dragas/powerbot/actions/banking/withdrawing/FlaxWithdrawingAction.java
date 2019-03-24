package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.withdrawing;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;

public class FlaxWithdrawingAction extends WithdrawingAction
{
    @Override
    protected int getItemId()
    {
        return Constant.Item.FLAX;
    }

    @Override
    protected int getItemCount()
    {
        return 0;
    }
}
