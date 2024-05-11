package ch.epfl.chacun;

import java.util.List;

/**
 * Enum representing the directions.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public enum Direction {

    /**
     * Represents the North direction.
     */
    N,

    /**
     * Represents the East direction.
     */
    E,

    /**
     * Represents the South direction.
     */
    S,

    /**
     * Represents the West direction.
     */
    W;

    /**
     * Immutable list of all directions.
     */
    public static final List<Direction> ALL = List.of(values());

    /**
     * Total number of directions.
     */
    public static final int COUNT = ALL.size();

    /**
     * Rotates the direction based on the specified rotation.
     *
     * @param rotation the rotation to apply
     * @return the rotated direction
     */
    public Direction rotated(Rotation rotation){
        return ALL.get((this.ordinal() + rotation.quarterTurnsCW()) % COUNT);
    }

    /**
     * Gets the opposite direction.
     *
     * @return the opposite direction
     */
    public Direction opposite() {
        return rotated(Rotation.HALF_TURN);
    }
}
