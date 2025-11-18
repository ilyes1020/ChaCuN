package ch.epfl.chacun.gui;

import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Set;

/**
 * User interface representing the message board of the game.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public final class MessageBoardUI {
    private MessageBoardUI() {}

    /**
     * Creates a JavaFx Node representing the Message Board User Interface.
     *
     * @param messagesOV           An Observable value of the messages
     * @param highlightedTilesIdOP An Object property of the tileIds
     * @return a JavaFx Node representing all the messages sent in the game.
     */

    public static Node create(ObservableValue<List<MessageBoard.Message>> messagesOV,
                              ObjectProperty<Set<Integer>> highlightedTilesIdOP) {
        //---VBox initialization---//
        VBox messagesVB = new VBox();

        //---ScrollPane initialization---//
        ScrollPane messagesSP = new ScrollPane(messagesVB);
        messagesSP.setId("message-board");
        messagesSP.getStylesheets().add("message-board.css");

        //---messages updating setup---//
        messagesOV.addListener((ignored, oldMessages, newMessages) -> {

            //---adding a new Text containing the newly added message---//
            for (MessageBoard.Message newMessage : newMessages
                    .stream()
                    .filter(m -> !oldMessages.contains(m))
                    .toList()) {

                //---new message Text initialization---//
                Text newMessageText = new Text(newMessage.text());

                //---fixing the max width of the Text---//
                newMessageText.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);

                //---updating the tileIds list when the mouse enter the text node---//
                newMessageText.setOnMouseEntered(e -> highlightedTilesIdOP.setValue(newMessage.tileIds()));
                newMessageText.setOnMouseExited(e -> highlightedTilesIdOP.setValue(Set.of()));

                //---adding the Text node to the VBox---//
                messagesVB.getChildren().add(newMessageText);
            }

            //---scrolling the scrollPane to the bottom---//
            messagesSP.layout();
            messagesSP.setVvalue(1);
        });
        return messagesSP;
    }
}
