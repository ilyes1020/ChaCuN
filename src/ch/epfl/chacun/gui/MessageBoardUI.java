package ch.epfl.chacun.gui;

import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javafx.application.Platform.runLater;

/**
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class MessageBoardUI {

    private MessageBoardUI() {}

    public static Node create(ObservableValue<List<MessageBoard.Message>> messagesOV, ObjectProperty<Set<Integer>> tileIdsOP){
        //---VBox initialization---//
        VBox messagesVB = new VBox();

        //---ScrollPane initialization---//
        ScrollPane messagesSP = new ScrollPane(messagesVB);
        messagesSP.setId("message-board");
        messagesSP.getStylesheets().add("message-board.css");

        //---messages updating setup---//
        messagesOV.addListener((o, oldMessagesOV, newMessagesOV) -> {

            //---adding a new Text containing the newly added message---//
            for (MessageBoard.Message newMessage : newMessagesOV.stream().filter(m -> !oldMessagesOV.contains(m)).collect(Collectors.toSet())) {

                //---new message Text initialization---//
                Text newMessageText = new Text(newMessage.text());

                //---fixing the max width of the Text---//
                newMessageText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);

                //---updating the tileIds list when the mouse enter the text node---//
                newMessageText.setOnMouseEntered((mouseEvent) -> tileIdsOP.setValue(newMessage.tileIds()));
                newMessageText.setOnMouseExited((mouseEvent) -> tileIdsOP.setValue(Set.of()));

                //---adding the Text node to the VBox---//
                messagesVB.getChildren().add(newMessageText);
            }

            //---scrolling the scrollPane to the bottom---//
            runLater(() -> messagesSP.setVvalue(1));
        });
        return messagesSP;
    }
}
