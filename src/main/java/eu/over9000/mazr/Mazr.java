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

	private static int height = 720;
	private static int width = 1280;
	private static long seed = System.currentTimeMillis();

	public static void main(final String[] args) {
		if (args.length == 3) {
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
			seed = Long.parseLong(args[2]);
		}

		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {


		final long start = System.currentTimeMillis();
		final int[][] distMap = Prim.calculateDistanceMap(height, width, seed);
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
