package ascii_art;

import image.Image;
import image.ImageManager;
import image_char_matching.SubImgCharMatcher;

import java.util.ArrayList;

public class AsciiArtAlgorithm {

    private final int charWidth;
    private final int charHeight;
    private final char[] charSet;
    private Image image;


    public AsciiArtAlgorithm(Image image, int charWidth, int charHeight,
                             char[] charSet) {
        this.image = image;
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        this.charSet = charSet;
    }

    public char[][] run() {

        // create utility classes
        ImageManager imageManager = new ImageManager();
        SubImgCharMatcher imageMatcher = new SubImgCharMatcher(charSet);

        char[][] resultAsciiImage = new char[charHeight][charWidth];
        Image[][] subImages = imageManager.divideToSubImages(image,
                charHeight, charWidth);

        for (int i = 0; i < charHeight; i++) {
            for (int j = 0; j < charWidth; j++) {
                double imageBrightness = imageManager.getImageBrightness(subImages[i][j]);
                resultAsciiImage[i][j] =
                        imageMatcher.getCharByImageBrightness(imageBrightness);
            }
        }
        return resultAsciiImage;
    }
}
