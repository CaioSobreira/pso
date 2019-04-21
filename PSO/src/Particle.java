import java.util.concurrent.ThreadLocalRandom;

public class Particle {

	//cognitive coefficient
	public static final double SIGMA1 = 2.05;

	//social coefficient
	public static final double SIGMA2 = 2.05;

	private SearchSpace searchSpace;

	//particle fitness function ('sphere', 'rastrigin' or 'rosenbrock')
	private String fitnessFunction;

	private Double[] position;

	private Double[] velocity;

	private double fitness;

	private Double[] positionPersonalBest;

	private double fitnessPersonalBest;

	private Particle[] neighborHood;

	private Double[] positionNeighborHoodBest;

	private double fitnessNeighborHoodBest;

	private boolean clercFactor;

	private boolean limitVelocity;

	private boolean limitPosition;

	public Particle(SearchSpace searchSpace, String fitnessFunction, boolean clercFactor,
			boolean limitVelocity, boolean limitPosition) {

		this.searchSpace = searchSpace;

		this.fitnessFunction = fitnessFunction;

		this.position = new Double[searchSpace.getNumDimensions()];

		this.velocity = new Double[searchSpace.getNumDimensions()];

		this.positionPersonalBest = new Double[searchSpace.getNumDimensions()];

		this.fitness = Double.MAX_VALUE;

		this.fitnessPersonalBest = Double.MAX_VALUE;

		this.positionNeighborHoodBest = new Double[searchSpace.getNumDimensions()];

		this.fitnessNeighborHoodBest = Double.MAX_VALUE;

		this.clercFactor = clercFactor;

		this.limitVelocity = limitVelocity;

		this.limitPosition = limitPosition;

		for (int i = 0; i < searchSpace.getNumDimensions(); i++) {

			//initializing dimension 'i' velocity with a random value between 0.0 and 1.0
			this.velocity[i] = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);

			//initializing dimension 'i' position with an a random value between dimension bounds
			this.position[i] = ThreadLocalRandom.current().nextDouble(searchSpace.getDimensions()[i].getLowerBound(),
					searchSpace.getDimensions()[i].getUpperBound());

			this.positionNeighborHoodBest[i] = 1.0;

			this.positionPersonalBest[i] = this.position[i];
		}
	}

	private double fitnessSphere() {

		double x = 0.0;

		for (int i = 0; i < this.position.length; i++) {
			x += Math.pow(this.position[i], 2);
		}

		return x;
	}

	private double fitnessRastrigin() {

		double x = 0.0;

		for (int i = 0; i < this.position.length; i++) {
			x += (Math.pow(this.position[i], 2) - 10 * Math.cos(2 * Math.PI * this.position[i]) + 10);
		}

		return x;
	}

	private double fitnessRosenbrock() {

		double x = 0.0;

		for (int i = 0; i < (this.position.length - 1); i++) {

			double term1 = this.position[i] * this.position[i] - this.position[i + 1];
			double term2 = 1 - this.position[i];
			x += (100. * term1 * term1 + term2 * term2);
		}

		return x;
	}

	private double clerc() {

		double z = ((SIGMA1 + SIGMA2) > 4.0) ? SIGMA1 + SIGMA2 : 0.0;

		return 2.0 / Math.abs((2 - z - Math.sqrt(Math.pow(z, 2) - 4 * z)));

	}

	public void evaluateFitness() {
		switch (this.fitnessFunction) {

		case "sphere":

			this.fitness = this.fitnessSphere();

			break;

		case "rastrigin":

			this.fitness = this.fitnessRastrigin();

			break;

		case "rosenbrock":

			this.fitness = this.fitnessRosenbrock();

			break;
		}

		if (this.fitness < this.fitnessPersonalBest) {

			this.fitnessPersonalBest = this.fitness;
			this.positionPersonalBest = this.position.clone();
		}
	}

	public void updateNeighborHoodBest() {

		for (int i = 0; i < this.neighborHood.length; i++) {

			if (this.neighborHood[i].getFitness() < this.fitnessNeighborHoodBest) {

				this.fitnessNeighborHoodBest = this.neighborHood[i].getFitness();
				this.positionNeighborHoodBest = this.neighborHood[i].getPosition().clone();

			}

		}
	}

	public void updateVelocity(double inertiaWeight) {

		for (int i = 0; i < this.velocity.length; i++) {

			double cognitiveRandom = ThreadLocalRandom.current().nextDouble(0.0, 1.0);

			double socialRandom = ThreadLocalRandom.current().nextDouble(0.0, 1.0);

			double cognitiveVelocity = SIGMA1 * cognitiveRandom * (this.positionPersonalBest[i] - this.position[i]);

			double socialVelocity = SIGMA2 * socialRandom * (this.positionNeighborHoodBest[i] - this.position[i]);

			double vel = inertiaWeight * this.velocity[i] + cognitiveVelocity + socialVelocity;

			this.velocity[i] = (this.clercFactor) ? vel * this.clerc() : vel;
			
			if (this.limitVelocity) {

				switch (this.fitnessFunction) {
				case "sphere":
					if (this.velocity[i] > 100.0) {
						this.velocity[i] = 100.0;
					}
					if (this.velocity[i] < -100.0) {
						this.velocity[i] = -100.0;
					}
					break;
				case "rosenbrock":
					if (this.velocity[i] < -2.048) {
						this.velocity[i] = -2.048;
					} else if (this.velocity[i] > 2.048) {
						this.velocity[i] = 2.048;
					}
					break;
				case "rastrigin":
					if (this.velocity[i] < -5.12) {
						this.velocity[i] = -5.12;
					} else if (this.velocity[i] > 5.12) {
						this.velocity[i] = 5.12;
					}
					break;
				}

			}


		}

	}

	public void updatePosition() {

		for (int i = 0; i < this.velocity.length; i++) {

			double newPosition = this.position[i] + this.velocity[i];

			if(this.limitPosition) {
				if (newPosition < this.searchSpace.getDimensions()[i].getLowerBound()) {
					this.position[i] = this.searchSpace.getDimensions()[i].getLowerBound();
				} else if (newPosition > this.searchSpace.getDimensions()[i].getUpperBound()) {
					this.position[i] = this.searchSpace.getDimensions()[i].getUpperBound();
				} else {
					this.position[i] = newPosition;
				}
			}else {
				this.position[i] = newPosition;
			}			

		}

	}

	public double getFitness() {
		return this.fitness;
	}

	public Double[] getPosition() {
		return position;
	}

	public void setNeighborHood(Particle[] neighborHood) {
		this.neighborHood = neighborHood;
	}

	public Particle[] getNeighborHood() {
		return neighborHood;
	}

}
