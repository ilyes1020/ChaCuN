package ch.epfl.chacun;

/**
 * Record representing the zone partitions of the game board.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests, ZonePartition<Zone.Meadow> meadows,
                             ZonePartition<Zone.River> rivers, ZonePartition<Zone.Water> riverSystems) {
    /**
     * The empty ZonePartitions.
     */
    public final static ZonePartitions EMPTY = new ZonePartitions(new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>());

    /**
     * A builder class for constructing instances of the {@code ZonePartitions} class.
     * <p>
     * This builder allows for the modification of a {@code ZonePartitions} by manipulating its internal four different type of {@code ZonePartition}.
     */
    public static final class Builder {
        private final ZonePartition.Builder<Zone.Forest> forestsBuilder;
        private final ZonePartition.Builder<Zone.Meadow> meadowsBuilder;
        private final ZonePartition.Builder<Zone.River> riversBuilder;
        private final ZonePartition.Builder<Zone.Water> riverSystemsBuilder;


        /**
         * Constructor to instantiate a {@code ZonePartitions}'s Builder from an existing one.
         *
         * @param initial the existing {@code ZonePartitions}
         */
        public Builder(ZonePartitions initial) {
            this.forestsBuilder = new ZonePartition.Builder<>(initial.forests());
            this.meadowsBuilder = new ZonePartition.Builder<>(initial.meadows());
            this.riversBuilder = new ZonePartition.Builder<>(initial.rivers());
            this.riverSystemsBuilder = new ZonePartition.Builder<>(initial.riverSystems());
        }

        /**
         * Add the areas made of the zones of the given tile to the partitions.
         *
         * @param tile the tile we want to add to the partitions
         */
        public void addTile(Tile tile) {
            addZonesToPartitions(tile);
            connectRiversToLakes(tile);
        }

        /**
         * Connects two tile sides by unionizing the areas.
         *
         * @param s1 first tile side
         * @param s2 second tile side
         * @throws IllegalArgumentException If the sides are not compatibles
         */
        public void connectSides(TileSide s1, TileSide s2) {
            switch (s1) {
                case TileSide.Forest(Zone.Forest f1)
                        when s2 instanceof TileSide.Forest(Zone.Forest f2) -> forestsBuilder.union(f1, f2);

                case TileSide.Meadow(Zone.Meadow m1)
                        when s2 instanceof TileSide.Meadow(Zone.Meadow m2) -> meadowsBuilder.union(m1, m2);

                case TileSide.River(Zone.Meadow m11, Zone.River r1, Zone.Meadow m12)
                        when s2 instanceof TileSide.River(Zone.Meadow m21, Zone.River r2, Zone.Meadow m22) -> {
                    riversBuilder.union(r1, r2);
                    meadowsBuilder.union(m11, m22);
                    meadowsBuilder.union(m12, m21);
                    riverSystemsBuilder.union(r1, r2);
                }
                default -> throw new IllegalArgumentException("Different kinds of tile sides");
            }
        }

        /**
         * Adds an initial occupant of a specific kind of by the specific player to the area containing the given zone.
         *
         * @param player       the player who will occupy the zone
         * @param occupantKind the kind of the occupant
         * @param occupiedZone the zone that will be occupied
         * @throws IllegalArgumentException if the occupant kind cannot be on the given zone type
         */
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone) {
            switch (occupantKind) {
                case PAWN -> addPawn(player, occupiedZone);
                case HUT -> addHut(player, occupiedZone);
            }
        }

        /**
         * Removes a pawn of the specified color from the area containing the given zone.
         *
         * @param player       the player who possess the pawn that will be removed
         * @param occupiedZone the zone to remove the pawn from
         * @throws IllegalArgumentException if the zone is a lake
         */
        public void removePawn(PlayerColor player, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.Forest forest -> forestsBuilder.removeOccupant(forest, player);
                case Zone.Meadow meadow -> meadowsBuilder.removeOccupant(meadow, player);
                case Zone.River river -> riversBuilder.removeOccupant(river, player);
                default -> throw new IllegalArgumentException("There cannot be a pawn in a lake zone");
            }
        }

        /**
         * Removes all gatherers (PAWN) from the given forest area.
         *
         * @param forest the forest from which all gatherers (PAWN) should be removed
         */
        public void clearGatherers(Area<Zone.Forest> forest) {
            forestsBuilder.removeAllOccupantsOf(forest);
        }

        /**
         * Removes all fishers (PAWN) from the given river area.
         *
         * @param river the river from which all fishers (PAWN) should be removed
         */
        public void clearFishers(Area<Zone.River> river) {
            riversBuilder.removeAllOccupantsOf(river);
        }

        /**
         * Builds the zone partitions.
         *
         * @return the new zone partitions
         */
        public ZonePartitions build() {
            return new ZonePartitions(forestsBuilder.build(), meadowsBuilder.build(), riversBuilder.build(), riverSystemsBuilder.build());
        }

        /**
         * Adds the zones of the given tile to the partitions.
         *
         * @param tile The tile containing the zones to be added.
         */

        private void addZonesToPartitions(Tile tile) {
            //compute the open connections
            int[] zoneIdSortedOpenConnections = new int[10];
            for (TileSide tileSide : tile.sides()) {
                for (Zone zone : tileSide.zones()) {
                    if (zone instanceof Zone.River river && river.hasLake()) {
                        zoneIdSortedOpenConnections[river.lake().localId()]++;
                        zoneIdSortedOpenConnections[river.localId()]++;
                    }
                    zoneIdSortedOpenConnections[zone.localId()]++;
                }
            }
            //add zones to the partitions
            for (Zone zone : tile.zones()) {
                switch (zone) {
                    case Zone.Forest forest ->
                            forestsBuilder.addSingleton(forest, zoneIdSortedOpenConnections[zone.localId()]);
                    case Zone.Meadow meadow ->
                            meadowsBuilder.addSingleton(meadow, zoneIdSortedOpenConnections[zone.localId()]);
                    case Zone.River river -> {
                        if (river.hasLake()) {
                            riversBuilder.addSingleton(river, zoneIdSortedOpenConnections[zone.localId()] - 1);
                        } else {
                            riversBuilder.addSingleton(river, zoneIdSortedOpenConnections[zone.localId()]);
                        }
                        riverSystemsBuilder.addSingleton(river, zoneIdSortedOpenConnections[zone.localId()]);
                    }
                    case Zone.Lake lake ->
                            riverSystemsBuilder.addSingleton(lake, zoneIdSortedOpenConnections[zone.localId()]);
                }
            }
        }

        /**
         * Connects rivers to lakes within a given tile.
         *
         * @param tile The tile containing the rivers and lakes to be connected.
         */
        private void connectRiversToLakes(Tile tile) {
            for (Zone zone : tile.sideZones()) {
                if (zone instanceof Zone.River river && river.hasLake())
                    riverSystemsBuilder.union(river, river.lake());
            }
        }

        /**
         * Adds a pawn for a player to a given zone.
         *
         * @param player       The player for whom to add the pawn.
         * @param occupiedZone The zone to which to add the pawn.
         * @throws IllegalArgumentException if the zone is a lake
         */
        private void addPawn(PlayerColor player, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.Forest forest -> forestsBuilder.addInitialOccupant(forest, player);
                case Zone.Meadow meadow -> meadowsBuilder.addInitialOccupant(meadow, player);
                case Zone.River river -> riversBuilder.addInitialOccupant(river, player);
                default -> throw new IllegalArgumentException("The occupant type cannot occupy the given zone");
            }
        }

        /**
         * Adds a hut for a player to a given zone.
         *
         * @param player       The player for whom to add the hut.
         * @param occupiedZone The zone to which to add the hut.
         * @throws IllegalArgumentException if the zone is a meadow, forest or a river connected to a lake
         */
        private void addHut(PlayerColor player, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.River river when !river.hasLake() -> riverSystemsBuilder.addInitialOccupant(river, player);
                case Zone.Lake lake -> riverSystemsBuilder.addInitialOccupant(lake, player);
                default -> throw new IllegalArgumentException("The occupant type cannot occupy the given zone");
            }
        }
    }
}
