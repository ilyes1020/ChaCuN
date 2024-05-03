package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class BoardUI {
    private BoardUI(){}

    public static Node create(int reach,
                              ObservableValue<GameState> gameStateOV,
                              ObservableValue<Rotation> rotationOV,
                              ObservableValue<Set<Occupant>> visibleOccupantsOV,
                              ObservableValue<Set<Integer>> highlightedTilesIdsOV,
                              Consumer<Rotation> rotationHandler,
                              Consumer<Pos> placeTileHandler,
                              Consumer<Occupant> occupantSelectHandler){

        //---ScrollPane instanciation---//
        ScrollPane boardSP = new ScrollPane();
        boardSP.getStylesheets().add("board.css");
        boardSP.setId("board-scroll-pane");

        //---GridPane instanciation---//
        GridPane boardGP = new GridPane();

        // Iterate over the x and y indices of the board
        for (int x = 0; x < reach; x++) {
            for (int y = 0; y < reach; y++) {
                final Pos currentPos = new Pos(x,y);

                Group tileGroup = new Group(new ImageView());

                boardGP.add(tileGroup,x,y);

                ObservableValue<Tile> tileOV = gameStateOV.map(m -> m.board().tileAt(currentPos).tile());

                //---Background image---//
                WritableImage emptyTileImage = new WritableImage(1, 1);
                emptyTileImage
                        .getPixelWriter()
                        .setColor(0, 0, Color.gray(0.98));



            }
        }

        return null;
    }
}
