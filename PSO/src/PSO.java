import java.util.concurrent.ThreadLocalRandom;

public class PSO {

	private int numParticles;

	private Particle[] swarm;

	private SearchSpace searchSpace;

	// 'global', 'ring' or 'focal'
	private String topology;

	// 'sphere', 'rastrigin' or 'rosenbrock'
	private String fitnessFunction;

	// private int numIterations;

	private int maxFitnessReadings;

	private double inertiaWeight;

	private boolean inertiaWeightDecay;

	private boolean clercFactor;

	private boolean limitVelocity;

	private boolean limitPosition;

	public PSO(int numParticles, SearchSpace searchSpace, String topology, String fitnessFunction,
			/* int numIterations, */ int maxFitnessReadings, double inertiaWeight, boolean inertiaWeightDecay,
			boolean clercFactor, boolean limitVelocity, boolean limitPosition) {
		super();
		this.numParticles = numParticles;
		this.searchSpace = searchSpace;
		this.topology = topology;
		this.fitnessFunction = fitnessFunction;
		// this.numIterations = numIterations;
		this.maxFitnessReadings = maxFitnessReadings;
		this.inertiaWeight = inertiaWeight;
		this.inertiaWeightDecay = inertiaWeightDecay;
		this.clercFactor = clercFactor;
		this.limitVelocity = limitVelocity;
		this.limitPosition = limitPosition;
		this.generateParticles();
	}

	private void generateParticles() {

		this.swarm = new Particle[numParticles];

		Particle[] neighborHood;

		switch (this.topology) {

		case "global":
			for (int i = 0; i < numParticles; i++) {

				swarm[i] = new Particle(searchSpace, this.fitnessFunction, this.clercFactor, this.limitVelocity,
						this.limitPosition);
			}

			for (int i = 0; i < numParticles; i++) {

				swarm[i].setNeighborHood(this.swarm);
			}

			break;

		case "ring":

			for (int i = 0; i < numParticles; i++) {
				swarm[i] = new Particle(searchSpace, this.fitnessFunction, this.clercFactor, this.limitVelocity,
						this.limitPosition);
			}

			for (int j = 0; j < numParticles; j++) {

				neighborHood = new Particle[2];

				if (j == 0) {
					neighborHood[0] = swarm[numParticles - 1];
					neighborHood[1] = swarm[j + 1];
				} else if (j == numParticles - 1) {
					neighborHood[0] = swarm[j - 1];
					neighborHood[1] = swarm[0];
				} else {
					neighborHood[0] = swarm[j - 1];
					neighborHood[1] = swarm[j + 1];
				}

				swarm[j].setNeighborHood(neighborHood);
			}

			break;

		case "focal":

			for (int i = 0; i < numParticles; i++) {
				swarm[i] = new Particle(searchSpace, this.fitnessFunction, this.clercFactor, this.limitVelocity,
						this.limitPosition);
			}

			int focalPointIndex = ThreadLocalRandom.current().nextInt(0, numParticles);

			Particle focalPoint = swarm[focalPointIndex];

			focalPoint.setNeighborHood(swarm);

			for (int j = 0; j < numParticles; j++) {

				if (j != focalPointIndex) {
					swarm[j].setNeighborHood(new Particle[] { focalPoint });
				}
			}

			break;
		}

	}

	public double[] run() {

		// double[] gBestList = new double[numIterations];
		double[] gBestList = new double[maxFitnessReadings / 10];
		int gBestListIdx = 0;

		double bestFitness = Double.MAX_VALUE;

		// if 'inertiaWeightDecay' is 'true', 'n' variable will slice the interval
		// (number of iterations) in 6 equal parts (round up) to 'w' decay
		// int n = numIterations / 6 + ((numIterations % 6 == 0) ? 0 : 1);
		int n = maxFitnessReadings / 6 + ((maxFitnessReadings % 6 == 0) ? 0 : 1);
		int nextDecay = n;

		int numFitnessReadings = 0;

		// for (int i = 0; i < this.numIterations; i++) {
		while (numFitnessReadings < maxFitnessReadings) {

			if (this.inertiaWeightDecay) {

				if (numFitnessReadings > nextDecay) {
					this.inertiaWeight -= 0.1;
					nextDecay += n;
				}

			}

			for (int j = 0; j < this.swarm.length; j++) {

				Particle particle = swarm[j];

				particle.updateVelocity(this.inertiaWeight);

				particle.updatePosition();

				particle.evaluateFitness();
				numFitnessReadings++;

				particle.updateNeighborHoodBest();

				if (particle.getFitness() < bestFitness) {

					bestFitness = particle.getFitness();

				}
				if (numFitnessReadings % 10 == 0 && numFitnessReadings <= maxFitnessReadings) {
					gBestList[gBestListIdx] = bestFitness;
					gBestListIdx++;

				}
			}
			
		}
		return gBestList;
	}
}
