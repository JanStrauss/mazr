/*
 * Mazr
 * Copyright (C) 2015 s1mpl3x
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package eu.over9000.mazr.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Provides a static method to calculate a distance map based on Prim's MST algorithm.
 */
public final class Prim {

	public static int[][] calculateDistanceMap(final int height, final int width, final long seed) {
		final Random random = new Random(seed);

		final int[][] distanceMap = new int[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				distanceMap[i][j] = Integer.MAX_VALUE;
			}
		}

		final int startX = random.nextInt(width);
		final int startY = random.nextInt(height);

		distanceMap[startY][startX] = 0;

		final PriorityQueue<Edge> dangling = new PriorityQueue<>();

		dangling.addAll(getEdgesFor(startY, startX, width, height, random, 0));

		while (!dangling.isEmpty()) {
			final Edge currentEdge = dangling.remove();

			if (distanceMap[currentEdge.targetY][currentEdge.targetX] < Integer.MAX_VALUE) {
				continue;
			}

			distanceMap[currentEdge.targetY][currentEdge.targetX] = currentEdge.distance;

			final Collection<Edge> outgoingEdges = getEdgesFor(currentEdge.targetY, currentEdge.targetX, width, height, random, currentEdge.distance);
			final Collection<Edge> unvisitedTargets = outgoingEdges.stream().filter(distEdge -> distanceMap[distEdge.targetY][distEdge.targetX] == Integer.MAX_VALUE).collect(Collectors.toList());
			dangling.addAll(unvisitedTargets);
		}

		return distanceMap;

	}

	private static Collection<Edge> getEdgesFor(final int y, final int x, final int width, final int height, final Random random, final int dist) {
		final Collection<Edge> result = new ArrayList<>(4);

		final int above = y - 1;
		final int below = y + 1;
		final int left = x - 1;
		final int right = x + 1;

		final int newDist = dist + 1;

		if (above >= 0) {
			result.add(new Edge(above, x, random.nextFloat(), newDist));
		}
		if (below < height) {
			result.add(new Edge(below, x, random.nextFloat(), newDist));
		}
		if (left >= 0) {
			result.add(new Edge(y, left, random.nextFloat(), newDist));
		}
		if (right < width) {
			result.add(new Edge(y, right, random.nextFloat(), newDist));
		}

		return result;
	}

	private static class Edge implements Comparable<Edge> {
		public final int targetX;
		public final int targetY;
		public final float weight;
		public final int distance;

		public Edge(final int targetY, final int targetX, final float weight, final int distance) {
			this.targetY = targetY;
			this.targetX = targetX;
			this.weight = weight;
			this.distance = distance;
		}

		@Override
		public int compareTo(final Edge other) {
			return Float.compare(weight, other.weight);
		}
	}

}
