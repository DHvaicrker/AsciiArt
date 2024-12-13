package image;

import org.junit.jupiter.api.Test;
import java.awt.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class ImageManagerTest {

    @Test
    void testImagePadding() throws IOException {
        // Create a sample image with known dimensions
        Color[][] pixelArray = new Color[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                pixelArray[i][j] = new Color(0, 0, 0);
            }
        }
        Image image = new Image(pixelArray, 6, 6);

        ImageManager imageManager = new ImageManager();
        Image paddedImage = imageManager.imagePadding(image);

        // Verify the new dimensions are powers of 2
        assertEquals(8, paddedImage.getWidth());
        assertEquals(8, paddedImage.getHeight());

        // Verify the padding is white
        for (int i = 0; i < paddedImage.getHeight(); i++) {
            for (int j = 0; j < paddedImage.getWidth(); j++) {
                if (i < 1 || i >= 7 || j < 1 || j >= 7) {
                    assertEquals(new Color(255, 255, 255), paddedImage.getPixel(i, j));
                } else {
                    assertEquals(new Color(0, 0, 0), paddedImage.getPixel(i, j));
                }
            }
        }
    }

    @Test
    void testImagePaddingWithNonSquareImage() throws IOException {
        // Create a sample non-square image with known dimensions
        Color[][] pixelArray = new Color[6][4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                pixelArray[i][j] = new Color(0, 0, 0);
            }
        }
        Image image = new Image(pixelArray, 4, 6);

        ImageManager imageManager = new ImageManager();
        Image paddedImage = imageManager.imagePadding(image);

        // Verify the new dimensions are powers of 2
        assertEquals(4, paddedImage.getWidth());
        assertEquals(8, paddedImage.getHeight());
    }
}