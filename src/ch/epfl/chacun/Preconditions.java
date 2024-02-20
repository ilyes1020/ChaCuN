package ch.epfl.chacun;

public final class Preconditions {
    private Preconditions() {}

    public static void checkArgument(boolean shouldBeTrue) throws IllegalArgumentException{
        if (!shouldBeTrue) {
            throw new IllegalArgumentException("Error: This parameter is wrong");
        }
    }
}
