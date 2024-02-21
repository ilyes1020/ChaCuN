package ch.epfl.chacun;

import java.util.List;

/**
 * Enum that contain 5 different colors for the players
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public enum PlayerColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    PURPLE;
    public static final List<PlayerColor> ALL = List.of(values());
}
