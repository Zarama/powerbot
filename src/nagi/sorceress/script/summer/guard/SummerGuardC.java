package nagi.sorceress.script.summer.guard;

import nagi.sorceress.type.Guard;
import nagi.sorceress.type.Vector;

public class SummerGuardC extends Guard {

    public Vector[] path = new Vector[] { 
        new Vector(2910, 5493, 0), new Vector(2910, 5492, 0), new Vector(2910, 5491, 0), new Vector(2910, 5490, 0), new Vector(2910, 5489, 0), new Vector(2910, 5488, 0), new Vector(2910, 5487, 0), 
        new Vector(2910, 5487, 4), new Vector(2910, 5488, 4), new Vector(2910, 5489, 4), new Vector(2910, 5490, 4), new Vector(2910, 5491, 4), new Vector(2910, 5492, 4), new Vector(2910, 5493, 4)
    };

	@Override
	public int getTag() {
		return 1803;
	}

	@Override
	public Vector[] getPath() {
		return this.path;
	}
}
