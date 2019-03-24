package vaflis.lt.saltyjuice.dragas.powerbot.actions;

import org.powerbot.script.rt4.ClientContext;

public interface Action
{
    /**
     * Determines Whether or not this action is usable
     * @param ctx ClientContext context attached to current runescape client.
     * @return true, when this action can be executed, otherwise false
     */
    boolean isUsable(ClientContext ctx);

    /**
     * Executes defined action.
     * @param ctx ClientContext context attached to current runescape client.
     */
    void execute(ClientContext ctx);

    /**
     * Determines whether or not this action has succeeded and factory can return the next action.
     * @param ctx ClientContext context which is attached to runescape client.
     * @return true, when this action succeeded in executing, otherwise false.
     */
    boolean isFinished(ClientContext ctx);

    /**
     * Undoes last done action.
     * @param ctx ClientContext context which is attached to runescape client.
     */
    void undo(ClientContext ctx);
}
