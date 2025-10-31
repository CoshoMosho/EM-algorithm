package utils;

import org.apache.commons.rng.UniformRandomProvider;

public class Util {

	public static int[] getPointsProportions(UniformRandomProvider rng, int npoints, int partitions, int limitValue) {

		int[] part = new int[partitions];
		int left = npoints;
		for (int i = 0; i < part.length - 1; i++) {
			part[i] = rng.nextInt(limitValue, left - limitValue);
			System.out.printf("points part: (%d punti)%n", part[i]);
			left -= part[i];
		}
		part[part.length-1] = left;
		System.out.printf("points part: (%d punti)%n", left);
		System.out.println();

		return part;
	}

	public static int[] getPointsProportions(UniformRandomProvider rng, int npoints) {
		//standard
		return getPointsProportions(rng, npoints, 2, 20);
	}

}
