import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.commons.math4.legacy.distribution.MultivariateNormalDistribution;
import org.apache.commons.math4.legacy.linear.MatrixUtils;
import org.apache.commons.math4.legacy.linear.RealMatrix;

public class EMAlgorithm {

    private final List<double[]> points;
    private final int clusters;
    private final int dimension;
    private final int maxIterations;
    private final double tolerance;

    private List<Double> weights;
    private List<double[]> means;
    private List<RealMatrix> covariances;
    private double[][] responsibilities;
    private double logLikelihood;

    // distributions cache
    private List<MultivariateNormalDistribution> distributions;

    public EMAlgorithm(List<double[]> points, int clusters, int maxIterations, double tolerance) {
        this.points = new ArrayList<>(points);
        this.clusters = clusters;
        this.dimension = points.isEmpty() ? 0 : points.get(0).length;
        this.maxIterations = maxIterations;
        this.tolerance = tolerance;

        validateInput();
        initializeParameters();
    }

    private void validateInput() {
        if (points.isEmpty()) {
            throw new IllegalArgumentException("Points list cannot be empty");
        }
        if (clusters <= 0) {
            throw new IllegalArgumentException("Clusters must be > 0");
        }
        if (clusters > points.size()) {
            throw new IllegalArgumentException("More clusters than points");
        }
        if (dimension <= 0) {
            throw new IllegalArgumentException("Dimension cannot be <= 0");
        }
        if (maxIterations <= 0) {
            throw new IllegalArgumentException("MaxIterations cannot be <= 0");
        }
    }

    private void initializeParameters() {
        weights = new ArrayList<>();
        means = new ArrayList<>();
        covariances = new ArrayList<>();
        responsibilities = new double[points.size()][clusters];

        // Uniform weights initialization
        for (int k = 0; k < clusters; k++) {
            weights.add(1.0 / clusters);
        }
        
        // Initial means: random points from dataset //way better with Kmeans!!
        Random random = new Random();
        for (int k = 0; k < clusters; k++) {
            int randomIndex = random.nextInt(points.size());
            means.add(Arrays.copyOf(points.get(randomIndex), dimension));
        }

        // Initial covariances: identity matrix
        for (int k = 0; k < clusters; k++) {
            double[][] identity = new double[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                identity[i][i] = 1.0;
            }
            covariances.add(MatrixUtils.createRealMatrix(identity));
        }
    }

    public void run() {
        double oldLogLikelihood = Double.NEGATIVE_INFINITY;

        for (int iteration = 0; iteration < maxIterations; iteration++) {

            // E-step
            expectationStep();

            // M-step
            maximizationStep();

            // controllo convergenza numerica
            if (Double.isNaN(logLikelihood) || Double.isInfinite(logLikelihood)) {
                System.err.println("Numerical instability detected, stopping early.");
                break;
            }

            double diff = Math.abs(logLikelihood - oldLogLikelihood);
            if (diff < tolerance) {
                System.out.printf("EM converged after %d iterations\n", iteration + 1, diff);
                break;
            }

            oldLogLikelihood = logLikelihood;

            if (iteration % 10 == 0) {
                System.out.printf("Iteration %3d | Log-Likelihood = %.6f\n", iteration, logLikelihood);
            }
        }
    }

    private void expectationStep() {
        logLikelihood = 0.0;
        int n = points.size();

        // update distributions with current parameterss
        distributions = new ArrayList<>(clusters);
        for (int k = 0; k < clusters; k++) {
            distributions.add(new MultivariateNormalDistribution(means.get(k), covariances.get(k).getData()));
        }

        IntStream.range(0, n).forEach(i -> {
            double[] point = points.get(i);
            double sum = 0.0;
            double[] clusterProbs = new double[clusters];

            for (int k = 0; k < clusters; k++) {
                double prob;
                try {
                    prob = weights.get(k) * distributions.get(k).density(point);
                } catch (Exception e) {
                    prob = 1e-10;
                }
                clusterProbs[k] = prob;
                sum += prob;
            }

            if (sum > 0) {
                for (int k = 0; k < clusters; k++) {
                    responsibilities[i][k] = clusterProbs[k] / sum;
                }
                synchronized (this) {
                    logLikelihood += Math.log(sum);
                }
            } else {
                for (int k = 0; k < clusters; k++) {
                    responsibilities[i][k] = 1.0 / clusters;
                }
            }
        });
    }

