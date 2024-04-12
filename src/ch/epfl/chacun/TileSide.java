package ch.epfl.chacun;

import java.util.List;

/**
 * Interface representing a tile side.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public sealed interface TileSide {

    /**
     * Returns the zones that are in contact with the tile side (this).
     *
     * @return a list of the zones that touches the tile side
     */
    List<Zone> zones();

    /**
     * Return true iff the given tile side (that) is the same kind as the receptor one (this).
     *
     * @param that a tile side we want to compare
     * @return true iff that tile side is the same kind as this tile side
     */
    boolean isSameKindAs(TileSide that);

    /**
     * Record representing a forest in contact with a tile side.
     *
     * @param forest the forest (Zone) that is in contact with the tile side
     */
    public record Forest(Zone.Forest forest) implements TileSide{
        /**
         * Returns the zones that are in contact with the tile side (this).
         *
         * @return a list of the zones that touches the tile side
         */
        @Override
        public List<Zone> zones() {
            return List.of(forest);
        }

        /**
         * Return true iff the given tile side (that) is the same kind as the receptor one (this).
         *
         * @param that a tile side we want to compare
         * @return true iff that tile side is the same kind as this tile side
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof Forest;
        }
    }

    /**
     * Record representing a meadow in contact with a tile side.
     *
     * @param meadow the meadow (Zone) that is in contact with the tile side
     */
    public record Meadow(Zone.Meadow meadow) implements TileSide{

        /**
         * Returns the zones that are in contact with the tile side (this).
         *
         * @return a list of the zones that touches the tile side
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow);
        }

        /**
         * Return true iff the given tile side (that) is the same kind as the receptor one (this).
         *
         * @param that a tile side we want to compare
         * @return true iff that tile side is the same kind as this tile side
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof Meadow;
        }
    }

    /**
     * Record representing a river surrounded by two meadows, in contact with a tile side.
     *
     * @param meadow1 the first clockwise meadow (Zone) that is in contact with the tile side and the river
     * @param river the river (Zone) that is in contact with the tile side
     * @param meadow2 the second clockwise meadow (Zone) that is in contact with the tile side and the river
     */
    public record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide{
        /**
         * Returns the zones that are in contact with the tile side (this).
         *
         * @return a list of the zones that touches the tile side
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        /**
         * Return true iff the given tile side (that) is the same kind as the receptor one (this).
         *
         * @param that a tile side we want to compare
         * @return true iff that tile side is the same kind as this tile side
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof River;
        }
    }
}
