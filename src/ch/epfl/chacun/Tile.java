package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * record representing a tile
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
     * enum representing the three kinds of tiles
     */
    public enum Kind{
        START,
        NORMAL,
        MENHIR;
    }

    /**
     * gives a list of the tile sides of the tile
     * @return the tile sides of the tile (List)
     */
    public List<TileSide> sides(){
        return List.of(n, e, s, w);
    }

    /**
     * gives the zones that are in contact with at least one side of the tile
     * @return the zones that are in contact with at least one side of the tile (Set)
     */
    public Set<Zone> sideZones(){
        Set<Zone> zones = new HashSet<>();
        for (TileSide tileSide: sides()) {
            zones.addAll(tileSide.zones());
        }
        return zones;
    }

    /**
     * gives all the zones on the tile
     * @return all the zones on the tile (Set)
     */
    public Set<Zone> zones(){
        Set<Zone> zones = new HashSet<>();
        for (Zone zone: sideZones()) {
            zones.add(zone);
            if (zone instanceof Zone.River river && river.hasLake()) {
                zones.add(river.lake());
            }
        }
        return zones;
    }
}
