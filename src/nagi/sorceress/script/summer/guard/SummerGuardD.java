package nagi.sorceress.script.summer.guard;

import nagi.sorceress.type.Guard;
import nagi.sorceress.type.Vector;

public class SummerGuardD extends Guard {

    public Vector[] path = new Vector[] { 
        new Vector(2912, 5485, 6), new Vector(2913, 5485, 6), new Vector(2914, 5485, 6), new Vector(2915, 5485, 6), 
        new Vector(2915, 5485, 0), new Vector(2915, 5484, 0), new Vector(2915, 5483, 0), 
        new Vector(2915, 5483, 6), new Vector(2916, 5483, 6), new Vector(2917, 5483, 6), new Vector(2918, 5483, 6), 
        new Vector(2918, 5483, 4), new Vector(2918, 5484, 4), new Vector(2918, 5485, 4), 
        new Vector(2918, 5485, 2), new Vector(2917, 5485, 2), new Vector(2916, 5485, 2), new Vector(2915, 5485, 2), 
        new Vector(2915, 5485, 0), new Vector(2915, 5484, 0), new Vector(2915, 5483, 0), 
        new Vector(2915, 5483, 2), new Vector(2914, 5483, 2), new Vector(2913, 5483, 2), new Vector(2912, 5483, 2), 
        new Vector(2912, 5483, 4), new Vector(2912, 5484, 4), new Vector(2912, 5485, 4)
    };

	@Override
	public int getTag() {
		return 1804;
	}

	@Override
	public Vector[] getPath() {
		return this.path;
	}
}
