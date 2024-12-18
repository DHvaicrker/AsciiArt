package ascii_art;

import image.Image;
import image.ImageUtility;
import image_char_matching.SubImgCharMatcher;


/**
 * The AsciiArtAlgorithm class converts an image to ASCII art.
 * It divides the image into sub-images based on the specified resolution,
 * calculates the brightness of each sub-image, and matches each sub-image
 * to a character based on its brightness.
 */
public class AsciiArtAlgorithm {

    private final int resolution;
    private final int numOfVerticalSubImages;
    private final SubImgCharMatcher subImageCharMatcher;
    // Represents the brightness of each sub-image.
    private double[][] brightnessGrid;

    /**
     * Constructs an AsciiArtAlgorithm with the specified image, resolution, and character matcher.
     *
     * @param image the image to be converted to ASCII art
     * @param resolution the resolution for dividing the image into sub-images
     * @param subImageCharMatcher the matcher for finding characters based on brightness
     */
    public AsciiArtAlgorithm(Image image, int resolution, SubImgCharMatcher subImageCharMatcher) {
        this.resolution = resolution;
        this.subImageCharMatcher = subImageCharMatcher;
        numOfVerticalSubImages = ImageUtility.countVerticalSubImages(image, resolution);
        initialSetup(image, resolution);
    }

    /**
     * Initializes the brightness grid by dividing the image into sub-images
     * and calculating the brightness of each sub-image.
     *
     * @param image the image to be divided
     * @param resolution the resolution for dividing the image into sub-images
     */
    private void initialSetup(Image image, int resolution) {
        Image[][] subImages = ImageUtility.divideToSubImages(image, resolution);
        // Sub-images and their brightnesses don't change throughout class lifetime
        brightnessGrid = new double[numOfVerticalSubImages][resolution];
        for (int i = 0; i < numOfVerticalSubImages; i++) {
            for (int j = 0; j < resolution; j++) {
                brightnessGrid[i][j] = ImageUtility.getImageBrightness(subImages[i][j]);
            }
        }
    }

    /**
     * Runs the ASCII art conversion algorithm.
     * It matches each sub-image to a character based on its brightness.
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