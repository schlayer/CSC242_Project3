package bn.inference;

import java.util.List;
import java.util.NoSuchElementException;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class ExactInferencer {
	
	private BayesianNetwork BN;
	
	public ExactInferencer(BayesianNetwork BN) {
		this.BN = BN;
	}

	public Distribution exactEnumerationAsk(BayesianNetwork bn, RandomVariable X, Assignment a) {
		Distribution Q = new Distribution(X);

		for (Object x: X.getDomain()) { // For each x (an Object in the domain of X):
			a.set(X, x); // a is the assignment where X = x
			Q.put(x, exactEnumerateAll(bn.getVariableListTopologicallySorted(), a)); // Add to the distribution
		}
		 
		//this.BN = bn; // ?
		
		Q.normalize();
		return Q;

	}


	public double exactEnumerateAll(List <RandomVariable> vars, Assignment a) {

		if (vars.size() == 0) { // If vars is empty, return 1.0
			return 1.0;
		}
		
		RandomVariable Y = vars.get(0); // First hidden variable
		List <RandomVariable> rest = vars.subList(1, vars.size()); // Remaining vars
		
		
		if (a.containsKey(Y)) { // if Y has some value yVal in the assignment:
			//System.out.print("no Y  ");
			return BN.getProb(Y, a) * exactEnumerateAll(rest, a); // return that probability
		}
		// Else... sum probabilities for all the possibilities for Y...
		double sum = 0;
		for (Object y: Y.getDomain()) { // For all values y in the domain of Y
			//System.out.print("y = " + y + "  ");
			Assignment e = a.copy();
			e.set(Y, y); // set Y = y in the assignment
			
			
			try {
				sum += BN.getProb(Y, e) * exactEnumerateAll(rest, e); // add probability to sum
			} catch (NoSuchElementException E) {
				//e.printStackTrace();
				System.out.println("Failed!");
			}
		}
		return sum; // ... then return that sum.

	}

	
}
