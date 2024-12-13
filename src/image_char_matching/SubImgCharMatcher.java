package image_char_matching;

import java.util.*;

/**
 * The SubImgCharMatcher class is responsible for matching characters to image brightness levels.
 * It maintains a mapping of characters to their brightness values and provides methods to find
 * the closest matching character for a given brightness level.
 */
public class SubImgCharMatcher {
    private static final int TOTAL_PIXELS = 16 * 16;

    // Maps normalized brightness to a set of characters.
    // The characters in the set are sorted by their ASCII values.
    private final TreeMap<Double, TreeSet<Character>> normalizedBrightnessMap;

    // Maps characters to their raw brightness values (before normalization).
    private final HashMap<Character, Double> rawCharBrightnessMap;

    /**
     * Constructs a SubImgCharMatcher with the given character set.
     * Initializes the brightness mappings for the characters.
     *
     * @param charset the array of characters to be used for matching
     */
    public SubImgCharMatcher(char[] charset) {
        this.normalizedBrightnessMap = new TreeMap<>();
        this.rawCharBrightnessMap = new HashMap<>();
        for (char c : charset) {
            rawCharBrightnessMap.put(c, calculateCharBrightness(c));
        }
        normalizeBrightness();
    }

    /**
     * Finds the character that best matches the given brightness level.
     * The closest brightness value is determined using the TreeMap.
     *
     * @param brightness the brightness level to match
     * @return the character that best matches the given brightness level
     */
    public char getCharByImageBrightness(double brightness) {
        // Find the closest normalized brightness value using TreeMap.
        Map.Entry<Double, TreeSet<Character>> floorEntry = normalizedBrightnessMap.floorEntry(brightness);
        Map.Entry<Double, TreeSet<Character>> ceilingEntry = normalizedBrightnessMap.ceilingEntry(brightness);

        if (floorEntry == null) return ceilingEntry.getValue().first(); // No smaller value, return the smallest ASCII character in the ceiling set.
        if (ceilingEntry == null) return floorEntry.getValue().first(); // No larger value, return the smallest ASCII character in the floor set.

        // Compare which is closer to the target brightness.
        double floorDiff = Math.abs(floorEntry.getKey() - brightness);
        double ceilingDiff = Math.abs(ceilingEntry.getKey() - brightness);

        // Get the char with the closest brightness and with the lowest ASCII value.
        if (floorDiff < ceilingDiff || (floorDiff == ceilingDiff && floorEntry.getValue().first() < ceilingEntry.getValue().first())) {
            return floorEntry.getValue().first();
        } else {
            return ceilingEntry.getValue().first();
        }
    }

    /**
     * Adds a character to the matcher and updates the brightness mappings.
     *
     * @param c the character to add
     */
    public void addChar(char c) {
        if (!rawCharBrightnessMap.containsKey(c)) {
            double brightness = calculateCharBrightness(c);
            rawCharBrightnessMap.put(c, brightness);
            normalizeBrightness();
        }
    }

    /**
     * Removes a character from the matcher and updates the brightness mappings.
     *
     * @param c the character to remove
     */
    public void removeChar(char c) {
        if (rawCharBrightnessMap.containsKey(c)) {
            rawCharBrightnessMap.remove(c);
            normalizeBrightness();
        }
    }

    /*
     * Calculates the brightness of a given character.
     * The brightness is determined by the proportion of black pixels in the character's image.
     *
     * @param c the character to calculate the brightness for
     * @return the brightness value of the character
     */
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

    /*
     * Normalizes the brightness values of the characters and updates the normalized brightness map.
     * The normalized brightness is calculated using a linear stretch formula.
     */
    private void normalizeBrightness() {
        normalizedBrightnessMap.clear(); // Clear the existing normalized map.

        double minBrightness = Collections.min(rawCharBrightnessMap.values());
        double maxBrightness = Collections.max(rawCharBrightnessMap.values());

        for (Map.Entry<Character, Double> entry : rawCharBrightnessMap.entrySet()) {
            // Calculate the normalized brightness using the linear stretch formula.
            double normalizedBrightness = (entry.getValue() - minBrightness) / (maxBrightness - minBrightness);

            // Add the character to the corresponding TreeSet in the map.
            normalizedBrightnessMap.putIfAbsent(normalizedBrightness, new TreeSet<>());
            // Get the tree that represents the brightness - normalizedBrightness.
            TreeSet<Character> brightnessTree = normalizedBrightnessMap.get(normalizedBrightness);
            // Add the character to the tree such that it will automatically be ordered by ASCII values.
            brightnessTree.add(entry.getKey());
        }
    }
}