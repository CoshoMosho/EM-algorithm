import java.util.ArrayList;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

public class EmProcessing {

	ArrayList<Double> allParameters;
	int[] pointsAssignement;
	double[] parAssignment;
	double[] points;

	public EmProcessing(double[] points) {
		this.points = points;
		this.pointsAssignement = new int[points.length];
	}

	public ArrayList<Double> solve(Type type, Mode mode) {
		switch (type) {
		case NORMAL:
			switch (mode) {
			case RANDOM1:
				return solveNormalRandom1();
			default:
				return solveNormalRandom1();
			}
		default:
			switch (mode) {
			case RANDOM1:
				return solveNormalRandom1();
			default:
				return solveNormalRandom1();
			}
		}

	}

	private ArrayList<Double> solveNormalRandom1() {
		// think of stopping method!
		randAssign();
		
		return new ArrayList<Double>();
	}

	private void randAssign() {
		UniformRandomProvider rng = RandomSource.MT.create();
		this.pointsAssignement = rng.ints(pointsAssignement.length, 0, 2).toArray();
		for (int i = 0; i < points.length; i++) {
			//System.out.println(pointsAssignement[i]);
		}
	}

}
