package nagi.sorceress.script.summer.guard;

import nagi.sorceress.type.Guard;
import nagi.sorceress.type.Vector;

public class SummerGuardA extends Guard {

	public Vector[] path = new Vector[] { 
	    new Vector(2907, 5488, 0), new Vector(2907, 5487, 0), new Vector(2907, 5486, 0), new Vector(2907, 5485, 0), new Vector(2907, 5484, 0), new Vector(2907, 5483, 0), 
	    new Vector(2907, 5483, 4), new Vector(2907, 5484, 4), new Vector(2907, 5485, 4), new Vector(2907, 5486, 4), new Vector(2907, 5487, 4), new Vector(2907, 5488, 4)
	};
	
	@Override
	public int getTag() {
		return 1801;
	}

	@Override
	public Vector[] getPath() {
		return this.path;
	}
}
