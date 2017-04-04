package bn.inference;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class RejectionInferencer {

	/**
	function ELIMINATION-ASK(X, e, bn) returns a distribution over X
		inputs: X, the query variable
			e, observed values for variables E
			bn, a Bayesian network specifying joint distribution P(X1, . . . , Xn)
		factors <[ ]
		for each var in ORDER(bn.VARS) do
			factors <[MAKE-FACTOR(var , e)|factors]
			if var is a hidden variable then factors <SUM-OUT(var, factors )
		return NORMALIZE(POINTWISE-PRODUCT(factors))
	 */ 
	
	public static Distribution eliminationAsk(BayesianNetwork bn, RandomVariable X, Assignment a) {
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
