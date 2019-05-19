
public class Dimension {

	//limite inferior da dimensao
	private double lowerBound;
	
	//limite superior da dimensao
	private double upperBound;
	
	private double initSubSpaceLowerBound;
	private double initSubSpaceUpperBound;

	public Dimension(double lowerBound, double upperBound, double initSubSpaceLowerBound, double initSubSpaceUpperBound) {
		super();
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.initSubSpaceLowerBound = initSubSpaceLowerBound;
		this.initSubSpaceUpperBound = initSubSpaceUpperBound;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public double getInitSubSpaceLowerBound() {
		return initSubSpaceLowerBound;
	}

	public double getInitSubSpaceUpperBound() {
		return initSubSpaceUpperBound;
	}	
}
