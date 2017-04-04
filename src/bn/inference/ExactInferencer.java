package bn.inference;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class ExactInferencer {

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
