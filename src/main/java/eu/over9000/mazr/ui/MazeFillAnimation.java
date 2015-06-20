package eu.over9000.mazr.ui;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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

	public MazeFillAnimation(final int height, final int width, final int[][] distanceMap, final double durationMs) {
		this.distanceMap = distanceMap;
		this.width = width;
		this.height = height;

		image = new WritableImage(width, height);

		updateImage(0, 0);

		this.imageView = new ImageView(image);
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
				//image.getPixelWriter().setColor(x, y, Color.hsb(color, k, 1-k));

				image.getPixelWriter().setColor(x, y, Color.hsb(value * 0.33 % 360, 1, 1));
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

	public ImageView getView() {
		return imageView;
	}

	public WritableImage getImage() {
		return image;
	}
}