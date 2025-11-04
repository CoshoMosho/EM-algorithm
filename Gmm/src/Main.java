import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.math4.legacy.distribution.MultivariateNormalDistribution;
import org.apache.commons.math4.legacy.linear.RealMatrix;

import utils.Util;

public class Main {

	public static void main(String[] args) {

		/*
		 * Type NORMAL (distribution) is default type Mode RANDOMMULTIVARIATE2 means
		 * we're choosing multiunivariate gaussians with mean and variance randomly
		 * choosed in a predefined interval Hard and soft mode follows EM theory
		 */

		// setting parameters and generating points
		final int totalPointsNumber = 1000;
		final int variatesDimension = 10;
		final int nClusters = 6;
		SetWorld sw = SetWorld.getPoints(DistributionType.NORMAL, variatesDimension, nClusters, totalPointsNumber);
		ArrayList<MultivariateNormalDistribution> mvns = sw.getMvns();
		ArrayList<double[]> points = sw.getPoints();
		Util.printGaussianParameters(mvns);
		Util.printFirst30Points(points);
		

		

		// EM algorithm
		// EmProcessing em = new EmProcessing(points, nClusters);
		// em.solve(DistributionType.NORMAL, Mode.SOFTSOLVING);
		// System.out.println(em.getParams());

	}

	
}
