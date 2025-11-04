import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math4.legacy.linear.Array2DRowRealMatrix;
import org.apache.commons.math4.legacy.linear.LUDecomposition;
import org.apache.commons.math4.legacy.linear.MatrixUtils;
import org.apache.commons.math4.legacy.linear.RealMatrix;

public class MultiVariateGaussians {

	private void solveMultivariateEM(double[][] points, int K, int maxIter, double tol) {

		int N = points.length;
		int D = points[0].length;

		// --- Inizializzazione ---
		double[][] means = initializeMeans(points, K);
		double[][][] covariances = initializeCovariances(D, K);
		double[] weights = new double[K];
		Arrays.fill(weights, 1.0 / K);

		double[][] responsibilities = new double[N][K];
		double prevLogLik = Double.NEGATIVE_INFINITY;

		for (int iter = 0; iter < maxIter; iter++) {

			// --- E-step ---
			for (int i = 0; i < N; i++) {
				double norm = 0.0;
				for (int k = 0; k < K; k++) {
					double p = weights[k] * multivariateGaussianPdf(points[i], means[k], covariances[k]);
					responsibilities[i][k] = p;
					norm += p;
				}
				for (int k = 0; k < K; k++)
					responsibilities[i][k] /= norm + 1e-12; // normalizza
			}

			// --- M-step ---
			double[] Nk = new double[K];
			for (int k = 0; k < K; k++) {
				for (int i = 0; i < N; i++)
					Nk[k] += responsibilities[i][k];
				weights[k] = Nk[k] / N;

				// Aggiorna mean
				double[] newMean = new double[D];
				for (int i = 0; i < N; i++)
					for (int d = 0; d < D; d++)
						newMean[d] += responsibilities[i][k] * points[i][d];
				for (int d = 0; d < D; d++)
					newMean[d] /= Nk[k];
				means[k] = newMean;

				// Aggiorna covariance
				RealMatrix cov = MatrixUtils.createRealMatrix(D, D);
				for (int i = 0; i < N; i++) {
					double[] diff = new double[D];
					for (int d = 0; d < D; d++)
						diff[d] = points[i][d] - means[k][d];
					RealMatrix diffM = new Array2DRowRealMatrix(diff);
					RealMatrix outer = diffM.multiply(diffM.transpose()).scalarMultiply(responsibilities[i][k]);
					cov = cov.add(outer);
				}
				cov = cov.scalarMultiply(1.0 / Nk[k]);
				covariances[k] = cov.getData();
			}

			// --- Calcolo log-likelihood ---
			double logLik = 0.0;
			for (int i = 0; i < N; i++) {
				double sum = 0;
				for (int k = 0; k < K; k++)
					sum += weights[k] * multivariateGaussianPdf(points[i], means[k], covariances[k]);
				logLik += Math.log(sum + 1e-12);
			}

			System.out.printf("Iter %3d | logL=%.6f%n", iter + 1, logLik);

			// --- Condizione di stop ---
			if (Math.abs(logLik - prevLogLik) < tol) {
				System.out.println("Converged after " + (iter + 1) + " iterations.");
				break;
			}
			prevLogLik = logLik;
		}

		// --- Risultato finale ---
		for (int k = 0; k < K; k++) {
			System.out.printf("G%d: π=%.3f μ=%s%n", k + 1, weights[k], Arrays.toString(means[k]));
		}
	}

	private double multivariateGaussianPdf(double[] x, double[] mean, double[][] cov) {
		int D = x.length;
		RealMatrix xm = new Array2DRowRealMatrix(x);
		RealMatrix mu = new Array2DRowRealMatrix(mean);
		RealMatrix diff = xm.subtract(mu);

		RealMatrix covM = MatrixUtils.createRealMatrix(cov);
		double det = new LUDecomposition(covM).getDeterminant();
		RealMatrix inv = new LUDecomposition(covM).getSolver().getInverse();

		double exponent = -0.5 * diff.transpose().multiply(inv).multiply(diff).getEntry(0, 0);
		double denom = Math.pow(2 * Math.PI, D / 2.0) * Math.sqrt(Math.abs(det) + 1e-12);
		return Math.exp(exponent) / (denom + 1e-12);
	}

	private double[][] initializeMeans(double[][] points, int K) {
		double[][] means = new double[K][points[0].length];
		Random rand = new Random();
		for (int k = 0; k < K; k++)
			means[k] = points[rand.nextInt(points.length)].clone();
		return means;
	}

	private double[][][] initializeCovariances(int D, int K) {
		double[][][] covs = new double[K][D][D];
		for (int k = 0; k < K; k++) {
			for (int i = 0; i < D; i++)
				covs[k][i][i] = 1.0; // cov = I
		}
		return covs;
	}

}