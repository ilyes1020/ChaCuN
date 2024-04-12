package ch.epfl.chacun;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a placed tile.
 *
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
     * Compact constructor that throws a NullPointerException if tile or rotation or position is null.
     *
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
     * Constructor to create a placed tile without occupant.
     *
     * @param tile the placed tile
     * @param placer the placer of the tile
     * @param rotation the rotation of the tile
     * @param pos the position on which the tile is placed
     */
    public PlacedTile(Tile tile, PlayerColor placer, Rotation rotation, Pos pos){
        this(tile, placer, rotation, pos, null);
    }

    /**
     * Gives the id of the placed tile.
     *
     * @return the id of the placed tile
     */
    public int id(){
        return tile.id();
    }

    /**
     * Gives the kind of the placed tile.
     *
     * @return the kind of the placed tile
     */
    public Tile.Kind kind(){
        return tile.kind();
    }

    /**
     * Gives the tile side of the given direction.
     *
     * @param direction the direction of the side
     * @return the tile side in the given direction
     */
    public TileSide side(Direction direction){
        int newDirectionIndex = (direction.ordinal() - rotation.ordinal() + tile.sides().size()) % tile.sides().size();
        return tile.sides().get(newDirectionIndex);
    }

    /**
     * Gives the zone with the given ID.
     *
     * @param id the id of the zone
     * @return the zone with the given ID
     */
    public Zone zoneWithId(int id){
        return tile.zones().stream()
                .filter(zone -> zone.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No zone with such ID in this tile."));
    }

    /**
     * Gives the zone that has the given special power.
     *
     * @return the zone that has the given special power
     */
    public Zone specialPowerZone(){
        return tile.zones().stream()
                .filter(zone -> zone.specialPower() != null)
                .findFirst()
                .orElse(null);
    }

    /**
     * Gives a set of all forest zones in the tile.
     *
     * @return a set of all forest zones in the tile
     */
    public Set<Zone.Forest> forestZones(){
        return zonesOfType(Zone.Forest.class);
    }

    /**
     * Gives a set of all meadow zones in the tile.
     *
     * @return a set of all meadow zones in the tile
     */
    public Set<Zone.Meadow> meadowZones(){
        return zonesOfType(Zone.Meadow.class);
    }

    /**
     * Gives a set of all river zones in the tile.
     *
     * @return a set of all river zones in the tile
     */
    public Set<Zone.River> riverZones(){
        return zonesOfType(Zone.River.class);
    }

    /**
     * Gives the set of all potential occupants in the tile.
     *
     * @return the set of all potential occupants in the tile
     */
    public Set<Occupant> potentialOccupants(){
        Set<Occupant> potentialOccupants = new HashSet<>();
        if (placer != null){
            potentialOccupants.addAll(potentialOccupantsOfType(Occupant.Kind.PAWN, tile().sideZones()));
            potentialOccupants.addAll(potentialOccupantsOfType(Occupant.Kind.HUT, riverZones().stream().filter(river -> !river.hasLake()).collect(Collectors.toList())));
            potentialOccupants.addAll(potentialOccupantsOfType(Occupant.Kind.HUT, zonesOfType(Zone.Lake.class)));
        }
        return potentialOccupants;
    }

    /**
     * Gives the same placed tile, but with an occupant.
     *
     * @param occupant the occupant of the tile
     * @return the same placed tile, but with an occupant
     */
    public PlacedTile withOccupant(Occupant occupant){
        if (this.occupant == null){
            return new PlacedTile(tile, placer, rotation, pos, occupant);
        }
        throw new IllegalArgumentException("The tile is already occupied");
    }

    /**
     * Gives the same placed tile, but without occupant.
     *
     * @return the same placed tile, but without occupant
     */
    public PlacedTile withNoOccupant(){
        return new PlacedTile(tile, placer, rotation, pos);
    }

    /**
     * Gives the ID of the occupied zone if the occupant is not null and its kind matches with the given kind,
     * otherwise it returns -1.
     *
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

    /**
     * This method returns a set of all zones in the tile of a specific type.
     *
     * @param zoneType The class of the zone type to filter by.
     * @return A set of all zones in the tile of the specified type.
     */
    private <T extends Zone> Set<T> zonesOfType(Class<T> zoneType){
        return tile.zones().stream()
                .filter(zoneType::isInstance)
                .map(zoneType::cast)
                .collect(Collectors.toSet());
    }

    /**
     * This method returns a set of potential occupants of a specific kind for a given iterable of zones.
     *
     * @param occupantKind The kind of the occupant to create.
     * @param zones The iterable of zones to create occupants for.
     * @return A set of potential occupants of the specified kind for the given zones.
     */
    private Set<Occupant> potentialOccupantsOfType(Occupant.Kind occupantKind, Iterable<? extends Zone> zones){
        Set<Occupant> occupants = new HashSet<>();
        for (Zone zone : zones){
            occupants.add(new Occupant(occupantKind, zone.id()));
        }
        return occupants;
    }
}
