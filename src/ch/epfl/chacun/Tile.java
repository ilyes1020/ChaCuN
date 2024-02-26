package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record Tile(int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {
    public enum Kind{
        START,
        NORMAL,
        MENHIR;
    }
    public List<TileSide> sides(){
        return List.of(n, e, s, w);
    }
    public Set<Zone> sideZones(){
        Set<Zone> zones = new HashSet<>();
        for (TileSide tileSide: sides()) {
            zones.addAll(tileSide.zones());
        }
        return zones;
    }
    public Set<Zone> zones(){
        Set<Zone> zones = new HashSet<>();
        for (Zone zone: sideZones()) {
            if (zone instanceof Zone.River river && river.hasLake()) {
                zones.add(river);
                zones.add(river.lake());
            } else {
                zones.add(zone);
            }
        }
        return zones;
    }
}
