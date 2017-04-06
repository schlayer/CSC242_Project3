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
	
	public static Object getRandSample(Distribution dist, RandomVariable Y) {
		
		double r = rand.nextDouble();
		
		Domain yDom = Y.getDomain();
		int size = yDom.size();
		
		double[] distValues = new double[size]; for (double d: distValues) { d = 0; } 	// All zeros
		double[] samples =  new double[size+1]; samples[0] = 0;							// First is zero
		
		int index = 0;
		for (Object y: yDom) {
			distValues[index] = dist.get(y);
			index ++;
			samples[index] = sumList(distValues);
			System.out.print(" Sample: " + samples[index]);
		}
		
		
		
		return Y;
		
	}

	
	public static Distribution likelihoodWeighting(BayesianNetwork bn, RandomVariable X, Assignment a, int numSamples) {
		
		Distribution dist = new Distribution(X);
		
		for (int n = 0; n < numSamples; n++) {
			// event, weight = Weighted Sample(bn,a)
			
			
		}
	
		dist.normalize();
		return dist;
		
	}
	
	public static double weightedSample(BayesianNetwork bn, Assignment a, RandomVariable X) {
		Assignment e = a.copy();
		List<RandomVariable> Y = bn.getVariableListTopologicallySorted();
		double weight = 1.0;
		for (RandomVariable Yv: Y) { // For all values y in the domain of Y
			if (e.containsKey(Yv)) {
				weight = weight * bn.getProb(Yv, e);
			} else {
				double r = Math.random();
				Domain d = Yv.getDomain();
				Distribution Ydist = new Distribution(Yv);

				for (Object y: d) {
					Assignment k = e.copy();
					k.put(Yv, y);

					Ydist.put(y, bn.getProb(Yv, k));
				}
			}
		}
		
		
		return 0;
	}
	
	/*
	The likelihood-weighting algorithm for inference in Bayesian networks. In
	WEIGHTED-SAMPLE, each nonevidence variable is sampled according to the conditional
	distribution given the values already sampled for the variable’s parents, while a weight is
	accumulated based on the likelihood for each evidence variable.
	 */

}
