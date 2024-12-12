package image;

import java.awt.*;

public class ImageManager {

    private static final double BLUE_FACTOR = 0.0722;
    private static final double GREEN_FACTOR = 0.7152;
    private static final double RED_FACTOR = 0.2126;

    /**
     * Adds padding to the given image to make its dimensions the closest power of two.
     *
     * @param image the original image to be padded
     * @return a new Image object with padding added, or the original image if no padding is needed
     */
    public Image imagePadding(Image image) {

        int oldHeight = image.getHeight();
        int oldWidth = image.getWidth();

        // Calculating padding needed on each side
        int verticalPadding =
                (findClosestPowerOfTwo(oldHeight) - oldHeight) / 2;
        int horizontalPadding =
                (findClosestPowerOfTwo(oldWidth) - oldWidth) / 2;

        int newHeight = oldHeight + (verticalPadding * 2);
        int newWidth = oldWidth + (horizontalPadding * 2);

        // If new dimensions are same as old ones, do nothing
        if (newWidth == oldWidth && newHeight == oldHeight) {
            return image;
        }

        // actually padding the image.
        Color[][] paddedImage = new Color[newHeight][newWidth];
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (i < verticalPadding
                        || i >= verticalPadding + oldHeight
                        || j < horizontalPadding
                        || j >= horizontalPadding + oldWidth) {
                    paddedImage[i][j] = new Color(255, 255, 255);
                } else {
                    paddedImage[i][j] =
                            image.getPixel(i - verticalPadding,
                                    j - horizontalPadding);
                }
            }
        }
        return new Image(paddedImage, newWidth, newHeight);
    }


    // finding the Closest bigger power of two given an Integer
    private int findClosestPowerOfTwo(int num) {
        int highestOneBit = Integer.highestOneBit(num);
        return (num == highestOneBit) ? num : highestOneBit << 1;
    }


    /**
     * Divides the given image into a grid of sub-images.
     *
     * @param image  the original image to be divided
     * @param height the number of sub-images along the vertical axis
     * @param width  the number of sub-images along the horizontal axis
     * @return a 2D array of sub-images
     */
    public Image[][] divideToSubImages(Image image, int height, int width) {

        Image[][] subImagesGrid = new Image[height][width];

        // Calculating the subImage dimensions
        int subImageWidth = image.getWidth() / width;
        int subImageHeight = image.getHeight() / height;

        // division into sub-images
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                subImagesGrid[i][j] = CreateSubImage(image, i * subImageHeight, j * subImageWidth, subImageHeight, subImageWidth);
            }
        }
        return subImagesGrid;
    }


    /**
     * Creates one sub-image from the given image.
     *
     * @param image          the original image
     * @param startRowIdx    the starting row index for the sub-image
     * @param startColIdx    the starting column index for the sub-image
     * @param subImageHeight the height of the sub-image
     * @param subImageWidth  the width of the sub-image
     * @return a new Image object representing the sub-image
     */
    private Image CreateSubImage(Image image, int startRowIdx,
                                 int startColIdx, int subImageHeight,
                                 int subImageWidth) {

        Color[][] newSubImage = new Color[subImageHeight][subImageWidth];

        // coping the SubImage part from the source image
        for (int i = 0; i < subImageHeight; i++) {
            for (int j = 0; j < subImageWidth; j++) {
                newSubImage[i][j] = image.getPixel(startRowIdx + i,
                        startColIdx + j);
            }
        }
        return new Image(newSubImage, subImageWidth, subImageHeight);
    }

    /**
     * Calculates the brightness of the given image.
     *
     * @param image the image whose brightness is to be calculated
     * @return the brightness of the image as a value between 0 and 1
     */
    public double getImageBrightness(Image image) {
        int pixelSumValue = 0;
        int pixelCount = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                pixelSumValue += greyscalePixel(image.getPixel(i, j));
                pixelCount++;
            }
        }
        // returning normalized value (0 to 1)
        return ((double) pixelSumValue / (pixelCount * 255));
    }

    /**
     * Converts a color to its greyscale value using the luminance formula.
     *
     * @param color the color to be converted to greyscale
     * @return the greyscale value of the color
     */
    private int greyscalePixel(Color color) {
        return (int) (color.getRed() * RED_FACTOR + color.getGreen() * GREEN_FACTOR + color.getBlue() * BLUE_FACTOR);
    }
}

