package bn.inference;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import bn.core.*;
import bn.util.*;

public class GibbsInferencer {

	static Random rand = new Random();

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

	public int expectedSizeofDist(RandomVariable X) {
		int s = X.getDomain().size();
		return s;
	}

	public Object randomValue(RandomVariable Zi) {
		//Object obj = null;
		int s = Zi.getDomain().size();
		return Zi.getDomain().get(rand.nextInt(s));
	}

	public static Object getRandSample(Distribution dist, RandomVariable Y) {

		double r = rand.nextDouble();

		Domain yDom = Y.getDomain();
		int size = yDom.size();

		double[] distValues = new double[size]; for (double d: distValues) { d = 0; } 	// All zeros initially
		double[] samples =  new double[size+1]; samples[0] = 0;							// First is zero

		int index = 0;
		for (Object y: yDom) {	// For each object in the domain of Y: 
			distValues[index] = dist.get(y); index ++;			// Store the next distribution value
			samples[index] = sumList(distValues);				// Store the sum of dist. 
																// values as the sample probability
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
		if (w == null) System.out.println("whoops");
		
		return w;

	}

	// copy
	public Distribution gibbsAsk(RandomVariable X, Assignment a, BayesianNetwork bn, int numSamples) {
		
		Distribution dist = new Distribution(X);
		for (Object o: X.getDomain()) { dist.put(o, 0); }		// Zero all weights
			
		// V: All vars
		List <RandomVariable> V = bn.getVariableListTopologicallySorted();
		List <RandomVariable> Z = new ArrayList<RandomVariable> (V);
		
		for (RandomVariable R: a.keySet()) {			// Remove all evidence vars from Z
			Z.remove(R);} 								// Now Z is the list of non-evidence variables
		Assignment state = a.copy();					// state = current state
		
		for (RandomVariable zi: Z) {
			Domain zDom = zi.getDomain();				// vDomain
			Distribution zDist = new Distribution(zi);	// vDistribution

			for (Object zVal: zDom) {					// For each value in the domain of Yv:
				Assignment k = state.copy();			// Instantiate 'state'
				k.put(zi, zVal);							// Add the value

				zDist.put(zVal, bn.getProb(zi, k));			// Update the distribution
			}

			Object chosen = getRandSample(zDist, zi);
			state.set(zi, chosen);						// Update the event

		}
		
		for (int n = 0; n < numSamples; n++) {
			for (RandomVariable zi: Z) {
				state.put(zi, gibbsSample(zi, state, bn));		// Insert a MBsample into the state
				
				System.out.println(dist.toString());
				
				dist.put(state.get(zi), (dist.get(state.get(zi)) + 1.0));	// Increment the distribution	
			}
		}
		
		dist.normalize();
		return dist;
	}

	// Markov Blanket Sampling
	private Object gibbsSample(RandomVariable zi, Assignment state, BayesianNetwork bn) {
		
		Domain zDom = zi.getDomain();				// vDomain
		Distribution zDist = new Distribution(zi);	// vDistribution

		for (Object zVal: zDom) {					// For each value in the domain of Yv:
			Assignment k = state.copy();			// Instantiate 'state'
			k.put(zi, zVal);							// Add the value
			double probability = bn.getProb(zi, k);
			Set <RandomVariable> ziChildren = bn.getChildren(zi);
			
			for (RandomVariable child: ziChildren) {
				probability *= bn.getProb(child, k);	// Big PI of children * parent prob
			}
			
			zDist.put(zVal, probability);			// Update the distribution
			zDist.normalize();
		}

		Object chosen = getRandSample(zDist, zi);
		state.set(zi, chosen);						// Update the state

		return chosen;
	}


	// copy


}
