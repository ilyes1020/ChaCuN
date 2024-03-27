package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the board in ChaCuN
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public final class Board {
    private final PlacedTile[] placedTiles;
    private final int[] tilesIndex;
    private final ZonePartitions zonePartitions;
    private final Set<Animal> cancelledAnimals;

    public static final int REACH = 12;
    public static final Board EMPTY = new Board(new PlacedTile[625], new int[0], ZonePartitions.EMPTY, new HashSet<>());

    public Board(PlacedTile[] placedTiles, int[] tilesIndex, ZonePartitions zonePartitions, Set<Animal> cancelledAnimals) {
        this.placedTiles = placedTiles;
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
        if (pos.x() < -REACH ||
                pos.y() < -REACH ||
                pos.x() > REACH ||
                pos.y() > REACH){
            return null;
        }
        int BOARD_SIZE = 25;
        int tileIndex = (pos.y() + REACH) * BOARD_SIZE +
                (pos.x() + REACH);
        return placedTiles[tileIndex];
    }

    /**
     * Returns the tile with the given ID, or throws IllegalArgumentException if the tile is not on the board.
     *
     * @param tileId The ID of the tile to search for.
     * @return The tile with the given ID.
     * @throws IllegalArgumentException if the tile is not found on the board.
     */
    public PlacedTile tileWithId(int tileId) {
        for (int placedTileIndex : tilesIndex) {
            if (placedTiles[placedTileIndex].tile().id() == tileId)
                return placedTiles[placedTileIndex];
        }
        throw new IllegalArgumentException("Tile with such ID is not on the board");
    }

    /**
     * Returns the set of cancelled animals.
     *
     * @return The set of cancelled animals.
     */
    public Set<Animal> cancelledAnimals() {
        return Set.copyOf(cancelledAnimals);
    }

    /**
     * Returns the set of all occupants on the board.
     *
     * @return The set of all occupants on the board.
     */
    public Set<Occupant> occupants() {
        Set<Occupant> totalOccupants = new HashSet<>();
        for (int placedTileIndex : tilesIndex) {
            if (placedTiles[placedTileIndex].occupant() != null) {
                totalOccupants.add(placedTiles[placedTileIndex].occupant());
            }
        }
        return Set.copyOf(totalOccupants);
    }

    /**
     * Returns the forest area containing the given zone, or throws IllegalArgumentException if the zone does not belong to the board.
     *
     * @param forest The forest zone to search for.
     * @return The forest area containing the given zone.
     * @throws IllegalArgumentException if the zone does not belong to the board.
     */
    public Area<Zone.Forest> forestArea(Zone.Forest forest) {
        return zonePartitions.forests().areaContaining(forest);
    }

    /**
     * Returns the meadow area containing the given zone, or throws IllegalArgumentException if the zone does not belong to the board.
     *
     * @param meadow The meadow zone to search for.
     * @return The meadow area containing the given zone.
     * @throws IllegalArgumentException if the zone does not belong to the board.
     */
    public Area<Zone.Meadow> meadowArea(Zone.Meadow meadow) {
        return zonePartitions.meadows().areaContaining(meadow);
    }

    /**
     * Returns the river area containing the given zone, or throws IllegalArgumentException if the zone does not belong to the board.
     *
     * @param riverZone The river zone to search for.
     * @return The river area containing the given zone.
     * @throws IllegalArgumentException if the zone does not belong to the board.
     */
    public Area<Zone.River> riverArea(Zone.River riverZone) {
        return zonePartitions.rivers().areaContaining(riverZone);
    }

    /**
     * Returns the water area containing the given zone, or throws IllegalArgumentException if the zone does not belong to the board.
     *
     * @param water The water zone to search for.
     * @return The water area containing the given zone.
     * @throws IllegalArgumentException if the zone does not belong to the board.
     */
    public Area<Zone.Water> riverSystemArea(Zone.Water water) {
        return zonePartitions.riverSystems().areaContaining(water);
    }

    /**
     * Returns the set of all meadow areas on the board.
     *
     * @return The set of all meadow areas on the board.
     */
    public Set<Area<Zone.Meadow>> meadowAreas() {
        return zonePartitions.meadows().areas();
    }

    /**
     * Returns the set of all water areas on the board.
     *
     * @return The set of all water areas on the board.
     */
    public Set<Area<Zone.Water>> riverSystemAreas() {
        return zonePartitions.riverSystems().areas();
    }

    /**
     * Returns the adjacent meadow to the given zone. The returned area contains all the occupants of the initial area.
     *
     * @param pos         The position to check for adjacency.
     * @param meadowZone  The meadow zone to search for.
     * @return The adjacent meadow to the given zone, including all occupants of the initial meadow.
     */
    public Area<Zone.Meadow> adjacentMeadow(Pos pos, Zone.Meadow meadowZone) {
        Area<Zone.Meadow> initialMeadowArea = zonePartitions.meadows().areaContaining(meadowZone);
        Set<Zone.Meadow> adjacentMeadowZones = initialMeadowArea
                .zones()
                .stream()
                .filter(z ->{
                    Pos zTilePosition = tileWithId(z.tileId()).pos();
                    return (Math.abs(zTilePosition.x() - pos.x()) <= 1) && (Math.abs(zTilePosition.y() - pos.y()) <= 1);
                })
                .collect(Collectors.toSet());
        return new Area<>(adjacentMeadowZones, initialMeadowArea.occupants(), 0);
    }


    /**
     * Returns the number of occupants of the specified kind belonging to the given player on the board.
     *
     * @param player        The player whose occupants to count.
     * @param occupantKind  The kind of occupant to count.
     * @return The number of occupants of the specified kind belonging to the given player on the board.
     */
    public int occupantCount(PlayerColor player, Occupant.Kind occupantKind) {
        int occupantCount = 0;
        for (int placedTileIndex : tilesIndex) {
            if ((placedTiles[placedTileIndex].placer() == player) &&
                    (placedTiles[placedTileIndex].occupant() != null) &&
                    (placedTiles[placedTileIndex].occupant().kind() == occupantKind)){
                occupantCount++;
            }
        }
        return occupantCount;
    }

    /**
     * Returns the set of insertion positions on the board.
     *
     * @return The set of insertion positions on the board.
     */
    public Set<Pos> insertionPositions() {
        Set<Pos> insertionPositions = new HashSet<>();
        for (int placedTileIndex : tilesIndex) {
            for (Direction direction : Direction.values()){

                Pos placedTileNeighborPos = placedTiles[placedTileIndex].pos().neighbor(direction);

                if (placedTileNeighborPos.x() >= -REACH &&
                        placedTileNeighborPos.y() >= -REACH &&
                        placedTileNeighborPos.x() <= REACH &&
                        placedTileNeighborPos.y() <= REACH) {
                    if (tileAt(placedTileNeighborPos) == null) {
                        insertionPositions.add(placedTileNeighborPos);
                    }
                }
            }
        }
        return insertionPositions;
    }

    /**
     * Returns the last placed tile on the board, or null if the board is empty.
     *
     * @return The last placed tile on the board, or null if the board is empty.
     */
    public PlacedTile lastPlacedTile() {
        if (tilesIndex.length == 0){
            return null;
        }
        return placedTiles[tilesIndex[tilesIndex.length - 1]];
    }

    /**
     * Returns the set of forest areas closed by the last placed tile.
     *
     * @return The set of forest areas closed by the last placed tile.
     */
    public Set<Area<Zone.Forest>> forestsClosedByLastTile() {
        if (lastPlacedTile() == null){
            return Set.of();
        }
        PlacedTile lastPlacedTile = lastPlacedTile();
        Set<Area<Zone.Forest>> closedForestAreas = new HashSet<>();

        for (Direction direction : Direction.values()) {
            if (lastPlacedTile.side(direction) instanceof TileSide.Forest lastTileForestSide){
                if (forestArea(lastTileForestSide.forest()).isClosed()){
                    closedForestAreas.add(forestArea(lastTileForestSide.forest()));
                }
            }
        }
        return Set.copyOf(closedForestAreas);
    }

    /**
     * Returns the set of river areas closed by the last placed tile.
     *
     * @return The set of river areas closed by the last placed tile.
     */
    public Set<Area<Zone.River>> riversClosedByLastTile() {
        if (lastPlacedTile() == null){
            return Set.of();
        }
        PlacedTile lastPlacedTile = lastPlacedTile();
        Set<Area<Zone.River>> closedRiverAreas = new HashSet<>();

        for (Direction direction : Direction.values()) {
            if (lastPlacedTile.side(direction) instanceof TileSide.River lastTileRiverSide){
                if (riverArea(lastTileRiverSide.river()).isClosed()){
                    closedRiverAreas.add(riverArea(lastTileRiverSide.river()));
                }
            }
        }
        return Set.copyOf(closedRiverAreas);
    }

    /**
     * Checks if the given tile can be added to the board.
     *
     * @param tile The tile to be checked for addition.
     * @return True if the tile can be added, false otherwise.
     */
    public boolean canAddTile(PlacedTile tile) {
        if (!insertionPositions().contains(tile.pos())){
            return false;
        }
        for (Direction direction : Direction.values()) {
            PlacedTile neighborTile = tileAt(tile.pos().neighbor(direction));
            if (neighborTile != null && !tile.side(direction).isSameKindAs(neighborTile.side(direction.opposite()))){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given tile can be placed on the board.
     *
     * @param tile The tile to be checked for placement.
     * @return True if the tile can be placed, false otherwise.
     */
    public boolean couldPlaceTile(Tile tile) {
        for (Pos insertionPosition : insertionPositions()) {
            for (Rotation rotation : Rotation.values()) {
                PlacedTile placedTileRotated = new PlacedTile(tile, null, rotation, insertionPosition);
                if (canAddTile(placedTileRotated)){
                    return true;
                }
            }
        }
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
        Preconditions.checkArgument(tilesIndex.length == 0 || canAddTile(tile));

        int BOARD_SIZE = 25;
        int newTileIndex = (tile.pos().y() + REACH) * BOARD_SIZE +
                (tile.pos().x() + REACH);

        //updating the tileIndex table
        int[] updatedTilesIndex = Arrays.copyOf(tilesIndex, tilesIndex.length + 1);
        updatedTilesIndex[updatedTilesIndex.length - 1] = newTileIndex;

        //updating the placedTiles table
        PlacedTile[] updatedPlacedTiles = placedTiles.clone();
        updatedPlacedTiles[newTileIndex] = tile;

        //updating the zonePartitions
        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(zonePartitions);
        zonePartitionsBuilder.addTile(tile.tile());

        //connecting the new tiles side's with the placed neighbors
        for (Direction direction : Direction.values()) {
            PlacedTile neighborTile = tileAt(tile.pos().neighbor(direction));
            if (neighborTile != null){
                zonePartitionsBuilder.connectSides(tile.side(direction), neighborTile.side(direction.opposite()));
            }
        }

        ZonePartitions updatedZonePartitions = zonePartitionsBuilder.build();

        return new Board(updatedPlacedTiles, updatedTilesIndex, updatedZonePartitions, cancelledAnimals);
    }

    /**
     * Returns a new board with the given occupant added to it.
     *
     * @param occupant The occupant to be added to the board.
     * @return A new board with the given occupant added.
     * @throws IllegalArgumentException if the tile on which the occupant would be placed is already occupied or if the tile on which the occupant stands is not on the board.
     */
    public Board withOccupant(Occupant occupant) {
        PlacedTile occupantsTile = tileWithId(Zone.tileId(occupant.zoneId()));
        //withOccupant throws the exception if the tile is already occupied
        PlacedTile occupantsTileWithOccupant = occupantsTile.withOccupant(occupant);

        //updating the placedTiles table
        int BOARD_SIZE = 25;
        int occupantsTileIndex = (occupantsTile.pos().y() + REACH) * BOARD_SIZE +
                (occupantsTile.pos().x() + REACH);
        PlacedTile[] updatedPlacedTiles = placedTiles.clone();

        updatedPlacedTiles[occupantsTileIndex] = occupantsTileWithOccupant;

        //updating the zonePartitions
        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(zonePartitions);
        Zone occupantsZone = occupantsTile.zoneWithId(occupant.zoneId());

        zonePartitionsBuilder.addInitialOccupant(occupantsTile.placer(), occupant.kind(), occupantsZone);
        ZonePartitions updatedZonePartitions = zonePartitionsBuilder.build();

        return new Board(updatedPlacedTiles, tilesIndex, updatedZonePartitions, cancelledAnimals);
    }

    /**
     * Returns a new board with the given occupant removed from it.
     *
     * @param occupant The occupant to be removed from the board.
     * @return A new board with the given occupant removed.
     * @throws IllegalArgumentException if the tile on which the occupant stands is not on the board.
     */
    public Board withoutOccupant(Occupant occupant) {
        PlacedTile occupantsTile = tileWithId(Zone.tileId(occupant.zoneId()));
        PlacedTile occupantsTileWithoutOccupant = occupantsTile.withNoOccupant();

        //updating the placedTiles table
        int BOARD_SIZE = 25;
        int occupantsTileIndex = (occupantsTile.pos().y() + REACH) * BOARD_SIZE +
                (occupantsTile.pos().x() + REACH);
        PlacedTile[] updatedPlacedTiles = placedTiles.clone();

        updatedPlacedTiles[occupantsTileIndex] = occupantsTileWithoutOccupant;

        //updating the zonePartitions
        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(zonePartitions);
        Zone occupantsZone = occupantsTile.zoneWithId(occupant.zoneId());

        zonePartitionsBuilder.removePawn(occupantsTile.placer(), occupantsZone);
        ZonePartitions updatedZonePartitions = zonePartitionsBuilder.build();

        return new Board(updatedPlacedTiles, tilesIndex, updatedZonePartitions, cancelledAnimals);
    }

    /**
     * Returns a new board with no gatherers or fishers in the specified forests and rivers.
     *
     * @param forests The set of forest areas.
     * @param rivers  The set of river areas.
     * @return A new board with no gatherers or fishers in the specified forests and rivers.
     */
    public Board withoutGatherersOrFishersIn(Set<Area<Zone.Forest>> forests, Set<Area<Zone.River>> rivers) {
        PlacedTile[] updatedPlacedTiles = placedTiles.clone();
        ZonePartitions.Builder zonePartitionsBuilder = new ZonePartitions.Builder(zonePartitions);

        for (Area<Zone.Forest> forestArea : forests) {
            zonePartitionsBuilder.clearGatherers(forestArea);

            for (int forestAreaTilesIds: forestArea.tileIds()) {
                PlacedTile forestAreaTile = tileWithId(forestAreaTilesIds);

                if (forestAreaTile.idOfZoneOccupiedBy(Occupant.Kind.PAWN) != -1){

                    int zoneContainingGathererId = forestAreaTile.idOfZoneOccupiedBy(Occupant.Kind.PAWN);
                    Zone zoneContainingGatherer = forestAreaTile.zoneWithId(zoneContainingGathererId);

                    if (forestArea.zones().contains(zoneContainingGatherer)){
                        int BOARD_SIZE = 25;
                        int forestAreaTileIndex = (forestAreaTile.pos().y() + REACH) * BOARD_SIZE +
                                (forestAreaTile.pos().x() + REACH);
                        updatedPlacedTiles[forestAreaTileIndex] = forestAreaTile.withNoOccupant();
                    }
                }
            }
        }
        for (Area<Zone.River> riverArea : rivers) {
            zonePartitionsBuilder.clearFishers(riverArea);

            for (int riverAreaTilesIds : riverArea.tileIds()) {
                PlacedTile riverAreaTile = tileWithId(riverAreaTilesIds);

                if (riverAreaTile.idOfZoneOccupiedBy(Occupant.Kind.PAWN) != -1){

                    int zoneContainingFisherId = riverAreaTile.idOfZoneOccupiedBy(Occupant.Kind.PAWN);
                    Zone zoneContainingFisher = riverAreaTile.zoneWithId(zoneContainingFisherId);

                    if (riverArea.zones().contains(zoneContainingFisher)){
                        int BOARD_SIZE = 25;
                        int riverAreaTileIndex = (riverAreaTile.pos().y() + REACH) * BOARD_SIZE +
                                (riverAreaTile.pos().x() + REACH);
                        updatedPlacedTiles[riverAreaTileIndex] = riverAreaTile.withNoOccupant();
                    }
                }
            }
        }
        ZonePartitions updatedZonePartitions = zonePartitionsBuilder.build();

        return new Board(updatedPlacedTiles, tilesIndex, updatedZonePartitions, cancelledAnimals);
    }

    /**
     * Returns a new board with the given animals added to the set of cancelled animals.
     *
     * @param newlyCancelledAnimals The set of animals to be added to the cancelled animals.
     * @return A new board with the given animals added to the cancelled animals.
     */
    public Board withMoreCancelledAnimals(Set<Animal> newlyCancelledAnimals) {
        Set<Animal> updatedCancelledAnimals = new HashSet<>(cancelledAnimals);
        updatedCancelledAnimals.addAll(newlyCancelledAnimals);
        return new Board(placedTiles, tilesIndex, zonePartitions, Set.copyOf(updatedCancelledAnimals));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board)) return false;
        return Arrays.equals(placedTiles, board.placedTiles) &&
                Arrays.equals(tilesIndex, board.tilesIndex) &&
                zonePartitions.equals(board.zonePartitions) &&
                cancelledAnimals.equals(board.cancelledAnimals);
    }

    @Override
    public int hashCode() {
        int placedTilesHashCode = Arrays.hashCode(placedTiles);
        int tileIndexHashCode = Arrays.hashCode(tilesIndex);
        return Objects.hash(placedTilesHashCode, tileIndexHashCode, zonePartitions, cancelledAnimals);
    }
}

