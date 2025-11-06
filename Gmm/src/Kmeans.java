import java.util.List;

import utils.DistributionType;
import utils.Util;

public class Kmeans {

	public static void main(String[] args) {

		final int totalPointsNumber = 100;
		final int variatesDimension = 1;
		final int nClusters = 3;

		SetWorld sw = SetWorld.getPoints(DistributionType.NORMAL, variatesDimension, nClusters, totalPointsNumber);
		List<double[]> points = sw.getPoints();
		Util.printFirst30Points(points);
	}

}
