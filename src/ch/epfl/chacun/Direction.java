package ch.epfl.chacun;

import java.util.List;

/**
 * enum representing the directions
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public enum Direction {
    N,
    E,
    S,
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
     * gives the rotated direction
     * @param rotation degree of rotation
     * @return rotated direction (Direction)
     */
    public Direction rotated(Rotation rotation){
        return ALL.get((this.ordinal() + rotation.quarterTurnsCW()) % COUNT);
    }

    /**
     * gives the opposite direction
     * @return the opposite direction (Direction)
     */
    public Direction opposite() {
        return ALL.get((this.ordinal() + Rotation.HALF_TURN.ordinal()) % COUNT);
    }
}
