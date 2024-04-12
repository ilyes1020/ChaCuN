package ch.epfl.chacun;

import java.util.List;

/**
 * Interface representing the zones in game.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public sealed interface Zone {

    /**
     * Enum representing the special powers of the zones.
     */
    enum SpecialPower {

        /**
         * Represents the shaman special power.
         */
        SHAMAN,

        /**
         * Represents the logboat special power.
         */
        LOGBOAT,

        /**
         * Represents the hunting trap special power.
         */
        HUNTING_TRAP,

        /**
         * Represents the pit trap special power.
         */
        PIT_TRAP,

        /**
         * Represents the wildfire special power.
         */
        WILD_FIRE,

        /**
         * Represents the raft special power.
         */
        RAFT;
    }
    /**
     * Gives the id of his tile.
     *
     * @param zoneId the id of the zone
     * @return the id of the tile (int)
     */
    static int tileId(int zoneId) {
        return zoneId / 10;
    }

    /**
     * Gives the local id of the zone in his tile.
     *
     * @param zoneId the id of the zone
     * @return the local id of the zone (int)
     */
    static int localId(int zoneId) {
        return zoneId % 10;
    }

    /**
     * Abstract method that gives the id of the zone.
     *
     * @return the id of the zone (int)
     */
    int id();

    /**
     * Default method that gives the id of the tile.
     *
     * @return the id of the tile (int)
     */
    default int tileId(){
        return id() / 10;
    }
    /**
     * Default method that gives the local id of the zone.
     *
     * @return the local id of the zone (int)
     */
    default int localId(){
        return id() % 10;
    }
    /**
     * Default method that gives the special power of the zone.
     *
     * @return the special power of the zone (SpecialPower)
     */
    default SpecialPower specialPower(){
        return null;
    }

    /**
     * Record representing a forest.
     *
     * @param id the id of the forest
     * @param kind the kind of the forest
     */
    record Forest(int id, Kind kind) implements Zone{

        public enum Kind{
            PLAIN, //empty forest
            WITH_MENHIR, //forest with at least one menhir
            WITH_MUSHROOMS; //forest with at least one group of mushrooms
        }
    }

    /**
     * Record representing a meadow.
     *
     * @param id the id of the meadow
     * @param animals list of animals in the meadow
     * @param specialPower the special power of the meadow
     */
    record Meadow(int id, List<Animal> animals, SpecialPower specialPower) implements Zone{
        public Meadow{
            animals = List.copyOf(animals);
        }
    }

    /**
     * Interface representing aquatic areas.
     */
    sealed interface Water extends Zone{
        /**
         * To be overridden by water zones
         * @return The number of fishes swimming in the area
         */
        int fishCount();
    }

    /**
     * Record representing a lake.
     *
     * @param id the id of the lake
     * @param fishCount the number of fish in the lake
     * @param specialPower the special power of the lake
     */
    record Lake (int id, int fishCount, SpecialPower specialPower) implements Water{}

    /**
     * Record representing a river.
     *
     * @param id the id of the river
     * @param fishCount the number of fish in the river
     * @param lake the lake which is linked to the river
     */
    record River (int id, int fishCount, Lake lake) implements Water{
        public boolean hasLake(){
            return lake != null;
        }
    }
}
