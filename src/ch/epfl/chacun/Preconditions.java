package ch.epfl.chacun;

/**
 * This class provides static methods to check conditions and throw exceptions if they fail.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public final class Preconditions {

    /**
     * Private constructor to prevent instantiation.
     */
    private Preconditions() {}

    /**
     * Throws an exception if the given argument is false
     * @param shouldBeTrue the condition to check
     * @throws IllegalArgumentException if the condition is false
     */
    public static void checkArgument(boolean shouldBeTrue) throws IllegalArgumentException{
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}
