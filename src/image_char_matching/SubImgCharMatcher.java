package image_char_matching;

import java.util.*;

/**
 * The SubImgCharMatcher class is responsible for matching characters to image brightness levels.
 * It maintains a mapping of characters to their brightness values and provides methods to find
 * the closest matching character for a given brightness level.
 */
public class SubImgCharMatcher {
    private static final int TOTAL_PIXELS = 16 * 16;
    public static final String CELLING_ROUND_KEYWORD = "up";
    public static final String FLOOR_ROUNDING_KEYWORD = "down";
    public static final String ABS_ROUNDING_KEYWORD = "abs";
    public static final String ROUNDING_ERROR_MSG = "Did not change rounding method due to incorrect format";
    public static final int MIN_NUM_OF_CHARS_TO_NORMELIZE = 2;

    // Maps normalized brightness to a set of characters.
    // In the case of a tie, the character with the lower ASCII value is chosen.
    private final TreeMap<Double, Character> normalizedBrightnessMap;

    // Maps characters to their raw brightness values (before normalization).
    private final HashMap<Character, Double> rawCharBrightnessMap;
    private static final int ROUND_METHOD_FLOOR = -1;
    private static final int ROUND_METHOD_CEIL = 1;
    private static final int ROUND_METHOD_ABS = 0;
    private int roundMethod = 0;

    /**
     * Sets the rounding method for brightness matching.
     *
     * @param roundMethod the rounding method ("up", "down", or "abs")
     * @throws IllegalArgumentException if the rounding method is invalid
     */
    public void setRoundMethod(String roundMethod) throws IllegalArgumentException {
        if (roundMethod.equals(CELLING_ROUND_KEYWORD)) {
            this.roundMethod = ROUND_METHOD_CEIL;
        } else if (roundMethod.equals(FLOOR_ROUNDING_KEYWORD)) {
            this.roundMethod = ROUND_METHOD_FLOOR;
        } else if (roundMethod.equals(ABS_ROUNDING_KEYWORD)) {
            this.roundMethod = ROUND_METHOD_ABS;
        } else {
            throw new IllegalArgumentException(ROUNDING_ERROR_MSG);
        }
    }

    /**
     * Returns the character set used for matching.
     *
     * @return the character set used for matching
     */
    public TreeSet<Character> getCharSet() {
        return new TreeSet<>(rawCharBrightnessMap.keySet());
    }

    /**
     * Checks if the character is already in the matcher.
     *
     * @param c the character to check
     * @return true if the character is in the matcher, false otherwise
     */
    public boolean containsChar(char c) {
        return rawCharBrightnessMap.containsKey(c);
    }

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
     * @throws IllegalArgumentException if no character is found for the given brightness level
     */
    public char getCharByImageBrightness(double brightness) throws IllegalArgumentException {
        if (roundMethod == ROUND_METHOD_FLOOR) {
            Map.Entry<Double, Character> floorEntry = normalizedBrightnessMap.floorEntry(brightness);
            if (floorEntry == null) {
                throw new IllegalArgumentException(ROUNDING_ERROR_MSG);
            }
            return floorEntry.getValue();
        } else if (roundMethod == ROUND_METHOD_CEIL) {
            Map.Entry<Double, Character> ceilingEntry = normalizedBrightnessMap.ceilingEntry(brightness);
            if (ceilingEntry == null) {
                throw new IllegalArgumentException(ROUNDING_ERROR_MSG);
            }
            return ceilingEntry.getValue();
        }
        Map.Entry<Double, Character> floorEntry = normalizedBrightnessMap.floorEntry(brightness);
        Map.Entry<Double, Character> ceilingEntry = normalizedBrightnessMap.ceilingEntry(brightness);

        if (floorEntry == null) return ceilingEntry.getValue();
        if (ceilingEntry == null) return floorEntry.getValue();

        double floorDiff = Math.abs(floorEntry.getKey() - brightness);
        double ceilingDiff = Math.abs(ceilingEntry.getKey() - brightness);

        if (floorDiff < ceilingDiff) {
            return floorEntry.getValue();
        } else if (ceilingDiff < floorDiff) {
            return ceilingEntry.getValue();
        } else { // Tie-breaking: return the smallest ASCII character.
            return (floorEntry.getValue() < ceilingEntry.getValue())
                    ? floorEntry.getValue()
                    : ceilingEntry.getValue();
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
            if (rawCharBrightnessMap.size() >= MIN_NUM_OF_CHARS_TO_NORMELIZE) {
                normalizeBrightness();
            }
        }
    }

    /**
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

    /**
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

            // If the brightness value already exists, store the character with the lower ASCII value.
            if (normalizedBrightnessMap.containsKey(normalizedBrightness)) {
                char existingChar = normalizedBrightnessMap.get(normalizedBrightness);
                if (entry.getKey() < existingChar) {
                    normalizedBrightnessMap.put(normalizedBrightness, entry.getKey());
                }
            } else {
                normalizedBrightnessMap.put(normalizedBrightness, entry.getKey());
            }
        }
    }
}