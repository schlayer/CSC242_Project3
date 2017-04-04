package bn.core;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bn.parser.XMLBIFParser;

public class RunMe {
	/**
	 * This is the main class which is intended to be used to test the project. 
	 * @param args
	 */

	static XMLBIFParser parser;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Here be arguments:\n");
		for(String arg: args) {System.out.println(arg);}
		System.out.println("\n ^ args\n");

		String lang = args[0];			// Language
		String inferencer =  args[1];	// Type of inferencer to use
		String[] parameters;
		String filename = "";

		// Parse the arguments
		if (!lang.equals("java")){ System.out.println("This is Java code!"); }

		switch (inferencer){
		case "MyBNInferencer":
			System.out.println("Inferencer chosen!");
			filename = args[2];		// Name of XML or BIF file for probabilities
			parameters = Arrays.copyOfRange(args, 3, args.length);
			// select
			break;
		case "MyBNApproxInferencer":
			System.out.println("Approximate chosen!");
			int samples = Integer.parseInt(args[2]); // Grab samples, not filename

			System.out.println("Number of samples: " + samples + "."); 
			filename = args[3];		// Name of XML or BIF file for probabilities
			parameters = Arrays.copyOfRange(args, 4, args.length);
			// select
			break;

		default:
			System.out.println("Not a valid selection!");
			return;

		}

		// Files
		if (filename.toLowerCase().endsWith(".bif")) { System.out.println("BIF file."); }
		else if (filename.toLowerCase().endsWith(".xml")) { System.out.println("XML file."); }
		else { System.out.println("ERROR!"); return;}

		// Parameters
		System.out.println("\n\nParameters:");
		for(String P: parameters) {
			System.out.print(P + " ");
		}

		String queryVar = parameters[0];
		System.out.println("\nPrinting distribution for: " + queryVar);
		
		/* Time to actually do things! */
		
		BayesianNetwork BN;
		try {
			System.out.println("\n Attempting to read file: " + filename);
			String newpath = "../examples/" + filename; System.out.println(newpath);
			BN = parser.readNetworkFromFile(newpath);
			
			
			
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
