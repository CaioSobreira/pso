
public class Dimension {

	//limite inferior da dimensao
	private double lowerBound;
	
	//limite superior da dimensao
	private double upperBound;

	public Dimension(double lowerBound, double upperBound) {
		super();
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}	
}
