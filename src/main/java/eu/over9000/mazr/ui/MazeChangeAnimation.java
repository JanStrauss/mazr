package eu.over9000.mazr.ui;

import eu.over9000.mazr.util.Util;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by jan on 16.06.15.
 */
public class MazeChangeAnimation extends Transition {

    public static final int CYCLE_COUNT = 180;
    private final ImageView view;
    private final int[][] distanceMap;
    private final int width;
    private final int height;
    private WritableImage[] aniData = new WritableImage[CYCLE_COUNT];

    public MazeChangeAnimation(ImageView view, int[][] distanceMap, int width, int height) {
        this.view = view;
        this.distanceMap = distanceMap;
        this.width = width;
        this.height = height;

        setCycleCount(Animation.INDEFINITE);
        //setAutoReverse(true);
        setCycleDuration(Duration.millis(5000));
        setInterpolator(Interpolator.LINEAR);

        ExecutorService e = Executors.newCachedThreadPool();

        List<Callable<Pair<WritableImage,Integer>>> callableImageGenList = new ArrayList<>();
        for (int i = 0; i < CYCLE_COUNT; i++) {

            final int finalI = i;
            Callable<Pair<WritableImage,Integer>> callableImageGen = () -> new Pair<>(buildAnim(finalI), finalI);
            callableImageGenList.add(callableImageGen);
        }

        try {
            e.invokeAll(callableImageGenList).forEach(future -> {
                try {
                    Pair<WritableImage, Integer> result = future.get();
                    aniData[result.getValue()] = result.getKey();

                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    protected void interpolate(final double k) {
        final int index = Math.min((int) Math.floor(k * CYCLE_COUNT), CYCLE_COUNT - 1);
       view.setImage(aniData[index]);

    }

    private WritableImage buildAnim( int k) {

        WritableImage image = new WritableImage(width,height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                final int value = distanceMap[y][x];

                //final double color = Util.scale(value, 0, maxDistance, 0, 360);
                //image.getPixelWriter().setColor(x, y, Color.hsb(color, k, 1-k));

                image.getPixelWriter().setColor(x, y, Color.hsb(Util.scale(k,0,CYCLE_COUNT,0,360) + value * 0.33 % 360, 1, 1));
            }
        }

       return image;
    }
}
