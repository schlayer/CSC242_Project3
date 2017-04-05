package bn.inference;

import java.util.List;

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
		
		//copy
		// Q(X) <- a distribution over X, initially empty
		// final ProbabilityTable Q = new ProbabilityTable(X);
		// final ObservedEvidence e = new ObservedEvidence(X, observedEvidence, bn);
		
		ProbabilityTable.Iterator di = new ProbabilityTable.Iterator() {
			int cnt = 0;

			/**
			 * <pre>
			 * Q(x<sub>i</sub>) <- ENUMERATE-ALL(bn.VARS, e<sub>x<sub>i</sub></sub>)
			 *   where e<sub>x<sub>i</sub></sub> is e extended with X = x<sub>i</sub>
			 * </pre>
			 */
			public void iterate(Map<RandomVariable, Object> possibleWorld, double probability) {
				for (int i = 0; i < X.length; i++) {
					e.setExtendedValue(X[i], possibleWorld.get(X[i]));
				}
				Q.setValue(cnt,
						enumerateAll(bn.getVariableList(), e));
				cnt++;
			}
		};
		Q.iterateOverTable(di);

		// return NORMALIZE(Q(X))
		return Q.normalize();


		//copy



		//return null;
		
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

	
	public static double exactEnumerateAll(List<RandomVariable> vars, Assignment a) {
		
		// copy
		// if EMPTY?(vars) then return 1.0
		if (0 == vars.size()) {
			return 1;
		}
		// Y <- FIRST(vars)
		RandomVariable Y = vars.get(0);
		// if Y has value y in e
		if (a.containsValue(Y)) {
			// then return P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e)
			return a.posteriorForParents(Y) * exactEnumerateAll(vars.subList(1, vars.size()), a);
		}
		/**
		 * <pre>
		 *  else return &sum;<sub>y</sub> P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e<sub>y</sub>)
		 *       where e<sub>y</sub> is e extended with Y = y
		 * </pre>
		 */
		double sum = 0;
		for (Object y: Y.getDomain()) {
			a.setExtendedValue(Y, y);
			sum += a.posteriorForParents(Y) * enumerateAll(vars.subList(1, vars.size()), a);
		}

		return sum;


		// copy
		
		return 0;
	}
	
	
	
}
