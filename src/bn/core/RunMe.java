package bn.core;

import java.awt.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import bn.inference.*;
import bn.parser.*;

public class RunMe {
	/**
	 * This is the main class which is intended to be used to test the project. 
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Here be arguments:\n");
		for(String arg: args) {System.out.println(arg);}
		System.out.println("\n ^ args\n");

		String lang = args[0];			// Language
		String inferencer = args[1];	// Type of inferencer to use
		String[] parameters;
		String filename = "";

		// Parse the arguments
		if (!lang.equals("java")){ System.out.println("This is Java code!"); }

		int samples = 0;
		
		switch (inferencer){
		case "MyBNInferencer":
			System.out.println("Exact chosen!");
			filename = args[2];		// Name of XML or BIF file for probabilities
			parameters = Arrays.copyOfRange(args, 3, args.length);
			// select
			break;
		case "MyBNApproxInferencer":
			System.out.println("Likelihood chosen!");
			samples = Integer.parseInt(args[2]); // Grab samples, not filename

			System.out.println("Number of samples: " + samples + "."); 
			filename = args[3];		// Name of XML or BIF file for probabilities
			parameters = Arrays.copyOfRange(args, 4, args.length);
			// select
			break;
		case "MyBNGibbsInferencer":
			System.out.println("Gibbs chosen!");
			samples = Integer.parseInt(args[2]); // Grab samples, not filename

			System.out.println("Number of samples: " + samples + "."); 
			filename = args[3];		// Name of XML or BIF file for probabilities
			parameters = Arrays.copyOfRange(args, 4, args.length);
			// select
			break;

		default:
			System.out.println("Not a valid selection!");
			return;

		}
		
		
		String type = "XML";
		// Files
		if (filename.toLowerCase().endsWith(".bif")) { 
			System.out.println("BIF file."); type = "BIF";
		}
		else if (filename.toLowerCase().endsWith(".xml")) { 
			System.out.println("XML file."); type = "XML";
		}
		else { System.out.println("ERROR!"); return;}

		// Parameters
		System.out.println("\n\nParameters:");
		
		String[] evidenceVarNames = new String[parameters.length/2]; 	// If 5 parameters, 2 are evidence,
		Object[] evidenceValues = new Object[parameters.length/2]; 	// and 2 are values
		
		int evIndex = 0;
		for(int i = 0; i < parameters.length; i++) {
			String P = parameters[i];
			System.out.print(P + " ");
			
			if (i == 0) continue; 	// Skip ahead after query
			
			if (i % 2 == 1) { 				// Every other is a variable name
				//System.out.print("<< ");
				evidenceVarNames[evIndex] = P;
				evIndex ++;
			} else {						// The next item is always the value object
				evidenceValues[evIndex-1] = P;
			}
		}
		
		System.out.println("\n\nEvidence: ");
		for (String e: evidenceVarNames) System.out.print(e + "\t"); System.out.println("");
		for (Object e: evidenceValues) System.out.print(e + "\t");

		String queryVarName = parameters[0];
		System.out.println("\n\nPrinting distribution for: " + queryVarName);
		
		/* Time to actually do things! */
		try {
			System.out.println("\nAttempting to read file: " + filename + " at location");
			String newpath = "src/bn/examples/" + filename; System.out.println(newpath); // Correct the path to /examples/
			
			BayesianNetwork BN = new BayesianNetwork();
			
			if (type.equals("BIF")) {
				BIFParser parser = new BIFParser(new FileInputStream(newpath));

				BN = parser.parseNetwork();
			}
			else {
				XMLBIFParser parser = new XMLBIFParser();
				
				BN = parser.readNetworkFromFile(newpath);
			}
			
			RandomVariable query = BN.getVariableByName(queryVarName);
			
			Assignment A = new Assignment();
			RandomVariable[] evidence = new RandomVariable[evidenceValues.length];
			for (int i = 0; i < evidence.length; i++) {		// Get RandomVariables for each evidence value
				evidence[i] = BN.getVariableByName(evidenceVarNames[i]);
				A.set(evidence[i], evidenceValues[i]);		// Add the evidence var/val to the set of assignments 
			}
			
			
			///BN.print(System.out);
			

			System.out.println("\n\nStarting Inferencing... \n");
			
			
			if (inferencer.equals("MyBNInferencer")) {
				ExactInferencer inf = new ExactInferencer(BN);
				
				final long startTime = System.currentTimeMillis();
				Distribution dist = inf.exactEnumerationAsk(BN, query, A);
				final long endTime = System.currentTimeMillis();
				
				System.out.println("Completed in " + (endTime-startTime) + " ms.");
				System.out.println("\n\nProbabilities:" + dist.toString());
			}
			
			if (inferencer.equals("MyBNApproxInferencer")) {
				if (samples < 1000) System.out.println("WARNING: Insufficient sample count! May be unreliable.");
				
				LikelihoodInferencer inf = new LikelihoodInferencer();
				
				final long startTime = System.currentTimeMillis();
				Distribution dist = inf.likelihoodWeighting(BN, query, A, samples);
				final long endTime = System.currentTimeMillis();
				
				System.out.println("Completed " + samples + " samples in " + (endTime-startTime) + " ms.");
				System.out.format("Average %.3f samples/ms. \n", (double)(samples/(endTime-startTime+0.0)));
				
				System.out.println("\n\nProbabilities:" + dist.toString());
			}
			
			if (inferencer.equals("MyBNGibbsInferencer")) {
				if (samples < 1000) System.out.println("WARNING: Insufficient sample count! May be unreliable.");
				
				GibbsInferencer inf = new GibbsInferencer();
				
				final long startTime = System.currentTimeMillis();
				Distribution dist = inf.gibbsAsk(query, A, BN, samples);
				final long endTime = System.currentTimeMillis();
				
				System.out.println("Completed " + samples + " samples in " + (endTime-startTime) + " ms.");
				System.out.format("Average %.3f samples/ms. \n", (double)(samples/(endTime-startTime+0.0)));
				
				System.out.println("\n\nProbabilities:" + dist.toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