    private void maximizationStep() {
        int n = points.size();

        double[] clusterWeights = new double[clusters];
        for (int k = 0; k < clusters; k++) {
            for (int i = 0; i < n; i++) {
                clusterWeights[k] += responsibilities[i][k];
            }
        }

        // update weights
        for (int k = 0; k < clusters; k++) {
            weights.set(k, clusterWeights[k] / n);
        }

        // update means
        for (int k = 0; k < clusters; k++) {
            double[] newMean = new double[dimension];
            for (int i = 0; i < n; i++) {
                double[] point = points.get(i);
                for (int d = 0; d < dimension; d++) {
                    newMean[d] += responsibilities[i][k] * point[d];
                }
            }
            for (int d = 0; d < dimension; d++) {
                newMean[d] /= clusterWeights[k];
            }
            means.set(k, newMean);
        }

        // update covariances
        for (int k = 0; k < clusters; k++) {
            double[][] newCov = new double[dimension][dimension];

            for (int i = 0; i < n; i++) {
                double[] point = points.get(i);
                double[] diff = new double[dimension];
                for (int d = 0; d < dimension; d++) {
                    diff[d] = point[d] - means.get(k)[d];
                }
                for (int d1 = 0; d1 < dimension; d1++) {
                    for (int d2 = 0; d2 < dimension; d2++) {
                        newCov[d1][d2] += responsibilities[i][k] * diff[d1] * diff[d2];
                    }
                }
            }

            for (int d1 = 0; d1 < dimension; d1++) {
                for (int d2 = 0; d2 < dimension; d2++) {
                    newCov[d1][d2] /= clusterWeights[k];
                    if (d1 == d2) {
                        newCov[d1][d2] += 1e-3; // floor variance più robusto
                    }
                }
            }

            covariances.set(k, MatrixUtils.createRealMatrix(newCov));
        }
    }

    // --------Getters

    public List<Integer> getClusterAssignments() {
        List<Integer> assignments = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            int bestCluster = 0;
            double maxResp = responsibilities[i][0];
            for (int k = 1; k < clusters; k++) {
                if (responsibilities[i][k] > maxResp) {
                    maxResp = responsibilities[i][k];
                    bestCluster = k;
                }
            }
            assignments.add(bestCluster);
        }
        return assignments;
    }

    public List<MultivariateNormalDistribution> getEstimatedDistributions() {
        return new ArrayList<>(distributions);
    }

    public List<Double> getWeights() {
        return new ArrayList<>(weights);
    }

    public List<double[]> getMeans() {
        List<double[]> result = new ArrayList<>();
        for (double[] mean : means) {
            result.add(Arrays.copyOf(mean, mean.length));
        }
        return result;
    }

    public double[][] getResponsibilities() {
        double[][] copy = new double[responsibilities.length][];
        for (int i = 0; i < responsibilities.length; i++) {
            copy[i] = Arrays.copyOf(responsibilities[i], responsibilities[i].length);
        }
        return copy;
    }

    public double getLogLikelihood() {
        return logLikelihood;
    }

    public static EMResult clusterPoints(List<double[]> points, int clusters) {
    	//check these parameters here!
    	final int maxIterations = 100;
    	final double tolerance = 1e-6;
        return clusterPoints(points, clusters, maxIterations, tolerance);
    }

    public static EMResult clusterPoints(List<double[]> points, int clusters, int maxIterations, double tolerance) {
        EMAlgorithm em = new EMAlgorithm(points, clusters, maxIterations, tolerance);
        em.run();
        return new EMResult(em);
    }
}
