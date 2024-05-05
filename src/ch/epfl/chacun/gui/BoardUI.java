package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
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

        //---Background image---//
        WritableImage emptyTileImage = new WritableImage(1, 1);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));

        // Iterate over the x and y indices of the board
        for (int y = 0 ; y <= 2 * reach ; y++) {
            for (int x = 0 ; x <= 2 * reach ; x++) {
                final Pos currentPos = new Pos(x - reach,y - reach);

                Group tileGroup = new Group();
                boardGP.add(tileGroup, x, y);

                ObservableValue<Integer> groupRotationOV = rotationOV.map(Rotation::degreesCW);
                tileGroup.rotateProperty().bind(groupRotationOV);

                tileGroup.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY){
                        if (event.isAltDown()) rotationHandler.accept(Rotation.RIGHT);
                        else rotationHandler.accept(Rotation.LEFT);
                    }
                });

                tileGroup.getChildren().add(new ImageView(emptyTileImage));

                ObservableValue<PlacedTile> placedTileOV = gameStateOV.map(m -> m.board().tileAt(currentPos));
                
                placedTileOV.addListener((o, oldTile, newTile) -> {
                    if (newTile != null) {
                        //---Adding the image of the Tile---//
                        CellData.IMAGE_CACHE.putIfAbsent(newTile.id(), ImageLoader.normalImageForTile(newTile.id()));
                        tileGroup.getChildren().add(new ImageView(CellData.IMAGE_CACHE.get(newTile.id())));

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
                            ObservableValue<Integer> occupantRotationOV = rotationOV.map(rotation -> rotation.negated().degreesCW());
                            occupantNode.rotateProperty().bind(occupantRotationOV);

                            //---making the occupant clickable---//
                            occupantNode.setOnMouseClicked(event -> {
                                if (event.getButton() == MouseButton.PRIMARY) occupantSelectHandler.accept(occupant);
                            });
                        }
                    }
                });

                ColorInput veilImage = new ColorInput(x, y, ImageLoader.LARGE_TILE_FIT_SIZE, ImageLoader.LARGE_TILE_FIT_SIZE, Color.WHITE);
                Blend veilEffect = new Blend(BlendMode.SRC_OVER, null, veilImage);

                veilEffect.setOpacity(0.5);

//                tileGroup.effectProperty().bind();
            }
        }
        return null;
    }

    private record CellData(Image backgroundImage, Rotation rotation, Color veilColor) {
        public static Map<Integer, Image> IMAGE_CACHE = new HashMap<>();
    }
}


