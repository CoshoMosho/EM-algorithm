import java.util.ArrayList;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

import utils.GaussianOperations;

public class EmProcessing {

	private double[][] points; // input data
	private int[] clusterAssignment; // cluster assignment for every point
	private double[] params; // means and sigmas
	private int nClusters; // how many gaussians

	public EmProcessing(double[][] points, int nClusters) {
		this.points = points;
		this.clusterAssignment = new int[points.length];
		this.nClusters = nClusters;
	}

	public void solve(DistributionType type, Mode mode) {
		switch (type) {
		case NORMAL:
			switch (mode) {
			case SOFTSOLVING:
				//solveNormalRandomSoft2();
				break;
			case HARDSOLVING:
				//solveNormalRandomHard();
				break;
			}
		default:
			//solveNormalRandomHard();
			break;
		}
	}
	/*
	private void solveNormalRandomSoft2() {

	    initializeRandomAssignment();

	    double[] weights = initializeWeights();
	    double[][] responsibilities = new double[getPoints().length][getGaussianCounter()];

	    double prevLogLik = Double.NEGATIVE_INFINITY;
	    double tol = 1e-6;
	    int maxIter = 500;
	    int it = 0;

	    while (it++ < maxIter) {

	        // E-step
	        for (int i = 0; i < points.length; i++) {
	            double p0 = GaussianOperations.gaussianPdf(points[i], params[0], params[1]) * weights[0];
	            double p1 = GaussianOperations.gaussianPdf(points[i], params[2], params[3]) * weights[1];
	            double norm = p0 + p1 + 1e-12; // avoid diving by zero

	            responsibilities[i][0] = p0 / norm;
	            responsibilities[i][1] = p1 / norm;
	        }

	        // M step
	        double N0 = 0, N1 = 0;
	        for (int i = 0; i < points.length; i++) {
	            N0 += responsibilities[i][0];
	            N1 += responsibilities[i][1];
	        }

	        weights[0] = N0 / points.length;
	        weights[1] = N1 / points.length;

	        double mean0 = 0, mean1 = 0;
	        for (int i = 0; i < points.length; i++) {
	            mean0 += responsibilities[i][0] * points[i];
	            mean1 += responsibilities[i][1] * points[i];
	        }
	        mean0 /= N0;
	        mean1 /= N1;

	        double var0 = 0, var1 = 0;
	        for (int i = 0; i < points.length; i++) {
	            double d0 = points[i] - mean0;
	            double d1 = points[i] - mean1;
	            var0 += responsibilities[i][0] * d0 * d0;
	            var1 += responsibilities[i][1] * d1 * d1;
	        }
	        var0 /= N0;
	        var1 /= N1;

	        params[0] = mean0;
	        params[1] = Math.sqrt(var0);
	        params[2] = mean1;
	        params[3] = Math.sqrt(var1);

	        // Compute log-likelihood for convergence check
	        double logLik = 0;
	        for (double x : points) {
	            double p = weights[0] * GaussianOperations.gaussianPdf(x, params[0], params[1])
	                     + weights[1] * GaussianOperations.gaussianPdf(x, params[2], params[3]);
	            logLik += Math.log(p + 1e-12); // numerical stability
	        }

	        System.out.printf("Iter %3d | logL=%.6f | μ1=%.3f σ1=%.3f π1=%.3f | μ2=%.3f σ2=%.3f π2=%.3f%n",
	                it, logLik, mean0, Math.sqrt(var0), weights[0], mean1, Math.sqrt(var1), weights[1]);

	        // --- Stopping condition ---
	        if (Math.abs(logLik - prevLogLik) < tol) {
	            System.out.println("Converged after " + it + " iterations.");
	            break;
	        }

	        prevLogLik = logLik;
	    }

	    if (it >= maxIter) {
	        System.out.println("Reached max iterations (" + maxIter + ") without full convergence.");
	    }

	    //hard assignment for visualization
	    for (int i = 0; i < points.length; i++) {
	        clusterAssignment[i] = (responsibilities[i][0] > responsibilities[i][1]) ? 0 : 1;
	    }

	    // Print result
	    System.out.println("\nFinal parameters:");
	    System.out.printf("G1 μ=%.3f σ=%.3f π=%.3f%n", params[0], params[1], weights[0]);
	    System.out.printf("G2 μ=%.3f σ=%.3f π=%.3f%n", params[2], params[3], weights[1]);

	    // Collect result (if needed)
	    ArrayList<Double> result = new ArrayList<>();
	    for (double p : params) result.add(p);
	}

	// SOFT MODE
	private void solveNormalRandomSoft() {

		initializeRandomAssignment();

		double[] weights = initializeWeights();
		double[][] responsibilities = new double[getPoints().length][getGaussianCounter()];

		double prevLogLik = Double.NEGATIVE_INFINITY;
	    double tol = 1e-6;
	    int maxIter = 500;
	    int it = 0;
	    
	    while (it++ < maxIter) {

			// --- E-step: compute responsibilities ---
			for (int i = 0; i < points.length; i++) {
				double p0 = GaussianOperations.gaussianPdf(points[i], params[0], params[1]) * weights[0];
				double p1 = GaussianOperations.gaussianPdf(points[i], params[2], params[3]) * weights[1];
				double norm = p0 + p1;

				responsibilities[i][0] = p0 / norm;
				responsibilities[i][1] = p1 / norm;
			}

			// --- M-step: update parameters ---
			double N0 = 0, N1 = 0;
			for (int i = 0; i < points.length; i++) {
				N0 += responsibilities[i][0];
				N1 += responsibilities[i][1];
			}

			weights[0] = N0 / points.length;
			weights[1] = N1 / points.length;

			double mean0 = 0, mean1 = 0;
			for (int i = 0; i < points.length; i++) {
				mean0 += responsibilities[i][0] * points[i];
				mean1 += responsibilities[i][1] * points[i];
			}
			mean0 /= N0;
			mean1 /= N1;

			double var0 = 0, var1 = 0;
			for (int i = 0; i < points.length; i++) {
				var0 += responsibilities[i][0] * Math.pow(points[i] - mean0, 2);
				var1 += responsibilities[i][1] * Math.pow(points[i] - mean1, 2);
			}
			var0 /= N0;
			var1 /= N1;

			params[0] = mean0;
			params[1] = Math.sqrt(var0);
			params[2] = mean1;
			params[3] = Math.sqrt(var1);

			System.out.printf("Iter %2d | μ1=%.3f σ1=%.3f π1=%.3f | μ2=%.3f σ2=%.3f π2=%.3f%n", it + 1, mean0,
					Math.sqrt(var0), weights[0], mean1, Math.sqrt(var1), weights[1]);
		}

		// --- Optional: hard assign at the end for visualization ---
		for (int i = 0; i < points.length; i++) {
			clusterAssignment[i] = (responsibilities[i][0] > responsibilities[i][1]) ? 0 : 1;
		}

		// --- Print final result ---
		System.out.println("\nFinal parameters:");
		System.out.printf("G1 μ=%.3f σ=%.3f π=%.3f%n", params[0], params[1], weights[0]);
		System.out.printf("G2 μ=%.3f σ=%.3f π=%.3f%n", params[2], params[3], weights[1]);

		ArrayList<Double> result = new ArrayList<>();
		for (double p : params)
			result.add(p);
	}

	// HARD MODE
	private ArrayList<Double> solveNormalRandomHard() {

		double[] weights = { 0.5, 0.5 };
		int iterations = 10;
		initializeRandomAssignment();

		for (int it = 0; it < iterations; it++) {

			// E-step:
			// points assignment to clusters
			for (int j = 0; j < points.length; j++) {
				double p0 = GaussianOperations.gaussianPdf(points[j], params[0], params[1]) * weights[0];
				double p1 = GaussianOperations.gaussianPdf(points[j], params[2], params[3]) * weights[1];
				clusterAssignment[j] = (p0 > p1) ? 0 : 1;
			}

			// M-step:
			double sum0 = 0, sum1 = 0;
			int count0 = 0, count1 = 0;

			// calculating
			for (int j = 0; j < points.length; j++) {
				if (clusterAssignment[j] == 0) {
					sum0 += points[j];
					count0++;
				} else {
					sum1 += points[j];
					count1++;
				}
			}

			if (count0 == 0 || count1 == 0) {
				System.out.println("cluster empty. game over");
				break;
			}

			weights[0] = (double) count0 / points.length;
			weights[1] = (double) count1 / points.length;

			double mean0 = sum0 / count0;
			double mean1 = sum1 / count1;

			double var0 = 0, var1 = 0;
			for (int j = 0; j < points.length; j++) {
				if (clusterAssignment[j] == 0)
					var0 += Math.pow(points[j] - mean0, 2);
				else
					var1 += Math.pow(points[j] - mean1, 2);
			}

			var0 /= count0;
			var1 /= count1;

			params[0] = mean0;
			params[1] = Math.sqrt(var0);
			params[2] = mean1;
			params[3] = Math.sqrt(var1);

			System.out.printf("Iter %d: G1 μ=%.3f σ=%.3f | G2 μ=%.3f σ=%.3f%n", it + 1, params[0], params[1], params[2],
					params[3]);
		}

		// counting points
		int count0 = 0, count1 = 0;
		for (int j = 0; j < points.length; j++) {
			if (clusterAssignment[j] == 0)
				count0++;
			else
				count1++;
		}
		System.out.printf("%nFinal assignment: %d points in G1, %d points in G2%n", count0, count1);

		// parameters
		ArrayList<Double> result = new ArrayList<>();
		for (double p : params)
			result.add(p);
		return result;
	}
*/
	private void initializeRandomAssignment() {
		UniformRandomProvider rng = RandomSource.MT.create();

		// random assignment (0 o 1)
		for (int i = 0; i < clusterAssignment.length; i++) {
			clusterAssignment[i] = rng.nextInt(2);
		}

		params = GaussianOperations.getRandomGaussianParams(rng);
	}

	private double[] initializeWeights() {
		double[] weights = new double[nClusters];
		for (int i = 0; i < getGaussianCounter(); i++) {
			weights[i] = 0.5;
		}
		return weights;
	}

	public double[][] getPoints() {
		return points;
	}

	public void setPoints(double[][] points) {
		this.points = points;
	}

	public int[] getClusterAssignment() {
		return clusterAssignment;
	}

	public void setClusterAssignment(int[] clusterAssignment) {
		this.clusterAssignment = clusterAssignment;
	}

	public double[] getParams() {
		return params;
	}

	public void setParams(double[] params) {
		this.params = params;
	}

	public int getGaussianCounter() {
		return nClusters;
	}

	public void setGaussianCounter(int gaussianCounter) {
		this.nClusters = gaussianCounter;
	}

}
