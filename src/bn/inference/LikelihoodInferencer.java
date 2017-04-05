package bn.inference;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class LikelihoodInferencer {
	
	/**
	function LIKELIHOOD-WEIGHTING(X, e, bn,N) returns an estimate of P(X|e)
		inputs: X, the query variable
				e, observed values for variables E
				bn, a Bayesian network specifying joint distribution P(X1, . . . , Xn)
				N, the total number of samples to be generated
		local variables: W, a vector of weighted counts for each value of X, initially zero
		for j = 1 to N do
			x,w <WEIGHTED-SAMPLE(bn, e)
			W[x ]<W[x] + w where x is the value of X in x
		return NORMALIZE(W)

	 */ 
	
	public static Distribution likelihoodWeighting(BayesianNetwork bn, RandomVariable X, Assignment a) {
		return null;
		
	}
	
	/*
	function WEIGHTED-SAMPLE(bn, e) returns an event and a weight
				w <1; x<an event with n elements initialized from e
		foreach variable Xi in X1, . . . , Xn do
			if Xi is an evidence variable with value xi in e
				then w <w × P(Xi = xi | parents(Xi))
				else x[i]<a random sample from P(Xi | parents(Xi))
		return x, w
	 */

	
	public static double weightedSample(RandomVariable X, Assignment a) {
		
		return 0;
	}
	
	/*
	The likelihood-weighting algorithm for inference in Bayesian networks. In
	WEIGHTED-SAMPLE, each nonevidence variable is sampled according to the conditional
	distribution given the values already sampled for the variable’s parents, while a weight is
	accumulated based on the likelihood for each evidence variable.
	 */
	
}
