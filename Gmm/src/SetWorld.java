import java.util.ArrayList;

import org.apache.commons.math4.legacy.distribution.MultivariateNormalDistribution;
import org.apache.commons.math4.legacy.distribution.MultivariateRealDistribution.Sampler;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

import utils.GaussianOperations;
import utils.Util;

public class SetWorld {

	private ArrayList<MultivariateNormalDistribution> mvns;
	private double[][] points;

	private SetWorld(ArrayList<MultivariateNormalDistribution> mvns, double[][] points) {
		this.mvns = mvns;
		this.points = points;
	};

	public static SetWorld getPoints(DistributionType type, int dimension, int clusters, int nTotalpoints) {

		// input few controls
		if (clusters <= 0) {
			throw new IllegalArgumentException("clusters must be > 0, got: " + clusters);
		}
		if (nTotalpoints <= 0) {
			throw new IllegalArgumentException("nTotalpoints must be > 0, got: " + nTotalpoints);
		}
		if (nTotalpoints < clusters) {
			throw new IllegalArgumentException("nTotalpoints must be >= clusters");
		}
		UniformRandomProvider rng = RandomSource.MT.create();

		// just 1 case for now
		if (type == DistributionType.NORMAL) {
			return generateNormalRandom2Variate(clusters, nTotalpoints, dimension, rng);
		}

		// not yet implemented
		throw new UnsupportedOperationException("Combination not supported yet ");
	}

	// random gaussian generator
	private static SetWorld generateNormalRandom2Variate(int clusters, int nTotalpoints, int dimension,
			UniformRandomProvider rng) {

		ArrayList<double[]> points = new ArrayList<double[]>();
		ArrayList<MultivariateNormalDistribution> mvns = new ArrayList<MultivariateNormalDistribution>();
		int[] pointsProportions = Util.getPointsProportions(rng, nTotalpoints, clusters);
		for (int c = 0; c < clusters; c++) {

			double[] mean = GaussianOperations.getRandomMeanVector(rng, dimension);
			double[][] covMatrix = GaussianOperations.getRandomCovarianceMatrix(rng, dimension);
			// Sampler MVN for this cluster
			MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(mean, covMatrix);
			Sampler sampler = mvn.createSampler(rng);
			mvns.add(mvn);
			for (int i = 0; i < pointsProportions[c]; i++) {
				points.add(sampler.sample());
			}
		}
		double[][] pointsArray = points.stream().toArray(double[][]::new);
		return new SetWorld(mvns, pointsArray);

	}

	public ArrayList<MultivariateNormalDistribution> getMvns() {
		return mvns;
	}

	public double[][] getPoints() {
		return points;
	}

}
