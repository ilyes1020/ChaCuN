package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * User interface representing the last actions and allows the player to enter actions to perform.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding(379902)
 */
public final class ActionsUI {
    private ActionsUI(){}


    /**
     * Creates a JavaFx Node representing the Actions User Interface.
     *
     * @param actionListOV   An Observable value of the history of all actions.
     * @param eventHandler   A handler of actions performance.
     * @return a JavaFx node of the user interface with the history of the 4 last actions and a text field to enter the action to perform.
     */
    public static Node create(ObservableValue<List<String>> actionListOV, Consumer<String> eventHandler){

        HBox actionsHB = new HBox();
        actionsHB.getStylesheets().add("actions.css");


        //---Action history text initialization---//
        Text lastFourActionsB32 = new Text();
        actionsHB.getChildren().add(lastFourActionsB32);
        ObservableValue<String> lastFourActionsToStringOV = actionListOV.map(list -> {
            StringBuilder sb = new StringBuilder();
            for (int i = list.size() - 4 ; i < list.size(); i++) {
                sb.append(STR."\{i + 1}:\{list.get(i)}");
                if (i != list.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        });
        lastFourActionsB32.textProperty().bind(lastFourActionsToStringOV);

        //---TextField initialization---//
        TextField actionTextField = new TextField();
        actionTextField.setId("action-field");
        actionsHB.getChildren().add(actionTextField);

        //---TextField textFormatter---//
        actionTextField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().chars()
                    .mapToObj(c -> (char) c)
                    .map(Character::toUpperCase)
                    .filter(c -> Base32.ALPHABET.contains(String.valueOf(c)))
                    .map(String::valueOf)
                    .collect(Collectors.joining()));
            return change;
            })
        );

        //---The event to enter actions in the TextField---//
        actionTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                eventHandler.accept(actionTextField.getText());
                actionTextField.clear();
            }
        });

        return actionsHB;
    }
}
