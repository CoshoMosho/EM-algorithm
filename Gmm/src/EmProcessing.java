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
		double prob0;
		double prob1;
		
		int stop = 3;
		randAssign();
		double sum1 = 0;
		double sum2 = 0;
		int count1 = 0;
		int count2 = 0;
		for (int i = 0; i < stop; i++) {
			sum1 = 0;
			sum2 = 0;
			count1 = 0;
			count2 = 0;
			//HARD MODE
			for (int j = 0; j < points.length; j++) {
				prob0 = gaussianPdf(points[j], parAssignment[0], parAssignment[1]);
				prob1 = gaussianPdf(points[j], parAssignment[2], parAssignment[3]);
				if (prob0>prob1) {
					pointsAssignement[j] = 0;
				}else {
					pointsAssignement[j] = 1;
				}
			}
			for (int j = 0; j < points.length; j++) {
				if (pointsAssignement[j] == 0) {
					sum1 += points[j];
					count1++;
				}else {
					sum2 += points[j];
					count2++;
				}
			}
			parAssignment[0]=sum1/count1;
			parAssignment[2]=sum2/count2;
			sum1=0;
			sum2=0;
			for (int j = 0; j < points.length; j++) {
				if (pointsAssignement[j] == 0) {
					sum1 += Math.pow(points[j]-parAssignment[0],2);
				}else {
					sum2 += Math.pow(points[j]-parAssignment[2],2);
				}
			}
			parAssignment[2]=sum1/count1;
			parAssignment[3]=sum2/count2;
			System.out.println(count1);
			System.out.println(count2);
			System.out.printf("G1: mean=%.2f sigma=%.2f ", parAssignment[0], parAssignment[1]);
			System.out.printf("G2: mean=%.2f sigma=%.2f ", parAssignment[2], parAssignment[3]);
			System.out.println();
		}
		
		return new ArrayList<Double>();
	}

	private void randAssign() {
		UniformRandomProvider rng = RandomSource.MT.create();
		this.pointsAssignement = rng.ints(pointsAssignement.length, 0, 2).toArray();
		for (int i = 0; i < points.length; i++) {
			// System.out.println(pointsAssignement[i]);
		}
		this.parAssignment = rng.doubles(4, -6, 6).toArray();
	}

	public double gaussianPdf(double x, double mean, double sigma) {
		double a = 1.0 / (Math.sqrt(2 * Math.PI) * sigma);
		double b = Math.exp(-Math.pow(x - mean, 2) / (2 * sigma * sigma));
		return a * b;
	}

}
