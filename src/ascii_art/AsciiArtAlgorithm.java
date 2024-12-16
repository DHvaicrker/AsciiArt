package ascii_art;

import image.Image;
import image.ImageManager;
import image_char_matching.SubImgCharMatcher;

/**
 * The AsciiArtAlgorithm class converts an image to ASCII art.
 */
public class AsciiArtAlgorithm {

    private final int charWidth;
    private final int charHeight;
    private final ImageManager imageManager;
    private final SubImgCharMatcher imageMatcher;
    // represents the brightness of each subImage.
    private double[][] brightnessGrid;

    /**
     * Constructs an AsciiArtAlgorithm instance.
     *
     * @param image      the image to be converted to ASCII art
     * @param charWidth  the width of each character in the ASCII art
     * @param charHeight the height of each character in the ASCII art
     * @param charSet    the set of characters to be used in the ASCII art
     */
    public AsciiArtAlgorithm(Image image, int charWidth, int charHeight, char[] charSet) {
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        this.imageManager = new ImageManager();
        this.imageMatcher = new SubImgCharMatcher(charSet);
        initialSetup(image, charHeight, charWidth);
    }

    /**
     * Initializes the brightness grid by dividing the image into sub-images
     * and calculating their brightness.
     *
     * @param image      the image to be divided
     * @param charHeight the height of each sub-image
     * @param charWidth  the width of each sub-image
     */
    private void initialSetup(Image image, int charHeight, int charWidth) {
        Image[][] subImages = imageManager.divideToSubImages(image, charHeight, charWidth);
        // subImages - and their brightnesses don't change throughout class lifetime
        brightnessGrid = new double[charHeight][charWidth];
        for (int i = 0; i < charHeight; i++) {
            for (int j = 0; j < charWidth; j++) {
                brightnessGrid[i][j] = imageManager.getImageBrightness(subImages[i][j]);
            }
        }
    }

    /**
     * Runs the ASCII art conversion algorithm.
     *
     * @return a 2D array of characters representing the ASCII art
     */
    public char[][] run() {
        char[][] resultAsciiImage = new char[charHeight][charWidth];
        for (int i = 0; i < charHeight; i++) {
            for (int j = 0; j < charWidth; j++) {
                resultAsciiImage[i][j] = imageMatcher.getCharByImageBrightness(brightnessGrid[i][j]);
            }
        }
        return resultAsciiImage;
    }
}