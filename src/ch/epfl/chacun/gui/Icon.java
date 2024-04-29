package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import javafx.scene.Node;
import javafx.scene.shape.SVGPath;

/**
 * This class provides static methods to create icons for the game.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public final class Icon {

    private static final String PAWN_SVG_PATH = "M -10 10 H -4 L 0 2 L 6 10 H 12 L 5 0 L 12 -2 L 12 -4 L 6 -6 L 6 -10 L 0 -10 L -2 -4 L -6 -2 L -8 -10 L -12 -10 L -8 6 Z";
    private static final String HUT_SVG_PATH = "M -8 10 H 8 V 2 H 12 L 0 -10 L -12 2 H -8 Z";

    private Icon() {}

    /**
     * Returns a new node representing the occupant.
     *
     * @param playerColor the color of the player
     * @param occupantKind the kind of the occupant
     * @return              a new node representing the occupant
     */
    public static Node newFor(PlayerColor playerColor, Occupant.Kind occupantKind) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(occupantKind == Occupant.Kind.PAWN ? PAWN_SVG_PATH : HUT_SVG_PATH);

        svgPath.setFill(ColorMap.fillColor(playerColor));
        svgPath.setStroke(ColorMap.strokeColor(playerColor));

        return svgPath;
    }
}
