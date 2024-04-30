package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;

import java.util.function.Consumer;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class DecksUI {
    private DecksUI() {}

    public static void create(ObservableValue<Tile> tileToPlace,
                              ObservableValue<Integer> normalTilesRemaining,
                              ObservableValue<Integer> menhirTilesRemaining,
                              ObservableValue<String> textToDisplay,
                              Consumer<Occupant> onTileTextClick){

    }
}
