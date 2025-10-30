import java.util.ArrayList;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

public class EmProcessing {

    double[] points;              // input data
    int[] clusterAssignment;      // cluster assignment for every point
    double[] params;              // means and sigmas

    public EmProcessing(double[] points) {
        this.points = points;
        this.clusterAssignment = new int[points.length];
    }

    public ArrayList<Double> solve(Type type, Mode mode) {
        switch (type) {
            case NORMAL:
                return solveNormalRandom1();
            default:
                return solveNormalRandom1();
        }
    }

    private ArrayList<Double> solveNormalRandom1() {

        int iterations = 10;
        initializeRandom();

        for (int it = 0; it < iterations; it++) {

            // E-step: HARD MODE
            for (int j = 0; j < points.length; j++) {
                double p0 = gaussianPdf(points[j], params[0], params[1]);
                double p1 = gaussianPdf(points[j], params[2], params[3]);
                clusterAssignment[j] = (p0 > p1) ? 0 : 1;
            }

            // M-step: 
            double sum0 = 0, sum1 = 0;
            int count0 = 0, count1 = 0;

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

            System.out.printf("Iter %d: G1 μ=%.3f σ=%.3f | G2 μ=%.3f σ=%.3f%n",
                    it + 1, params[0], params[1], params[2], params[3]);
        }

        // counting points 
        int count0 = 0, count1 = 0;
        for (int j = 0; j < points.length; j++) {
            if (clusterAssignment[j] == 0) count0++;
            else count1++;
        }
        System.out.printf("%nFinal assignment: %d points in G1, %d points in G2%n", count0, count1);

        // parameters
        ArrayList<Double> result = new ArrayList<>();
        for (double p : params)
            result.add(p);
        return result;
    }


    private void initializeRandom() {
        UniformRandomProvider rng = RandomSource.MT.create();

        // random assignment (0 o 1)
        for (int i = 0; i < clusterAssignment.length; i++) {
            clusterAssignment[i] = rng.nextInt(2);
        }

        // initial parameters: [mean1, sigma1, mean2, sigma2]
        params = new double[4];
        params[0] = rng.nextDouble(-5, 5);
        params[1] = rng.nextDouble(0.5, 2.5); 
        params[2] = rng.nextDouble(-5, 5);
        params[3] = rng.nextDouble(0.5, 2.5);
    }

    /** PDF gausson */
    public static double gaussianPdf(double x, double mean, double sigma) {
        double a = 1.0 / (Math.sqrt(2 * Math.PI) * sigma);
        double b = Math.exp(-Math.pow(x - mean, 2) / (2 * sigma * sigma));
        return a * b;
    }
}
