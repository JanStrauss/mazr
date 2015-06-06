package eu.over9000.mazr;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import eu.over9000.mazr.model.Maze;

/**
 * Created by jan on 05.06.15.
 */
public class Mazr extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		final int width = 1280;
		final int height = 720;

		WritableImage img = new WritableImage(width, height);
		PixelWriter pw = img.getPixelWriter();

		Maze m = new Maze(width, height, 1337);
		final int[][] distMap = m.buildDistMap();


		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				int val = distMap[y][x];
				pw.setColor(x, y, Color.hsb(val * 0.25 % 360, 1, 1));
			}
		}

		ImageView imageView = new ImageView(img);
		Group root = new Group(imageView);
		Scene scene = new Scene(root);

		stage.setTitle("Mazr");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}
}
