package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.function.Consumer;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The main class for the ChaCuN game application.
 * This class extends the {@link Application} class from JavaFX, providing the entry point for the JavaFX application.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class Main extends Application {

    /**
     * The main method for launching the application.
     * This method launches the JavaFX application by calling the {@link Application#launch(String...)} method.
     *
     * @param args the command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main entry point for the JavaFX application.
     * This method is called by the JavaFX runtime to start the application.
     *
     * @param primaryStage the primary stage for the application, onto which the application scene can be set.
     * @throws Exception if an error occurs during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        List<String> playerNames = getParameters().getUnnamed();
        Map<String,String> seedMap = getParameters().getNamed();

        //-----INITIALIZING THE GAMESTATE-----//
        long seed;
        List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        //Checking for the validity of the arguments and shuffle with the seed if there is one provided
        if (playerNames.size() < 2 || playerNames.size() > 5) {
            throw new IllegalArgumentException("Invalid player number.");
        }
        if (!seedMap.isEmpty()){
            try {
                seed = Long.parseUnsignedLong(seedMap.get("seed"));
                Collections.shuffle(tiles, RandomGeneratorFactory.getDefault().create(seed));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid seed format.");
            }
        } else {
            Collections.shuffle(tiles, RandomGeneratorFactory.getDefault().create());
        }

        //instantiating a mapping from playerColors to playerNames
        Map<PlayerColor, String> colorAndNameMap = IntStream.range(0, playerNames.size())
                .boxed()
                .collect(Collectors.toMap(
                        PlayerColor.ALL::get,
                        playerNames::get,
                        (a, b) -> a,        // merge function, in case of duplicate keys (should never happen as keys are different indices)
                        LinkedHashMap::new  // linkedHashMap to maintain insertion order
                ));

        //instantiating playerColor list
        List<PlayerColor> playerColors = new ArrayList<>(colorAndNameMap.keySet());

        //instantiating tileDecks
        Map<Tile.Kind, List<Tile>> tileDeckMap = tiles.stream().collect(Collectors.groupingBy(Tile::kind));
        TileDecks tileDecks = new TileDecks(tileDeckMap.get(Tile.Kind.START), tileDeckMap.get(Tile.Kind.NORMAL), tileDeckMap.getOrDefault(Tile.Kind.MENHIR, List.of()));

        //instantiating a French textMaker
        TextMakerFr textMakerFr = new TextMakerFr(colorAndNameMap);

        //instantiating the initial GameState
        GameState gameState = GameState.initial(playerColors, tileDecks, textMakerFr);
        //-----END OF GAMESTATE'S INITIALIZATION-----//


        //---UI arguments instantiations---//
        ObjectProperty<List<String>> actionListOV = new SimpleObjectProperty<>(List.of());

        ObjectProperty<GameState> gameStateOV = new SimpleObjectProperty<>(gameState);

        ObjectProperty<Rotation> rotationOV = new SimpleObjectProperty<>(Rotation.NONE);

        ObservableValue<Set<Occupant>> visibleOccupantsOV = gameStateOV.map(g ->
                g.nextAction() == GameState.Action.OCCUPY_TILE ?
                Stream.concat(g.lastTilePotentialOccupants().stream(), g.board().occupants().stream()).collect(Collectors.toSet()) :
                g.board().occupants());

        ObjectProperty<Set<Integer>> highlightedTilesIdOP = new SimpleObjectProperty<>(Set.of());

        ObservableValue<List<MessageBoard.Message>> messageOV = gameStateOV.map(g -> g.messageBoard().messages());

        Consumer<Rotation> rotationHandler = rotation -> rotationOV.setValue(rotationOV.getValue().add(rotation));

        Consumer<Pos> placeTileHandler = pos -> {
            GameState currentGameState = gameStateOV.getValue();
            PlacedTile placedTileToPlace = new PlacedTile(currentGameState.tileToPlace(), currentGameState.currentPlayer(), rotationOV.getValue(), pos);
            if (currentGameState.board().canAddTile(placedTileToPlace)) {
                ActionEncoder.StateAction stateAction = ActionEncoder.withPlacedTile(currentGameState, placedTileToPlace);

                updateState(actionListOV, stateAction, gameStateOV);
                rotationOV.setValue(Rotation.NONE);
            }
        };

        Consumer<Occupant> occupantSelectionHandler = occupant -> {
            GameState currentGameState = gameStateOV.getValue();
            if (currentGameState.nextAction() == GameState.Action.OCCUPY_TILE) {
                ActionEncoder.StateAction stateActionAddedOccupant = ActionEncoder.withNewOccupant(currentGameState, occupant);
                updateState(actionListOV, stateActionAddedOccupant, gameStateOV);
            } else if (currentGameState.nextAction() == GameState.Action.RETAKE_PAWN
                       &&
                       currentGameState.currentPlayer() == currentGameState.board().tileWithId(Zone.tileId(occupant.zoneId())).placer()){
                ActionEncoder.StateAction stateActionRemovedPawn = ActionEncoder.withOccupantRemoved(currentGameState, occupant);
                updateState(actionListOV, stateActionRemovedPawn, gameStateOV);
            }
        };

        ObservableValue<Tile> tileToPlaceOV = gameStateOV.map(GameState::tileToPlace);

        ObservableValue<Integer> nbOfRemainingNormalTilesOV = gameStateOV.map(g -> g.tileDecks().normalTiles().size());

        ObservableValue<Integer> nbOfRemainingMenhirTilesOV = gameStateOV.map(g -> g.tileDecks().menhirTiles().size());

        ObservableValue<String> textToDisplayOV = gameStateOV.map(g -> {
            if (g.nextAction() == GameState.Action.OCCUPY_TILE)
                return textMakerFr.clickToOccupy();
            else if (g.nextAction() == GameState.Action.RETAKE_PAWN)
                return textMakerFr.clickToUnoccupy();
            else
                return "";
        });

        Consumer<Occupant> clickableText = occupant -> {
            GameState currentGameState = gameStateOV.getValue();
            if (gameStateOV.getValue().nextAction() == GameState.Action.OCCUPY_TILE) {
                ActionEncoder.StateAction stateActionAddedNoOccupant = ActionEncoder.withNewOccupant(currentGameState, null);
                actionListOV.setValue(Stream.concat(actionListOV.getValue().stream(), Stream.of(stateActionAddedNoOccupant.actionB32())).toList());

                gameStateOV.setValue(gameStateOV.getValue().withNewOccupant(occupant));
            } else if (gameStateOV.getValue().nextAction() == GameState.Action.RETAKE_PAWN){
                ActionEncoder.StateAction stateActionRemovedNoPawn = ActionEncoder.withOccupantRemoved(currentGameState, null);
                actionListOV.setValue(Stream.concat(actionListOV.getValue().stream(), Stream.of(stateActionRemovedNoPawn.actionB32())).toList());
                gameStateOV.setValue(gameStateOV.getValue().withOccupantRemoved(occupant));
            }
        };

        Consumer<String> eventHandler = s -> {
            ActionEncoder.StateAction decodedStateAction = ActionEncoder.decodeAndApply(gameStateOV.getValue(), s);
            if (decodedStateAction != null){
                updateState(actionListOV, decodedStateAction, gameStateOV);
            }
        };
        //---End of UI arguments instantiations---//

        //-----STAGE CREATION-----//
        BorderPane rootBP = new BorderPane();
        BorderPane infosBP = new BorderPane();

        rootBP.centerProperty().set(BoardUI.create(Board.REACH,
                                                   gameStateOV,
                                                   rotationOV,
                                                   visibleOccupantsOV,
                                                   highlightedTilesIdOP,
                                                   rotationHandler,
                                                   placeTileHandler,
                                                   occupantSelectionHandler));
        rootBP.rightProperty().set(infosBP);

        VBox actionsAndDecksVB = new VBox();

        infosBP.topProperty().set(PlayersUI.create(gameStateOV, textMakerFr));
        infosBP.centerProperty().set(MessageBoardUI.create(messageOV, highlightedTilesIdOP));
        infosBP.bottomProperty().set(actionsAndDecksVB);

        actionsAndDecksVB.getChildren().add(ActionsUI.create(actionListOV, eventHandler));
        actionsAndDecksVB.getChildren().add(DecksUI.create(tileToPlaceOV,
                                                           nbOfRemainingNormalTilesOV,
                                                           nbOfRemainingMenhirTilesOV,
                                                           textToDisplayOV,
                                                           clickableText));

        Scene scene = new Scene(rootBP);

        // FEATURE: Idle mode
        // press ctrl to just advance the game state by placing a tile/occ anywhere it fits
        rootBP.setOnKeyPressed(e -> {
        if (e.getCode() != KeyCode.CONTROL) {
            return;
        }
        GameState state = gameStateOV.get();
        switch (state.nextAction()) {
            case PLACE_TILE -> {
                for (Pos pos : state.board().insertionPositions()
                    .stream()
                    .sorted(Comparator.comparing((Pos p) -> p.x() * p.x() + p.y() * p.y()))
                    .toList()) {
                    for (Rotation _ : Rotation.ALL) {
                        if (state.tileToPlace() != null && state.board().canAddTile(new PlacedTile(state.tileToPlace(),
                            state.currentPlayer(), rotationOV.get(),
                            pos))) {
                            placeTileHandler.accept(pos);
                        return;
                        }
                        rotationHandler.accept(Rotation.RIGHT);
                    }
                }
            }
            case OCCUPY_TILE -> updateState(actionListOV, ActionEncoder.withNewOccupant(state
                            ,state.lastTilePotentialOccupants()
                    .stream()
                    .findAny()
                    .orElse(null))
                    , gameStateOV);

            case RETAKE_PAWN -> updateState(actionListOV, ActionEncoder.withOccupantRemoved(state
                            , state.board()
                    .occupants()
                    .stream()
                    .filter(o -> o.kind().equals(Occupant.Kind.PAWN))
                    .filter(o -> state.board().tileWithId(Zone.tileId(o.zoneId()))                                                                   .placer().equals(state.currentPlayer()))
                    .findAny()
                    .orElse(null))
                    , gameStateOV);
        }});

        //placing the starting tile
        gameStateOV.setValue(gameStateOV.getValue().withStartingTilePlaced());

        primaryStage.setScene(scene);
        primaryStage.setTitle("ChaCuN");
        primaryStage.setWidth(1440);
        primaryStage.setHeight(1080);
        primaryStage.show();
    }

    private static void updateState(ObjectProperty<List<String>> actionListOV, ActionEncoder.StateAction stateAction, ObjectProperty<GameState> gameStateOV) {
        actionListOV.setValue(Stream.concat(actionListOV.getValue().stream(), Stream.of(stateAction.actionB32())).toList());
        gameStateOV.setValue(stateAction.gameState());
    }

}
