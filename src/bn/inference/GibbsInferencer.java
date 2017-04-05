package bn.inference;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import bn.core.*;
import bn.util.*;

public class GibbsInferencer {

	Random rand = new Random();
	
	public int expectedSizeofDist(RandomVariable[] Z) {
		int total = 1;
		for (RandomVariable z: Z) {
			int s = z.getDomain().size();
			total *= s;
		}

		return total;
	}
	
	public Object randomValue(RandomVariable Zi) {
		//Object obj = null;
		int s = Zi.getDomain().size();
		return Zi.getDomain().get(rand.nextInt(s));
	}
	
	public int getOffset(Object value) {
		Integer idx = valueToIdx.get(value);
		if (null == idx) {
			throw new IllegalArgumentException("Value [" + value
					+ "] is not a possible value of this domain.");
		}
		return idx.intValue();
	}
	
	public static int indexOf(RandomVariable[] X, Map<RandomVariable, Object> x) {
		if (0 == X.length) {
			return X[0].getDomain().getOffset(x.get(X[0]));
		}
		
		int[] radixValues = new int[X.length];
		int[] radices = new int[X.length];
		int j = X.length - 1;
		for (int i = 0; i < X.length; i++) {
			Domain fd = X[i].getDomain();
			radixValues[j] = fd.getOffset(x.get(X[i]));
			radices[j] = fd.size();
			j--;
		}

		return new MixedRadixNumber(radixValues, radices).intValue();
	}
	
	// copy
	public Distribution gibbsAsk(RandomVariable[] X, Assignment e, BayesianNetwork bn, int Nsamples) {
		// local variables: <b>N</b>, a vector of counts for each value of X,
		// initially zero
		double[] N = new double[expectedSizeofDist(X)];
		// Z, the nonevidence variables in bn
		Set<RandomVariable> Z = new LinkedHashSet<RandomVariable>(bn.getVariableListTopologicallySorted());
		
		for ( Entry<RandomVariable, Object> ap: e.entrySet() ) { // for all Assignments:
			Z.remove(ap.getKey()); // remove the RandomVariable from the set of other vars
		}
		/*
		for (AssignmentProposition ap : e) {
			Z.remove(ap.getTermVariable());
		}*/
		// <b>x</b>, the current state of the network, initially copied from e
		Map<RandomVariable, Object> x = new LinkedHashMap<RandomVariable, Object>();
		for (Entry<RandomVariable, Object> ap: e.entrySet()) {
			x.put(ap.getKey(), ap.getValue());
		}

		// initialize <b>x</b> with random values for the variables in Z
		for (RandomVariable Zi : Z) {
			x.put(Zi, randomValue(Zi));
		}

		// for j = 1 to N do
		for (int j = 0; j < Nsamples; j++) {
			// for each Z<sub>i</sub> in Z do
			for (RandomVariable Zi : Z) {
				// set the value of Z<sub>i</sub> in <b>x</b> by sampling from
				// <b>P</b>(Z<sub>i</sub>|mb(Z<sub>i</sub>))
				x.put(Zi, randomValue(Zi)); // NEEDS specific distribution to be correct!!!!
			}
			// Note: moving this outside the previous for loop,
			// as described in fig 14.6, as will only work
			// correctly in the case of a single query variable X.
			// However, when multiple query variables, rare events
			// will get weighted incorrectly if done above. In case
			// of single variable this does not happen as each possible
			// value gets * |Z| above, ending up with the same ratios
			// when normalized (i.e. its still more efficient to place
			// outside the loop).
			//
			// <b>N</b>[x] <- <b>N</b>[x] + 1
			// where x is the value of X in <b>x</b>
			N[ProbUtil.indexOf(X, x)] += 1.0;
		}
		// return NORMALIZE(<b>N</b>)
		Distribution dist = new Distribution();
		return new ProbabilityTable(N, X).normalize();
	}
	
	
	// copy
	
	
}
