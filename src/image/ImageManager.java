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
    public static Image imagePadding(Image image) {

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
    private static int findClosestPowerOfTwo(int num) {
        int highestOneBit = Integer.highestOneBit(num);
        return (num == highestOneBit) ? num : highestOneBit << 1;
    }

    public static int countVertiaclSubImages(Image image, int resolution) {
        int subImageSize = getSubImageSize(image, resolution);
        return image.getHeight() / subImageSize;
    }

    private static int getSubImageSize(Image image, int resolution) {
        return image.getWidth() / resolution;
    }

    public static Image[][] divideToSubImages(Image image, int resolution) {

        // calculating number of subImages in column.
        int subImagesInCol = countVertiaclSubImages(image, resolution);
        Image[][] subImagesGrid = new Image[subImagesInCol][resolution];

        int subImageSize = getSubImageSize(image, resolution);
        // division into sub-images
        for (int i = 0; i < subImagesInCol; i++) {
            for (int j = 0; j < resolution; j++) {
                subImagesGrid[i][j] = createSubImage(image,
                        i * subImageSize, j * subImageSize, subImageSize);
            }
        }
        return subImagesGrid;
    }


    private static Image createSubImage(Image image, int startRowIdx,
                                 int startColIdx, int subImageSize) {

        Color[][] newSubImage = new Color[subImageSize][subImageSize];

        // coping the SubImage part from the source image
        for (int i = 0; i < subImageSize; i++) {
            for (int j = 0; j < subImageSize; j++) {
                newSubImage[i][j] = image.getPixel(startRowIdx + i,
                        startColIdx + j);
            }
        }
        return new Image(newSubImage, subImageSize, subImageSize);
    }

    /**
     * Calculates the brightness of the given image.
     *
     * @param image the image whose brightness is to be calculated
     * @return the brightness of the image as a value between 0 and 1
     */
    public static double getImageBrightness(Image image) {
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
    private static int greyscalePixel(Color color) {
        return (int) (color.getRed() * RED_FACTOR + color.getGreen() * GREEN_FACTOR + color.getBlue() * BLUE_FACTOR);
    }
}

