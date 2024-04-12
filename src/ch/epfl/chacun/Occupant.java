package ch.epfl.chacun;

import java.util.Objects;

/**
 * Record that represents the occupant of a zone.
 *
 * @param kind the kind of the occupant (defined by the inner enum Kind)
 * @param zoneId the id of the zone the occupant occupies
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record Occupant(Kind kind, int zoneId) {

    /**
     * Enum representing the 2 kinds of occupant
     */
    public enum Kind {

        /**
         * Represents a pawn occupant.
         */
        PAWN,

        /**
         * Represents a hut occupant
         */
        HUT;
    }

    /**
     * Compact constructor.
     *
     * @param kind the kind of the occupant
     * @param zoneId the id of the zone the occupant occupies
     * @throws IllegalArgumentException if the zoneId is null or < 0
     */
    public Occupant {
        Objects.requireNonNull(kind);
        Preconditions.checkArgument(zoneId >= 0);
    }

    /**
     * Count the number total of occupant.
     *
     * @param kind the kind of the occupant
     * @return the total number of occupant (int)
     */
    public static int occupantsCount(Kind kind){
        return switch (kind) {
            case HUT -> 3;
            case PAWN -> 5;
        };
    }
}
