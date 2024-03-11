package ch.epfl.chacun;

import java.util.List;
import java.util.Set;

/**
 * record representing the zone partitions of the game board
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests, ZonePartition<Zone.Meadow> meadows, ZonePartition<Zone.River> rivers, ZonePartition<Zone.Water> riverSystems) {
    /**
     * The empty ZonePartitions
     */
    public final static ZonePartitions EMPTY = new ZonePartitions(new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>(), new ZonePartition<>());

    public static final class Builder<Z extends Zone> {
        private ZonePartition.Builder<Zone.Forest> forestsBuilder;
        private ZonePartition.Builder<Zone.Meadow> meadowsBuilder;
        private ZonePartition.Builder<Zone.River> riversBuilder;
        private ZonePartition.Builder<Zone.Water> riverSystemsBuilder;

        public Builder(ZonePartitions initial){
            this.forestsBuilder = new ZonePartition.Builder<>(initial.forests());
            this.meadowsBuilder = new ZonePartition.Builder<>(initial.meadows());
            this.riversBuilder = new ZonePartition.Builder<>(initial.rivers());
            this.riverSystemsBuilder = new ZonePartition.Builder<>(initial.riverSystems());
        }

        public void addTile(Tile tile){

        }

        public void connectSides(TileSide s1, TileSide s2){

        }

        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone){

        }

        public void removePawn(PlayerColor player, Zone occupiedZone){

        }

        public void clearGatherers(Area<Zone.Forest> forest){

        }

        public void clearFishers(Area<Zone.River> river){

        }

        public ZonePartitions build(){
            return new ZonePartitions(forestsBuilder.build(), meadowsBuilder.build(), riversBuilder.build(), riverSystemsBuilder.build());
        }

    }

}
