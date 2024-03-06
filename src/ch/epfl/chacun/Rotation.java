package ch.epfl.chacun;

import java.util.List;

/**
 * Enumeration representing different possible rotations.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public enum Rotation {

    /**
     * Represents no rotation (0 degrees).
     */
    NONE,

    /**
     * Represents a 90-degree clockwise rotation.
     */
    RIGHT,

    /**
     * Represents a 180-degree rotation.
     */
    HALF_TURN,

    /**
     * Represents a 90-degree counterclockwise rotation.
     */
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
