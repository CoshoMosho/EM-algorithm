import org.knowm.xchart.*;
import java.util.*;

public class PlotUtils {
    public static void plotClusters(double[] points, int[] clusters) {
        List<Double> x0 = new ArrayList<>();
        List<Double> x1 = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            if (clusters[i] == 0) x0.add(points[i]);
            else x1.add(points[i]);
        }

        XYChart chart = new XYChartBuilder().width(800).height(400)
                .title("Gaussian Clusters (Hard EM)")
                .xAxisTitle("X").yAxisTitle("Density").build();

        chart.addSeries("Cluster 0", x0, Collections.nCopies(x0.size(), 0.0));
        chart.addSeries("Cluster 1", x1, Collections.nCopies(x1.size(), 0.0));

        new SwingWrapper<>(chart).displayChart();
    }
}
