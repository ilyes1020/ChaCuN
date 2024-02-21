package ch.epfl.chacun;

import java.util.List;

/**
 * Enumeration representing different possible rotations.
 */
public enum Rotation {
    NONE,
    RIGHT,
    HALF_TURN,
    LEFT;

    /**
     * Immutable list of all rotations.
     */
    public static final List<Rotation> ALL = List.of(values());

    /**
     * Total number of rotations.
     */
    public static final int COUNT = ALL.size();

    /**
     * Performs addition of two rotations.
     *
     * @param that The rotation to add to the current rotation.
     * @return The resulting rotation.
     */
    public Rotation add(Rotation that) {
        return ALL.get((this.ordinal() + that.ordinal()) % COUNT);
    }

    /**
     * @return The negated rotation.
     */
    public Rotation negated() {
        return ALL.get((COUNT - this.ordinal()) % COUNT);
    }
    /**
     * @return The number of quarter turns clockwise.
     */
    public int quarterTurnsCW() {
        return this.ordinal();
    }

    /**
     * @return The angle in degrees clockwise.
     */
    public int degreesCW() {
        return this.ordinal() * 90;
    }
}
