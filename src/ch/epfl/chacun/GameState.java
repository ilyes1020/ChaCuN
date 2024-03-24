package ch.epfl.chacun;

import java.util.Arrays;
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
     * enum representing the different actions that can be executed
     */


    public enum Action{
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
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

    /**
     * Returns the number of free occupants of the given kind belonging to the given player.
     * Free occupants are those not currently placed on the game board.
     * @param player The player color.
     * @param kind The kind of occupant.
     * @return The number of free occupants of the specified kind and belonging to the specified player.
     */
    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind){
        return Occupant.occupantsCount(kind) - board.occupantCount(player, kind);
    }

    /**
     * Returns the set of potential occupants of the last placed tile.
     * @return The set of potential occupants of the last placed tile.
     * @throws IllegalArgumentException If the board is empty.
     */
    public Set<Occupant> lastTilePotentialOccupants(){
        Preconditions.checkArgument(board.lastPlacedTile() != null);
        Preconditions.checkArgument(!board.equals(Board.EMPTY));

        PlacedTile lastPlacedTile = board.lastPlacedTile();
        Set<Occupant> lastTilePotentialOccupants = lastPlacedTile.potentialOccupants();

        Arrays.stream(Occupant.Kind.values()).forEach(kind -> {
            int playersFreeOccupantsCount = freeOccupantsCount(lastPlacedTile.placer(), kind);
            lastTilePotentialOccupants.removeIf(occupant -> playersFreeOccupantsCount == 0 && occupant.kind() == kind);
        });

        lastTilePotentialOccupants.removeIf(occupant -> {
            Zone potentialOccupantZone = lastPlacedTile.zoneWithId(occupant.zoneId());
            return switch (potentialOccupantZone) {
                case Zone.Forest forestZone -> board.forestArea(forestZone).isOccupied();
                case Zone.Meadow meadowZone -> board.meadowArea(meadowZone).isOccupied();
                case Zone.River riverZone -> switch (occupant.kind()) {
                    case PAWN -> board.riverArea(riverZone).isOccupied();
                    case HUT -> board.riverSystemArea(riverZone).isOccupied();
                };
                case Zone.Lake lakeZone -> board.riverSystemArea(lakeZone).isOccupied();
            };
        });
        return lastTilePotentialOccupants;
    }

    /**
     * Handles the transition from START_GAME to PLACE_TILE by placing the starting tile
     * in the center of the board and drawing the first normal tile from the tile deck.
     * @return The updated game state after placing the starting tile.
     * @throws IllegalArgumentException If the next action is not START_GAME.
     */
    public GameState withStartingTilePlaced() throws IllegalArgumentException {
        // TODO: Implement this method
        return null; // Placeholder
    }

    /**
     * Handles transitions from PLACE_TILE by adding the given tile to the board, assigning any points
     * obtained from placing the canoe or pit trap, and determining the next action.
     * @param tile The tile to be placed.
     * @return The updated game state after placing the given tile.
     * @throws IllegalArgumentException If the next action is not PLACE_TILE or if the given tile is already occupied.
     */
    public GameState withPlacedTile(PlacedTile tile) throws IllegalArgumentException {
        // TODO: Implement this method
        return null; // Placeholder
    }

    /**
     * Handles transitions from RETAKE_PAWN by removing the given occupant, unless it is null
     * indicating that the player does not wish to retake a pawn.
     * @param occupant The occupant to be removed.
     * @return The updated game state after removing the given occupant.
     * @throws IllegalArgumentException If the next action is not RETAKE_PAWN or if the given occupant is neither null nor a pawn.
     */
    public GameState withOccupantRemoved(Occupant occupant) throws IllegalArgumentException {
        // TODO: Implement this method
        return null; // Placeholder
    }

    /**
     * Handles transitions from OCCUPY_TILE by adding the given occupant to the last placed tile,
     * unless it is null indicating that the player does not wish to place an occupant.
     * @param occupant The occupant to be added.
     * @return The updated game state after adding the given occupant.
     * @throws IllegalArgumentException If the next action is not OCCUPY_TILE.
     */
    public GameState withNewOccupant(Occupant occupant) throws IllegalArgumentException {
        // TODO: Implement this method
        return null; // Placeholder
    }
}
