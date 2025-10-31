import java.util.ArrayList;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.distribution.BoxMullerNormalizedGaussianSampler;
import org.apache.commons.rng.sampling.distribution.GaussianSampler;
import org.apache.commons.rng.sampling.distribution.NormalizedGaussianSampler;
import org.apache.commons.rng.simple.RandomSource;

import utils.GaussianOperations;
import utils.Util;

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
		case RANDOM1HARD:
			return randomSampling(npoints);
		default:
			return randomSampling(npoints);
		}
	}

	private static double[] randomSampling(int npoints) {

		UniformRandomProvider rng = RandomSource.MT.create();

		double[] gaussParameters = GaussianOperations.getRandomGaussianParams(rng);
		int[] pointsProportions = Util.getPointsProportions(rng, npoints);

		ArrayList<Double> samples = new ArrayList<Double>();

		for (int i = 0; i < pointsProportions.length; i++) {
			NormalizedGaussianSampler n = new BoxMullerNormalizedGaussianSampler(rng);
			GaussianSampler g = new GaussianSampler(n, gaussParameters[2 * i], gaussParameters[1 + 2 * i]);
			for (int j = 0; j < pointsProportions[i]; j++)
				samples.add(g.sample());
		}
		return samples.stream().mapToDouble(Double::doubleValue).toArray();
	}

	

	

}
