import java.util.Arrays;
import java.util.List;

import org.apache.commons.math4.legacy.distribution.MultivariateNormalDistribution;

import utils.DistributionType;
import utils.PlotUtils;
import utils.Util;

public class Main {

	public static void main(String[] args) {

		/*
		 * Type NORMAL (distribution) is default type Mode RANDOMMULTIVARIATE2 means
		 * we're choosing multiunivariate gaussians with mean and variance randomly
		 * choosed in a predefined interval. Hard and soft mode follows EM theory
		 */

		// setting parameters and generating points
		final int totalPointsNumber = 100;
		final int variatesDimension = 2;
		final int nClusters = 3;
		
		
		
		SetWorld sw = SetWorld.getPoints(DistributionType.NORMAL, variatesDimension, nClusters, totalPointsNumber);
		List<MultivariateNormalDistribution> mvns = sw.getMvns();
		List<double[]> points = sw.getPoints();
		Util.printGaussianParameters(mvns);
		Util.printFirst30Points(points);

		// EM Algorithm

		// Apply EM to find clusters
		EMResult result = EMAlgorithm.clusterPoints(sw.getPoints(), nClusters);

		// Print results
		result.printResults();

		// Compare with original distributions
		System.out.println("\n=== COMPARISON WITH ORIGINAL ===");
		List<MultivariateNormalDistribution> estimated = result.getDistributions();
		List<MultivariateNormalDistribution> originalMvns = sw.getMvns();

		for (int i = 0; i < estimated.size(); i++) {
			System.out.printf("Cluster %d:\n", i);
			System.out.println("  Estimated mean: " + Arrays.toString(estimated.get(i).getMeans()));
			System.out.println("  Original mean:  " + Arrays.toString(originalMvns.get(i).getMeans()));

		}

		PlotUtils.plot2DSoftClusters(points, result.getResponsibilities());

	}

}
