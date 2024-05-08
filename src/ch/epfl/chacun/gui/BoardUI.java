package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.FormatProcessor.FMT;

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

    //---ScrollPane instantiation---//
    ScrollPane boardSP = new ScrollPane();
    boardSP.getStylesheets().add("board.css");
    boardSP.setId("board-scroll-pane");

    //---GridPane instantiation---//
    GridPane boardGP = new GridPane();
    boardGP.setId("board-grid");
    boardSP.setContent(boardGP);

    //---empty Tile image---//
    WritableImage emptyTileImage = new WritableImage(1, 1);
    emptyTileImage
            .getPixelWriter()
            .setColor(0, 0, Color.gray(0.98));

    // Iterate over all the positions of the board
    for (int y = 0 ; y <= 2 * reach ; y++) {
        for (int x = 0 ; x <= 2 * reach ; x++) {
            final Pos currentPos = new Pos(x - reach,y - reach);

            Group tileGroup = new Group();
            ReadOnlyBooleanProperty mouseHovering = tileGroup.hoverProperty();
            boardGP.add(tileGroup, x, y);

            ObservableValue<PlacedTile> placedTileOV = gameStateOV.map(m -> m.board().tileAt(currentPos));

            ObservableValue<Set<Pos>> fringeOV = gameStateOV.map(gameState -> {
                if (gameState.nextAction() == GameState.Action.PLACE_TILE) {
                    return  gameState.board().insertionPositions();
                }
                return Set.of();
            });

            ObservableValue<CellData> cellDataOV = Bindings.createObjectBinding(
                    () -> {
                        Image backgroundImage = emptyTileImage;
                        Rotation rotation = rotationOV.getValue();
                        Color veilColor = Color.TRANSPARENT;
                        PlayerColor currentPlayer = gameStateOV.getValue().currentPlayer();

                        Tile tileToPlace = gameStateOV.getValue().tileToPlace();

                        if (placedTileOV.getValue() == null) {
                            if (fringeOV.getValue().contains(currentPos)){
                                if(mouseHovering.getValue()){
                                    backgroundImage = CellData.IMAGE_CACHE.putIfAbsent(tileToPlace.id(), ImageLoader.normalImageForTile(tileToPlace.id()));
                                    PlacedTile placedTileToPlace = new PlacedTile(tileToPlace, currentPlayer, rotation, currentPos);
                                    if (!gameStateOV.getValue().board().canAddTile(placedTileToPlace)){
                                        veilColor = Color.WHITE;
                                    }
                                }
                                else {
                                    veilColor = ColorMap.fillColor(currentPlayer);
                                }
                            }
                        }

                        else {
                            backgroundImage = CellData.IMAGE_CACHE.putIfAbsent(placedTileOV.getValue().id(), ImageLoader.normalImageForTile(placedTileOV.getValue().id()));
                            rotation = placedTileOV.getValue().rotation();
                            if (!highlightedTilesIdsOV.getValue().isEmpty()){
                                if(!highlightedTilesIdsOV.getValue().contains(placedTileOV.getValue().id())){
                                    veilColor = Color.BLACK;
                                }
                            }
                        }

                        return new CellData(backgroundImage, rotation, veilColor);
                    }
                    , placedTileOV
                    , rotationOV
                    , gameStateOV
                    , highlightedTilesIdsOV
                    , fringeOV
                    , mouseHovering);

            ImageView backgroundImageIV = new ImageView(emptyTileImage);
            backgroundImageIV.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
            backgroundImageIV.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
            tileGroup.getChildren().add(backgroundImageIV);
            backgroundImageIV.imageProperty().bind(cellDataOV.map(CellData::backgroundImage));

            tileGroup.rotateProperty().bind(cellDataOV.map(cellData -> cellData.rotation().degreesCW()));

            // Create a ColorInput instance for the veil image.
            ObservableValue<ColorInput> veilImageOV = cellDataOV.map(cellData -> {
                ColorInput veilColor = new  ColorInput();
                veilColor.setPaint(cellData.veilColor());
                veilColor.setHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                veilColor.setWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                return veilColor;
            });

            // Create a Blend instance for the veil effect.
            ObservableValue<Blend> veilEffectOV = veilImageOV.map(veilImage -> {
                Blend blend = new Blend(BlendMode.SRC_OVER, null, veilImage);
                blend.setOpacity(0.5);
                return blend;
            });

            // Apply the veil effect to the Group instance.
            tileGroup.effectProperty().bind(veilEffectOV);


            tileGroup.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY){
                    if (event.isAltDown()) rotationHandler.accept(Rotation.RIGHT);
                    else rotationHandler.accept(Rotation.LEFT);
                }
                if (event.getButton() == MouseButton.PRIMARY
                        && fringeOV.getValue().contains(currentPos)) {
                    placeTileHandler.accept(currentPos);
                }
            });

            placedTileOV.addListener((o, oldTile, newTile) -> {
                if (newTile != null) {
                    //---Adding the Cancelled Animals Markers---//
                    Set<Animal> animals = newTile
                            .meadowZones()
                            .stream()
                            .flatMap(zone -> zone.animals().stream())
                            .collect(Collectors.toSet());
                    for (Animal animal : animals) {
                        ImageView markerIV = new ImageView("marker.png");
                        markerIV.getStyleClass().add("marker");
                        markerIV.setId(String.format("marker_%d", animal.id()));
                        tileGroup.getChildren().add(markerIV);

                        //---making the marker visible when the animal is cancelled---//
                        ObservableValue<Boolean> markerVisibilityOV = gameStateOV.map(gameState -> gameState.board().cancelledAnimals().contains(animal));
                        markerIV.visibleProperty().bind(markerVisibilityOV);
                    }

                    //---Adding the occupants---//
                    for (Occupant occupant : newTile.potentialOccupants()) {
                        Node occupantNode = Icon.newFor(newTile.placer(), occupant.kind());
                        occupantNode.setId(FMT."/\{occupant.kind().toString().toLowerCase()}/%02d\{occupant.zoneId()}.jpg");
                        tileGroup.getChildren().add(occupantNode);

                        //---making the occupant visible if it should be---//
                        ObservableValue<Boolean> occupantVisibilityOV = visibleOccupantsOV.map(visibleOccupants -> visibleOccupants.contains(occupant));
                        occupantNode.visibleProperty().bind(occupantVisibilityOV);

                        //---making the occupant always vertical---//
                        occupantNode.rotateProperty().bind(cellDataOV.map(cellData -> cellData.rotation().negated().degreesCW()));

                        //---making the occupant clickable---//
                        occupantNode.setOnMouseClicked(event -> {
                            if (event.getButton() == MouseButton.PRIMARY) occupantSelectHandler.accept(occupant);
                        });
                    }
                }
            });
        }
    }
    return boardSP;
}

    private record CellData(Image backgroundImage, Rotation rotation, Color veilColor) {
        public static Map<Integer, Image> IMAGE_CACHE = new HashMap<>();
    }
}


