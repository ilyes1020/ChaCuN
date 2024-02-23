package ch.epfl.chacun;

/**
 * class representing the animals in game
 * @param kind the kind of the animal
 * @param id the animal's id
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public record Animal( int id, Kind kind) {

    /**
     * enum representing the four kinds of animal in game
     */
    public enum Kind {
        MAMMOTH,
        AUROCHS,
        DEER,
        TIGER;
    }

    /**
     * @return the id of the tile on which the animal is standing
     */
    public int tileId(){
        return id / 100;
    }
}
