package apple1417.misc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

// This javafx stuff is kind of tacked on because it was annoying not knowing how much was generated
public class Fractal extends Application {
    private static String path;
    public static void main(String args[]) {
        path = args.length >= 1 ? args[0] : "Fractal.png";
        launch(args);
    }

    private static PixelWriter pw;
    private static WritableImage wrImg;
    public void start(Stage stage) {
        Group root = new Group();

        wrImg = new WritableImage(WIDTH, HEIGHT);
        pw = wrImg.getPixelWriter();

        ImageView imgView = new ImageView(wrImg);
        root.getChildren().add(imgView);
        imgView.fitWidthProperty().bind(stage.widthProperty());
        imgView.fitHeightProperty().bind(stage.heightProperty());

        // Make sure the aspect ratio is right
        int w = 750;
        int h = 750;
        if (WIDTH > HEIGHT) {
            w = 750;
            h = 750*HEIGHT/WIDTH;
        } else {
            w = 750*WIDTH/HEIGHT;
            h = 750;
        }
        Scene scene = new Scene(root, w, h, Color.WHITE);
        stage.setTitle("Fractal Generator");
        stage.setScene(scene);
        stage.show();
        // Quick hack so it doesn't continue generating when you close the window
        stage.setOnCloseRequest((WindowEvent e) -> System.exit(0));

        // UI will freeze if I don't thread this
        new Thread("Fractal Generator") {public void run(){
            generate();
            //System.exit(0);
        }}.start();
    }



    private static final int WIDTH = 4096;
    private static final int HEIGHT = 4096;
    private static final boolean TEN_PERCENT_GRID = false;

    private static void generate() {
        for (int y = 0; y < HEIGHT; y++) { for (int x = 0; x < WIDTH; x++) {

            final int finalColour;
            if (TEN_PERCENT_GRID && ((x % (WIDTH/10)) == 0 || (y % (HEIGHT/10)) == 0)) {
                finalColour = 0xff00ffff;
            } else {
                // Here you manually switch the function to get the fractal you want
                finalColour = mandlebrot(x, y);
            }
            final int finalX = x;
            final int finalY = y;
            Platform.runLater(() -> pw.setArgb(finalX, finalY, finalColour));
        }}

        try {
            BufferedImage img = SwingFXUtils.fromFXImage(wrImg, null);
            ImageIO.write(img, "png", new File(path));
        } catch (IOException e) {}
    }

    private static int RGB(int r, int g, int b) {
        r = Math.max(0, Math.min(r, 255));
        g = Math.max(0, Math.min(g, 255));
        b = Math.max(0, Math.min(b, 255));
        return 0xff000000 | r << 16 | g << 8 | b;
    }

    private static int getColour(int iter, ComplexNumber z) {
        if (iter == MAX_ITER) {return 0xff000000;}

        /*
          The basic colour formula is: ((255 * iter)/MAX_ITER) % 4 * 64
          Some smoothing straight from wikipedia that I don't quite understand is added ontop of it
        */
        double log_zn = Math.log(z.re()*z.re() + z.im()*z.im());
        double nu = Math.log(log_zn/Math.log(2)) / Math.log(2);
        double adjustIter = iter + 1 - nu;

        double c1 = ((255 * adjustIter)/MAX_ITER) % 4 * 64;
        double c2 = ((255 * (adjustIter + 1))/MAX_ITER) % 4 * 64;
        int c = (int) (c1 + (adjustIter % 1) * (c2 - c1));

        return RGB(255, c, c);
    }

    /*
      The coordinates you want to show will always be different between fractals, so you have to
       manually uncomment the particular ones you want
      Sometimes too many iterations makes stuff dissapear into the background, so those are specific
       to fractals too
    */

