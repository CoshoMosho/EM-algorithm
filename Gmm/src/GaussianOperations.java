import org.apache.commons.rng.UniformRandomProvider;

public class GaussianOperations {

	/** PDF normel */
	public static double gaussianPdf(double x, double mean, double sigma) {
		double a = 1.0 / (Math.sqrt(2 * Math.PI) * sigma);
		double b = Math.exp(-Math.pow(x - mean, 2) / (2 * sigma * sigma));
		return a * b;
	}

	public static double[] getGaussianParams(UniformRandomProvider rng, int gaussiansNumber, double maxMean,
			double minMean, double maxSigma, double minSigma) {

		// return mean,sigma of normal dist in double array and in sequence
		// mean0,sd0,mean1,sd1 and so on
		double[] params = new double[gaussiansNumber];
		for (int i = 0; i < gaussiansNumber; i++) {
			params[2 * i] = rng.nextDouble(minMean, maxMean);
			params[1 + 2 * i] = rng.nextDouble(minMean, maxMean);
			System.out.println();
			System.out.printf("Generated gaussian parameters: mean=%.2f sigma=%.2f ", params[2 * i], params[1 + 2 * i]);
		}
		return params;
	}

	public static double[] getGaussianParams(UniformRandomProvider rng) {
		// standardTest
		return getGaussianParams(rng, 2, 5.0, -5.0, 2.0, 0);
	}
}
