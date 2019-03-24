package ih_justin.zammywine;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.TilePath;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(
        name = "CHDLZammyWiner", properties = "author=ih_justin; client=4;",
        description = "Grabs the wine, then banks and returns."
)
public class CHDLZammyWiner extends PollingScript<ClientContext> implements PaintListener {

    public static Worlds worlds;

    private static final Tile[] templeTiles = {
        new Tile(2947, 3368, 0),
        new Tile(2946, 3372, 0),
        new Tile(2947, 3368, 0),
        new Tile(2946, 3372, 0),
        new Tile(2948, 3376, 0),
        new Tile(2951, 3379, 0),
        new Tile(2955, 3381, 0),
        new Tile(2959, 3382, 0),
        new Tile(2963, 3385, 0),
        new Tile(2963, 3389, 0),
        new Tile(2964, 3393, 0),
        new Tile(2965, 3397, 0),
        new Tile(2965, 3401, 0),
        new Tile(2965, 3405, 0),
        new Tile(2965, 3409, 0),
        new Tile(2963, 3413, 0),
        new Tile(2960, 3416, 0),
        new Tile(2956, 3418, 0),
        new Tile(2953, 3421, 0),
        new Tile(2952, 3425, 0),
        new Tile(2952, 3429, 0),
        new Tile(2951, 3433, 0),
        new Tile(2948, 3436, 0),
        new Tile(2948, 3440, 0),
        new Tile(2948, 3444, 0),
        new Tile(2948, 3448, 0),
        new Tile(2948, 3452, 0),
        new Tile(2946, 3456, 0),
        new Tile(2945, 3460, 0),
        new Tile(2945, 3464, 0),
        new Tile(2945, 3468, 0),
        new Tile(2945, 3472, 0),
        new Tile(2945, 3476, 0),
        new Tile(2945, 3480, 0),
        new Tile(2945, 3484, 0),
        new Tile(2942, 3487, 0),
        new Tile(2942, 3491, 0),
        new Tile(2939, 3494, 0),
        new Tile(2942, 3497, 0),
        new Tile(2941, 3501, 0),
        new Tile(2941, 3505, 0),
        new Tile(2941, 3509, 0),
        new Tile(2941, 3513, 0),
        new Tile(2942, 3517, 0),
        new Tile(2938, 3517, 0),
        new Tile(2934, 3516, 0),
        new Tile(2932, 3515, 0)
    };

    private static Tile[] bankTiles = {
        new Tile(2967, 3377, 0),
        new Tile(2963, 3377, 0),
        new Tile(2959, 3378, 0),
        new Tile(2955, 3379, 0),
        new Tile(2951, 3378, 0),
        new Tile(2947, 3375, 0),
        new Tile(2946, 3371, 0),
        new Tile(2947, 3368, 0)
    };

    private List<Task> taskList = new ArrayList<Task>();


    @Override
    public void start() {
        worlds = new Worlds(ctx);

        if (ctx.camera.pitch() < 99) {
            ctx.camera.pitch(true);
        }

        TilePath toBank = ctx.movement.newTilePath(bankTiles);
        TilePath toTemple = ctx.movement.newTilePath(templeTiles);

        taskList.addAll(Arrays.asList(new WalkToBank(ctx, toBank), new ih_justin.zammywine.Bank(ctx), new WalkToTemple(ctx, toTemple), new Telegrab(ctx)));
    }

    @Override
    public void poll() {
        for (Task t : taskList) {
            if (t.activate()) {
                t.execute();
            }
        }
    }

    @Override
    public void stop() {
        log.info("finished");
    }

    private static String timeConversion(long seconds) {

        final long MINUTES_IN_AN_HOUR = 60;
        final long SECONDS_IN_A_MINUTE = 60;

        long minutes = seconds / SECONDS_IN_A_MINUTE;
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        long hours = minutes / MINUTES_IN_AN_HOUR;
        minutes -= hours * MINUTES_IN_AN_HOUR;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA);

        int gp = Info.getInstance().getWineCollected() * Info.getInstance().getWinePrice();
        int gpPerHour = (int)((gp * 3600000D) / getRuntime());

        g.drawString("Runtime: " + timeConversion(getRuntime() / 1000), 10, 30);
        g.drawString("Current Task: " + Info.getInstance().getCurrentTask(), 10, 45);
        g.drawString(String.format("Wines collected: %,d", Info.getInstance().getWineCollected()), 10, 60);
        g.drawString(String.format("GP (GP/HR): %,d (%,d)", gp, gpPerHour), 10, 75);
        g.drawString(String.format("Law runes used: %,d", Info.getInstance().getLawRunesUsed()), 10, 90);

    }

}