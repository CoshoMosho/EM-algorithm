import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		/*
		 * Type NORMAL is default type - if working with normal distribution 
		 * Mode Random1 means we're choosing 2 univariate gaussians with mean and variance randomly
		 * choosed in a predefined interval
		 */

		int npoints = 100;
		double[] points = SetWorld.getPoints(Type.NORMAL, Mode.RANDOM1, npoints);
		for (double d : points) {
			//System.out.println(d);
		}
		
		
		ArrayList<Double> allParameters;
		EmProcessing em = new EmProcessing(points);
		allParameters = em.solve(Type.NORMAL, Mode.RANDOM1);
		System.out.println(allParameters);
		System.out.println(gaussianPdf(0, 0, 1));
		PlotUtils.plotClusters(points, em.clusterAssignment);

		
	}
	
	public static double gaussianPdf(double x, double mean, double sigma) {
		double a = 1.0 / (Math.sqrt(2 * Math.PI) * sigma);
		double b = Math.exp(-Math.pow(x - mean, 2) / (2 * sigma * sigma));
		return a * b;
	}

}
