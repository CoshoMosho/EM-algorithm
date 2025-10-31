import java.util.ArrayList;
import java.util.Iterator;

import utils.GaussianOperations;

public class Main {

	public static void main(String[] args) {

		/*
		 * Type NORMAL is default type - if working with normal distribution 
		 * Mode Random1 means we're choosing 2 univariate gaussians with mean and variance randomly
		 * choosed in a predefined interval
		 */

		int npoints = 100;
		double[] points = SetWorld.getPoints(Type.NORMAL, Mode.RANDOM1HARD, npoints);
		for (double d : points) {
			//System.out.println(d);
		}
		
		
		ArrayList<Double> allParameters;
		EmProcessing em = new EmProcessing(points);
		allParameters = em.solve(Type.NORMAL, Mode.RANDOM1HARD);
		System.out.println(allParameters);
		System.out.println(GaussianOperations.gaussianPdf(0, 0, 1));
		PlotUtils.plotClusters(points, em.clusterAssignment);

	}

}
