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

package eu.over9000.mazr.ui;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MazeFillAnimation extends Transition {

	private final ImageView imageView;
	private final WritableImage image;

	private final int maxDistance;
	private final int[][] distanceMap;
	private final int width;
	private final int height;

	private int lastLimit = -1;

	public MazeFillAnimation(final int height, final int width, final int[][] distanceMap, final double durationMs, final BorderPane parent) {
		this.distanceMap = distanceMap;
		this.width = width;
		this.height = height;

		image = new WritableImage(width, height);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				image.getPixelWriter().setColor(x, y, Color.WHITE);
			}
		}

		updateImage(0, 0);

		this.imageView = new ImageView(image);
		imageView.fitWidthProperty().bind(parent.widthProperty());
		imageView.fitHeightProperty().bind(parent.heightProperty());
		parent.setCenter(imageView);
		this.maxDistance = findMaxDistance();

		setCycleCount(1);
		setCycleDuration(Duration.millis(durationMs));

		setInterpolator(Interpolator.LINEAR);
	}

	private void updateImage(final int newLimit, final double k) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				final int value = distanceMap[y][x];

				if (value > newLimit || value <= lastLimit) {
					continue;
				}

				//final double color = Util.scale(value, 0, maxDistance, 0, 360);
				//image.getPixelWriter().setColor(x, y, Color.hsb(color, k, 1 - k));

				//image.getPixelWriter().setColor(x, y, Color.hsb(value, .5, Util.scale(k, 0, 0.25, 0, 1)));
				image.getPixelWriter().setColor(x, y, Color.hsb(value * 0.5 * k, 1, 1));
			}
		}

		lastLimit = newLimit;
	}

	private int findMaxDistance() {
		int result = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (distanceMap[y][x] > result) {
					result = distanceMap[y][x];
				}
			}
		}

		return result;
	}

	protected void interpolate(final double k) {
		final int newLimit = Math.min((int) Math.floor(k * maxDistance), maxDistance);
		if (newLimit != lastLimit) {
			updateImage(newLimit, k);
		}
	}

	public void saveToFile() {
		try {
			final Path path = Paths.get(System.getProperty("user.home"), ".mazr", System.currentTimeMillis() + ".png");
			path.toFile().mkdirs();
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", path.toFile());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("saved");
	}
}