import java.util.List;
import org.apache.commons.math4.legacy.distribution.MultivariateNormalDistribution;

public class EMResult {
	private final List<Integer> clusterAssignments;
	private final List<MultivariateNormalDistribution> distributions;
	private final List<Double> weights;
	private final double logLikelihood;
	private final double[][] responsibilities;

	public EMResult(EMAlgorithm em) {
		this.clusterAssignments = em.getClusterAssignments();
		this.distributions = em.getEstimatedDistributions();
		this.weights = em.getWeights();
		this.logLikelihood = em.getLogLikelihood();
		this.responsibilities = em.getResponsibilities();
	}

	// Getters
	public List<Integer> getClusterAssignments() {
		return clusterAssignments;
	}

	public List<MultivariateNormalDistribution> getDistributions() {
		return distributions;
	}

	public List<Double> getWeights() {
		return weights;
	}

	public double getLogLikelihood() {
		return logLikelihood;
	}

	public double[][] getResponsibilities() {
		return responsibilities;
	}

	public void printResults() {
		System.out.println("=== EM ALGORITHM RESULTS ===");
		System.out.printf("Log-Likelihood: %.6f\n", logLikelihood);
		System.out.println("Cluster weights:");
		for (int k = 0; k < weights.size(); k++) {
			System.out.printf("  Cluster %d: %.4f\n", k, weights.get(k));
		}

		// Count points per cluster
		int[] clusterCounts = new int[distributions.size()];
		for (int assignment : clusterAssignments) {
			clusterCounts[assignment]++;
		}

		System.out.println("Points per cluster:");
		for (int k = 0; k < clusterCounts.length; k++) {
			System.out.printf("  Cluster %d: %d points\n", k, clusterCounts[k]);
		}
	}

}