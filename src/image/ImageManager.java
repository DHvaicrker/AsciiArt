package image;

import java.awt.*;

public class ImageManager {

    private static final double BLUE_FACTOR = 0.0722;
    private static final double GREEN_FACTOR = 0.7152;
    private static final double RED_FACTOR = 0.2126;

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
                    paddedImage[i][j] = new Color(255, 255, 255);
                } else {
                    paddedImage[i][j] = image.getPixel(i - verticalPadding, j - horizontalPadding);
                }
            }
        }
        return new Image(paddedImage, newWidth, newHeight);
    }

    private static int findClosestPowerOfTwo(int num) {
        int highestOneBit = Integer.highestOneBit(num);
        return (num == highestOneBit) ? num : highestOneBit << 1;
    }

    public static int countVerticalSubImages(Image image, int resolution) {
        int subImageSize = getSubImageSize(image, resolution);
        return image.getHeight() / subImageSize;
    }

    private static int getSubImageSize(Image image, int resolution) {
        return image.getWidth() / resolution;
    }

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

    private static Image createSubImage(Image image, int startRowIdx, int startColIdx, int subImageSize) {
        Color[][] newSubImage = new Color[subImageSize][subImageSize];
        for (int i = 0; i < subImageSize; i++) {
            for (int j = 0; j < subImageSize; j++) {
                newSubImage[i][j] = image.getPixel(startRowIdx + i, startColIdx + j);
            }
        }
        return new Image(newSubImage, subImageSize, subImageSize);
    }

    public static double getImageBrightness(Image image) {
        double pixelSumValue = 0.0;
        int pixelCount = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                pixelSumValue += greyscalePixel(image.getPixel(i, j));
                pixelCount++;
            }
        }
        return pixelSumValue / (pixelCount * 255);
    }

    private static double greyscalePixel(Color color) {
        return color.getRed() * RED_FACTOR + color.getGreen() * GREEN_FACTOR + color.getBlue() * BLUE_FACTOR;
    }
}
