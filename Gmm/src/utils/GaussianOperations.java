package utils;

import org.apache.commons.rng.UniformRandomProvider;

public class GaussianOperations {

	final static double lowMeanLimit = -10;
	final static double upperMeanLimit = 10;
	final static double lowSigmaLimit = 0.5;
	final static double upperSigmaLimit = 2.5;

	/** PDF normel */
	public static double gaussianUnivariatePdf(double x, double mean, double sigma) {
		double a = 1.0 / (Math.sqrt(2 * Math.PI) * sigma);
		double b = Math.exp(-Math.pow(x - mean, 2) / (2 * sigma * sigma));
		return a * b;
	}

	public static double[] getRandomGaussianParams(UniformRandomProvider rng, int gaussiansNumber, double maxMean,
			double minMean, double maxSigma, double minSigma) {

		// return mean,sigma of normal dist in double array and in sequence
		// mean0,sd0,mean1,sd1 and so on
		double[] params = new double[2 * gaussiansNumber];
		for (int i = 0; i < gaussiansNumber; i++) {
			params[2 * i] = rng.nextDouble(minMean, maxMean);
			params[1 + 2 * i] = rng.nextDouble(minSigma, maxSigma);
			System.out.println();
			System.out.printf("Generated gaussian parameters: mean=%.2f sigma=%.2f ", params[2 * i], params[1 + 2 * i]);
			System.out.println();
		}
		return params;
	}

	public static double[] getRandomGaussianParams(UniformRandomProvider rng) {
		// standardTest
		return getRandomGaussianParams(rng, 2, 5.0, -5.0, 2.0, 0);
	}

	public static double[] getRandomMeanVector(UniformRandomProvider rng, int dimension) {
		double[] mean = new double[dimension];
		for (int d = 0; d < dimension; d++) {
			mean[d] = rng.nextDouble(lowMeanLimit, upperMeanLimit);
		}
		return mean;
	}

	public static double[][] getRandomCovarianceMatrix(UniformRandomProvider rng, int dimension) {
		// easy version
		double[][] covMatrix = new double[dimension][dimension];
		for (int d = 0; d < dimension; d++) {
			for (int i = 0; i < dimension; i++) {
				covMatrix[d][i] = 0;
				if (d == i) {
					covMatrix[d][i] = rng.nextDouble(lowSigmaLimit, upperSigmaLimit);
				}
			}
		}
		return covMatrix;
	}
}
