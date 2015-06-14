package eu.over9000.mazr;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import eu.over9000.mazr.algorithm.Prim;

/**
 * Generates the distance map by calling {@link Prim#calculateDistanceMap(int, int, long)} and displays the result as a colored image using JavaFX.
 */
public class Mazr extends Application {

	public static void main(final String[] args) {
		Application.launch(args);
	}

	private static double scale(final double val, final double srcFrom, final double srcTo, final double dstFrom, final double dstTo) {
		return ((val - srcFrom) / (srcTo - srcFrom)) * (dstTo - dstFrom) + dstFrom;
	}

	@Override
	public void start(final Stage stage) throws Exception {
		final int width = 1280;
		final int height = 720;

		final int[][] distMap = Prim.calculateDistanceMap(width, height, 1332);

		final WritableImage img = new WritableImage(width, height);
		final PixelWriter pw = img.getPixelWriter();


		final float baseMul = 0.25f;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				final int val = distMap[y][x];
				//double q = x * x + y * y;
				//pw.setColor(x, y, Color.hsb((val * baseMul + q * 0.0005) % 360, 1, 1));

				//double s = scale(val % 1000, 0, 1000, 0.66, 1.00) % 1;
				//double b = scale(val % 1000, 0, 1000, 0.50, 1.00) % 1;

				pw.setColor(x, y, Color.hsb(val * baseMul % 360, 1, 1));
			}
		}

		final ImageView imageView = new ImageView(img);
		final Group root = new Group(imageView);
		final Scene scene = new Scene(root);

		stage.setTitle("Mazr");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}
}
