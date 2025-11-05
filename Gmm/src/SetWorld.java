import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.math4.legacy.distribution.MultivariateNormalDistribution;
import org.apache.commons.math4.legacy.distribution.MultivariateRealDistribution.Sampler;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

import utils.DistributionType;
import utils.GaussianOperations;
import utils.Util;

public class SetWorld {

	private final List<MultivariateNormalDistribution> mvns;
	private final List<double[]> points;

	private SetWorld(List<MultivariateNormalDistribution> mvns, List<double[]> points) {
		this.mvns = new ArrayList<>(Objects.requireNonNull(mvns, "mvns cannot be null"));
		this.points = new ArrayList<>(Objects.requireNonNull(points, "points cannot be null"));
	};

	public static SetWorld getPoints(DistributionType type, int dimension, int clusters, int nTotalpoints) {

		// few controls on inputs
		validateInputParameters(dimension, clusters, nTotalpoints);
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

		List<double[]> points = new ArrayList<>(nTotalpoints);
		List<MultivariateNormalDistribution> mvns = new ArrayList<>(clusters);
		int[] pointsProportions = Util.getPointsProportions(rng, nTotalpoints, clusters);
		for (int cluster = 0; cluster < clusters; cluster++) {

			double[] mean = GaussianOperations.getRandomMeanVector(rng, dimension);
			double[][] covMatrix = GaussianOperations.getRandomCovarianceMatrix(rng, dimension);
			// Sampler MVN for this cluster
			MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(mean, covMatrix);
			Sampler sampler = mvn.createSampler(rng);
			mvns.add(mvn);
			for (int i = 0; i < pointsProportions[cluster]; i++) {
				points.add(sampler.sample());
			}
		}
		return new SetWorld(mvns, points);

	}

	private static void validateInputParameters(int dimension, int clusters, int nTotalPoints) {
		if (dimension <= 0) {
			throw new IllegalArgumentException("Dimension must be > 0, got: " + dimension);
		}
		if (clusters <= 0) {
			throw new IllegalArgumentException("Clusters must be > 0, got: " + clusters);
		}
		if (nTotalPoints <= 0) {
			throw new IllegalArgumentException("nTotalPoints must be > 0, got: " + nTotalPoints);
		}
		if (nTotalPoints < clusters) {
			throw new IllegalArgumentException(
					"nTotalPoints must be >= clusters, got: " + nTotalPoints + " < " + clusters);
		}
	}

	public List<MultivariateNormalDistribution> getMvns() {
		return new ArrayList<>(mvns);
	}

	public List<double[]> getPoints() {
		return new ArrayList<>(points);
	}

	public int getTotalPoints() {
		return points.size();
	}

	public int getNumberOfClusters() {
		return mvns.size();
	}

	public int getDimension() {
		return !points.isEmpty() ? points.get(0).length : 0;
	}

	@Override
	public String toString() {
		return "SetWorld [getTotalPoints()=" + getTotalPoints() + ", getNumberOfClusters()=" + getNumberOfClusters()
				+ ", getDimension()=" + getDimension() + "]";
	}

}
