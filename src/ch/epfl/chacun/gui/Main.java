package ch.epfl.chacun.gui;

import ch.epfl.chacun.GameState;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        List<String> playersName = getParameters().getUnnamed();
        Map<String,String> seedMap = getParameters().getNamed();

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        root.centerProperty().setValue(BoardUI.create(12
        , GameState.initial()));







        primaryStage.setTitle("ChaCun");
        primaryStage.setWidth(1440);
        primaryStage.setHeight(1080);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
