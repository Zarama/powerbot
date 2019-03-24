package DieselSkrt.DRuneRunner;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created by Shane on 15-1-2018.
 */
public abstract class Task<C extends ClientContext> extends ClientAccessor {

    protected final int[] PORTALID = {14846, 14841, 14844};
    protected final int[] ESSENCE = {7936, 1436};
    protected final int[] RUINSID = {14399, 14409, 14405};
    protected final int[] craftAltar = {14902,14900,14897};

    public Task(C ctx){
        super(ctx);
    }

    public abstract boolean activate();
    public abstract void execute();
}
