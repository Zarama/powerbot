package ih_justin.zammywine;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by Justin on 2/13/2017.
 */
public class WorldHop extends Task<ClientContext> {

    private final Tile wineTile = new Tile(2930, 3515);

    private World[] possibleWorlds = {new World(ctx, 37, 0, null, null, null),
                                      new World(ctx, 25, 0, null, null, null)};
                                      //new World(ctx, 54, 0, null, null, null)};

    private Random r = new Random();

    public WorldHop(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return wineTile.distanceTo(ctx.players.local()) < 5 &&
                Info.getInstance().tryWorldHop;
    }

    public void execute() {
        Info.getInstance().setCurrentTask("World hopping");

        Condition.sleep(r.nextInt(4000) + 4000);
        CHDLZammyWiner.worlds.open();
        CHDLZammyWiner.worlds.select(new Filter<World>() {
            @Override
            public boolean accept(World world) {
                return world.id() != CHDLZammyWiner.worlds.current().id();
            }
        }).joinable().types(World.Type.MEMBERS).servers(World.Server.NORTH_AMERICA); // Select worlds and filter to PVP worlds only

        CHDLZammyWiner.worlds.shuffle();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println(CHDLZammyWiner.worlds.peek());
                return CHDLZammyWiner.worlds.peek().hop();
            }
        }, 1000, 1);

        Info.getInstance().tryWorldHop = false;


    }
}