    /*
    private static final int MAX_ITER = 16383;
    private static final double MIN_X = -1.85;
    private static final double MAX_X = -1.65;
    private static final double MIN_Y = -0.05;
    private static final double MAX_Y = 0.15;
    */
    private static int burningShip(int x, int y) {
        double scale_x = ((double) x) * (MAX_X - MIN_X)/WIDTH + MIN_X;
        // Flip it vertically so the "ship" is upright
        double scale_y = ((double) HEIGHT - y) * (MAX_Y - MIN_Y)/HEIGHT + MIN_Y;

        /*
          This function doesn't use any specific complex number stuff so we can store it as two
           doubles rather than add all the function calls from storing it as a ComplexNumber
        */
        double re = scale_x;
        double im = scale_y;

        int iter = 0;
        while (re*re + im*im < 4 && iter < MAX_ITER) {
            double newRe = re*re - im*im + scale_x;
            im = Math.abs(2*re*im - scale_y);
            re = Math.abs(newRe);

            iter++;
        }
        int colour = getColour(iter, new ComplexNumber(re, im));
        // colour will be red-ish, force it to grey
        return 0xff000000 | (colour & 0xffff) | ((colour & 0xff) << 16);
    }

    // We also want to update width for this one cause it's not a square
    /*
    private static final int WIDTH = HEIGHT*11/7;
    private static final int MAX_ITER = 512;
    private static final double MIN_X = -5.5;
    private static final double MAX_X = 5.5;
    private static final double MIN_Y = -3.25;
    private static final double MAX_Y = 3.75;
    */
    private static int mistakeThatLookedCool(int x, int y) {
        double scale_x = ((double) x) * (MAX_X - MIN_X)/WIDTH + MIN_X;
        double scale_y = ((double) y) * (MAX_Y - MIN_Y)/HEIGHT + MIN_Y;

        ComplexNumber z = new ComplexNumber(scale_x, scale_y);

        int iter = 0;
        while (z.mod() < 64 && iter < MAX_ITER) {
            z = z.cosh().add(-1, 0);

            iter++;
        }
        return getColour(iter, z);
    }

    /*
    private static final int MAX_ITER = 512;
    private static final double MIN_X = -3.5;
    private static final double MAX_X = 3.5;
    private static final double MIN_Y = -3.5;
    private static final double MAX_Y = 3.5;

    private static final int MAX_ITER = 512;
    private static final double MIN_X = 0;
    private static final double MAX_X = 1;
    private static final double MIN_Y = -0.5;
    private static final double MAX_Y = 0.5;
    */
    private static final int MAX_ITER = 4096;
    private static final double MIN_X = -2.25;
    private static final double MAX_X = -1.25;
    private static final double MIN_Y = -0.5;
    private static final double MAX_Y = 0.5;

    private static int mandlebrot(int x, int y) {
        double scale_x = ((double) x) * (MAX_X - MIN_X)/WIDTH + MIN_X;
        double scale_y = ((double) y) * (MAX_Y - MIN_Y)/HEIGHT + MIN_Y;

        ComplexNumber z = new ComplexNumber(scale_x, scale_y);
        ComplexNumber c = z.clone().add(-1, 0);

        int iter = 0;
        // Use 8 with the third set of coords
        //while (z.mod() < 2 && iter < MAX_ITER) {
        while (z.mod() < 8 && iter < MAX_ITER) {
            z = z.cosh().add(c);

            iter++;
        }
        return getColour(iter, z);
    }

    /*
    private static final int MAX_ITER = 16383;
    private static final double MIN_X = -0.5;
    private static final double MAX_X = 1.5;
    private static final double MIN_Y = -1;
    private static final double MAX_Y = 1;

    private static final int MAX_ITER = 512;
    private static final double MIN_X = -0.4;
    private static final double MAX_X = 0.6;
    private static final double MIN_Y = -0.5;
    private static final double MAX_Y = 0.5;
    */
    private static int logThing(int x, int y) {
        double scale_x = ((double) x) * (MAX_X - MIN_X)/WIDTH + MIN_X;
        double scale_y = ((double) y) * (MAX_Y - MIN_Y)/HEIGHT + MIN_Y;

        ComplexNumber z = new ComplexNumber(scale_x, scale_y);
        ComplexNumber c = z.clone().add(-1, 0);

        int iter = 0;
        while (z.mod() < 2 && iter < MAX_ITER) {
            z = z.mult(z.log(1.75)).add(c);

            iter++;
        }
        return getColour(iter, z);
    }
}
