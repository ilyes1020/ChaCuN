package ch.epfl.chacun.gui;

import ch.epfl.chacun.GameState;
import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMaker;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Map;

/**
 * User interface representing the list of all player, their respective occupants, and their scored points.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class PlayersUI {
    private PlayersUI() {}

    /**
     * Creates a JavaFx Node representing the Player User Interface.
     *
     * @param gameStateOV An Observable value of the gameState.
     * @param textMaker   A text maker.
     * @return a JavaFx node of the user interface with all player, occupants and scored points.
     */
    public static Node create(ObservableValue<GameState> gameStateOV,
                              TextMaker textMaker) {

        //---PlayersVB initializing---//
        VBox playersVB = new VBox();
        playersVB.setId("players");
        playersVB.getStylesheets().add("players.css");

        List<PlayerColor> players = gameStateOV.getValue().players();

        ObservableValue<PlayerColor> currentPlayerOV = gameStateOV.map(GameState::currentPlayer);

        ObservableValue<Map<PlayerColor, Integer>> pointsMapOV = gameStateOV.map(o -> o.messageBoard().points());

        //---playerNode creation---//
        for (PlayerColor p : players) {

            //---auto updating text for points initialization---//
                ObservableValue<String> pointsTextOV =
                    pointsMapOV.map(pointsMap ->
                        " " + textMaker.playerName(p) + " : " + pointsMap.getOrDefault(p, 0)
                            + " " + (pointsMap.getOrDefault(p, 0) > 1 ? "points" : "point") + "\n");

            Text playerPointsText = new Text();
            playerPointsText.textProperty().bind(pointsTextOV);

            //---spacing between occupants initialization---//
            Text occupantsSpacing = new Text("   ");

            //---playerColorIndicator initialization---//
            Circle playerColorIndicator = new Circle(5, ColorMap.fillColor(p));

            //---Hut and Pawn SVGPaths initialization---//
            Node[] huts = new Node[3];
            Node[] pawns = new Node[5];
            for (int i = 0; i < 3; i++) {
                huts[i] = createIcon(gameStateOV, p, Occupant.Kind.HUT, i + 1);
            }
            for (int i = 0; i < 5; i++) {
                pawns[i] = createIcon(gameStateOV, p, Occupant.Kind.PAWN, i + 1);
            }

            //---PlayerNode initialization---//
            TextFlow playerNode = new TextFlow();
            playerNode.getChildren().addAll(playerColorIndicator, playerPointsText);
            playerNode.getChildren().addAll(huts);
            playerNode.getChildren().add(occupantsSpacing);
            playerNode.getChildren().addAll(pawns);
            playerNode.getStyleClass().add("player");

            //---currentPlayer indicator update setup---//
            currentPlayerOV.addListener((_, oldPlayer, newPlayer) -> {
                if (oldPlayer == p) {
                    playerNode.getStyleClass().remove("current");
                }
                if (newPlayer == p) {
                    playerNode.getStyleClass().add("current");
                }
            });

            //---adding playerNode to VBox children list---//
            playersVB.getChildren().add(playerNode);
        }
        return playersVB;
    }

    private static Node createIcon(ObservableValue<GameState> gameStateOV, PlayerColor p, Occupant.Kind kind, int count) {
        Node icon = Icon.newFor(p, kind);
        ObservableValue<Double> opacityOV = gameStateOV.map(o -> o.freeOccupantsCount(p, kind) >= count ? 1.0d : 0.1d);
        icon.opacityProperty().bind(opacityOV);
        return icon;
    }

}
