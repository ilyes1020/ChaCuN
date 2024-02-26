package ch.epfl.chacun;

import java.util.List;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public sealed interface TileSide {

    /**
     * returns the zones that are in contact with the tile side (this)
     * @return a list of the zones that touches the tile side
     */
    List<Zone> zones();

    /**
     * return true iff the given tile side (that) is the same kind as the receptor one (this)
     * @param that a tile side we want to compare
     * @return true iff that tile side is the same kind as this tile side
     */
    boolean isSameKindAs(TileSide that);

    public record Forest(Zone.Forest forest) implements TileSide{
        /**
         * returns the zones that are in contact with the tile side (this)
         *
         * @return a list of the zones that touches the tile side
         */
        @Override
        public List<Zone> zones() {
            return List.of(forest);
        }

        /**
         * return true iff the given tile side (that) is the same kind as the receptor one (this)
         *
         * @param that a tile side we want to compare
         * @return true iff that tile side is the same kind as this tile side
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof Forest;
        }
    }
    public record Meadow(Zone.Meadow meadow) implements TileSide{

        /**
         * returns the zones that are in contact with the tile side (this)
         *
         * @return a list of the zones that touches the tile side
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow);
        }

        /**
         * return true iff the given tile side (that) is the same kind as the receptor one (this)
         *
         * @param that a tile side we want to compare
         * @return true iff that tile side is the same kind as this tile side
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof Meadow;
        }
    }
    public record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide{
        /**
         * returns the zones that are in contact with the tile side (this)
         *
         * @return a list of the zones that touches the tile side
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        /**
         * return true iff the given tile side (that) is the same kind as the receptor one (this)
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
