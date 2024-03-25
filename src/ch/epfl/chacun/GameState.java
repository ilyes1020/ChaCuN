package ch.epfl.chacun;

import java.util.*;

/**
 * Record representing the game state
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 *
 * @param players the list of players in the game.
 * @param tileDecks the three different tile decks.
 * @param tileToPlace the eventual tile to place (null if no tile to place).
 * @param board the board of the game.
 * @param nextAction the next action to execute.
 * @param messageBoard the message board of the game.
 */

public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace, Board board, Action nextAction, MessageBoard messageBoard) {

    /**
     * Compact constructor of {@code GameState}.
     * @param players the list of players in the game.
     * @param tileDecks the three different tile decks.
     * @param tileToPlace the eventual tile to place (null if no tile to place).
     * @param board the board of the game.
     * @param nextAction the next action to execute.
     * @param messageBoard the message board of the game.
     * @throws IllegalArgumentException if the number of players is less than 2
     * or if the tile to place is null and the next action is PLACE_TILE
     * or if neither the tile to place is null, nor the next action is PLACE_TILE
     * or if the tile decks, the board, the next action or the message board is null.
     */
    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(tileToPlace == null ^ nextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tileDecks != null && board != null && nextAction != null && messageBoard != null);
        players = List.copyOf(players);
    }

    /**
     * Creates the initial game state.
     * @param players the given list of players in the game.
     * @param tileDecks the given three different tile decks.
     * @param textMaker the given text maker.
     * @return the initial game state.
     */
    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker){
        return new GameState(players, tileDecks, null, Board.EMPTY, Action.START_GAME, new MessageBoard(textMaker, List.of()));
    }

    /**
     * Enum representing the different actions that can be executed.
     */


    public enum Action{
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
    }

    /**
     * Returns the current player or null if the next action is START_GAME or END_GAME.
     * @return the current player or null if the next action is START_GAME or END_GAME.
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
    public GameState withStartingTilePlaced(){
        Preconditions.checkArgument(nextAction == Action.START_GAME);

        PlacedTile placedStartTile = new PlacedTile(tileDecks.topTile(Tile.Kind.START), null, Rotation.NONE, new Pos(0, 0));
        Board updatedBoard = board.withNewTile(placedStartTile);

        TileDecks updatedTileDecks = tileDecks.withTopTileDrawn(Tile.Kind.START);
        Tile updatedTileToPlace = updatedTileDecks.topTile(Tile.Kind.NORMAL);
        updatedTileDecks = updatedTileDecks.withTopTileDrawn(Tile.Kind.NORMAL);

        Action updatedNextAction = Action.PLACE_TILE;

        return new GameState(
                  players
                , updatedTileDecks
                , updatedTileToPlace
                , updatedBoard
                , updatedNextAction
                , messageBoard);
    }

    /**
     * Handles transitions from PLACE_TILE by adding the given tile to the board, assigning any points
     * obtained from placing the canoe or pit trap, and determining the next action.
     * @param tile The tile to be placed.
     * @return The updated game state after placing the given tile.
     * @throws IllegalArgumentException If the next action is not PLACE_TILE or if the given tile is already occupied.
     */
    public GameState withPlacedTile(PlacedTile tile){
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tile.occupant() == null);

        Board updatedBoard = board.withNewTile(tile);

        Action updatedNextAction = Action.OCCUPY_TILE;

        TileDecks updatedTileDecks = tileDecks;

        Tile updatedTileToPlace = tileToPlace;

        MessageBoard updatedMessageBoard = messageBoard;

        if (tile.tile().kind() == Tile.Kind.MENHIR){

            updatedTileDecks = updatedTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, board::couldPlaceTile);
            updatedTileToPlace = updatedTileDecks.topTile(Tile.Kind.NORMAL);
            updatedTileDecks = updatedTileDecks.withTopTileDrawn(Tile.Kind.NORMAL);

            Zone.SpecialPower specialPower = null;
            Zone zoneWithSpecialPower = null;
            for (Zone zone : tile.tile().zones()) {
                if (zone.specialPower() != null) {
                    specialPower = zone.specialPower();
                    zoneWithSpecialPower = zone;}
            }

            switch (specialPower){
                case SHAMAN -> updatedNextAction  = Action.RETAKE_PAWN;
                case LOGBOAT -> updatedMessageBoard = updatedMessageBoard.withScoredLogboat(tile.placer(), board.riverSystemArea((Zone.Water) zoneWithSpecialPower));
                case HUNTING_TRAP -> {
                    //TODO: vraiment suspecte cette partie
                    Set<Animal> cancelledAnimals = new HashSet<>(board.cancelledAnimals());
                    Set<Animal> notCancelledYetAnimals = Area.animals(board.adjacentMeadow(tile.pos(), (Zone.Meadow) zoneWithSpecialPower), board.cancelledAnimals());

                    Map<Animal.Kind, Integer> animalCountMap = new HashMap<>();
                    for (Animal animal : notCancelledYetAnimals) {
                        animalCountMap.merge(animal.kind(), 1, Integer::sum);
                    }


                    Iterator<Animal> notCancelledYetAnimalIterator = notCancelledYetAnimals.iterator();
                    int smilodonCount = animalCountMap.getOrDefault(Animal.Kind.TIGER, 0);
                    int deerCount = animalCountMap.getOrDefault(Animal.Kind.DEER, 0);
                    int cancelledDeerCount = 0;
                    while (notCancelledYetAnimalIterator.hasNext() && cancelledDeerCount < smilodonCount && deerCount > 0) {

                        Animal notCancelledYetAnimal = notCancelledYetAnimalIterator.next();

                        if (notCancelledYetAnimal.kind() == Animal.Kind.DEER) {
                            cancelledAnimals.add(notCancelledYetAnimal);
                            cancelledDeerCount++;
                            deerCount--;
                        }
                    }
                    updatedMessageBoard = updatedMessageBoard.withScoredHuntingTrap(tile.placer(), board.adjacentMeadow(tile.pos(), (Zone.Meadow) zoneWithSpecialPower));
                }
                case null, default -> {}
            }
        } else {
            for (Zone zone : tile.tile().zones()) {
                if (zone instanceof Zone.Forest forestZone) {
                    Area<Zone.Forest> forestArea = board.forestArea(forestZone);
                    if (forestArea.isClosed() && Area.hasMenhir(forestArea)) {
                        updatedTileDecks = tileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, board::couldPlaceTile);
                        updatedTileToPlace = updatedTileDecks.topTile(Tile.Kind.MENHIR);
                        updatedNextAction = Action.PLACE_TILE;
                        updatedMessageBoard = messageBoard.withClosedForestWithMenhir(tile.placer(), forestArea);
                    } else {
                        updatedTileDecks = tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, board::couldPlaceTile);
                        updatedTileToPlace = updatedTileDecks.topTile(Tile.Kind.NORMAL);
                        updatedTileDecks = updatedTileDecks.withTopTileDrawn(Tile.Kind.NORMAL);
                    }
                }
            }
        }
        return new GameState(
                  players
                , updatedTileDecks
                , updatedTileToPlace
                , updatedBoard
                , updatedNextAction
                , updatedMessageBoard);
    }

    /**
     * Handles transitions from RETAKE_PAWN by removing the given occupant, unless it is null
     * which indicates that the player does not wish to retake a pawn.
     * @param occupant The occupant to be removed.
     * @return The updated game state after removing the given occupant.
     * @throws IllegalArgumentException If the next action is not RETAKE_PAWN or if the given occupant is neither null nor a pawn.
     */
    public GameState withOccupantRemoved(Occupant occupant){
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
    public GameState withNewOccupant(Occupant occupant){
        // TODO: Implement this method
        return null; // Placeholder
    }
}
