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
import org.apache.commons.cli.*;

/**
 * Generates the distance map by calling {@link Prim#calculateDistanceMap(int, int, long)} and displays the result as an animation using JavaFX.
 */
public class Mazr extends Application {

	private static final String widthParam = "width";
	private static final String heightParam = "height";
	private static final String seedParam = "seed";
	private static final String fileParam = "file";
	private static final String limitParam = "limit";
	private static final String exitParam = "exit";
	private static final String helpParam = "help";

	private static int width = 1920;
	private static int height = 1080;
	private static long seed = 1337;
	private static float limit = 1.0f;
	private static boolean saveOnFinish = false;
	private static boolean exitOnFinish = false;

	public static void main(final String[] args) {

		final Options options = new Options();
		options.addOption(Option.builder("w").longOpt(widthParam).desc("the width of the canvas, default: 1920").hasArg().type(Number.class).build());
		options.addOption(Option.builder("h").longOpt(heightParam).desc("the height of the canvas, default: 1080").hasArg().type(Number.class).build());
		options.addOption(Option.builder("s").longOpt(seedParam).desc("the seed used to construct the MST, default: 1337").hasArg().type(Number.class).build());
		options.addOption(Option.builder("f").longOpt(fileParam).desc("save canvas to file, default: false").build());
		options.addOption(Option.builder("l").longOpt(limitParam).desc("stop the animation at l percent, default: 1.0").hasArg().type(Number.class).build());
		options.addOption(Option.builder("e").longOpt(exitParam).desc("terminate after animation finished, default: false").build());
		options.addOption(Option.builder().longOpt(helpParam).desc("print help").build());

		try {
			final CommandLineParser parser = new DefaultParser();
			final CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption(helpParam)) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("mazr", options);
				System.exit(0);
			}

			if (cmd.hasOption(widthParam)) {
				width = ((Number) cmd.getParsedOptionValue(widthParam)).intValue();
			}
			if (cmd.hasOption(heightParam)) {
				height = ((Number) cmd.getParsedOptionValue(heightParam)).intValue();
			}
			if (cmd.hasOption(seedParam)) {
				seed = ((Number) cmd.getParsedOptionValue(seedParam)).longValue();
			}
			if (cmd.hasOption(limitParam)) {
				limit = ((Number) cmd.getParsedOptionValue(limitParam)).floatValue();
			}
			if (cmd.hasOption(fileParam)) {
				saveOnFinish = true;
			}
			if (cmd.hasOption(exitParam)) {
				exitOnFinish = true;
			}

			System.out.println("parameters:");
			for (final Option option : cmd.getOptions()) {
				System.out.println("\t" + option.getLongOpt() + (option.hasArg() ? "=" + cmd.getOptionValue(option.getOpt()) : ""));
			}

		} catch (final ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}

		Application.launch();
	}

	@Override
	public void start(final Stage stage) throws Exception {

		System.out.println("building distance map..");

		final long start = System.currentTimeMillis();
		final int[][] distMap = Prim.calculateDistanceMap(height, width, seed);
		final long duration = System.currentTimeMillis() - start;

		System.out.println("build distance map in " + duration + "ms");

		final BorderPane root = new BorderPane();
		final MazeFillAnimation animation = new MazeFillAnimation(height, width, limit, distMap, 10_000, root);
		//final MazeChangeAnimation animation2 = new MazeChangeAnimation(animation.getView(),  distMap,width,height);
		//animation.setOnFinished(e -> animation2.play());

		animation.setOnFinished(e -> {
			System.out.println("finished animation");
			if (saveOnFinish) {
				animation.saveToFile();
			}
			if (exitOnFinish) {
				stage.close();
			}
		});


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
