package nagi.sorceress.type;

import org.powerbot.script.Tile;

public class PsuedoTile extends Tile {

    public PsuedoTile(int x, int y) {
        super(x, y);
    }

    public PsuedoTile(int x, int y, int floor) {
        super(x, y, floor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PsuedoTile) {
            PsuedoTile tile = (PsuedoTile) obj;
            return tile.x() == this.x() && tile.y() == this.y();
        }
        return false;
    }
}