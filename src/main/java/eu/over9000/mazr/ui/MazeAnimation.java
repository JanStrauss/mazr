package eu.over9000.mazr.ui;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MazeAnimation extends Transition {

	private final ImageView imageView;
	private final int count;
	private final int[][] distanceMap;
	private final int width;
	private final int height;
	private final WritableImage image;
	private int lastLimit = -1;

	public MazeAnimation(final int height, final int width, final int[][] distanceMap, final double durationMs) {
		this.distanceMap = distanceMap;
		this.width = width;
		this.height = height;

		image = new WritableImage(width, height);

		updateImage(0);

		this.imageView = new ImageView(image);
		this.count = findMaxDistance();

		setCycleCount(1);
		setCycleDuration(Duration.millis(durationMs));
		setInterpolator(Interpolator.LINEAR);
	}

	private void updateImage(final int newLimit) {

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				final int val = distanceMap[y][x];


				if (val > newLimit || val <= lastLimit) {
					continue;
				}

				image.getPixelWriter().setColor(x, y, Color.hsb(val * 0.25 % 360, 1, 1));
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
		final int newLimit = Math.min((int) Math.floor(k * count), count);
		if (newLimit != lastLimit) {
			updateImage(newLimit);
		}
	}

	public ImageView getView() {
		return imageView;
	}

}