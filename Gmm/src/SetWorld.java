import java.util.ArrayList;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.distribution.BoxMullerNormalizedGaussianSampler;
import org.apache.commons.rng.sampling.distribution.GaussianSampler;
import org.apache.commons.rng.sampling.distribution.NormalizedGaussianSampler;
import org.apache.commons.rng.simple.RandomSource;

public class SetWorld {

	public static double[] getPoints(Type type, Mode mode, int npoints) {
		switch (type) {
		case NORMAL:
			return getModeNormal(mode, npoints);
		default:
			return getModeNormal(mode, npoints);
		}
	}

	private static double[] getModeNormal(Mode mode, int npoints) {
		switch (mode) {
		case RANDOM1:
			return buildIt(npoints);
		default:
			return buildIt(npoints);
		}
	}

	private static double[] buildIt(int npoints) {

		UniformRandomProvider rng = RandomSource.MT.create();

		double[] gaussParameters = getGaussianParams(rng);
		int[] pointsProportions = getPointsProportions(rng, npoints);

		ArrayList<Double> samples = new ArrayList<Double>();

		for (int i = 0; i < pointsProportions.length; i++) {
			NormalizedGaussianSampler n = new BoxMullerNormalizedGaussianSampler(rng);
			GaussianSampler g = new GaussianSampler(n, gaussParameters[2 * i], gaussParameters[1 + 2 * i]);
			for (int j = 0; j < pointsProportions[i]; j++)
				samples.add(g.sample());
		}
		return samples.stream().mapToDouble(Double::doubleValue).toArray();
	}

	private static int[] getPointsProportions(UniformRandomProvider rng, int npoints) {
		int n1 = rng.nextInt(20, npoints - 20);
		int n2 = npoints - n1;
		System.out.printf("G1: (%d punti)%n", n1);
		System.out.printf("G2: (%d punti)%n", n2);
		System.out.println();

		return new int[] { n1, n2 };
	}

	private static double[] getGaussianParams(UniformRandomProvider rng) {

		double mean1 = rng.nextDouble(-5.0, 0.0);
		double sigma1 = rng.nextDouble(0.5, 2.0);

		double mean2 = rng.nextDouble(0.0, 5.0);
		double sigma2 = rng.nextDouble(0.5, 2.0);
		System.out.printf("G1: mean=%.2f sigma=%.2f ", mean1, sigma1);
		System.out.printf("G2: mean=%.2f sigma=%.2f ", mean2, sigma2);
		System.out.println();
		return new double[] { mean1, sigma1, mean2, sigma2 };
	}

}
