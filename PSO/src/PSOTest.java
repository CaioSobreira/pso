import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class PSOTest {

	public static void main(String[] args) {
		
		
		//SET PARAMETERS BELOW TO RUN PSO:

		int numParticles = 30;
				
		int numSimulations = 30;
		
		//int numIterations = 10000;
		int maxFitnessReadings = 500000;
		
		//sphere, rosenbrock or rastrigin
		String fitnessFunction = "sphere";
		
		//DIMENSIONS -> SPHERE (-100, 100) | ROSENBROCK (-30, 30) | RASTRIGIN (-5.12, 5.12)
		double dimensionLowerBound = -100.0;
		double dimensionUpperBound = 100.0;
		
		//SPHERE (50, 100) | ROSENBROCK (15, 30) | RASTRIGIN (2.56, 5.12)
		double dimensionInitSubsPaceLowerBound = 50.0;
		double dimensionInitSubSpaceUpperBound = 100.0;
		
		int numDimensions = 30;
	
		//global, focal or ring
		String topology = "ring";	
		
		double inertiaWeight = 0.9;
		
		boolean inertiaWeightDecay = true;
		
		boolean clercFactor = false;
		
		boolean limitVelocity = true;
		
		boolean limitPosition = true;
		
		Dimension[] dimensions = new Dimension[numDimensions];

		for(int i = 0; i < dimensions.length; i++) { 
			dimensions[i] = new Dimension(dimensionLowerBound, dimensionUpperBound, dimensionInitSubsPaceLowerBound, dimensionInitSubSpaceUpperBound); 
		}
	
		SearchSpace searchSpace = new SearchSpace(dimensions);		

		double[][] simulationsResult = new double[numSimulations][];

		for (int i = 0; i < numSimulations; i++) {
			System.out.println(i);

			//parameters: numParticles, searchSpace, topology, fitnessFunction, numIterations, inertiaWeight, inertiaWeightDecay, clercFactor, limitVelocity, limitPosition
			
			PSO pso = new PSO(numParticles, searchSpace, topology, fitnessFunction, maxFitnessReadings, inertiaWeight, inertiaWeightDecay, clercFactor, limitVelocity, limitPosition);

			simulationsResult[i] = pso.run();

		}
		
		//EXPORTING RESULT TO CSV FILE:
		try {
			
			String strLowerBound = new Double(dimensionLowerBound).toString().replace("-", "minus").replace(".", "dot");
			String strUpperBound = new Double(dimensionUpperBound).toString().replace("-", "minus").replace(".", "dot");
			String strInitSubspaceLowerBound = new Double(dimensionInitSubsPaceLowerBound).toString().replace("-", "minus").replace(".", "dot");
			String strInitSubspaceUpperBound = new Double(dimensionInitSubSpaceUpperBound).toString().replace("-", "minus").replace(".", "dot");
			String strInertiaWeight = new Double(inertiaWeight).toString().replace(".", "dot");
			String strInertiaWeightDecay = inertiaWeightDecay ? "yes" : "no";
			String strClercFactor = clercFactor ? "yes" : "no";
			String strLimitVelocity = limitVelocity ? "yes" : "no";
			String strLimitPosition = limitPosition ? "yes" : "no";
			
			String fileName = "pso_numsimul-" + numSimulations + "_searchspace-" + strLowerBound + "to" + strUpperBound +
					"_initsubspace-" + strInitSubspaceLowerBound + "to" + strInitSubspaceUpperBound +
					"_numdimensions-" + numDimensions + "_topology-" + topology + "_fitnessfunction-" + fitnessFunction + 
					"_maxfitnessreadings-" + maxFitnessReadings + "_inertiaweight-" + strInertiaWeight + "_inertiadecay-" + strInertiaWeightDecay +
					"_clercfactor-" + strClercFactor + "_limitvelocity-" + strLimitVelocity + "_limitposition-" + strLimitPosition +
					".csv";

			PrintWriter writer = new PrintWriter(new File(fileName));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < numSimulations; i++) {

				sb.append("sim_" + (i+1));
				if (i < (numSimulations-1)) {
					sb.append(',');
				} else {
					sb.append('\n');
				}

			}
			
			for(int i = 0; i < (maxFitnessReadings/10); i++) {
				for(int j = 0; j < numSimulations; j++) {
					sb.append(simulationsResult[j][i]);
					if(j < (numSimulations-1)) {
						sb.append(",");
					}
				}
				sb.append("\n");
				
			}
			
			writer.write(sb.toString());
					
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
