package ch.epfl.chacun;

import java.util.List;
import java.util.Set;

/**
 * record representing the game state
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 *
 * @param players the list of players in the game
 * @param tileDecks the three different tile decks
 * @param tileToPlace the eventual tile to place (null if no tile to place)
 * @param board the board of the game
 * @param nextAction the next action to execute
 * @param messageBoard the message board of the game
 */

public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace, Board board, Action nextAction, MessageBoard messageBoard) {

    /**
     * compact constructor
     * @throws IllegalArgumentException if the number of players is less than 2
     * or if the tile to place is null and the next action is PLACE_TILE
     * or if neither the tile to place is null, nor the next action is PLACE_TILE
     * or if the tile decks, the board, the next action or the message board is null
     */
    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(tileToPlace == null ^ nextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tileDecks != null && board != null && nextAction != null && messageBoard != null);
        players = List.copyOf(players);
    }

    /**
     * creates the initial game state
     * @param players the given list of players in the game
     * @param tileDecks the given three different tile decks
     * @param textMaker the given text maker
     * @return the initial game state
     */
    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker){
        return new GameState(players, tileDecks, null, Board.EMPTY, Action.START_GAME, new MessageBoard(textMaker, List.of()));
    }

    /**
     * return the current player or null if the next action is START_GAME or END_GAME
     * @return the current player or null if the next action is START_GAME or END_GAME
     */
    public PlayerColor currentPlayer(){
        if (nextAction == Action.START_GAME || nextAction == Action.END_GAME){
            return null;
        }
        return players.getFirst();
    }

    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind){
        return board.occupantCount(player, kind) - board.occupants().size(); //pas sur
    }

    public Set<Occupant> lastTilePotentialOccupants(){
        Preconditions.checkArgument(board.lastPlacedTile() != null);
        Tile lastTile = board.lastPlacedTile().tile();
        Set<Occupant> allOccupants = board.lastPlacedTile().potentialOccupants();

        for (Zone zone : board.lastPlacedTile().tile().zones()) {
            switch (zone){
                case Zone.Forest forest-> allOccupants.remove(board.forestArea(forest));
                case Zone.Meadow meadow-> allOccupants.remove(board.meadowArea(meadow));
                case Zone.River river-> allOccupants.remove(board.riverArea(river));
                default -> {
                    return allOccupants;
                }
            }
        }
        return allOccupants; //apparement pas
    }

    /**
     * enum representing the different actions that can be executed
     */


    public enum Action{
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
    }
}
