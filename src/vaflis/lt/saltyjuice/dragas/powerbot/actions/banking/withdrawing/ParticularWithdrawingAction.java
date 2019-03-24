package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.withdrawing;

public class ParticularWithdrawingAction extends WithdrawingAction
{
    private final int id;
    private final int count;

    public ParticularWithdrawingAction(int id, int count)
    {
        this.id = id;
        this.count = count;
    }
    @Override
    protected int getItemId()
    {
        return id;
    }

    @Override
    protected int getItemCount()
    {
        return count;
    }
}
