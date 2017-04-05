package bn.inference;

import bn.core.RandomVariable;
import java.util.ArrayList;
import java.util.Collection;

public class weight {

	protected ArrayList<Object> objArray;
	protected ArrayList<Double> counts;

	public weight(ArrayList<Object> objArray, ArrayList<Double> counts) {
		super();
		this.objArray = objArray;
		this.counts = counts;
		
	}

	public weight(RandomVariable V) {
		this.objArray = V.getDomain();
		this.counts = new ArrayList<Double>(objArray.size());
		for (double d: counts) {
			d = 0;
		}
	}
	

	
	
}
