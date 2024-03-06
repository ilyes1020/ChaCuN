package ch.epfl.chacun;

import java.util.List;

/**
 * Enum that contain 5 different colors for the players
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public enum PlayerColor {

    /**
     * Represents the player RED
     */
    RED,

    /**
     * Represents the player BLUE
     */
    BLUE,

    /**
     * Represents the player GREEN
     */
    GREEN,

    /**
     * Represents the player YELLOW
     */
    YELLOW,

    /**
     * Represents the player PURPLE
     */
    PURPLE;

    /**
     * A static list containing all available player colors
     */
    public static final List<PlayerColor> ALL = List.of(values());
}
