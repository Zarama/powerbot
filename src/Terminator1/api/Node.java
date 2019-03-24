package Terminator1.api;

import org.powerbot.script.rt4.*;

import java.util.logging.Logger;

/**
 * Created by Zack on 12/12/2016.
 */
public abstract class Node<C extends ClientContext> extends ClientAccessor implements NodeStructure {

    public String name;
    protected Logger log;

    public Node(C ctx) {
        super(ctx);
        Logger.getLogger("Log").info("Node has been created.");
    }

    public Node(C ctx,String name) {
        super(ctx);
        log = Logger.getLogger(""+name);
        log.info("Node["+name+"] has been created.");
    }

}
