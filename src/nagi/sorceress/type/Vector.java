package nagi.sorceress.type;

public class Vector {

    public int x;
    public int y;
    public int dir;

    public Vector(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector) {
            Vector o = (Vector) obj;
            return o.x == this.x && o.y == this.y && o.dir == this.dir;
        }

        return false;
    }
}