package ch.epfl.chacun;

import java.util.Objects;

/**
 * Record that represents the occupant of a zone
 * @param kind the kind of the occupant (defined by the inner enum Kind)
 * @param zoneId the id of the zone the occupant occupies
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record Occupant(Kind kind, int zoneId) {
    /**
     * enum representing the 2 kinds of occupant
     */
    public enum Kind {
        PAWN,
        HUT;
    }

    /**
     * compact constructor that throws an IllegalArgumentException if the zoneId is null or < 0
     * @param kind the kind of the occupant
     * @param zoneId the id of the zone the occupant occupies
     */
    public Occupant {
        Objects.requireNonNull(kind);
        Preconditions.checkArgument(zoneId >= 0);
    }

    /**
     * count the number total of occupant
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
