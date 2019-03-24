package ih_justin.zammywine;

import org.powerbot.script.AbstractQuery;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to manipulate the world switcher interface.
 *
 */
public class Worlds extends AbstractQuery<Worlds, ih_justin.zammywine.World, ClientContext> {

    protected static final int WORLD_WIDGET = 69,
            LOGOUT_WIDGET = 182,
            WORLD_COMPONENT = 2;

    private ArrayList<ih_justin.zammywine.World> cache = new ArrayList<ih_justin.zammywine.World>();

    @Override
    protected List<ih_justin.zammywine.World> get() {
        ArrayList<ih_justin.zammywine.World> worlds = new ArrayList<ih_justin.zammywine.World>();
        Component list = list();
        if(list == null)
            return cache;
        Component[] comps = list.components();
        for(int off = 0; off < comps.length - 6; off += 6) {
            ih_justin.zammywine.World.Type type = ih_justin.zammywine.World.Type.forType(comps[off + 1].textureId());
            ih_justin.zammywine.World.Server server = ih_justin.zammywine.World.Server.forType(comps[off + 3].textureId());
            ih_justin.zammywine.World.Specialty special = ih_justin.zammywine.World.Specialty.get(comps[off + 5].text());
            int number = Integer.valueOf(comps[off + 2].text());
            int population = Integer.valueOf(comps[off + 4].text());
            worlds.add(new ih_justin.zammywine.World(ctx, number, population, type, server, special));
        }
        cache = new ArrayList<ih_justin.zammywine.World>(worlds);
        return worlds;
    }

    @Override
    protected Worlds getThis() {
        return this;
    }

    /**
     * A query of worlds which could be hopped to.
     *
     * @param ctx The client context.
     */
    public Worlds(ClientContext ctx) {
        super(ctx);
    }

    /**
     * Filters the worlds by types.
     *
     * @param types The types to target.
     * @return this instance for chaining purposes.
     */
    public Worlds types(final ih_justin.zammywine.World.Type... types) {
        return select(new Filter<ih_justin.zammywine.World>() {
            public boolean accept(ih_justin.zammywine.World world) {
                for(ih_justin.zammywine.World.Type t : types)
                    if(t.equals(world.type()))
                        return true;
                return false;
            }
        });
    }

    /**
     * Filters the worlds by specialties.
     *
     * @param specialties The specialties to target.
     * @return this instance for chaining purposes.
     */
    public Worlds specialties(final ih_justin.zammywine.World.Specialty... specialties) {
        return select(new Filter<ih_justin.zammywine.World>() {
            public boolean accept(ih_justin.zammywine.World world) {
                for(ih_justin.zammywine.World.Specialty s : specialties)
                    if(s.equals(world.specialty()))
                        return true;
                return false;
            }
        });
    }

    /**
     * Filters the worlds down to the specified servers.
     *
     * @param servers The server locations to filter.
     * @return This instance for chaining purposes.
     */
    public Worlds servers(final ih_justin.zammywine.World.Server... servers) {
        return select(new Filter<ih_justin.zammywine.World>() {
            public boolean accept(ih_justin.zammywine.World world) {
                for(ih_justin.zammywine.World.Server s : servers)
                    if(s.equals(world.server()))
                        return true;
                return false;
            }
        });
    }

    /**
     * Filters the worlds by player count. This will filter down to any world
     * which is less than or equal to the parameter.
     *
     * @param population The population the worlds should be less than or equal to.
     * @return this instance for chaining purposes.
     */
    public Worlds population(final int population) {
        return select(new Filter<ih_justin.zammywine.World>() {
            public boolean accept(ih_justin.zammywine.World world) {
                return world.size() <= population;
            }
        });
    }

    /**
     * Filters the worlds by joinable worlds. This will filter out any
     * dangerous or skill-required worlds.
     *
     * @return this instance for chaining purposes.
     */
    public Worlds joinable() {
        return select(new Filter<ih_justin.zammywine.World>() {
            public boolean accept(ih_justin.zammywine.World world) {
                return world.valid() &&
                        world.type() != ih_justin.zammywine.World.Type.DEAD_MAN &&
                        world.specialty() != ih_justin.zammywine.World.Specialty.PVP &&
                        world.specialty() != ih_justin.zammywine.World.Specialty.SKILL_REQUIREMENT;
            }
        });
    }

    /**
     * Opens the world switcher.
     *
     * @return <ii>true</ii> if successfully opened, <ii>false</ii> otherwise.
     */
    public boolean open() {
        ctx.game.tab(Game.Tab.LOGOUT);
        if(ctx.widgets.widget(WORLD_WIDGET).valid())
            return true;
        Component c = component(LOGOUT_WIDGET, "World Switcher");
        return c != null && c.click() && Condition.wait(new Condition.Check() {
            public boolean poll() {
                return ctx.widgets.widget(WORLD_WIDGET).valid();
            }
        }, 100, 20);
    }

    @Override
    public ih_justin.zammywine.World nil() {
        return ih_justin.zammywine.World.NIL;
    }

    protected final Component list() {
        for(Component c : ctx.widgets.widget(WORLD_WIDGET).components())
            if(c.width() == 174 && c.height() == 204)
                return c;
        return null;
    }

    protected final Component component(int widget, String text) {
        for(Component c : ctx.widgets.widget(widget).components())
            if(c.text().equalsIgnoreCase(text))
                return c;
        return null;
    }

    public ih_justin.zammywine.World current() {
        try {
            List<ih_justin.zammywine.World> worlds = get();
            int id = Integer.parseInt(ctx.widgets.component(WORLD_WIDGET, WORLD_COMPONENT)
                    .text().split("-")[1].trim()) - 300;
            for (ih_justin.zammywine.World w : worlds) {
                if (w.id() == id)
                    return w;
            }
        } catch(Exception ignored) {}
        return nil();
    }
}