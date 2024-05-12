package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import javafx.scene.paint.Color;

/**
 * This class provides static methods to get the color of the player in JavaFX.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class ColorMap {
    private ColorMap() {}

    /**
     * Returns the color of the player in JavaFX.
     *
     * @param playerColor the color of the player
     * @return the color of the player in JavaFX
     */

    public static Color fillColor(PlayerColor playerColor){
        return switch (playerColor) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.LIME;
            case YELLOW -> Color.YELLOW;
            case PURPLE -> Color.PURPLE;
        };
    }

    /**
     * Returns the stroke color of the player in JavaFX.
     *
     * @param playerColor the color of the player
     * @return the stroke color of the player in JavaFX
     */

    public static Color strokeColor(PlayerColor playerColor){
        return playerColor == PlayerColor.YELLOW || playerColor == PlayerColor.GREEN ?
                fillColor(playerColor).deriveColor(0,1,0.6,1):
                Color.WHITE;
    }
}
