package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.math4.legacy.distribution.MultivariateNormalDistribution;
import org.apache.commons.math4.legacy.linear.RealMatrix;
import org.apache.commons.rng.UniformRandomProvider;

public class Util {

	public static int[] getPointsProportions(UniformRandomProvider rng, int npoints, int partitions) {

		int[] part = new int[partitions];
		int left = npoints;
		for (int i = 0; i < part.length - 1; i++) {
			part[i] = rng.nextInt(0, left);
			System.out.printf("points part: (%d punti)%n", part[i]);
			left -= part[i];
			// some clusters could be zero
			// not yet implemented
			if (part[i] <= 0) {
				throw new UnsupportedOperationException("Cluster partition = 0 not supported yet,sorry");
			}
		}
		part[part.length - 1] = left;
		System.out.printf("points part: (%d punti)%n", left);
		System.out.println();

		return part;
	}

	public static String matrixToString(RealMatrix matrix) {
		return Arrays.stream(matrix.getData()).map(Arrays::toString).collect(Collectors.joining(", ", "[", "]"));
	}

	public static void printGaussianParameters(ArrayList<MultivariateNormalDistribution> mvns) {
		System.out.println("=== MULTIVARIATE NORMAL DISTRIBUTIONS ===");
		mvns.forEach(mvn -> {
			double[] mean = mvn.getMeans();
			RealMatrix covariance = mvn.getCovariances();

			System.out.println("Mean: " + Arrays.toString(mean));
			System.out.println("Covariance: " + Util.matrixToString(covariance));
		});
	}

	public static void printAllPoints(ArrayList<double[]> points) {
		System.out.println("\n=== GENERATED POINTS ===");
		points.stream().map(Arrays::toString).forEach(System.out::println);
	}

	public static void printFirst30Points(ArrayList<double[]> points) {
		System.out.println("\n=== 30 POINTS ===");
		points.stream().limit(30).map(Arrays::toString).forEach(System.out::println);
	}

}
