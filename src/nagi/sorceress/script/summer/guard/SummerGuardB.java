package nagi.sorceress.script.summer.guard;

import nagi.sorceress.type.Guard;
import nagi.sorceress.type.Vector;

public class SummerGuardB extends Guard {

    public Vector[] path = new Vector[] { 
        new Vector(2907, 5490, 4), new Vector(2907, 5491, 4), new Vector(2907, 5492, 4), new Vector(2907, 5493, 4), new Vector(2907, 5494, 4), new Vector(2907, 5495, 4),
        new Vector(2907, 5495, 0), new Vector(2907, 5494, 0), new Vector(2907, 5493, 0), new Vector(2907, 5492, 0), new Vector(2907, 5491, 0), new Vector(2907, 5490, 0)
    };

	@Override
	public int getTag() {
		return 1802;
	}

	@Override
	public Vector[] getPath() {
		return this.path;
	}
}
