package ch.epfl.fxtest;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import ch.epfl.chacun.TileDecks;
import ch.epfl.chacun.Tiles;
import ch.epfl.chacun.gui.DecksUI;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.stream.Collectors;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class DecksUITest extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {

        var tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));

        var tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.NORMAL),
                        tilesByKind.get(Tile.Kind.MENHIR));

        var tileToPlace =
                tileDecks.topTile(Tile.Kind.START);

        var nbOfRemainingNormalTiles =
                tileDecks.deckSize(Tile.Kind.NORMAL);
        var nbOfRemainingMenhirTiles =
                tileDecks.deckSize(Tile.Kind.MENHIR);

        var textToDisplay = "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";



        var decksNode = DecksUI.create(new SimpleObjectProperty<>(tileToPlace),
                new SimpleObjectProperty<>(nbOfRemainingNormalTiles),
                new SimpleObjectProperty<>(nbOfRemainingMenhirTiles),
                new SimpleObjectProperty<>(textToDisplay),
                (Occupant o) -> {});

        var rootNode = new BorderPane(decksNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}
