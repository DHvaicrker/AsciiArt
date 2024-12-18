package ascii_art;

import image.Image;
import image.ImageManager;
import image_char_matching.SubImgCharMatcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The AsciiArtAlgorithm class converts an image to ASCII art.
 */
public class AsciiArtAlgorithm {

    private final int resolution;
    private final int numOfVerticalSubImages;
    private final SubImgCharMatcher subImageCharMatcher;
    // represents the brightness of each subImage.
    private double[][] brightnessGrid;


    public AsciiArtAlgorithm(Image image, int resolution,
                             SubImgCharMatcher subImageCharMatcher) {
        this.resolution = resolution;
        this.subImageCharMatcher = subImageCharMatcher;
        numOfVerticalSubImages =
                ImageManager.countVerticalSubImages(image, resolution);
        initialSetup(image, resolution);
    }

    private void initialSetup(Image image, int resolution) {
        Image[][] subImages = ImageManager.divideToSubImages(image,
                resolution);
        // subImages - and their brightnesses don't change throughout class lifetime
        brightnessGrid = new double[numOfVerticalSubImages][resolution];
        for (int i = 0; i < numOfVerticalSubImages; i++) {
            for (int j = 0; j < resolution; j++) {
                brightnessGrid[i][j] =
                        ImageManager.getImageBrightness(subImages[i][j]);
            }
        }
    }

    /**
     * Runs the ASCII art conversion algorithm.
     *
     * @return a 2D array of characters representing the ASCII art
     */
    public char[][] run() {
        char[][] resultAsciiImage = new char[numOfVerticalSubImages][resolution];
        for (int i = 0; i < numOfVerticalSubImages; i++) {
            for (int j = 0; j < resolution; j++) {

                resultAsciiImage[i][j] = subImageCharMatcher.getCharByImageBrightness(brightnessGrid[i][j]);

            }
        }
        return resultAsciiImage;
    }
}