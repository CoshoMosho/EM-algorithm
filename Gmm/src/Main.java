import java.util.List;

import org.apache.commons.math4.legacy.distribution.MultivariateNormalDistribution;

import utils.Util;

public class Main {

	public static void main(String[] args) {

		/*
		 * Type NORMAL (distribution) is default type Mode RANDOMMULTIVARIATE2 means
		 * we're choosing multiunivariate gaussians with mean and variance randomly
		 * choosed in a predefined interval Hard and soft mode follows EM theory
		 */

		// setting parameters and generating points
		final int totalPointsNumber = 100;
		final int variatesDimension = 4;
		final int nClusters = 3;
		SetWorld sw = SetWorld.getPoints(DistributionType.NORMAL, variatesDimension, nClusters, totalPointsNumber);
		List<MultivariateNormalDistribution> mvns = sw.getMvns();
		List<double[]> points = sw.getPoints();
		Util.printGaussianParameters(mvns);
		Util.printFirst30Points(points);
		
		
		//EM Algorithm
		

		

		// EM algorithm
		// EmProcessing em = new EmProcessing(points, nClusters);
		// em.solve(DistributionType.NORMAL, Mode.SOFTSOLVING);
		// System.out.println(em.getParams());

	}

	
}
