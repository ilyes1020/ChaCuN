package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Record representing the message board.
 *
 * @author Ilyes Rouibi (372420)
 * @author Weifeng Ding (379902)
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    /**
     * Computes the points earned by each player based on the messages on the message board.
     *
     * @return A map associating each player with the total number of points they have earned.
     */
    public Map<PlayerColor, Integer> points(){
        Map<PlayerColor, Integer> playerPoints = new HashMap<>();
        messages.forEach(message ->
                message.scorers().forEach(scorer ->
                        playerPoints.merge(scorer, message.points(), Integer::sum)));
        return playerPoints;
    }

    /**
     * Returns a new message board identical to the current one, except if the given forest area is occupied.
     * If occupied, a new message is added indicating the majority occupants have scored points for its closure.
     *
     * @param forest The forest area to check for occupancy.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredForest(Area<Zone.Forest> forest){
        if (forest.isOccupied()){

            Set<Integer> forestTileIds = forest.tileIds();
            Set<PlayerColor> forestMajorityOccupants = forest.majorityOccupants();
            int mushroomGroupCount = Area.mushroomGroupCount(forest);
            int points = Points.forClosedForest(forestTileIds.size(), mushroomGroupCount);
            String text = textMaker.playersScoredForest(forestMajorityOccupants, points, mushroomGroupCount, forestTileIds.size());

            return addMessageAndReturnNewBoard(text, points, forestMajorityOccupants, forestTileIds);
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, but with a message indicating
     * that the given player has the right to take a second turn after closing the specified forest,
     * as it contains one or more menhirs.
     *
     * @param player The player who closed the forest.
     * @param forest The forest area closed by the player.
     * @return A new message board reflecting the changes.
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest){
        String text = textMaker.playerClosedForestWithMenhir(player);
        return addMessageAndReturnNewBoard(text, 0, Set.of(), forest.tileIds());
    }

    /**
     * Returns a new message board identical to the current one, except if the given river area is occupied.
     * If occupied, a new message is added indicating the majority occupants have scored points for its closure.
     *
     * @param river The river area to check for occupancy.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredRiver(Area<Zone.River> river){
        if (river.isOccupied()){

            Set<Integer> riverTileIds = river.tileIds();
            Set<PlayerColor> riverMajorityOccupants = river.majorityOccupants();
            int fishCount = Area.riverFishCount(river);
            int points = Points.forClosedRiver(riverTileIds.size(), fishCount);
            String text = textMaker.playersScoredRiver(riverMajorityOccupants, points, fishCount, riverTileIds.size());

            return addMessageAndReturnNewBoard(text, points, riverMajorityOccupants, riverTileIds);
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, except if the placement of the hunting trap
     * by the given player resulted in scoring points.
     *
     * @param scorer        The player who placed the hunting trap.
     * @param adjacentMeadow The meadow area adjacent to the hunting trap.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow){
        Map<Animal.Kind, Integer> animalCountMap = countAnimals(adjacentMeadow, Set.of());
        int points = calculatePoints(animalCountMap);

        if (points > 0){
            String text = textMaker.playerScoredHuntingTrap(scorer, points, animalCountMap);
            return addMessageAndReturnNewBoard(text, points, Set.of(scorer), adjacentMeadow.tileIds());
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, but with a message indicating
     * that the given player has scored points for placing a logboat in the specified river system.
     *
     * @param scorer      The player who placed the logboat.
     * @param riverSystem The water area representing the river system.
     * @return A new message board reflecting the changes.
     */
    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem){
        int lakeCount = Area.lakeCount(riverSystem);
        int points = Points.forLogboat(lakeCount);
        String text = textMaker.playerScoredLogboat(scorer, points, lakeCount);

        return addMessageAndReturnNewBoard(text, points, Set.of(scorer), riverSystem.tileIds());
    }

    /**
     * Returns a new message board identical to the current one, except if the given meadow area is occupied.
     * If occupied, a new message is added indicating the majority occupants have scored points for its closure.
     *
     * @param meadow          The meadow area to check for occupancy.
     * @param cancelledAnimals The set of animals whose points are canceled.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals){
        Map<Animal.Kind, Integer> animalCountMap = countAnimals(meadow, cancelledAnimals);
        int points = calculatePoints(animalCountMap);

        if (meadow.isOccupied() && points > 0){
            Set<PlayerColor> meadowMajorityOccupants = meadow.majorityOccupants();
            String text = textMaker.playersScoredMeadow(meadowMajorityOccupants, points, animalCountMap);
            return addMessageAndReturnNewBoard(text, points, meadowMajorityOccupants, meadow.tileIds());
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, except if the given river system area is occupied.
     * If occupied, a new message is added indicating the majority occupants have scored points for its closure.
     *
     * @param riverSystem The water area representing the river system.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem){
        int fishCount = Area.riverSystemFishCount(riverSystem);
        int points = Points.forRiverSystem(fishCount);

        if (riverSystem.isOccupied() && points > 0) {
            Set<PlayerColor> riverSystemMajorityOccupants = riverSystem.majorityOccupants();
            String text = textMaker.playersScoredRiverSystem(riverSystemMajorityOccupants, points, fishCount);
            return addMessageAndReturnNewBoard(text, points, riverSystemMajorityOccupants, riverSystem.tileIds());
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, except if the given meadow area containing
     * the pit trap is occupied. If occupied, a new message is added indicating the majority occupants
     * have scored points for its closure.
     *
     * @param adjacentMeadow   The meadow area containing the pit trap.
     * @param cancelledAnimals The set of animals whose points are canceled.
     * @return A new message board reflecting the changes, if any.
     */
    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals){
        Map<Animal.Kind, Integer> animalCountMap = countAnimals(adjacentMeadow, cancelledAnimals);
        int points = calculatePoints(animalCountMap);

        if (adjacentMeadow.isOccupied() && points > 0){
            Set<PlayerColor> adjacentMeadowMajorityOccupants = adjacentMeadow.majorityOccupants();
            String text = textMaker.playersScoredPitTrap(adjacentMeadowMajorityOccupants, points, animalCountMap);
            return addMessageAndReturnNewBoard(text, points, adjacentMeadowMajorityOccupants, adjacentMeadow.tileIds());
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, but with a message indicating
     * that the majority occupants of the specified river system have scored points for having a raft.
     *
     * @param riverSystem The water area representing the river system with the raft.
     * @return A new message board reflecting the changes.
     */
    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem){
        if (riverSystem.isOccupied()) {
            int lakeCount = Area.lakeCount(riverSystem);
            int points = Points.forRaft(lakeCount);
            Set<PlayerColor> riverSystemMajorityOccupants = riverSystem.majorityOccupants();

            String text = textMaker.playersScoredRaft(riverSystemMajorityOccupants , points, lakeCount);
            return addMessageAndReturnNewBoard(text, points, riverSystemMajorityOccupants, riverSystem.tileIds());
        }
        return this;
    }

    /**
     * Returns a new message board identical to the current one, but with a message indicating
     * the given winners of the game and the points they have scored.
     *
     * @param winners The set of winners.
     * @param points  The points scored by the winners.
     * @return A new message board reflecting the changes.
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points){
        String text = textMaker.playersWon(winners, points);
        return addMessageAndReturnNewBoard(text, 0, Set.of(), Set.of());
    }

    /**
     * Adds a new message to the message board and returns a new message board with the updated messages.
     *
     * @param text   The text content of the message.t
     * @param points The points associated with the message.
     * @param scorers The set of players who scored points with this message.
     * @param tileIds The set of tile IDs related to this message.
     * @return A new message board with the updated messages.
     */
    private MessageBoard addMessageAndReturnNewBoard(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {
        List<Message> updatedMessages = new ArrayList<>(this.messages);
        updatedMessages.add(new Message(text, points, scorers, tileIds));
        return new MessageBoard(textMaker, updatedMessages);
    }

    /**
     * Counts the number of animals in the given meadow area, excluding the animals in the cancelled set.
     *
     * @param area The meadow area to count the animals from.
     * @param cancelledAnimals The set of animals to exclude from the count.
     * @return A map associating each animal kind with the number of occurrences in the meadow area.
     */

    private Map<Animal.Kind, Integer> countAnimals(Area<Zone.Meadow> area, Set<Animal> cancelledAnimals) {
        return Area.animals(area, cancelledAnimals)
                .stream()
                .collect(Collectors.toMap(Animal::kind, animal -> 1, Integer::sum));
    }

    /**
     * Calculates the points scored in a meadow area based on the number of animals present.
     *
     * @param animalCountMap  A map associating each animal kind with the number of occurrences in the meadow area.
     * @return The total number of points scored with the numbers of animal in the meadow area.
     */

    private int calculatePoints(Map<Animal.Kind, Integer> animalCountMap) {
        return Points.forMeadow(
                animalCountMap.getOrDefault(Animal.Kind.MAMMOTH, 0),
                animalCountMap.getOrDefault(Animal.Kind.AUROCHS, 0),
                animalCountMap.getOrDefault(Animal.Kind.DEER, 0)
        );
    }



    /**
     * Represents a message within the message board.
     *
     * @param text     The text content of the message.
     * @param points   The points associated with the message.
     * @param scorers  The set of players who scored points with this message.
     * @param tileIds  The set of tile IDs related to this message.
     */
    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {

        /**
         * Compact constructor for a message with the provided text, points, scorers, and tileIds.
         *
         * @throws IllegalArgumentException if points are negative.
         * @throws NullPointerException     if text is null.
         */
        public Message {
            Preconditions.checkArgument(points >= 0);
            Objects.requireNonNull(text, "Text must not be null");
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}
