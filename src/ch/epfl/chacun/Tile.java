package ch.epfl.chacun;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Record representing a tile.
 *
 * @param id the id of the tile
 * @param kind the kind of the tile
 * @param n the north side of the tile
 * @param e the eastern side of the tile
 * @param s the south side of the tile
 * @param w the western side of the tile
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record Tile(int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {

    /**
     * Enum representing the three kinds of tiles.
     */
    public enum Kind{

        /**
         * Represents the starting tile.
         */
        START,

        /**
         * Represents a normal tile.
         */
        NORMAL,

        /**
         * Represents a tile with a menhir.
         */
        MENHIR;
    }

    /**
     * Gives a list of the tile sides of the tile.
     *
     * @return the tile sides of the tile (List)
     */
    public List<TileSide> sides(){
        return List.of(n, e, s, w);
    }

    /**
     * Gives the zones that are in contact with at least one side of the tile (excluding lakes).
     *
     * @return the zones that are in contact with at least one side of the tile (Set)
     */
    public Set<Zone> sideZones(){
        return sides().stream()
                .flatMap(tileSide -> tileSide.zones().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Gives all the zones on the tile.
     *
     * @return all the zones on the tile (Set)
     */
    public Set<Zone> zones(){
        return sideZones().stream()
                .flatMap(zone -> zone instanceof Zone.River river && river.hasLake() ?
                        Stream.of(zone, river.lake()) : Stream.of(zone))
                .collect(Collectors.toSet());
    }
}
