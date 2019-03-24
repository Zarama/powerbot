package nagi.sorceress.script.summer.guard;

import nagi.sorceress.type.Guard;
import nagi.sorceress.type.Vector;

public class SummerGuardF extends Guard {

    public Vector[] path = new Vector[] { 
        new Vector(2921, 5495, 0), new Vector(2921, 5494, 0), new Vector(2921, 5493, 0), new Vector(2921, 5492, 0), new Vector(2921, 5491, 0), 
        new Vector(2921, 5491, 6), new Vector(2922, 5491, 6), new Vector(2923, 5491, 6), 
        new Vector(2923, 5491, 4), new Vector(2923, 5492, 4), new Vector(2923, 5493, 4), new Vector(2923, 5494, 4), new Vector(2923, 5495, 4), 
        new Vector(2923, 5495, 2), new Vector(2922, 5495, 2), new Vector(2921, 5495, 2)
    };

	@Override
	public int getTag() {
		return 1806;
	}

	@Override
	public Vector[] getPath() {
		return this.path;
	}
}
