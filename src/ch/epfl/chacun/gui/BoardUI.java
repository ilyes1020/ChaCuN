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

/**
 * User interface representing the board of tiles.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class BoardUI {
    private BoardUI(){}

    /**
     * Creates a JavaFx Node representing the Board Interface.
     *
     * @param reach                     The reach of the board
     * @param gameStateOV               An Observable value of the gameState.
     * @param rotationOV                An Observable value of the rotation of the tile.
     * @param visibleOccupantsOV        An Observable value of the set of the visible occupants.
     * @param highlightedTilesIdsOV     An Observable value of the set of the highlighted tiles.
     * @param rotationHandler           A handler of tile rotations
     * @param placeTileHandler          A handler of tile placements
     * @param occupantSelectHandler     A handler of occupants selection (placing or removing)
     * @return                          a JavaFx node of the user interface of the board with cell nodes for tiles.
     */
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

    //---Iterate over all the positions of the board to create all cells---//
    for (int y = 0 ; y <= 2 * reach ; y++) {
        for (int x = 0 ; x <= 2 * reach ; x++) {
            final Pos currentPos = new Pos(x - reach,y - reach);

            //---the group containing the tile instantiation---//
            Group tileGroup = new Group();
            boardGP.add(tileGroup, x, y);

            //---Mouse hovering property of the cell---//
            ReadOnlyBooleanProperty mouseHovering = tileGroup.hoverProperty();

            //---Observable value of the PlacedTile on the cell (can be null)---//
            ObservableValue<PlacedTile> placedTileOV = gameStateOV.map(m -> m.board().tileAt(currentPos));

            //---Observable value of the fringe depending on the game state---//
            ObservableValue<Set<Pos>> fringeOV = gameStateOV.map(gameState -> {
                if (gameState.nextAction() == GameState.Action.PLACE_TILE) {
                    return  gameState.board().insertionPositions();
                }
                return Set.of();
            });

            //---The Cell's data instantiation---//
            ObservableValue<CellData> cellDataOV = Bindings.createObjectBinding(
                    () -> {

                        // Useful variables
                        PlayerColor currentPlayer = gameStateOV.getValue().currentPlayer();
                        PlacedTile placedTile = placedTileOV.getValue();
                        Tile tileToPlace = gameStateOV.getValue().tileToPlace();

                        // Default variables initialization
                        Image backgroundImage = emptyTileImage;
                        Rotation rotation = rotationOV.getValue();
                        Color veilColor = Color.TRANSPARENT;

                        // If there is a PlacedTile on the cell,
                        // load its image if not loaded yet and set the background image to the tile's image
                        if (placedTile != null) {
                            backgroundImage = CellData.IMAGE_CACHE.computeIfAbsent(placedTile.id(), ImageLoader::normalImageForTile);

                            // If there is a PlacedTile on the cell and there is other PlacedTiles that should be highlighted,
                            // then the veil should be black
                            if (!highlightedTilesIdsOV.getValue().isEmpty() && !highlightedTilesIdsOV.getValue().contains(placedTile.id())){
                                veilColor = Color.BLACK;
                            }
                        }

                        // If there is no PlacedTile on the cell, and it is in the fringe,
                        // we check if the mouse is hovering it.
                        else if (fringeOV.getValue().contains(currentPos)){

                            // If the mouse is hovering the cell,
                            // we set the background image to the tileToPlace's image
                            if(mouseHovering.getValue()){
                                backgroundImage = CellData.IMAGE_CACHE.computeIfAbsent(tileToPlace.id(), ImageLoader::normalImageForTile);
                                PlacedTile placedTileToPlace = new PlacedTile(tileToPlace, currentPlayer, rotation, currentPos);

                                // If the mouse is hovering the cell and the player cannot place the tileToPlace on the cell,
                                // the color of the veil should be white
                                if (!gameStateOV.getValue().board().canAddTile(placedTileToPlace)){
                                    veilColor = Color.WHITE;
                                }

                            // If the mouse is not hovering the tile in the fringe,
                            // the color of the veil is the same as the current player's.
                            } else {
                                veilColor = ColorMap.fillColor(currentPlayer);
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

            //---The ImageView of the cell instantiation---//
            ImageView backgroundImageIV = new ImageView();
            backgroundImageIV.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
            backgroundImageIV.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
            tileGroup.getChildren().add(backgroundImageIV);

            //---Binding the image of the cell to cellData's background image data---//
            backgroundImageIV.imageProperty().bind(cellDataOV.map(CellData::backgroundImage));

            //---Binding the rotation of the cell to cellData's rotation data---//
            tileGroup.rotateProperty().bind(cellDataOV.map(cellData -> cellData.rotation().degreesCW()));

            // Create a ColorInput instance for the veil image.
            //ObservableValue<ColorInput> veilImageOV = cellDataOV.map(cellData -> new  ColorInput(0, 0, ImageLoader.NORMAL_TILE_FIT_SIZE, ImageLoader.NORMAL_TILE_FIT_SIZE, cellData.veilColor()));

            //---Create a Blend instance for the veil effect depending on the cellData---//
            ObservableValue<Blend> veilEffectOV = cellDataOV.map(cellData -> {
                Blend blend = new Blend(
                        BlendMode.SRC_OVER
                        , null
                        , new  ColorInput(0, 0, ImageLoader.NORMAL_TILE_FIT_SIZE, ImageLoader.NORMAL_TILE_FIT_SIZE, cellData.veilColor()));
                blend.setOpacity(0.5);
                return blend;
            });

            //---Binding the effect property of the cell to the veil---//
            tileGroup.effectProperty().bind(veilEffectOV);

            //---Instantiation of the mouse events---//
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

            //---Adding all the occupants and cancelled animals markers when a new cell is added---//
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
                        markerIV.setId(STR."marker_\{animal.id()}");
                        tileGroup.getChildren().add(markerIV);

                        //---making the marker visible when the animal is cancelled---//
                        ObservableValue<Boolean> markerVisibilityOV = gameStateOV.map(gameState -> gameState.board().cancelledAnimals().contains(animal));
                        markerIV.visibleProperty().bind(markerVisibilityOV);
                    }

                    //---Adding the occupants---//
                    for (Occupant occupant : newTile.potentialOccupants()) {
                        Node occupantNode = Icon.newFor(newTile.placer(), occupant.kind());
                        occupantNode.setId(STR."\{occupant.kind().toString().toLowerCase()}_\{occupant.zoneId()}");
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

    /**
     * A private record that has a unique purpose of regrouping the cell's data depending on multiple observable values
     * @param backgroundImage   the background image of the cell
     * @param rotation          the rotation of the cell
     * @param veilColor         the color of the veil that should be on the cell
     */
    private record CellData(Image backgroundImage, Rotation rotation, Color veilColor) {
        public static Map<Integer, Image> IMAGE_CACHE = new HashMap<>();
    }
}


