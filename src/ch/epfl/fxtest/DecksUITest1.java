package ch.epfl.fxtest;

import ch.epfl.chacun.*;
import ch.epfl.chacun.gui.DecksUI;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import java.util.function.Consumer;

public final class DecksUITest1 extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {



        // Dummy values for the missing arguments
        ObservableValue<Tile> nextTile = new SimpleObjectProperty<>(Tiles.TILES.get(54));
        ObservableValue<Integer> normalTileDeckSize0 = new SimpleIntegerProperty(5).asObject();
        ObservableValue<Integer> menhirTileDeckSize0 = new SimpleIntegerProperty(5).asObject();
        //Deux variantes
        //ObservableValue<String> nextTileTextO = new SimpleStringProperty("");
        ObservableValue<String> nextTileTextO = new SimpleStringProperty("Cliquez sur le pion ou la hutte que vous voulez placer, ou ici pour ne pas en placer.");

        Consumer<Occupant> occupantConsumer = occupant -> System.out.println(STR."Occupant: \{occupant}");

        var decksNode = DecksUI.create(nextTile, normalTileDeckSize0, menhirTileDeckSize0, nextTileTextO, occupantConsumer);
        var rootNode = new BorderPane(decksNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}