package ih_justin.zammywine;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

/**
 * @author Mooshe
 */
public class World extends ClientAccessor
        implements Nillable<World>, Validatable, Identifiable {

    public static final World NIL = new World(null, -1, Integer.MAX_VALUE,
            Type.UNKNOWN, Server.RUNE_SCAPE, Specialty.NONE);

    public enum Type {
        FREE(1130),
        MEMBERS(1131),
        DEAD_MAN(1237),
        PVP(1238),
        UNKNOWN(-1);

        public final int textureId;

        Type(int id) {
            this.textureId = id;
        }

        public static Type forType(int id) {
            for(Type t : values())
                if(t.textureId == id)
                    return t;
            return Type.UNKNOWN;
        }
    }

    public enum Specialty {
        NONE,
        TRADE,
        MINI_GAME,
        PVP,
        DEAD_MAN,
        SKILL_REQUIREMENT;

        public static Specialty get(String str) {
            if(str.contains("Trade"))
                return Specialty.TRADE;
            if(str.contains("PVP"))
                return Specialty.PVP;
            if(str.contains("Deadman"))
                return Specialty.DEAD_MAN;
            if(str.contains("skill t"))
                return Specialty.SKILL_REQUIREMENT;
            if(!str.contains("-"))
                return Specialty.MINI_GAME;
            return Specialty.NONE;
        }
    }

    public enum Server {
        RUNE_SCAPE(-1),
        NORTH_AMERICA(1133),
        GERMANY(1140),
        UNITED_KINGDOM(1135);

        public final int texture;

        Server(int texture) {
            this.texture = texture;
        }

        public static Server forType(int texture) {
            for(Server s : values())
                if(s.texture == texture)
                    return s;
            return RUNE_SCAPE;
        }
    }

    private final int number, population;
    private final Type type;
    private final Server server;
    private final Specialty specialty;

    public World(ClientContext ctx, int number, int population,
                 Type type, Server server, Specialty specialty) {
        super(ctx);
        this.number = number;
        this.population = population;
        this.type = type;
        this.server = server;
        this.specialty = specialty;
    }

    /**
     * Grabs the world number.
     *
     * @return The world number.
     */
    @Override
    public int id() {
        return number;
    }

    /**
     * The amount of players in the world.
     *
     * @return The player count.
     */
    public int size() {
        return population;
    }

    /**
     * The {@link Type} of world it is.
     *
     * @return The world type.
     */
    public Type type() {
        return type;
    }

    /**
     * The {@link Specialty} of the world.
     *
     * @return The world specialty.
     */
    public Specialty specialty() {
        return specialty;
    }

    /**
     * The {@link Server} the world is hosted on.
     *
     * @return The server location.
     */
    public Server server() {
        return server;
    }

    /**
     * Attempts to hop to this world.
     *
     * @return <ii>true</ii> if successfully hopped,
     * <ii>false</ii> otherwise.
     */
    public boolean hop() {
        if(!valid())
            return false;

        CHDLZammyWiner.worlds.open();

        Component list = CHDLZammyWiner.worlds.list();
        if(list == null || !list.visible()) {
            return false;
        }

        for(Component c : list.components()) {
            if(c.index() % 6 != 2 || !c.text().equalsIgnoreCase(""+number))
                continue;
            ctx.widgets.scroll(list, c, bar());
            if (c.click()) {
                return Condition.wait(new ClientStateCondition(45), 100, 20) &&
                        Condition.wait(new ClientStateCondition(30), 100, 100);
            }
        }
        return false;
    }

    private Component component(int widget, int texture) {
        for(Component c : ctx.widgets.widget(widget).components())
            if(c.textureId() == texture)
                return c;
        return null;
    }

    private Component bar() {
        for(Component c : ctx.widgets.widget(Worlds.WORLD_WIDGET).components())
            if(c.components().length == 6)
                return c;
        return null;
    }

    @Override
    public World nil() {
        return NIL;
    }

    @Override
    public boolean valid() {
        return this.id() > -1;
    }

    @Override

    public String toString() {
        return "World[id="+number+"/population="+population+"/type="+type+"/location="+
                server+"/specialty="+specialty+"]";
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof World) && ((World) o).number == number;
    }

    private class ClientStateCondition extends Condition.Check {

        private final int state;

        private ClientStateCondition(int state) {
            this.state = state;
        }


        @Override
        public boolean poll() {
            return ctx.game.clientState() == state;
        }
    }
}