package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class PlayersUI {

    private PlayersUI() {}

    public static Node create(ObservableValue<GameState> gameStateOV, TextMaker textMaker){

        //---PlayersVB initializing---//
        VBox playersVB = new VBox();
        playersVB.setId("players");
        playersVB.getStylesheets().add("players.css");

        List<PlayerColor> players = new ArrayList<>(PlayerColor.ALL);
        players.removeIf(p -> textMaker.playerName(p) == null);

        ObservableValue<PlayerColor> currentPlayerOV = gameStateOV.map(GameState::currentPlayer);

        ObservableValue<Map<PlayerColor, Integer>> pointsMapOV = gameStateOV.map(o -> o.messageBoard().points());

        //---playerNode creation---//
        for (PlayerColor p: players){

            //---auto updating text for points initialization---//
            ObservableValue<String> pointsTextOV =
                    pointsMapOV.map(pointsMap -> STR." \{textMaker.playerName(p)} : \{pointsMap.getOrDefault(p, 0)} \{pointsMap.getOrDefault(p,0) > 1 ? "points" : "point"}\n");

            Text playerPointsText = new Text();
            playerPointsText.textProperty().bind(pointsTextOV);

            //---spacing between occupants initialization---//
            Text occupantsSpacing = new Text("   ");

            //---playerColorIndicator initialization---//
            Circle playerColorIndicator = new Circle(5, ColorMap.fillColor(p));

            //---Hut and Pawn SVGPaths initialization---//
            SVGPath hut1 = (SVGPath) Icon.newFor(p, Occupant.Kind.HUT);
            SVGPath hut2 = (SVGPath) Icon.newFor(p, Occupant.Kind.HUT);
            SVGPath hut3 = (SVGPath) Icon.newFor(p, Occupant.Kind.HUT);

            SVGPath pawn1 = (SVGPath) Icon.newFor(p, Occupant.Kind.PAWN);
            SVGPath pawn2 = (SVGPath) Icon.newFor(p, Occupant.Kind.PAWN);
            SVGPath pawn3 = (SVGPath) Icon.newFor(p, Occupant.Kind.PAWN);
            SVGPath pawn4 = (SVGPath) Icon.newFor(p, Occupant.Kind.PAWN);
            SVGPath pawn5 = (SVGPath) Icon.newFor(p, Occupant.Kind.PAWN);

            //---Hut and Pawn icon opacity update setup---//
            ObservableValue<Double> hut1OpacityOV = gameStateOV.map(o -> o.freeOccupantsCount(p, Occupant.Kind.HUT) >= 1 ? 1.0d : 0.1d);
            ObservableValue<Double> hut2OpacityOV = gameStateOV.map(o -> o.freeOccupantsCount(p, Occupant.Kind.HUT) >= 2 ? 1.0d : 0.1d);
            ObservableValue<Double> hut3OpacityOV = gameStateOV.map(o -> o.freeOccupantsCount(p, Occupant.Kind.HUT) >= 3 ? 1.0d : 0.1d);

            ObservableValue<Double> pawn1OpacityOV = gameStateOV.map(o -> o.freeOccupantsCount(p, Occupant.Kind.PAWN) >= 1 ? 1.0d : 0.1d);
            ObservableValue<Double> pawn2OpacityOV = gameStateOV.map(o -> o.freeOccupantsCount(p, Occupant.Kind.PAWN) >= 2 ? 1.0d : 0.1d);
            ObservableValue<Double> pawn3OpacityOV = gameStateOV.map(o -> o.freeOccupantsCount(p, Occupant.Kind.PAWN) >= 3 ? 1.0d : 0.1d);
            ObservableValue<Double> pawn4OpacityOV = gameStateOV.map(o -> o.freeOccupantsCount(p, Occupant.Kind.PAWN) >= 4 ? 1.0d : 0.1d);
            ObservableValue<Double> pawn5OpacityOV = gameStateOV.map(o -> o.freeOccupantsCount(p, Occupant.Kind.PAWN) >= 5 ? 1.0d : 0.1d);

            hut1.opacityProperty().bind(hut1OpacityOV);
            hut2.opacityProperty().bind(hut2OpacityOV);
            hut3.opacityProperty().bind(hut3OpacityOV);

            pawn1.opacityProperty().bind(pawn1OpacityOV);
            pawn2.opacityProperty().bind(pawn2OpacityOV);
            pawn3.opacityProperty().bind(pawn3OpacityOV);
            pawn4.opacityProperty().bind(pawn4OpacityOV);
            pawn5.opacityProperty().bind(pawn5OpacityOV);

            //---PlayerNode initialization---//
            TextFlow playerNode = new TextFlow(
                    playerColorIndicator,
                    playerPointsText,
                    hut1, hut2, hut3,
                    occupantsSpacing,
                    pawn1, pawn2, pawn3, pawn4, pawn5);
            playerNode.getStyleClass().add("player");

            //---currentPlayer indicator update setup---//
            currentPlayerOV.addListener((o, oldPlayer, newPlayer) -> {
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
}
