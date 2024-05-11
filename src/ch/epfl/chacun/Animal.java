package ch.epfl.chacun;

/**
 * Class representing the animals in game.
 *
 * @param id the id of the animal
 * @param kind the kind of the animal
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public record Animal(int id, Kind kind) {

    /**
     * Enum representing the four kinds of animal in game.
     */
    public enum Kind {

        /**
         * Represents a mammoth
         */
        MAMMOTH,

        /**
         * Represents an aurochs
         */
        AUROCHS,

        /**
         * Represents a deer
         */
        DEER,

        /**
         * Represents a tiger
         */
        TIGER;
    }

    /**
     * Returns the id of the tile on which the animal is standing
     *
     * @return the id of the tile on which the animal is standing
     */
    public int tileId(){
        return Zone.tileId(id / 10);
    }
}
