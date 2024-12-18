package image;

import java.awt.*;
/**
 * The ImageUtility class provides utility methods for image processing.
 * It includes methods for padding images, dividing images into sub-images,
 * and calculating image brightness.
 */
public class ImageUtility {

    private static final double BLUE_FACTOR = 0.0722;
    private static final double GREEN_FACTOR = 0.7152;
    private static final double RED_FACTOR = 0.2126;
    private static final int MAX_PIXEL_BRIGHTNESS = 255;
    private static final Color WHITE_COLOR = new Color(255, 255, 255);

    /**
     * Pads the given image to the nearest power of two dimensions.
     * The padding is added equally on all sides with white color.
     *
     * @param image the image to be padded
     * @return the padded image
     */
    public static Image imagePadding(Image image) {
        int oldHeight = image.getHeight();
        int oldWidth = image.getWidth();

        int verticalPadding = (findClosestPowerOfTwo(oldHeight) - oldHeight) / 2;
        int horizontalPadding = (findClosestPowerOfTwo(oldWidth) - oldWidth) / 2;

        int newHeight = oldHeight + (verticalPadding * 2);
        int newWidth = oldWidth + (horizontalPadding * 2);

        if (newWidth == oldWidth && newHeight == oldHeight) {
            return image;
        }

        Color[][] paddedImage = new Color[newHeight][newWidth];
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (i < verticalPadding || i >= verticalPadding + oldHeight || j < horizontalPadding || j >= horizontalPadding + oldWidth) {
                    paddedImage[i][j] = WHITE_COLOR;
                } else {
                    paddedImage[i][j] = image.getPixel(i - verticalPadding, j - horizontalPadding);
                }
            }
        }
        return new Image(paddedImage, newWidth, newHeight);
    }

    /**
     * Finds the closest power of two greater than or equal to the given number.
     *
     * @param num the number to find the closest power of two for
     * @return the closest power of two
     */
    private static int findClosestPowerOfTwo(int num) {
        int highestOneBit = Integer.highestOneBit(num);
        return (num == highestOneBit) ? num : highestOneBit << 1;
    }

    /**
     * Counts the number of vertical sub-images that can be created from the given image
     * based on the specified resolution.
     *
     * @param image the image to be divided
     * @param resolution the resolution for dividing the image into sub-images
     * @return the number of vertical sub-images
     */
    public static int countVerticalSubImages(Image image, int resolution) {
        int subImageSize = getSubImageSize(image, resolution);
        return image.getHeight() / subImageSize;
    }

    /**
     * Calculates the size of each sub-image based on the image dimensions and resolution.
     *
     * @param image the image to be divided
     * @param resolution the resolution for dividing the image into sub-images
     * @return the size of each sub-image
     */
    private static int getSubImageSize(Image image, int resolution) {
        return image.getWidth() / resolution;
    }

    /**
     * Divides the given image into sub-images based on the specified resolution.
     *
     * @param image the image to be divided
     * @param resolution the resolution for dividing the image into sub-images
     * @return a 2D array of sub-images
     */
    public static Image[][] divideToSubImages(Image image, int resolution) {
        int subImagesInCol = countVerticalSubImages(image, resolution);
        Image[][] subImagesGrid = new Image[subImagesInCol][resolution];

        int subImageSize = getSubImageSize(image, resolution);
        for (int i = 0; i < subImagesInCol; i++) {
            for (int j = 0; j < resolution; j++) {
                subImagesGrid[i][j] = createSubImage(image, i * subImageSize, j * subImageSize, subImageSize);
            }
        }
        return subImagesGrid;
    }

    /**
     * Creates a sub-image from the given image starting at the specified row and column indices.
     *
     * @param image the image to create the sub-image from
     * @param startRowIdx the starting row index
     * @param startColIdx the starting column index
     * @param subImageSize the size of the sub-image
     * @return the created sub-image
     */
    private static Image createSubImage(Image image, int startRowIdx, int startColIdx, int subImageSize) {
        Color[][] newSubImage = new Color[subImageSize][subImageSize];
        for (int i = 0; i < subImageSize; i++) {
            for (int j = 0; j < subImageSize; j++) {
                newSubImage[i][j] = image.getPixel(startRowIdx + i, startColIdx + j);
            }
        }
        return new Image(newSubImage, subImageSize, subImageSize);
    }

    /**
     * Calculates the brightness of the given image.
     * The brightness is determined by the average greyscale value of the pixels.
     *
     * @param image the image to calculate the brightness for
     * @return the brightness value of the image
     */
    public static double getImageBrightness(Image image) {
        double pixelSumValue = 0.0;
        int pixelCount = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                pixelSumValue += greyscalePixel(image.getPixel(i, j));
                pixelCount++;
            }
        }
        return pixelSumValue / (pixelCount * MAX_PIXEL_BRIGHTNESS);
    }

    /**
     * Converts the given color to its greyscale value.
     *
     * @param color the color to convert
     * @return the greyscale value of the color
     */
    private static double greyscalePixel(Color color) {
        return color.getRed() * RED_FACTOR + color.getGreen() * GREEN_FACTOR + color.getBlue() * BLUE_FACTOR;
    }
}