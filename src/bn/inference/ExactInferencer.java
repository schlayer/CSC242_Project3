package bn.inference;

import java.util.List;
import java.util.Map;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class ExactInferencer {
	
	// This is used to determine if a FULL vars list or the REST of the list
	// is used in exactEnumerateAll. This changes the code so bn isn't
	// needed as an instance variable for this class, and bn is an argument
	// for enumeration instead of the vars list. 
	
	private final static int REST = 1357;
	private final static int FULL = 0;


	public static Distribution exactEnumerationAsk(BayesianNetwork bn, RandomVariable X, Assignment a) {
		Distribution Q = new Distribution(X);

		for (Object x: X.getDomain()) { // For each x (an Object in the domain of X):
			a.set(X, x); // a is the assignment where X = x
			Q.put(x, exactEnumerateAll(bn, a, FULL)); // Add to the distribution
		}

		Q.normalize();
		return Q;

	}

	

	public static double exactEnumerateAll(BayesianNetwork bn, Assignment a, int listType) {

		List <RandomVariable> vars = bn.getVariableList();
		if (listType == REST) { vars = vars.subList(1, vars.size()); }
		// copy

		if (vars.size() == 0) { // If vars is empty, return 1.0
			return 1.0;
		}

		RandomVariable Y = vars.get(0); // First hidden variable

		Object yVal = a.get(Y);
		System.out.println(yVal.toString());
		if (yVal != null) { // if Y has some value yVal in the assignment:
			return bn.getProb(Y, a) * exactEnumerateAll(bn, a, REST); // return that probability
		}
		// Else... sum probabilities for all the possibilities for Y...
		double sum = 0;
		for (Object y: Y.getDomain()) { // For all values y in the domain of Y
			a.set(Y, y); // set Y = y in the assignment
			sum += bn.getProb(Y, a) * exactEnumerateAll(bn, a, REST); // add probability to sum
		}

		return sum; // ... then return that sum.

	}

	
	
}
