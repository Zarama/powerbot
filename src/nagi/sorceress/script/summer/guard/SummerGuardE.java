package nagi.sorceress.script.summer.guard;

import nagi.sorceress.type.Guard;
import nagi.sorceress.type.Vector;

public class SummerGuardE extends Guard {

    public Vector[] path = new Vector[] { 
        new Vector(2923, 5490, 0), new Vector(2923, 5489, 0), new Vector(2923, 5488, 0), new Vector(2923, 5487, 0), new Vector(2923, 5486, 0), 
        new Vector(2923, 5486, 2), new Vector(2922, 5486, 2), new Vector(2921, 5486, 2), 
        new Vector(2921, 5486, 6), new Vector(2922, 5486, 6), new Vector(2923, 5486, 6), 
        new Vector(2921, 5486, 4), new Vector(2921, 5487, 4), new Vector(2921, 5488, 4), new Vector(2921, 5489, 4), new Vector(2921, 5490, 4) 
    };

	@Override
	public int getTag() {
		return 1805;
	}

	@Override
	public Vector[] getPath() {
		return this.path;
	}
}
