package bn.inference;

import bn.core.*;

/**
 * Defines the interface supported by implementations of the
 * inference algorithms for Bayesian Networks.
 */
public interface Inferencer {
	
	/*
	 * Returns the Distribution of the query RandomVariable X
	 * given evidence Assignment e, using the distribution encoded
	 * by the BayesianNetwork bn.
	 * 
	 * Note that some algorithms may require additional parameters, for example
	 * the number of samples for approximate inferencers. You can have methods
	 * that accept those parameters and use them in your testing. Just implement
	 * this method using some reasonable default value to satisfy the interface.
	 * 
	 * Or don't implement this interface at all. It's really here to guide you
	 * as to what an inferencer should do (namely, compute a posterior distribution).
	 */
	public static Distribution ask(BayesianNetwork bn, RandomVariable X, Assignment a){
		Distribution d = new Distribution();
		return d;
	}
	

	/**
	function ENUMERATION-ASK(X, e, bn) returns a distribution over X
		inputs: X, the query variable
				e, observed values for variables E
				bn, a Bayes net with variables {X} u  E u  Y // Y = hidden variables 
		Q(X)<a distribution over X, initially empty
		for each value xi of X do
			Q(xi)<ENUMERATE-ALL(bn.VARS, exi )
				where exi is e extended with X = xi
		return NORMALIZE(Q(X))
	 */ 
	
	public static Distribution exactEnumerationAsk(BayesianNetwork bn, RandomVariable X, Assignment a) {
		return null;
		
	}
	
	/*
	function ENUMERATE-ALL(vars, e) returns a real number
		if EMPTY?(vars) then return 1.0
		Y <FIRST(vars)
		if Y has value y in e
			then return P(y | parents(Y )) × ENUMERATE-ALL(REST(vars), e)
			else return sum y P(y | parents(Y )) × ENUMERATE-ALL(REST(vars), ey)
				where ey is e extended with Y = y
	 */

	
	public static double exactEnumerateAll(RandomVariable X, Assignment a) {
		
		return 0;
	}
	
	
	

}
