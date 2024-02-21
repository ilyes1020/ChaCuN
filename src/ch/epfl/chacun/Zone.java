package ch.epfl.chacun;

/**
 * interface representing the zones in game
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public interface Zone {

    /**
     * enum representing the special powers of the zones
     */
    enum SpecialPower {
        SHAMAN,
        LOGBOAT,
        HUNTING_TRAP,
        PIT_TRAP,
        WILD_FIRE,
        RAFT;
    }

    /**
     * gives the id of his tile
     * @param zoneId the id of the zone
     * @return the id of the tile (int)
     */
    static int tileId(int zoneId) {
        return zoneId / 10;
    }

    /**
     * gives the local id of the zone in his tile
     * @param zoneId the id of the zone
     * @return the local id of the zone (int)
     */
    static int localId(int zoneId) {
        return zoneId % 10;
    }

    /**
     * abstract method that gives the id of the zone
     * @return the id of the zone (int)
     */
    abstract int id();

    /**
     * default method that gives the id of the tile
     * @return the id of the tile (int)
     */
    default int tileId(){
        return id() / 10;
    }
    /**
     * default method that gives the local id of the zone
     * @return the local id of the zone (int)
     */
    default int localId(){
        return id() % 10;
    }
    /**
     * default method that gives the special power of the zone
     * @return the special power of the zone (SpecialPower)
     */
    default SpecialPower specialPower(){
        return null;
    }
}
