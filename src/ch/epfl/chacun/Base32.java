package ch.epfl.chacun;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class that contains method to encode binary values in base32 and decode base32 values in binary.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public final class Base32 {
    private Base32(){}

    /**
     * The Base32 alphabet used for encoding and decoding.
     */
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    /**
     * Checks if the given string is a valid Base32 string.
     *
     * @param value The string to be checked.
     * @return      true if the string is a valid Base32 string, false otherwise.
     */
    public static boolean isValid(String value) {
        return value.chars().allMatch(c -> ALPHABET.indexOf((char) c) != -1);
    }

    /**
     * Encodes the given 5-bit value into Base32.
     *
     * @param value The value to be encoded.
     * @return      the Base32 encoded string.
     * @throws IllegalArgumentException if the value is not within the range [0, 31].
     */
    public static String encodeBits5(int value) {
        Preconditions.checkArgument(value >= 0 && value <= 31);
        return String.valueOf(ALPHABET.charAt(value));
    }

    /**
     * Encodes the given 10-bit value into Base32.
     *
     * @param value The value to be encoded.
     * @return      the Base32 encoded string.
     * @throws IllegalArgumentException if the value is not within the range [0, 1023].
     */
    public static String encodeBits10(int value) {
        Preconditions.checkArgument(value >= 0 && value <= Math.pow(32, 2) - 1);
        return STR."\{ALPHABET.charAt(value / 32)}\{ALPHABET.charAt(value % 32)}";
    }

    /**
     * Decodes the given Base32 string into an integer value.
     *
     * @param value The Base32 string to be decoded.
     * @return      the decoded integer value.
     * @throws IllegalArgumentException if the string length is greater than 2 or if the string is not a valid Base32 string.
     */
    public static int decode(String value) {
        Preconditions.checkArgument(value.length() <= 2 && isValid(value));
        return value.chars()
                .map(c -> ALPHABET.indexOf((char) c))
                .reduce((i1, i2) -> (i1 * 32) + i2)
                .orElse(-1); //should never happen
    }
}
