package utils;

import org.apache.commons.rng.UniformRandomProvider;

public class Util {

	public static int[] getPointsProportions(UniformRandomProvider rng, int npoints, int partitions) {

		int[] part = new int[partitions];
		int left = npoints;
		for (int i = 0; i < part.length - 1; i++) {
			part[i] = rng.nextInt(0, left);
			System.out.printf("points part: (%d punti)%n", part[i]);
			left -= part[i];
			// some clusters could be zero
			// not yet implemented
			if (part[i] <= 0) {
				throw new UnsupportedOperationException("Cluster partition = 0 not supported yet,sorry");
			}
		}
		part[part.length - 1] = left;
		System.out.printf("points part: (%d punti)%n", left);
		System.out.println();

		return part;
	}

}
