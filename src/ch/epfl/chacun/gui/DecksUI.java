package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.function.Consumer;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class DecksUI {
    private DecksUI() {}

    public static void create(ObservableValue<Tile> tileToPlaceOV,
                              ObservableValue<Integer> nbOfRemainingNormalTilesOV,
                              ObservableValue<Integer> nbOfRemainingMenhirTilesOV,
                              ObservableValue<String> textToDisplayOV,
                              Consumer<Occupant> clickableText){
        //---DecksUI's VBox initialization---//
        VBox decksVB = new VBox();
        decksVB.getStylesheets().add("decks.css");

        //---tile to place StackPane initialization---//
        StackPane tileToPlaceSP = new StackPane();
        tileToPlaceSP.setId("next-tile");

        //---tile to place's image observable value---//
        ObservableValue<Integer> tileToPlaceIdOV = tileToPlaceOV.map(Tile::id);
        ObservableValue<Image> tileToPlaceImageOV = tileToPlaceIdOV.map(ImageLoader::largeImageForTile);

        //---tile to place's ImageView initialization---//
        ImageView tileToPlace = new ImageView();
        tileToPlace.imageProperty().bind(tileToPlaceImageOV);

        //---reduction of the size of the displayed image---//
        tileToPlace.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        tileToPlace.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);

        //---text to display initialization---//
        Text textToDisplay = new Text();
        textToDisplay.textProperty().bind(textToDisplayOV);

        //---making the text be at most 80% of the tile to place's image's width---//
        textToDisplay.setWrappingWidth(0.8 * ImageLoader.LARGE_TILE_FIT_SIZE);

        //---making the text to display invisible when the text is empty---//
        ObservableValue<Boolean> textToDisplayVisibilityOV = textToDisplayOV.map(o -> !o.isEmpty());
        textToDisplay.visibleProperty().bind(textToDisplayVisibilityOV);

        //---adding tile to place's ImageView and the Text to display as children of StackPane---//
        tileToPlaceSP.getChildren().addAll(tileToPlace, textToDisplay);

        //---adding the tile to place's StackPane as the child of decks VBox---//
        decksVB.getChildren().add(tileToPlaceSP);

        //---remaining tiles HBox initialization---//
        HBox remainingTilesHB = new HBox();
        remainingTilesHB.setId("decks");
    }
}
