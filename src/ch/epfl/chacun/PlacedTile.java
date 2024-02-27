package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * represents a placed tile
 * @param tile the placed tile
 * @param placer the placer of the tile
 * @param rotation the rotation of the tile
 * @param pos the position on which the tile is placed
 * @param occupant the occupant of the tile
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos, Occupant occupant) {

    /**
     * compact constructor that throws a NullPointerException if tile or rotation or position is null
     * @param tile the placed tile
     * @param placer the placer of the tile
     * @param rotation the rotation of the tile
     * @param pos the position on which the tile is placed
     * @param occupant the occupant of the tile
     */
    public PlacedTile{
        Objects.requireNonNull(tile);
        Objects.requireNonNull(rotation);
        Objects.requireNonNull(pos);
    }

    /**
     * constructor to create a placed tile without occupant
     * @param tile the placed tile
     * @param placer the placer of the tile
     * @param rotation the rotation of the tile
     * @param pos the position on which the tile is placed
     */
    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos){
        this(tile, placer, rotation, pos, null);
    }

    /**
     * gives the id of the placed tile
     * @return the id of the placed tile
     */
    public int id(){
        return tile.id();
    }

    /**
     * gives the kind of the placed tile
     * @return the kind of the placed tile
     */
    public Tile.Kind kind(){
        return tile.kind();
    }

    /**
     * gives the tile side of the given direction
     * @param direction the direction of the side
     * @return the tile side in the given direction
     */
    public TileSide side(Direction direction){
        int newDirectionIndex = (direction.ordinal() - rotation.ordinal() + tile.sides().size()) % tile.sides().size();
        return tile.sides().get(newDirectionIndex);
    }

    /**
     * gives the zone with the given ID
     * @param id the id of the zone
     * @return the zone with the given ID
     */
    public Zone zoneWithId(int id){
        for (Zone zone : tile.zones()){
            if (zone.id() == id) {
                return zone;
            }
        }
        throw new IllegalArgumentException("No zone with such ID in this tile.");
    }

    /**
     * gives the zone that has the given special power
     * @return the zone that has the given special power
     */
    public Zone specialPowerZone(){
        for (Zone zone : tile.zones()){
            if (zone.specialPower() != null){
                return zone;
            }
        }
        return null;
    }

    /**
     * gives a set of all forest zones in the tile
     * @return a set of all forest zones in the tile
     */
    public Set<Zone.Forest> forestZones(){
        Set<Zone.Forest> forestZones = new HashSet<>();
        for (Zone zone : tile.zones()){
            if (zone instanceof Zone.Forest forest){
                forestZones.add(forest);
            }
        }
        return forestZones;
    }

    /**
     * gives a set of all meadow zones in the tile
     * @return a set of all meadow zones in the tile
     */
    public Set<Zone.Meadow> meadowZones(){
        Set<Zone.Meadow> meadowZones = new HashSet<>();
        for (Zone zone : tile.zones()){
            if (zone instanceof Zone.Meadow meadow){
                meadowZones.add(meadow);
            }
        }
        return meadowZones;
    }

    /**
     * gives a set of all river zones in the tile
     * @return a set of all river zones in the tile
     */
    public Set<Zone.River> riverZones(){
        Set<Zone.River> riverZones = new HashSet<>();
        for (Zone zone : tile.zones()){
            if (zone instanceof Zone.River river){
                riverZones.add(river);
            }
        }
        return riverZones;
    }

    /**
     * gives the set of all potential occupants in the tile
     * @return the set of all potential occupants in the tile
     */
    public Set<Occupant> potentialOccupants(){
        Set<Occupant> potentialOccupants = new HashSet<>();
        if (placer == null){
            return potentialOccupants;
        } else {
            for (Zone sideZone : tile().sideZones()){
                potentialOccupants.add(new Occupant(Occupant.Kind.PAWN, sideZone.id()));
            }
            for (Zone.River riverZone : riverZones()){
                if (!riverZone.hasLake()) {
                    potentialOccupants.add(new Occupant(Occupant.Kind.HUT, riverZone.id()));
                }
            }
            for (Zone zone : tile.zones()){
                if (zone instanceof Zone.Lake lake){
                    potentialOccupants.add(new Occupant(Occupant.Kind.HUT, lake.id()));
                }
            }
        }
        return potentialOccupants;
    }

    /**
     * gives the same placed tile, but with an occupant
     * @param occupant the occupant of the tile
     * @return the same placed tile, but with an occupant
     */
    public PlacedTile withOccupant(Occupant occupant){
        return new PlacedTile(tile, placer, rotation, pos, occupant);
    }

    /**
     * gives the same placed tile, but without occupant
     * @return the same placed tile, but without occupant
     */
    public PlacedTile withNoOccupant(){
        return new PlacedTile(tile, placer, rotation, pos);
    }

    /**
     * gives the ID of the occupied zone if the occupant is not null and its kind matches with the given kind,
     * otherwise it returns -1
     * @param occupantKind the kind of the occupant of the tile
     * @return the ID of the occupied zone, or -1
     *
     */
    public int idOfZoneOccupiedBy(Occupant.Kind occupantKind){
        if (occupant == null || !occupantKind.equals(occupant.kind())){
            return -1;
        } else {
            return occupant.zoneId();
        }
    }
}
