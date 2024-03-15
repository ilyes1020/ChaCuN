package ch.epfl.chacun;

import java.util.List;
import java.util.Set;

/**
 * Record representing the zone partitions of the game board.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests, ZonePartition<Zone.Meadow> meadows, ZonePartition<Zone.River> rivers, ZonePartition<Zone.Water> riverSystems) {
    /**
     * The empty ZonePartitions
     */
    public final static ZonePartitions EMPTY = new ZonePartitions(new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>());

    /**
     * A builder class for constructing instances of the {@code ZonePartitions} class.
     * This builder allows for the modification of a {@code ZonePartitions} by manipulating its internal four different type of {@code ZonePartition}.
     */
    public static final class Builder {
        private ZonePartition.Builder<Zone.Forest> forestsBuilder;
        private ZonePartition.Builder<Zone.Meadow> meadowsBuilder;
        private ZonePartition.Builder<Zone.River> riversBuilder;
        private ZonePartition.Builder<Zone.Water> riverSystemsBuilder;


        /**
         * Constructor to instantiate a {@code ZonePartitions}'s Builder from an existing one.
         * @param initial the existing {@code ZonePartitions}
         */
        public Builder(ZonePartitions initial){
            this.forestsBuilder = new ZonePartition.Builder<>(initial.forests());
            this.meadowsBuilder = new ZonePartition.Builder<>(initial.meadows());
            this.riversBuilder = new ZonePartition.Builder<>(initial.rivers());
            this.riverSystemsBuilder = new ZonePartition.Builder<>(initial.riverSystems());
        }

        /**
         * Add the areas made of the zones of the given tile to the partitions.
         * @param tile the tile we want to add to the partitions
         */
        public void addTile(Tile tile){
            //compute the number of open connections for each zone in the tile
            int[] zoneIdSortedOpenConnections = new int[10];

            for (Zone zone : tile.sideZones()){

                int openConnectionCount = 0;
                if (zone instanceof Zone.River river && river.hasLake()){
                    zoneIdSortedOpenConnections[river.lake().localId()]++;
                    zoneIdSortedOpenConnections[river.localId()]++;
                }
                for (TileSide tileSide : tile.sides()){
                    if (tileSide.zones().contains(zone))
                        zoneIdSortedOpenConnections[zone.localId()]++;
                }
            }

            //add each zone as an area in the respective partition,
            //if a river is connected to a lake, we remove the extra open connection only in the river partition, not in the river system partition
            for (Zone zone : tile.zones()){
                switch (zone) {
                    case Zone.Forest forest-> forestsBuilder.addSingleton(forest, zoneIdSortedOpenConnections[zone.localId()]);
                    case Zone.Meadow meadow -> meadowsBuilder.addSingleton(meadow, zoneIdSortedOpenConnections[zone.localId()]);
                    case Zone.River river -> {
                        if (river.hasLake()){
                            riversBuilder.addSingleton(river, zoneIdSortedOpenConnections[zone.localId()] -1);
                        } else {
                            riversBuilder.addSingleton(river, zoneIdSortedOpenConnections[zone.localId()]);
                        }
                        riverSystemsBuilder.addSingleton(river, zoneIdSortedOpenConnections[zone.localId()]);
                    }
                    case Zone.Lake lake-> riverSystemsBuilder.addSingleton(lake, zoneIdSortedOpenConnections[zone.localId()]);
                }
            }
            //we connect all rivers areas to their respective lake, if they have one
            for (Zone zone : tile.sideZones()){
                if (zone instanceof Zone.River river && river.hasLake())
                    riverSystemsBuilder.union(river, river.lake());
            }
        }

        /**
         * Connects two tile sides by unionizing the areas.
         * @param s1 first tile side
         * @param s2 second tile side
         */
        public void connectSides(TileSide s1, TileSide s2){
            switch (s1) {
                case TileSide.Forest(Zone.Forest f1)
                        when s2 instanceof TileSide.Forest(Zone.Forest f2) ->
                        forestsBuilder.union(f1, f2);

                case TileSide.Meadow(Zone.Meadow m1)
                        when s2 instanceof TileSide.Meadow(Zone.Meadow m2) ->
                        meadowsBuilder.union(m1, m2);

                case TileSide.River(Zone.Meadow m11, Zone.River r1, Zone.Meadow m12)
                        when s2 instanceof TileSide.River(Zone.Meadow m21, Zone.River r2, Zone.Meadow m22) -> {
                        riversBuilder.union(r1, r2);
                        meadowsBuilder.union(m11, m22);
                        meadowsBuilder.union(m12, m21);
                }
                default -> throw new IllegalArgumentException("Different kinds of tile sides");
            }
        }

        /**
         * Adds an initial occupant of a specific kind of by the specific player to the area containing the given zone.
         * Throws IllegalArgumentException if the occupant kind cannot be on the given zone type
         *
         * @param player the player who will occupy the zone
         * @param occupantKind the kind of the occupant
         * @param occupiedZone the zone that will be occupied
         */
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone){
            switch (occupiedZone) {
                case Zone.Forest forest
                        when occupantKind == Occupant.Kind.PAWN ->
                        forestsBuilder.addInitialOccupant(forest, player);

                case Zone.Meadow meadow
                        when occupantKind == Occupant.Kind.PAWN ->
                        meadowsBuilder.addInitialOccupant(meadow, player);

                case Zone.River river
                        when !river.hasLake() || occupantKind == Occupant.Kind.PAWN ->
                        riversBuilder.addInitialOccupant(river, player);

                case Zone.Lake lake
                        when occupantKind == Occupant.Kind.HUT ->
                        riverSystemsBuilder.addInitialOccupant(lake, player);

                default -> throw new IllegalArgumentException("The occupant type cannot occupy the given zone");
            }
        }

        /**
         * Removes a pawn of the specified color from the area containing the given zone.
         * Throws IllegalArgumentException if the zone is a lake
         *
         * @param player the player who possess the pawn that will be removed
         * @param occupiedZone the zone to remove the pawn from
         */
        public void removePawn(PlayerColor player, Zone occupiedZone){
            switch (occupiedZone) {
                case Zone.Forest forest ->
                        forestsBuilder.removeOccupant(forest, player);

                case Zone.Meadow meadow ->
                        meadowsBuilder.removeOccupant(meadow, player);

                case Zone.River river ->
                        riversBuilder.removeOccupant(river, player);
                default -> throw new IllegalArgumentException("There cannot be a pawn in a lake zone");
            }
        }

        /**
         * Removes all gatherers (PAWN) from the given forest area.
         *
         * @param forest the forest from which all gatherers (PAWN) should be removed
         */
        public void clearGatherers(Area<Zone.Forest> forest){
            forestsBuilder.removeAllOccupantsOf(forest);
        }

        /**
         * Removes all fishers (PAWN) from the given river area.
         *
         * @param river the river from which all fishers (PAWN) should be removed
         */
        public void clearFishers(Area<Zone.River> river){
            riversBuilder.removeAllOccupantsOf(river);
        }

        /**
         * Builds the zone partitions.
         *
         * @return the new zone partitions
         */
        public ZonePartitions build(){
            return new ZonePartitions(forestsBuilder.build(), meadowsBuilder.build(), riversBuilder.build(), riverSystemsBuilder.build());
        }
    }
}
