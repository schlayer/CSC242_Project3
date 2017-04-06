package bn.inference;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bn.core.*;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class LikelihoodInferencer {

	private static Random rand = new Random();

	public LikelihoodInferencer() {}

	// Sums all doubles in a list
	public static double sumList(double[] distValues) {
		double sum = 0;
		for (double n: distValues) { sum += n; }

		return sum;
	}

	// Prints all doubles in a list
	public static void printList(double[] list) {
		System.out.print("\t[");
		for (int i = 0; i < list.length; i++) { 
			double n = list[i];
			if (i == list.length-1) {
				System.out.print(n + "]");
				return;
			}
			System.out.print(n + ", "); 
		}

	}

	// Gets a random sample based on values in a distribution of a given variable
	public static Object getRandSample(Distribution dist, RandomVariable Y) {

		double r = rand.nextDouble();

		Domain yDom = Y.getDomain();
		int size = yDom.size();

		double[] distValues = new double[size]; for (double d: distValues) { d = 0; } 	// All zeros initially
		double[] samples =  new double[size+1]; samples[0] = 0;							// First is zero

		int index = 0;
		for (Object y: yDom) {	// For each object in the domain of Y: 
			distValues[index] = dist.get(y); index ++;			// Store the next distribution value
			samples[index] = sumList(distValues);				// Store the sum of dist. values as the sample probability
			//System.out.print(" Sample: " + samples[index]);		
		}

		//printList(distValues); 	// debug
		//printList(samples);		// debug
		//System.out.println("");

		// Now that we have a list of sample values [0.0, 0.1, 0.3, 0.45, 0.89, 1.0]
		// We can choose an index (i) pseudorandomly with weighting determined by the size of gaps
		// in between each value in sample[]
		Object w = null;
		// Find where Random # falls in sample[]
		for (int i = 0; i < samples.length-1; i++) {
			if (r > samples[i]) {
				if (r < samples[i+1]) {
					w = yDom.get(i); 		// w is the Object from the domain at the index 
					return w;
				}
			}
		}

		return w;

	}


	public static Distribution likelihoodWeighting(BayesianNetwork bn, RandomVariable X, Assignment a, int numSamples) {

		Distribution dist = new Distribution(X);
		for (Object o: X.getDomain()) { dist.put(o, 0); }	// Zero all weights

		for (int n = 0; n < numSamples; n++) {
			// event, weight = Weighted Sample(bn,a)
			Pair wSample = weightedSample(bn, a, X);
			Assignment event = wSample.event;
			double weight = wSample.weight;

			Object xVal = event.get(X);						// Value of X in the event

			double old = dist.get(xVal);					// Get prior weight
			dist.put(xVal, weight + old);					// Add new weight to it
		}

		dist.normalize();							// Normalize and return distribution
		return dist;

	}

	public static Pair weightedSample(BayesianNetwork bn, Assignment a, RandomVariable X) {
		Assignment e = a.copy();
		List<RandomVariable> Y = bn.getVariableListTopologicallySorted();
		double weight = 1.0;								// weight defaults to 1
		//Pair weightedPair = new Pair(e, weight);			// Initialize the Pair

		for (RandomVariable Yv: Y) { 		// For all Random Variables Yv in the List Y
			if (a.containsKey(Yv)) {		// If Y is consistent with the assignment...
				weight = weight * bn.getProb(Yv, e);		// Scale weight appropriately
			} else {
				
				Domain yDom = Yv.getDomain();				// yDomain
				Distribution Ydist = new Distribution(Yv);	// yDistribution


				for (Object y: yDom) {				// For each value in the domain of Yv:
					Assignment k = e.copy();				// Instantiate 'event' e
					k.put(Yv, y);							// Add the value

					Ydist.put(y, bn.getProb(Yv, k));		// Update the distribution
				}

				Object chosen = getRandSample(Ydist, Yv);
				e.set(Yv, chosen);							// Update the event

			}
		}

		Pair weightedPair = new Pair(e, weight);	// Assign proper event and weight

		return weightedPair;
	}

	/*
	"The likelihood-weighting algorithm for inference in Bayesian networks. In
	WEIGHTED-SAMPLE, each nonevidence variable is sampled according to the conditional
	distribution given the values already sampled for the variable’s parents, while a weight is
	accumulated based on the likelihood for each evidence variable." - AIMA figure 14.15
	 */

}
