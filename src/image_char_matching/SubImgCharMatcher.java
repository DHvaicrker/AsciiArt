package image_char_matching;
import java.util.*;
public class SubImgCharMatcher {
    private static final  int TOTAL_PIXELS = 16 * 16;


    // need to specific behavior that the Char will be sorted by brightness
    // and char with the same brightness would be sorted by their Ascii values
    private final TreeMap<Double, TreeSet<Character>> normalizedBrightnessMap; // Maps normalized brightness to a set of characters.
    // programing to an interface not implementation
    private final Map<Character, Double> rawCharBrightnessMap; // Maps characters to their raw brightness values (before normalization).

    public SubImgCharMatcher(char[] charset) {
        this.normalizedBrightnessMap = new TreeMap<>();
        this.rawCharBrightnessMap = new HashMap<>();
        for (char c : charset) {
            rawCharBrightnessMap.put(c,calculateCharBrightness(c));
        }
        normalizeBrightness();
    }

    public char getCharByImageBrightness(double brightness) {
        // Find the closest normalized brightness value using TreeMap.
        Map.Entry<Double, TreeSet<Character>> floorEntry = normalizedBrightnessMap.floorEntry(brightness);
        Map.Entry<Double, TreeSet<Character>> ceilingEntry = normalizedBrightnessMap.ceilingEntry(brightness);

        if (floorEntry == null) return ceilingEntry.getValue().first(); // No smaller value, return the smallest ASCII character in the ceiling set.
        if (ceilingEntry == null) return floorEntry.getValue().first(); // No larger value, return the smallest ASCII character in the floor set.

        // Compare which is closer to the target brightness.
        double floorDiff = Math.abs(floorEntry.getKey() - brightness);
        double ceilingDiff = Math.abs(ceilingEntry.getKey() - brightness);

        // get the char with the closest brightness and with the lowest ascii value :
        if (floorDiff < ceilingDiff || (floorDiff == ceilingDiff && floorEntry.getValue().first() < ceilingEntry.getValue().first())) {
            return floorEntry.getValue().first();
        } else {
            return ceilingEntry.getValue().first();
        }
    }

    public void addChar(char c) {
        if (!rawCharBrightnessMap.containsKey(c)) {
            double brightness = calculateCharBrightness(c);
            rawCharBrightnessMap.put(c, brightness);
            normalizeBrightness();
        }
    }

    public void removeChar(char c) {
        if (rawCharBrightnessMap.containsKey(c)) {
            rawCharBrightnessMap.remove(c);
            normalizeBrightness();
        }
    }

    private double calculateCharBrightness(char c) {
        boolean[][] charArray = CharConverter.convertToBoolArray(c);
        int trueCount = 0;


        for (boolean[] row : charArray) {
            for (boolean pixel : row) {
                if (pixel) {
                    trueCount++;
                }
            }
        }

        return (double) trueCount / TOTAL_PIXELS;
    }

    private void normalizeBrightness() {
        normalizedBrightnessMap.clear(); // Clear the existing normalized map.

        double minBrightness = Collections.min(rawCharBrightnessMap.values());
        double maxBrightness = Collections.max(rawCharBrightnessMap.values());

        for (Map.Entry<Character, Double> entry : rawCharBrightnessMap.entrySet()) {
            // Calculate the normalizedBrightness by the Linear Stretch Formula:
            double normalizedBrightness = (entry.getValue() - minBrightness) / (maxBrightness - minBrightness);

            // Add the character to the corresponding TreeSet in the map.
            normalizedBrightnessMap.putIfAbsent(normalizedBrightness, new TreeSet<>());
            // Get the tree that represent the brightness - normalizedBrightness
            TreeSet<Character> brightnessTree = normalizedBrightnessMap.get(normalizedBrightness);
            // Add the character to the tree such that it will automatically be ordered by ascii values
            brightnessTree.add(entry.getKey());
        }
    }
}
