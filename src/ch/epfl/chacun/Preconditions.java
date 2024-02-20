package ch.epfl.chacun;

/**
 * This class provides static methods to check conditions and throw exceptions if they fail.
 */
public final class Preconditions {
    private Preconditions() {}

    public static void checkArgument(boolean shouldBeTrue) throws IllegalArgumentException{
        if (!shouldBeTrue) {
            throw new IllegalArgumentException("Error: This parameter is wrong");
        }
    }
}
