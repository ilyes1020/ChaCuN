package ch.epfl.chacun;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the board in ChaCuN
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public class Board {
    private final PlacedTile[] placedTiles;
    private final int[] tilesIndex;
    private final ZonePartitions zonePartitions;
    private final Set<Animal> cancelledAnimals;

    public static final int REACH = 12;
    public static final Board EMPTY = new Board(new int[0], ZonePartitions.EMPTY, new HashSet<>());

    public Board(int[] tilesIndex, ZonePartitions zonePartitions, Set<Animal> cancelledAnimals) {
        this.placedTiles = new PlacedTile[625];
        this.tilesIndex = tilesIndex;
        this.zonePartitions = zonePartitions;
        this.cancelledAnimals = cancelledAnimals;
    }

    /**
     * Returns the tile at the given position, or null if there is none or if the position is out of the board.
     *
     * @param pos The position to check for the tile.
     * @return The tile at the given position, or null if no tile is found or the position is invalid.
     */
    public PlacedTile tileAt(Pos pos) {
        // To be implemented
        return null;
    }

    /**
     * Returns the tile with the given ID, or throws IllegalArgumentException if the tile is not on the board.
     *
     * @param tileId The ID of the tile to search for.
     * @return The tile with the given ID.
     * @throws IllegalArgumentException if the tile is not found on the board.
     */
    public PlacedTile tileWithId(int tileId) {
        // To be implemented
        return null;
    }

    /**
     * Returns the set of cancelled animals.
     *
     * @return The set of cancelled animals.
     */
    public Set<Animal> cancelledAnimals() {
        // To be implemented
        return null;
    }

    /**
     * Returns the set of all occupants on the board.
     *
     * @return The set of all occupants on the board.
     */
    public Set<Occupant> occupants() {
        // To be implemented
        return null;
    }

    /**
     * Returns the forest area containing the given zone, or throws IllegalArgumentException if the zone does not belong to the board.
     *
     * @param forest The forest zone to search for.
     * @return The forest area containing the given zone.
     * @throws IllegalArgumentException if the zone does not belong to the board.
     */
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        // To be implemented
        return null;
    }

    /**
     * Returns the meadow area containing the given zone, or throws IllegalArgumentException if the zone does not belong to the board.
     *
     * @param meadow The meadow zone to search for.
     * @return The meadow area containing the given zone.
     * @throws IllegalArgumentException if the zone does not belong to the board.
     */
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        // To be implemented
        return null;
    }

    /**
     * Returns the river area containing the given zone, or throws IllegalArgumentException if the zone does not belong to the board.
     *
     * @param riverZone The river zone to search for.
     * @return The river area containing the given zone.
     * @throws IllegalArgumentException if the zone does not belong to the board.
     */
    public Area<Zone.River> riverArea(Zone.River riverZone) {
        // To be implemented
        return null;
    }

    /**
     * Returns the water area containing the given zone, or throws IllegalArgumentException if the zone does not belong to the board.
     *
     * @param water The water zone to search for.
     * @return The water area containing the given zone.
     * @throws IllegalArgumentException if the zone does not belong to the board.
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        // To be implemented
        return null;
    }

    /**
     * Returns the set of all meadow areas on the board.
     *
     * @return The set of all meadow areas on the board.
     */
    public Set<Area<Zone.Meadow>> meadowAreas() {
        // To be implemented
        return null;
    }

    /**
     * Returns the set of all water areas on the board.
     *
     * @return The set of all water areas on the board.
     */
    public Set<Area<Zone.Water>> riverSystemAreas() {
        // To be implemented
        return null;
    }

    /**
     * Returns the adjacent meadow to the given zone. The returned area contains all the occupants of the initial area.
     *
     * @param pos         The position to check for adjacency.
     * @param meadowZone  The meadow zone to search for.
     * @return The adjacent meadow to the given zone, including all occupants of the initial meadow.
     */
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
        // To be implemented
        return null;
    }

    /**
     * Returns the number of occupants of the specified kind belonging to the given player on the board.
     *
     * @param player        The player whose occupants to count.
     * @param occupantKind  The kind of occupant to count.
     * @return The number of occupants of the specified kind belonging to the given player on the board.
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        // To be implemented
        return 0;
    }

    /**
     * Returns the set of insertion positions on the board.
     *
     * @return The set of insertion positions on the board.
     */
    public Set<Pos> insertionPositions() {
        // To be implemented
        return null;
    }

    /**
     * Returns the last placed tile on the board, or null if the board is empty.
     *
     * @return The last placed tile on the board, or null if the board is empty.
     */
    public PlacedTile lastPlacedTile() {
        // To be implemented
        return null;
    }

    /**
     * Returns the set of forest areas closed by the last placed tile.
     *
     * @return The set of forest areas closed by the last placed tile.
     */
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        // To be implemented
        return null;
    }

    /**
     * Returns the set of river areas closed by the last placed tile.
     *
     * @return The set of river areas closed by the last placed tile.
     */
    public Set<Area<Zone.River>> riversClosedByLastTile() {
        // To be implemented
        return null;
    }

    /**
     * Checks if the given tile can be added to the board.
     *
     * @param tile The tile to be checked for addition.
     * @return True if the tile can be added, false otherwise.
     */
    public boolean canAddTile(PlacedTile tile) {
        // To be implemented
        return false;
    }

    /**
     * Checks if the given tile can be placed on the board.
     *
     * @param tile The tile to be checked for placement.
     * @return True if the tile can be placed, false otherwise.
     */
    public boolean couldPlaceTile(Tile tile) {
        // To be implemented
        return false;
    }

    /**
     * Returns a new board with the given tile added to it.
     *
     * @param tile The tile to be added to the board.
     * @return A new board with the given tile added.
     * @throws IllegalArgumentException if the tile cannot be added to the board.
     */
    public Board withNewTile(PlacedTile tile) {
        // To be implemented
        return null;
    }

    /**
     * Returns a new board with the given occupant added to it.
     *
     * @param occupant The occupant to be added to the board.
     * @return A new board with the given occupant added.
     * @throws IllegalArgumentException if the tile on which the occupant would be placed is already occupied.
     */
    public Board withOccupant(Occupant occupant) {
        // To be implemented
        return null;
    }

    /**
     * Returns a new board with the given occupant removed from it.
     *
     * @param occupant The occupant to be removed from the board.
     * @return A new board with the given occupant removed.
     */
    public Board withoutOccupant(Occupant occupant) {
        // To be implemented
        return null;
    }

    /**
     * Returns a new board with no gatherers or fishers in the specified forests and rivers.
     *
     * @param forests The set of forest areas.
     * @param rivers  The set of river areas.
     * @return A new board with no gatherers or fishers in the specified forests and rivers.
     */
    public Board withoutGatherersOrFishersIn(Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers) {
        // To be implemented
        return null;
    }

    /**
     * Returns a new board with the given animals added to the set of cancelled animals.
     *
     * @param newlyCancelledAnimals The set of animals to be added to the cancelled animals.
     * @return A new board with the given animals added to the cancelled animals.
     */
    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        // To be implemented
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board)) return false;
        return Arrays.equals(placedTiles, board.placedTiles) && Arrays.equals(tilesIndex, board.tilesIndex) && zonePartitions.equals(board.zonePartitions) && cancelledAnimals.equals(board.cancelledAnimals);
    }

    @Override
    public int hashCode() {
        int placedTilesHashCode = Arrays.hashCode(placedTiles);
        int tileIndexHashCode = Arrays.hashCode(tilesIndex);
        return Objects.hash(placedTilesHashCode, tileIndexHashCode, zonePartitions, cancelledAnimals);
    }
}

