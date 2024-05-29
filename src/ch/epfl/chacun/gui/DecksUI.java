package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * User interface representing the next tile to place, the deck of normal cards and the deck of menhir cards.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class DecksUI {
    private static final Image NORMAL_TILE_IMAGE = new Image("/256/NORMAL.JPG");
    private static final Image MENHIR_TILE_IMAGE = new Image("/256/MENHIR.JPG");

    private DecksUI() {}

    /**
     * Creates a JavaFx Node representing the Decks User Interface.
     *
     * @param tileToPlaceOV              An Observable value of the tile to place.
     * @param nbOfRemainingNormalTilesOV An Observable value of the number of remaining normal tiles.
     * @param nbOfRemainingMenhirTilesOV An Observable value of the number of remaining menhir tiles.
     * @param textToDisplayOV            An Observable value of the text to display.
     * @param clickableText              A consumer of Occupant.
     * @return a JavaFx Node representing the Decks User Interface.
     */

    public static Node create(ObservableValue<Tile> tileToPlaceOV,
                              ObservableValue<Integer> nbOfRemainingNormalTilesOV,
                              ObservableValue<Integer> nbOfRemainingMenhirTilesOV,
                              ObservableValue<String> textToDisplayOV,
                              Consumer<Occupant> clickableText) {
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

        //---adding the click on action to the text to display---/
        textToDisplay.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) clickableText.accept(null);
        });

        //---remaining tiles HBox initialization---//
        HBox remainingTilesHB = new HBox();
        remainingTilesHB.setId("decks");


        //---normal tile stackPane---//
        StackPane normalTileSP = createTileStackPane(NORMAL_TILE_IMAGE, nbOfRemainingNormalTilesOV);

        //---menhir tile stackPane---//
        StackPane menhirTileSP = createTileStackPane(MENHIR_TILE_IMAGE, nbOfRemainingMenhirTilesOV);

        //---adding normalTileDeck and menhirTileDeck as children of Hbox---//
        remainingTilesHB.getChildren().addAll(normalTileSP, menhirTileSP);

        //---adding the remaining tiles Hbox and the tile to place's StackPane as children of decks VBox---//
        decksVB.getChildren().addAll(remainingTilesHB, tileToPlaceSP);

        return decksVB;
    }

    private static StackPane createTileStackPane(Image tileImage, ObservableValue<Integer> tileCountOV) {
        StackPane tileSP = new StackPane();

        ImageView tileDeck = new ImageView(tileImage);
        tileDeck.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        tileDeck.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);

        Text tileCounter = new Text();
        tileCounter.textProperty().bind(tileCountOV.map(Object::toString));

        tileSP.getChildren().addAll(tileDeck, tileCounter);

        return tileSP;
    }
}
