package eu.over9000.mazr;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.over9000.mazr.algorithm.Prim;
import eu.over9000.mazr.ui.MazeAnimation;

/**
 * Generates the distance map by calling {@link Prim#calculateDistanceMap(int, int, long)} and displays the result as an animation using JavaFX.
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
		final int height = 720;
		final int width = 1280;

		final long start = System.currentTimeMillis();
		final int[][] distMap = Prim.calculateDistanceMap(height, width, 1337);
		final long duration = System.currentTimeMillis() - start;
		System.out.println("build distance map in " + duration + "ms");


		final MazeAnimation animation = new MazeAnimation(height, width, distMap, 30_000);

		final Group root = new Group(animation.getView());
		final Scene scene = new Scene(root);


		stage.setTitle("Mazr");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();

		animation.play();
	}

}
