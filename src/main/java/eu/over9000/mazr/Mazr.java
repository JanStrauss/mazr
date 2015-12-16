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

package eu.over9000.mazr;

import eu.over9000.mazr.algorithm.Prim;
import eu.over9000.mazr.ui.MazeFillAnimation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Generates the distance map by calling {@link Prim#calculateDistanceMap(int, int, long)} and displays the result as an animation using JavaFX.
 */
public class Mazr extends Application {
	private static int width = 1920;
	private static int height = 1080;
	private static long seed = 1337;

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

		final BorderPane root = new BorderPane();
		final MazeFillAnimation animation = new MazeFillAnimation(height, width, distMap, 10_000, root);
		//final MazeChangeAnimation animation2 = new MazeChangeAnimation(animation.getView(),  distMap,width,height);
		//animation.setOnFinished(e -> animation2.play());

		//animation.setOnFinished(e -> animation.saveToFile());


		final Scene scene = new Scene(root, 1280, 720);

		scene.setOnKeyPressed(t -> {
			final KeyCode key = t.getCode();
			if (key == KeyCode.SPACE) {
				animation.saveToFile();
			} else if (key == KeyCode.ESCAPE) {
				System.exit(0);
			} else if (key == KeyCode.ENTER) {
				animation.saveToFile();
			}
		});
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();

		animation.play();
	}
}
