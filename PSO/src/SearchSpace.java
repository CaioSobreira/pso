
public class SearchSpace {

	private Dimension[] dimensions;

	public SearchSpace(Dimension[] dimensions) {
		super();
		
		this.dimensions = dimensions;
	}

	public Dimension[] getDimensions() {
		return this.dimensions;
	}
	
	public int getNumDimensions() {
		return this.dimensions.length;
	}
		
}
