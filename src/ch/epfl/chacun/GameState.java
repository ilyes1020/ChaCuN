package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

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
     *
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
        Objects.requireNonNull(tileDecks);
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextAction);
        Objects.requireNonNull(messageBoard);
        players = List.copyOf(players);
    }

    /**
     * Creates the initial game state.
     *
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
     *
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
     *
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
     *
     * @return The set of potential occupants of the last placed tile.
     * @throws IllegalArgumentException If the board is empty.
     */
    public Set<Occupant> lastTilePotentialOccupants(){
        Preconditions.checkArgument(board.lastPlacedTile() != null);
        Preconditions.checkArgument(!board.equals(Board.EMPTY));

        PlacedTile lastPlacedTile = board.lastPlacedTile();
        Set<Occupant> lastTilePotentialOccupants = lastPlacedTile.potentialOccupants();

        Arrays.stream(Occupant.Kind.values()).forEach(kind -> {
            if (freeOccupantsCount(lastPlacedTile.placer(), kind) == 0)
                lastTilePotentialOccupants.removeIf(occupant -> occupant.kind() == kind);
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
     *
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
     *
     * @param tile The tile to be placed.
     * @return The updated game state after placing the given tile.
     * @throws IllegalArgumentException If the next action is not PLACE_TILE or if the given tile is already occupied.
     */
    public GameState withPlacedTile(PlacedTile tile){
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE);
        Preconditions.checkArgument(tile.occupant() == null);

        Board updatedBoard = board.withNewTile(tile);

        MessageBoard updatedMessageBoard = messageBoard;

        if (tile.tile().kind() == Tile.Kind.MENHIR){

            Zone zoneWithSpecialPower = tile.specialPowerZone();
            Zone.SpecialPower specialPower = null;
            if (zoneWithSpecialPower != null) {
                specialPower = zoneWithSpecialPower.specialPower();
            }

            switch (specialPower){
                case SHAMAN -> {
                    if (freeOccupantsCount(tile.placer(), Occupant.Kind.PAWN) != Occupant.occupantsCount(Occupant.Kind.PAWN)) {
                        return new GameState(
                                  players
                                , tileDecks
                                , null
                                , updatedBoard
                                , Action.RETAKE_PAWN
                                , messageBoard);
                    }
                }
                case LOGBOAT -> updatedMessageBoard = updatedMessageBoard.withScoredLogboat(tile.placer(), updatedBoard.riverSystemArea((Zone.Water) zoneWithSpecialPower));
                case HUNTING_TRAP -> {
                    Area<Zone.Meadow> adjacentMeadow = updatedBoard.adjacentMeadow(tile.pos(), (Zone.Meadow) zoneWithSpecialPower);
                    Set<Animal> notCancelledYetAnimals = Area.animals(adjacentMeadow, updatedBoard.cancelledAnimals());
                    Set<Animal> newlyCancelledAnimals = new HashSet<>(updatedBoard.cancelledAnimals());

                    Map<Animal.Kind, Integer> animalCountMap = countAnimals(notCancelledYetAnimals);

                    int smilodonCount = animalCountMap.getOrDefault(Animal.Kind.TIGER, 0);
                    int deerCount = animalCountMap.getOrDefault(Animal.Kind.DEER, 0);

                    newlyCancelledAnimals.addAll(cancelDeers(smilodonCount, deerCount, notCancelledYetAnimals.iterator()));

                    updatedMessageBoard = updatedMessageBoard.withScoredHuntingTrap(tile.placer(), adjacentMeadow);
                    newlyCancelledAnimals.addAll(notCancelledYetAnimals);
                    updatedBoard = updatedBoard.withMoreCancelledAnimals(newlyCancelledAnimals);
                }
                case null, default -> {}
            }
        }
        GameState updatedGameState = new GameState(
                  players
                , tileDecks
                , tileToPlace
                , updatedBoard
                , nextAction
                , updatedMessageBoard);

        return updatedGameState.withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Handles transitions from RETAKE_PAWN by removing the given occupant, unless it is null
     * which indicates that the player does not wish to retake a pawn.
     *
     * @param occupant The occupant to be removed.
     * @return The updated game state after removing the given occupant.
     * @throws IllegalArgumentException If the next action is not RETAKE_PAWN or if the given occupant is neither null nor a pawn.
     */
    public GameState withOccupantRemoved(Occupant occupant){
        Preconditions.checkArgument(nextAction == Action.RETAKE_PAWN);
        Preconditions.checkArgument(occupant == null || occupant.kind() == Occupant.Kind.PAWN);
        if (occupant == null){
            return withTurnFinishedIfOccupationImpossible();
        }

        GameState updatedGamestate = new GameState(
                  players
                , tileDecks
                , null
                , board.withoutOccupant(occupant)
                , Action.OCCUPY_TILE
                , messageBoard);

        return updatedGamestate.withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Handles transitions from OCCUPY_TILE by adding the given occupant to the last placed tile,
     * unless it is null indicating that the player does not wish to place an occupant.
     *
     * @param occupant The occupant to be added.
     * @return The updated game state after adding the given occupant.
     * @throws IllegalArgumentException If the next action is not OCCUPY_TILE.
     */
    public GameState withNewOccupant(Occupant occupant){
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);
        if (occupant == null){
            return withTurnFinished();
        }

        GameState updatedGamestate = new GameState(
                  players
                , tileDecks
                , tileToPlace
                , board.withOccupant(occupant)
                , nextAction
                , messageBoard);

        return updatedGamestate.withTurnFinished();
    }

    /**
     * Handles the decision-making of skipping the state {@code OCCUPY_TILE} or not.
     * If the player who should occupy can not, or if the tile does not permit it,
     * skips the {@code OCCUPY_TILE} action and returns a game state with turn finished.
     * If the player can occupy, then return the game state with the next action being {@code OCCUPY_TILE}
     *
     * @return The updated game state.
     */
    private GameState withTurnFinishedIfOccupationImpossible(){

        if (lastTilePotentialOccupants().isEmpty())
            return withTurnFinished();

        return new GameState(
                  players
                , tileDecks
                , null
                , board
                , Action.OCCUPY_TILE
                , messageBoard);
    }

    /**
     * Ends the turn of the current player, calculate the points earned in the turn and makes decision of who should be the next player.
     *
     * @return The updated game state after the current players turn.
     */
    private GameState withTurnFinished(){

        List<PlayerColor> updatedPlayers = new LinkedList<>(players);
        TileDecks updatedTileDecks = tileDecks;
        Tile updatedTileToPlace = null;
        Board updatedBoard = board;
        Action updatedNextAction = Action.PLACE_TILE;
        MessageBoard updatedMessageBoard = messageBoard;

        PlacedTile lastPlacedTile = board.lastPlacedTile();

        boolean canPlayAgain = false;
        Area<Zone.Forest> closedForestAreaWithMenhir = null;
        if (!board.forestsClosedByLastTile().isEmpty()){
            for (Area<Zone.Forest> closedForestArea : board.forestsClosedByLastTile()) {
                updatedMessageBoard = updatedMessageBoard.withScoredForest(closedForestArea);

                if (Area.hasMenhir(closedForestArea) && lastPlacedTile.kind() != Tile.Kind.MENHIR && !tileDecks.menhirTiles().isEmpty()){
                    if (!updatedTileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, board::couldPlaceTile).menhirTiles().isEmpty()){
                        canPlayAgain = true;
                        closedForestAreaWithMenhir = closedForestArea;
                    }
                }
            }
        }

        if (!board.riversClosedByLastTile().isEmpty()) {
            for (Area<Zone.River> riverArea : board.riversClosedByLastTile()) {
                updatedMessageBoard = updatedMessageBoard.withScoredRiver(riverArea);
            }
        }

        updatedBoard = updatedBoard.withoutGatherersOrFishersIn(board.forestsClosedByLastTile(), board.riversClosedByLastTile());


        if (canPlayAgain){
            updatedMessageBoard = updatedMessageBoard.withClosedForestWithMenhir(currentPlayer(), closedForestAreaWithMenhir);

            updatedTileDecks = updatedTileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, updatedBoard::couldPlaceTile);
            updatedTileToPlace = updatedTileDecks.topTile(Tile.Kind.MENHIR);
            if (!updatedTileDecks.menhirTiles().isEmpty()) {
                updatedTileDecks = updatedTileDecks.withTopTileDrawn(Tile.Kind.MENHIR);
            }

            return new GameState(
                      players
                    , updatedTileDecks
                    , updatedTileToPlace
                    , updatedBoard
                    , updatedNextAction
                    , updatedMessageBoard);

        } else {

            updatedPlayers.addLast(updatedPlayers.removeFirst());

            if (tileDecks.normalTiles().isEmpty()){

                GameState updatedGameState = new GameState(
                          updatedPlayers
                        , updatedTileDecks
                        , updatedTileToPlace
                        , updatedBoard
                        , Action.END_GAME
                        , updatedMessageBoard);

                return updatedGameState.withFinalPointsCounted();
            }

            updatedTileDecks = updatedTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, updatedBoard::couldPlaceTile);
            updatedTileToPlace = updatedTileDecks.topTile(Tile.Kind.NORMAL);

            if (updatedTileDecks.normalTiles().isEmpty()) {

                GameState updatedGameState = new GameState(
                          updatedPlayers
                        , updatedTileDecks
                        , updatedTileToPlace
                        , updatedBoard
                        , Action.END_GAME
                        , updatedMessageBoard);

                return updatedGameState.withFinalPointsCounted();

            } else {

                updatedTileDecks = updatedTileDecks.withTopTileDrawn(Tile.Kind.NORMAL);

                return new GameState(
                          updatedPlayers
                        , updatedTileDecks
                        , updatedTileToPlace
                        , updatedBoard
                        , updatedNextAction
                        , updatedMessageBoard);
            }
        }
    }

    /**
     * Computing the total points at the end of the game and determining the winners.
     *
     * @return The updated game state after counting the final points.
     * @throws IllegalArgumentException If the next action is not END_GAME.
     */
    private GameState withFinalPointsCounted() {

        MessageBoard updatedMessageBoard = messageBoard;
        Board updatedBoard = board;

        // Compute the points for the meadows
        Set<Animal> cancelledAnimals = board.cancelledAnimals();

        for (Area<Zone.Meadow> meadowArea : board.meadowAreas()) {
            // Check if there is a PitTrap in the zone
            if (meadowArea.zones().stream().anyMatch(zone -> zone.specialPower() == Zone.SpecialPower.PIT_TRAP)) {

                Zone pitTrapZone = meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP);
                Area<Zone.Meadow> adjacentMeadow = updatedBoard.adjacentMeadow(board.tileWithId(pitTrapZone.tileId()).pos(), (Zone.Meadow) pitTrapZone);

                // Check if the zone has the WILD_FIRE special power
                if (meadowArea.zones().stream().anyMatch(zone -> zone.specialPower() == Zone.SpecialPower.WILD_FIRE)) {

                    // No cancelled deers, call withScoredPitTrap and withScoredMeadow
                    updatedMessageBoard = updatedMessageBoard.withScoredPitTrap(adjacentMeadow, cancelledAnimals);
                    updatedMessageBoard = updatedMessageBoard.withScoredMeadow(meadowArea, cancelledAnimals);

                } else {
                    Set<Animal> notCancelledYetAnimals = Area.animals(meadowArea, updatedBoard.cancelledAnimals());
                    Set<Animal> newlyCancelledAnimals = new HashSet<>(updatedBoard.cancelledAnimals());

                    Map<Animal.Kind, Integer> animalCountMap = countAnimals(notCancelledYetAnimals);

                    int smilodonCount = animalCountMap.getOrDefault(Animal.Kind.TIGER, 0);

                    Set<Animal> adjacentDeers = Area.animals(adjacentMeadow, cancelledAnimals)
                            .stream()
                            .filter(animal -> animal.kind() == Animal.Kind.DEER)
                            .collect(Collectors.toSet());

                    Set<Animal> notAdjacentDeers = Area.animals(meadowArea, cancelledAnimals)
                            .stream()
                            .filter(animal -> animal.kind() == Animal.Kind.DEER)
                            .filter(animal -> !adjacentDeers.contains(animal))
                            .collect(Collectors.toSet());

                    int adjacentDeerCount = adjacentDeers.size();
                    int notAdjacentDeerCount = notAdjacentDeers.size();

                    if (smilodonCount >= notAdjacentDeerCount) {
                        newlyCancelledAnimals.addAll(notAdjacentDeers);

                        int remainingAdjDeerToCancelCount = smilodonCount - notAdjacentDeerCount;

                        newlyCancelledAnimals.addAll(cancelDeers(remainingAdjDeerToCancelCount, adjacentDeerCount, adjacentDeers.iterator()));
                    } else {
                        newlyCancelledAnimals.addAll(cancelDeers(smilodonCount, notAdjacentDeerCount, notAdjacentDeers.iterator()));
                    }
                    updatedBoard = updatedBoard.withMoreCancelledAnimals(newlyCancelledAnimals);
                    updatedMessageBoard = updatedMessageBoard.withScoredPitTrap(adjacentMeadow, newlyCancelledAnimals);
                    updatedMessageBoard = updatedMessageBoard.withScoredMeadow(meadowArea, newlyCancelledAnimals);
                }

            } else {
                // No PitTrap
                if (meadowArea.zones().stream().anyMatch(zone -> zone.specialPower() == Zone.SpecialPower.WILD_FIRE)) {
                    updatedMessageBoard = updatedMessageBoard.withScoredMeadow(meadowArea, cancelledAnimals);

                } else {

                    Set<Animal> notCancelledYetAnimals = Area.animals(meadowArea, updatedBoard.cancelledAnimals());
                    Set<Animal> newlyCancelledAnimals = new HashSet<>(updatedBoard.cancelledAnimals());
                    Map<Animal.Kind, Integer> animalCountMap = countAnimals(notCancelledYetAnimals);

                    int deerCount = animalCountMap.getOrDefault(Animal.Kind.DEER, 0);
                    int smilodonCount = animalCountMap.getOrDefault(Animal.Kind.TIGER, 0);

                    newlyCancelledAnimals.addAll(cancelDeers(smilodonCount, deerCount, notCancelledYetAnimals.iterator()));

                    updatedBoard = updatedBoard.withMoreCancelledAnimals(newlyCancelledAnimals);
                    updatedMessageBoard = updatedMessageBoard.withScoredMeadow(meadowArea, newlyCancelledAnimals);
                }
            }
        }
        // Compute the points for the river systems
        for (Area<Zone.Water> riverSystemArea : board.riverSystemAreas()) {
            if (riverSystemArea.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null) {
                updatedMessageBoard = updatedMessageBoard.withScoredRaft(riverSystemArea);
            }
            updatedMessageBoard = updatedMessageBoard.withScoredRiverSystem(riverSystemArea);
        }

        int maxPoints = updatedMessageBoard.points().values().stream().mapToInt(Integer::intValue).max().orElse(0);

        Set<PlayerColor> topPlayers = updatedMessageBoard
                .points()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == maxPoints)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        updatedMessageBoard = updatedMessageBoard.withWinners(topPlayers, maxPoints);

        return new GameState(
                  players
                , tileDecks
                , tileToPlace
                , updatedBoard
                , Action.END_GAME
                , updatedMessageBoard);
    }

    /**
     * Counts the number of each kind of animal in the given set of animals.
     *
     * @param animals The set of animals to count.
     * @return A map associating each kind of animal to the number of animals of that kind in the given set.
     */
    private Map<Animal.Kind, Integer> countAnimals(Set<Animal> animals) {
        Map<Animal.Kind, Integer> animalCountMap = new HashMap<>();
        for (Animal animal : animals) {
            animalCountMap.merge(animal.kind(), 1, Integer::sum);
        }
        return animalCountMap;
    }

    /**
     * Cancels the given number of deers from the given set of animals.
     *
     * @param deerToCancelCount The number of deers to cancel.
     * @param deerCount The total number of deers.
     * @param notCancelledYetAnimals The set of animals to cancel deers from.
     * @return A set of the newly cancelled deers.
     */
    private Set<Animal> cancelDeers(int deerToCancelCount, int deerCount, Iterator<Animal> notCancelledYetAnimals) {
        Set<Animal> newlyCancelledAnimals = new HashSet<>();
        int cancelledDeerCount = 0;
        while (notCancelledYetAnimals.hasNext() && cancelledDeerCount < deerToCancelCount && deerCount > 0) {
            Animal notCancelledYetAnimal = notCancelledYetAnimals.next();
            if (notCancelledYetAnimal.kind() == Animal.Kind.DEER) {
                newlyCancelledAnimals.add(notCancelledYetAnimal);
                cancelledDeerCount++;
                deerCount--;
            }
        }
        return newlyCancelledAnimals;
    }

}
